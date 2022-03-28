DROP TEMPORARY TABLE IF EXISTS T_CUST_VEND;
CREATE TEMPORARY TABLE T_CUST_VEND (
  PRIMARY KEY (custno, vendno),
  INDEX (vendno)
)
SELECT IFNULL(C.no, -1) AS custno,
       V.no             AS vendno,
       V.auxLong4       AS fornecedorSap,
       V.email,
       V.name
FROM sqldados.vend          AS V
  INNER JOIN sqldados.custp AS C
	       ON C.cpf_cgc = V.cgc;

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
		       -1) * 1                                           AS vendObs,
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
       IFNULL(OP.name, '')                                               AS natureza,
       TRIM(CONCAT(N.c6, N.c5))                                          AS chaveDesconto,
       TRIM(CONCAT(N.c4, N.c3))                                          AS observacaoAuxiliar
FROM sqldados.nf               AS N /*FORCE INDEX (e3)*/
  LEFT JOIN  sqldados.natop    AS OP
	       ON OP.no = N.natopno
  LEFT JOIN  sqldados.nfes     AS X
	       ON X.storeno = N.storeno AND X.pdvno = N.pdvno AND X.xano = N.xano
  LEFT JOIN  sqldados.nfdevRmk AS R
	       ON R.storeno = N.storeno AND R.pdvno = N.pdvno AND R.xano = N.xano
  LEFT JOIN  sqldados.nfrmk    AS R2
	       ON R2.storeno = N.storeno AND R2.pdvno = N.pdvno AND R2.xano = N.xano
  LEFT JOIN  sqldados.eord     AS O
	       ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN  sqldados.eordrk   AS OBS
	       ON OBS.storeno = N.storeno AND OBS.ordno = N.eordno
  INNER JOIN sqldados.custp    AS C
	       ON C.no = N.custno AND C.name LIKE 'ENGECOPI%'
WHERE N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND CASE :TIPO_NOTA
	WHEN 'AJT'
	  THEN N.remarks LIKE 'GARANTIA %'
	WHEN 'AJD'
	  THEN N.remarks LIKE 'GARANTIA %' AND N.remarks NOT LIKE '%PERCA%' AND
	       N.remarks NOT LIKE '%PAGO%'
	WHEN 'AJP'
	  THEN N.remarks LIKE 'GARANTIA %PAGO%' AND N.remarks NOT LIKE '%PERCA%'
	WHEN 'AJC'
	  THEN N.remarks LIKE 'GARANTIA %PERCA%'
	ELSE FALSE
      END
  AND N.tipo = 7
  AND N.cfo = 5949
  AND N.nfse = '1'
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

SELECT N.storeno                                 AS loja,
       S.sname                                   AS sigla,
       N.pdvno                                   AS pdv,
       N.xano                                    AS transacao,
       N.eordno                                  AS pedido,
       CAST(N.pedidoDate AS DATE)                AS dataPedido,
       CAST(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       CAST(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nfAjuste,
       IFNULL(CAST(D.fatura AS CHAR), '')        AS fatura,
       CAST(N.issuedate AS DATE)                 AS dataNota,
       IFNULL(C.custno, 0)                       AS custno,
       IFNULL(C.name, 'Sem fornecedor')          AS fornecedor,
       IFNULL(C.email, '')                       AS email,
       C.fornecedorSap                           AS fornecedorSap,
       C.vendno                                  AS vendno,
       IFNULL(R.rmk, '')                         AS rmk,
       SUM(N.valor)                              AS valor,
       IFNULL(obsNota, '')                       AS obsNota,
       serie01Rejeitada                          AS serie01Rejeitada,
       IF((D.valorDevido IS NOT NULL AND D.valorDevido <= 0) OR
	  (D.status IS NOT NULL AND D.status = 2) OR (N.serie01Pago = 'S' && D.status IS NULL), 'S',
	  'N')                                   AS serie01Pago,
       serie01Coleta                             AS serie01Coleta,
       serie66Pago                               AS serie66Pago,
       remessaConserto                           AS remessaConserto,
       N.remarks                                 AS remarks,
       baseIcms                                  AS baseIcms,
       valorIcms                                 AS valorIcms,
       baseIcmsSubst                             AS baseIcmsSubst,
       icmsSubst                                 AS icmsSubst,
       valorFrete                                AS valorFrete,
       valorSeguro                               AS valorSeguro,
       outrasDespesas                            AS outrasDespesas,
       valorIpi                                  AS valorIpi,
       valorTotal                                AS valorTotal,
       N.obsPedido                               AS obsPedido,
       :TIPO_NOTA                                AS tipo,
       IFNULL(RV.rmk, '')                        AS rmkVend,
       chave                                     AS chave,
       natureza                                  AS natureza,
       chaveDesconto                             AS chaveDesconto,
       observacaoAuxiliar                        AS observacaoAuxiliar
FROM TNF                        AS N
  INNER JOIN sqldados.store     AS S
	       ON S.no = N.storeno
  LEFT JOIN  sqldados.nfdevRmk  AS R
	       USING (storeno, pdvno, xano)
  LEFT JOIN  TDUP               AS D
	       ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
  LEFT JOIN  sqldados.eordrk    AS O
	       ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN  T_CUST_VEND        AS C
	       ON C.vendno = N.vendObs
  LEFT JOIN  sqldados.nfvendRmk AS RV
	       ON RV.vendno = C.vendno AND RV.tipo = N.nfse
WHERE (IFNULL(status, 0) <> 5)
GROUP BY loja, pdv, transacao, dataNota, custno