DROP TEMPORARY TABLE IF EXISTS T_VEND;
CREATE TEMPORARY TABLE T_VEND (
  PRIMARY KEY (vendno)
)
SELECT vendno
FROM sqldados.ords
WHERE remarks LIKE '%DESC%'
  AND amt > 0
  AND date >= 20190101
UNION
SELECT inv.vendno
FROM sqldados.invxa
  INNER JOIN sqldados.inv
	       USING (invno)
WHERE invxa.remarks LIKE '%DESC%'
  AND invxa.paiddate = 0
  AND invxa.amtdue > 0
  AND invxa.duedate > 20210101;

DROP TEMPORARY TABLE IF EXISTS T_FORNECEDOR;
CREATE TEMPORARY TABLE T_FORNECEDOR (
  PRIMARY KEY (custno)
)
SELECT C.no       AS custno,
       V.no       AS vendno,
       C.name     AS fornecedorName,
       V.auxLong4 AS fornecedorSap,
       V.email
FROM sqldados.custp        AS C
  INNER JOIN sqldados.vend AS V
	       ON C.cpf_cgc = V.cgc
  INNER JOIN T_VEND        AS T
	       ON T.vendno = V.no
GROUP BY custno;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       N.issuedate,
       N.eordno,
       O.date                                                            AS pedidoDate,
       N.grossamt / 100                                                  AS valor,
       SUBSTRING_INDEX(SUBSTRING_INDEX(MID(N.remarks, LOCATE('FOR', N.remarks), 100), ' ', 2), ' ',
		       -1) * 1                                           AS custnoObs,
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
       grossamt / 100                                                    AS valorTotal,
       TRIM(IFNULL(OBS.remarks__480, ''))                                AS obsPedido,
       IFNULL(X.nfekey, '')                                              AS chave,
       natopno,
       F.*
FROM sqldados.nf               AS N
  INNER JOIN T_FORNECEDOR      AS F
	       USING (custno)
  LEFT JOIN  sqldados.nfes     AS X
	       USING (storeno, pdvno, xano)
  LEFT JOIN  sqldados.nfdevRmk AS R
	       USING (storeno, pdvno, xano)
  LEFT JOIN  sqldados.nfrmk    AS R2
	       USING (storeno, pdvno, xano)
  LEFT JOIN  sqldados.eord     AS O
	       ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN  sqldados.eordrk   AS OBS
	       ON OBS.storeno = N.storeno AND OBS.ordno = N.eordno
WHERE N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.nfse = 1
  AND N.tipo = 2
GROUP BY N.storeno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.nfstoreno                      AS storeno,
       N.nfno,
       N.nfse,
       D.status,
       CAST(N.dupno AS CHAR)            AS fatura,
       MAX(duedate)                     AS vencimento,
       SUM(amtdue - disc_amt - amtpaid) AS valorDevido
FROM sqldados.dup           AS D
  INNER JOIN sqldados.nfdup AS N
	       ON N.dupstoreno = D.storeno AND N.duptype = D.type AND N.dupno = D.dupno AND
		  N.dupse = D.dupse
  INNER JOIN TNF               NF
	       ON N.nfstoreno = NF.storeno AND N.nfno = NF.nfno AND N.nfse = NF.nfse
GROUP BY N.nfstoreno, N.nfno, N.nfse;

SELECT N.storeno                                                                      AS loja,
       S.sname                                                                        AS sigla,
       N.pdvno                                                                        AS pdv,
       N.xano                                                                         AS transacao,
       N.eordno                                                                       AS pedido,
       CAST(N.pedidoDate AS DATE)                                                     AS dataPedido,
       CAST(CONCAT(N.nfno, '/', N.nfse) AS CHAR)                                      AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')                                             AS fatura,
       CAST(N.issuedate AS DATE)                                                      AS dataNota,
       IFNULL(N.custno, 0)                                                            AS custno,
       IFNULL(N.fornecedorName, '')                                                   AS fornecedor,
       IFNULL(N.email, '')                                                            AS email,
       N.fornecedorSap                                                                AS fornecedorSap,
       N.vendno                                                                       AS vendno,
       IFNULL(R.rmk, '')                                                              AS rmk,
       SUM(N.valor)                                                                   AS valor,
       IFNULL(obsNota, '')                                                            AS obsNota,
       serie01Rejeitada                                                               AS serie01Rejeitada,
       IF((D.valorDevido IS NOT NULL AND D.valorDevido <= 0) OR
	  (D.status IS NOT NULL AND D.status = 2) OR (N.serie01Pago = 'S'), 'S',
	  'N')                                                                        AS serie01Pago,
       serie01Coleta                                                                  AS serie01Coleta,
       serie66Pago                                                                    AS serie66Pago,
       remessaConserto                                                                AS remessaConserto,
       N.remarks                                                                      AS remarks,
       baseIcms                                                                       AS baseIcms,
       valorIcms                                                                      AS valorIcms,
       baseIcmsSubst                                                                  AS baseIcmsSubst,
       icmsSubst                                                                      AS icmsSubst,
       valorFrete                                                                     AS valorFrete,
       valorSeguro                                                                    AS valorSeguro,
       outrasDespesas                                                                 AS outrasDespesas,
       valorIpi                                                                       AS valorIpi,
       valorTotal                                                                     AS valorTotal,
       N.obsPedido                                                                    AS obsPedido,
       'FIN'                                                                          AS tipo,
       IFNULL(RV.rmk, '')                                                             AS rmkVend,
       chave                                                                          AS chave,
       IFNULL(OP.name, '')                                                            AS natureza
FROM TNF                        AS N
  LEFT JOIN  sqldados.natop     AS OP
	       ON OP.no = N.natopno
  INNER JOIN sqldados.store     AS S
	       ON S.no = N.storeno
  LEFT JOIN  sqldados.nfdevRmk  AS R
	       USING (storeno, pdvno, xano)
  LEFT JOIN  TDUP               AS D
	       ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
  LEFT JOIN  sqldados.eordrk    AS O
	       ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN  sqldados.nfvendRmk AS RV
	       ON RV.vendno = N.vendno AND RV.tipo = N.nfse
GROUP BY loja, pdv, transacao, dataNota, custno


