DO @LOJA := :loja;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF
(
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       V.no                                                              AS vendno,
       N.issuedate,
       N.eordno,
       O.date                                                            AS pedidoDate,
       C.no                                                              AS custno,
       C.name                                                            AS fornecedorNome,
       N.grossamt / 100                                                  AS valor,
       CONCAT(TRIM(N.remarks), '\n', TRIM(IFNULL(R2.remarks__480, '')))  AS obsNota,
       IF(N.remarks LIKE 'REJEI% NF% RETOR%' AND N.nfse = '1', 'S', 'N') AS serie01Rejeitada,
       IF((N.remarks LIKE '%PAGO%') AND N.nfse = '1', 'S', 'N')          AS serie01Pago,
       IF((N.remarks LIKE '%COLETA%') AND N.nfse = '1', 'S', 'N')        AS serie01Coleta,
       IF((N.remarks LIKE '%PAGO%') AND N.nfse = '66', 'S', 'N')         AS serie66Pago,
       IF((N.remarks LIKE '%REMESSA%CONSERTO%'), 'S', 'N')               AS remessaConserto,
       TRIM(N.remarks)                                                   AS remarks,
       N.netamt / 100                                                    AS baseIcms,
       N.icms_amt / 100                                                  AS valorIcms,
       N.baseIcmsSubst / 100                                             AS baseIcmsSubst,
       N.icmsSubst / 100                                                 AS icmsSubst,
       N.fre_amt / 100                                                   AS valorFrete,
       N.sec_amt / 100                                                   AS valorSeguro,
       N.discount / 100                                                  AS valorDesconto,
       0.00                                                              AS outrasDespesas,
       N.ipi_amt / 100                                                   AS valorIpi,
       N.grossamt / 100                                                  AS valorTotal,
       TRIM(IFNULL(OBS.remarks__480, ''))                                AS obsPedido
FROM sqldados.nf AS N
       LEFT JOIN sqldados.nfdevRmk AS R
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.nfrmk AS R2
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.eord AS O
                 ON O.storeno = N.storeno AND O.ordno = N.eordno
       LEFT JOIN sqldados.eordrk AS OBS
                 ON OBS.storeno = N.storeno AND OBS.ordno = N.eordno
       LEFT JOIN sqldados.custp AS C
                 ON C.no = N.custno
                   AND C.no NOT IN (306263, 312585, 901705, 21295, 120420, 478, 102773, 21333,
                                    709327, 108751)
       LEFT JOIN sqldados.vend AS V
                 ON C.cpf_cgc = V.cgc
WHERE N.nfse IN ('1', '66')
  AND N.storeno IN (2, 3, 4, 5, 8)
  AND N.status <> 1
  AND N.tipo = 2
GROUP BY N.storeno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP
(
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.nfstoreno                      AS storeno,
       N.nfno,
       N.nfse,
       D.status,
       CAST(N.dupno AS CHAR)            AS fatura,
       MAX(duedate)                     AS vencimento,
       SUM(amtdue - disc_amt - amtpaid) AS valorDevido
FROM sqldados.dup AS D
       INNER JOIN sqldados.nfdup AS N
                  ON N.dupstoreno = D.storeno AND N.duptype = D.type AND N.dupno = D.dupno AND
                     N.dupse = D.dupse
       INNER JOIN TNF NF
                  ON N.nfstoreno = NF.storeno AND N.nfno = NF.nfno AND N.nfse = NF.nfse
GROUP BY N.nfstoreno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS T_NOTA;
CREATE TEMPORARY TABLE T_NOTA
(
  INDEX (loja, pedido)
)
SELECT N.storeno                                 AS loja,
       S.sname                                   AS sigla,
       N.pdvno                                   AS pdv,
       N.xano                                    AS transacao,
       N.eordno                                  AS pedido,
       CAST(N.pedidoDate AS DATE)                AS dataPedido,
       CAST(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')        AS fatura,
       CAST(N.issuedate AS DATE)                 AS dataNota,
       N.custno                                  AS custno,
       N.fornecedorNome                          AS fornecedor,
       N.vendno                                  AS vendno,
       IFNULL(R.rmk, '')                         AS rmk,
       SUM(N.valor)                              AS valor,
       IFNULL(obsNota, '')                       AS obsNota,
       serie01Rejeitada                          AS serie01Rejeitada,
       serie01Pago                               AS serie01Pago,
       serie01Coleta                             AS serie01Coleta,
       serie66Pago                               AS serie66Pago,
       remessaConserto                           AS remessaConserto,
       remarks                                   AS remarks,
       baseIcms                                  AS baseIcms,
       valorIcms                                 AS valorIcms,
       baseIcmsSubst                             AS baseIcmsSubst,
       icmsSubst                                 AS icmsSubst,
       valorFrete                                AS valorFrete,
       valorSeguro                               AS valorSeguro,
       outrasDespesas                            AS outrasDespesas,
       valorIpi                                  AS valorIpi,
       valorTotal                                AS valorTotal,
       N.obsPedido                               AS obsPedido
FROM TNF AS N
       INNER JOIN sqldados.store AS S
                  ON S.no = N.storeno
       INNER JOIN sqldados.eord AS E
                  ON E.storeno = N.storeno AND E.ordno = N.eordno AND E.paymno = 315
       LEFT JOIN sqldados.nfdevRmk AS R
                 ON R.storeno = E.storeno AND R.pdvno = 9999 AND R.xano = E.ordno
       LEFT JOIN TDUP AS D
                 ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
WHERE (IFNULL(D.valorDevido, 100) > 0)
  AND (IFNULL(D.status, 0) <> 5)
  AND ((D.fatura IS NOT NULL OR serie01Pago = 'N') OR N.nfse = '66')
GROUP BY loja, pdv, transacao, dataNota, custno;

SELECT E.storeno                                             AS loja,
       S.sname                                               AS sigla,
       IFNULL(N.pdv, 0)                                      AS pdv,
       IFNULL(N.transacao, E.ordno)                          AS transacao,
       E.ordno                                               AS pedido,
       CAST(E.date AS DATE)                                  AS dataPedido,
       IFNULL(N.nota, 0)                                     AS nota,
       IFNULL(N.fatura, 0)                                   AS fatura,
       N.dataNota                                            AS dataNota,
       E.custno                                              AS custno,
       IFNULL(C.name, '')                                    AS fornecedor,
       IFNULL(C.email, '')                                   AS email,
       IFNULL(V.no, 0)                                       AS vendno,
       IFNULL(V.auxLong4, 0)                                 AS fornecedorSap,
       IFNULL(N.rmk, '')                                     AS rmk,
       IFNULL(N.valor, E.amount / 100)                       AS valor,
       IFNULL(N.obsNota, '')                                 AS obsNota,
       IFNULL(N.serie01Rejeitada, '')                        AS serie01Rejeitada,
       IF(NC.chaveDesconto LIKE '%PAGO%', 'S', 'N')          AS serie01Pago,
       IFNULL(N.serie01Coleta, '')                           AS serie01Coleta,
       IFNULL(N.serie66Pago, '')                             AS serie66Pago,
       IFNULL(N.remessaConserto, '')                         AS remessaConserto,
       IFNULL(N.remarks, '')                                 AS remarks,
       IFNULL(N.baseIcms, 0.00)                              AS baseIcms,
       IFNULL(N.valorIcms, 0.00)                             AS valorIcms,
       IFNULL(N.baseIcmsSubst, 0.00)                         AS baseIcmsSubst,
       IFNULL(N.icmsSubst, 0.00)                             AS icmsSubst,
       IFNULL(N.valorFrete, 0.00)                            AS valorFrete,
       IFNULL(N.valorSeguro, 0.00)                           AS valorSeguro,
       IFNULL(N.outrasDespesas, 0.00)                        AS outrasDespesas,
       IFNULL(N.valorIpi, 0.00)                              AS valorIpi,
       IFNULL(N.valorTotal, 0.00)                            AS valorTotal,
       TRIM(IFNULL(OBS.remarks__480, ''))                    AS obsPedido,
       'AVA'                                                 AS tipo,
       IFNULL(RV.rmk, '')                                    AS rmkVend,
       ''                                                    AS chave,
       'DEVOLUÇÃO'                                           AS natureza,
       NC.chaveDesconto                                      AS chaveDesconto,
       NC.observacaoAuxiliar                                 AS observacaoAuxiliar,
       CAST(IF(NC.dataAgenda = 0, NULL, dataAgenda) AS DATE) AS dataAgenda,
       NC.pedidos                                            AS pedidos
FROM sqldados.eord AS E
       LEFT JOIN sqldados.nfComplemento NC
                 ON NC.xano = E.ordno AND NC.storeno = E.storeno AND NC.pdvno = 980
       LEFT JOIN sqldados.ords AS O
                 ON O.no = E.ordno AND O.storeno = E.storeno
       LEFT JOIN sqldados.eordrk AS OBS
                 ON OBS.storeno = E.storeno AND OBS.ordno = E.ordno
       LEFT JOIN sqldados.store AS S
                 ON S.no = E.storeno
       INNER JOIN sqldados.custp AS C
                  ON C.no = E.custno /*AND
                     C.no NOT IN (306263, 312585, 901705, 21295, 120420, 478, 102773, 21333,
                                  709327, 108751)

 */
       LEFT JOIN sqldados.vend AS V
                 ON C.cpf_cgc = V.cgc
       LEFT JOIN sqldados.nfvendRmk AS RV
                 ON RV.vendno = V.no AND RV.tipo = 'PED'
       LEFT JOIN T_NOTA AS N
                 ON E.storeno = N.loja AND E.ordno = N.pedido
WHERE E.paymno = 317
  AND N.loja IS NULL
  AND (E.storeno = @LOJA OR @LOJA = 0)
