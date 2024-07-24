USE sqldados;

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
                   AND C.no NOT IN (478, 21295, 21333, 102773, 108751, 120420, 709327, 926520, 901705, 306263, 312585)
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

DROP TEMPORARY TABLE IF EXISTS T_PRD_PENDENTE;
CREATE TEMPORARY TABLE T_PRD_PENDENTE
(
  PRIMARY KEY (prdno, grade)
)
SELECT prdno, grade, E.custno
FROM sqldados.eord AS E
       INNER JOIN sqldados.eoprd AS I
                  USING (storeno, ordno)
       LEFT JOIN T_NOTA AS N
                 ON E.storeno = N.loja AND E.ordno = N.pedido
WHERE E.paymno = 315
  AND E.custno NOT IN (478, 21295, 21333, 102773, 108751, 120420, 709327, 926520, 901705, 306263, 312585)
  AND N.loja IS NULL
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_QUERY;
CREATE TEMPORARY TABLE T_QUERY
SELECT N.invno                                         AS invno,
       N.storeno                                       AS storeno,
       L.sname                                         AS sigla,
       I.prdno                                         AS prdno,
       TRIM(I.prdno)                                   AS codigo,
       P.mfno_ref                                      AS refFor,
       I.grade                                         AS grade,
       TRIM(COALESCE(B.barcode, P.barcode, ''))        AS barcode,
       N.date                                          AS date,
       CONCAT(N.nfname, '/', N.invse)                  AS nota,
       N.ordno                                         AS pedido,
       N.grossamt / 100                                AS valorNota,
       N.vendno                                        AS vendno,
       V.name                                          AS fornecedor,
       IF(V.vendcust = 0, IFNULL(C.no, 0), V.vendcust) AS custno,
       V.auxLong4                                      AS fornecedorSap,
       TRIM(MID(P.name, 1, 37))                        AS descricao,
       TRIM(MID(P.name, 37, 40))                       AS un,
       N.remarks                                       AS remarks,
       I.fob / 100                                     AS valorUnitInv,
       SUM(I.qtty / 1000)                              AS quantInv
FROM sqldados.iprd AS I
       INNER JOIN sqldados.store AS L
                  ON L.no = I.storeno
       INNER JOIN T_PRD_PENDENTE AS T
                  USING (prdno, grade)
       LEFT JOIN sqldados.prdbar AS B
                 USING (prdno, grade)
       INNER JOIN sqldados.inv AS N
                  USING (invno)
       INNER JOIN sqldados.vend AS V
                  ON V.no = N.vendno
       INNER JOIN sqldados.custp AS C
                  ON C.cpf_cgc = V.cgc
       INNER JOIN sqldados.prd AS P
                  ON P.no = I.prdno
WHERE (N.date >= :dataInicial OR :dataInicial = 0)
  AND (N.date <= :dataFinal OR :dataFinal = 0)
  AND (N.storeno = :loja OR :loja = 0)
  AND (V.name NOT LIKE 'ENGECOPI%')
  AND (N.type = 0)
GROUP BY I.invno, I.prdno, I.grade;

DO @pesquisa := :pesquisa;
DO @pesquisaNum := IF(@pesquisa REGEXP '^[0-9]+$', @pesquisa * 1, 0);
DO @pesquisaLike := CONCAT(@pesquisa, '%');

SELECT invno                   AS ni,
       storeno                 AS loja,
       sigla                   AS sigla,
       prdno                   AS prdno,
       codigo                  AS codigo,
       refFor                  AS refFor,
       grade                   AS grade,
       barcode                 AS barcode,
       CAST(date AS DATE)      AS dataEntrada,
       nota                    AS nota,
       pedido                  AS pedido,
       valorNota               AS valorNota,
       vendno                  AS vendno,
       custno                  AS custno,
       fornecedorSap           AS fornecedorSap,
       fornecedor              AS fornecedor,
       descricao               AS descricao,
       un                      AS un,
       remarks                 AS observacao,
       valorUnitInv            AS valorUnit,
       quantInv                AS quant,
       valorUnitInv * quantInv AS valorTotal
FROM T_QUERY
WHERE (@pesquisa = '' OR
       invno = @pesquisaNum OR
       codigo = @pesquisa OR
       grade = @pesquisa OR
       vendno = @pesquisaNum OR
       fornecedor LIKE @pesquisaLike OR
       descricao LIKE @pesquisaLike OR
       remarks LIKE @pesquisaLike)
