DROP TEMPORARY TABLE IF EXISTS TNFSACI;
CREATE TEMPORARY TABLE TNFSACI (
  PRIMARY KEY (storeno, pdvno, xano)
)
SELECT N.*
FROM sqldados.nf            AS N
  INNER JOIN sqldados.custp AS C
	       ON C.no = N.custno AND C.name LIKE 'ENGECOPI%'
WHERE N.remarks LIKE 'GARANTIA %'
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 7
  AND N.cfo = 5949
  AND N.nfse = '1'
UNION
DISTINCT
SELECT *
FROM sqldados.nf AS N
WHERE (N.nfse = :serie)
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 2
UNION
DISTINCT
SELECT *
FROM sqldados.nf AS N
WHERE :serie = ''
  AND N.nfse IN ('1', '66')
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 2;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       V.no                                                                                    AS vendno,
       N.issuedate,
       N.eordno,
       O.date                                                                                  AS pedidoDate,
       C.no                                                                                    AS custno,
       C.name                                                                                  AS fornecedorNome,
       C.email                                                                                 AS email,
       V.auxLong4                                                                              AS fornecedorSap,
       N.grossamt / 100                                                                        AS valor,
       CONCAT(TRIM(N.remarks), '\n',
	      TRIM(IFNULL(R2.remarks__480, '')))                                               AS obsNota,
       IF(N.remarks LIKE 'REJEI% NF% RETOR%' AND N.nfse = '1', 'S',
	  'N')                                                                                 AS serie01Rejeitada,
       IF((N.remarks LIKE '%PAGO%' || N.remarks LIKE '%RETORNO%') AND N.nfse = '1', 'S',
	  'N')                                                                                 AS serie01Pago,
       IF((N.remarks LIKE '%COLETA%') AND N.nfse = '1', 'S', 'N')                              AS serie01Coleta,
       IF((N.remarks LIKE '%PAGO%' || N.remarks LIKE '%RETORNO%') AND N.nfse = '66', 'S',
	  'N')                                                                                 AS serie66Pago,
       IF((N.remarks LIKE '%REMESSA%CONSERTO%'), 'S', 'N')                                     AS remessaConserto,
       TRIM(N.remarks)                                                                         AS remarks,
       N.netamt / 100                                                                          AS baseIcms,
       N.icms_amt / 100                                                                        AS valorIcms,
       N.baseIcmsSubst / 100                                                                   AS baseIcmsSubst,
       N.icmsSubst / 100                                                                       AS icmsSubst,
       N.fre_amt / 100                                                                         AS valorFrete,
       N.sec_amt / 100                                                                         AS valorSeguro,
       N.discount / 100                                                                        AS valorDesconto,
       0.00                                                                                    AS outrasDespesas,
       N.ipi_amt / 100                                                                         AS valorIpi,
       grossamt / 100                                                                          AS valorTotal,
       TRIM(IFNULL(OBS.remarks__480, ''))                                                      AS obsPedido,
       IFNULL(X.nfekey, '')                                                                    AS chave,
       IFNULL(OP.name, '')                                                                     AS natureza,
       TRIM(CONCAT(N.c6, N.c5))                                                                AS chaveDesconto,
       TRIM(CONCAT(N.c4, N.c3))                                                                AS observacaoAuxiliar,
       CAST(IF(N.l15 = 0, NULL, N.l15) AS DATE)                                                AS dataAgenda,
       ''                                                                                      AS nfAjuste
FROM TNFSACI                  AS N
  LEFT JOIN sqldados.natop    AS OP
	      ON OP.no = N.natopno
  LEFT JOIN sqldados.nfes     AS X
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.nfdevRmk AS R
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.nfrmk    AS R2
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.eord     AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.eordrk   AS OBS
	      ON OBS.storeno = N.storeno AND OBS.ordno = N.eordno
  LEFT JOIN sqldados.custp    AS C
	      ON C.no = N.custno AND
		 C.no NOT IN (306263, 312585, 901705, 21295, 120420, 478, 102773, 21333,
			      709327, 108751)
  LEFT JOIN sqldados.vend     AS V
	      ON C.cpf_cgc = V.cgc
WHERE (N.nfse = :serie OR (N.remarks LIKE 'GARANTIA%') OR (:serie = '' AND (N.nfse IN ('1', '66'))))
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 2
  AND (V.no = :vendno OR :vendno = 0)
GROUP BY N.storeno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.nfstoreno                      AS storeno,
       N.nfno,
       N.nfse,
       D.status,
       D.remarks                        AS obsDup,
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

SELECT N.storeno                                                          AS loja,
       S.sname                                                            AS sigla,
       N.pdvno                                                            AS pdv,
       N.xano                                                             AS transacao,
       N.eordno                                                           AS pedido,
       CAST(N.pedidoDate AS DATE)                                         AS dataPedido,
       CAST(IF(nfAjuste = '', CONCAT(N.nfno, '/', N.nfse), '') AS CHAR)   AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')                                 AS fatura,
       CAST(N.issuedate AS DATE)                                          AS dataNota,
       IFNULL(N.custno, 0)                                                AS custno,
       IFNULL(N.fornecedorNome, '')                                       AS fornecedor,
       IFNULL(N.email, '')                                                AS email,
       N.fornecedorSap                                                    AS fornecedorSap,
       N.vendno                                                           AS vendno,
       IFNULL(R.rmk, '')                                                  AS rmk,
       SUM(N.valor)                                                       AS valor,
       IFNULL(obsNota, '')                                                AS obsNota,
       serie01Rejeitada                                                   AS serie01Rejeitada,
       @PAGO := IF((D.valorDevido IS NOT NULL AND D.valorDevido <= 0) OR
		   (D.status IS NOT NULL AND D.status = 2) OR
		   (N.serie01Pago = 'S' AND D.storeno IS NULL), 'S', 'N') AS serie01Pago,
       serie01Coleta                                                      AS serie01Coleta,
       serie66Pago                                                        AS serie66Pago,
       remessaConserto                                                    AS remessaConserto,
       remarks                                                            AS remarks,
       baseIcms                                                           AS baseIcms,
       valorIcms                                                          AS valorIcms,
       baseIcmsSubst                                                      AS baseIcmsSubst,
       icmsSubst                                                          AS icmsSubst,
       valorFrete                                                         AS valorFrete,
       valorSeguro                                                        AS valorSeguro,
       outrasDespesas                                                     AS outrasDespesas,
       valorIpi                                                           AS valorIpi,
       valorTotal                                                         AS valorTotal,
       N.obsPedido                                                        AS obsPedido,
       N.nfse                                                             AS tipo,
       IFNULL(RV.rmk, '')                                                 AS rmkVend,
       chave                                                              AS chave,
       natureza                                                           AS natureza,
       IF(@PAGO = 'S', IFNULL(D.obsDup, ''), N.chaveDesconto)             AS chaveDesconto,
       N.observacaoAuxiliar                                               AS observacaoAuxiliar,
       dataAgenda                                                         AS dataAgenda,
       nfAjuste                                                           AS nfAjuste
FROM TNF                        AS N
  INNER JOIN sqldados.store     AS S
	       ON S.no = N.storeno
  LEFT JOIN  sqldados.nfdevRmk  AS R
	       USING (storeno, pdvno, xano)
  LEFT JOIN  sqldados.nfvendRmk AS RV
	       ON RV.vendno = N.vendno AND RV.tipo = N.nfse
  LEFT JOIN  TDUP               AS D
	       ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
  LEFT JOIN  sqldados.eordrk    AS O
	       ON O.storeno = N.storeno AND O.ordno = N.eordno
WHERE (IFNULL(status, 0) <> 5)
  AND ((D.fatura IS NOT NULL OR serie01Pago = 'N') OR N.nfse = '66')
  AND N.fornecedorNome IS NOT NULL
GROUP BY loja, pdv, transacao, dataNota, custno