DO @SERIE := :serie;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       V.no                                                             AS vendno,
       N.issuedate,
       N.eordno,
       O.date                                                           AS pedidoDate,
       C.no                                                             AS custno,
       C.name                                                           AS fornecedorNome,
       N.grossamt / 100                                                 AS valor,
       CONCAT(TRIM(N.remarks), '\n', TRIM(IFNULL(R2.remarks__480, ''))) AS obsNota
FROM sqldados.nf              AS N
  LEFT JOIN sqldados.nfdevRmk AS R
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.nfrmk    AS R2
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.eord     AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.custp    AS C
	      ON C.no = N.custno
  LEFT JOIN sqldados.vend     AS V
	      ON C.cpf_cgc = V.cgc
WHERE N.nfse = @SERIE
  AND N.storeno IN (2, 3, 4, 5)
  AND C.name NOT LIKE '%ENGECOPI%'
  AND N.status <> 1
  AND N.tipo = 2
GROUP BY storeno, nfno, nfse;

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
       cast(N.pedidoDate AS DATE)                AS dataPedido,
       cast(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')        AS fatura,
       cast(N.issuedate AS DATE)                 AS dataNota,
       N.custno                                  AS custno,
       N.fornecedorNome                          AS fornecedor,
       N.vendno                                  AS vendno,
       IFNULL(R.rmk, '')                         AS rmk,
       SUM(N.valor)                              AS valor,
       IFNULL(obsNota, '')                       AS obsNota
FROM TNF                       AS N
  INNER JOIN sqldados.store    AS S
	       ON S.no = N.storeno
  LEFT JOIN  sqldados.nfdevRmk AS R
	       USING (storeno, pdvno, xano)
  LEFT JOIN  TDUP              AS D
	       ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
WHERE (IFNULL(D.valorDevido, 100) > 0)
  AND (IFNULL(status, 0) <> 5)
GROUP BY loja, pdv, transacao, dataNota, custno