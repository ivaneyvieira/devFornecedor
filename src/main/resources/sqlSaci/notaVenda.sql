DO @VENDNO := :fornecedor * 1;
DO @NOTA := :nota * 1;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       V.no                                    AS vendno,
       N.issuedate,
       N.eordno,
       O.date                                  AS pedidoDate,
       cast(CONCAT(C.no, ' ', C.name) AS CHAR) AS fornecedor
FROM sqldados.nf              AS N
  LEFT JOIN sqldados.nfdevRmk AS R
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.eord     AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.custp    AS C
	      ON C.no = N.custno
  LEFT JOIN sqldados.vend     AS V
	      ON C.cpf_cgc = V.cgc
WHERE (N.issuedate BETWEEN :dataInicial AND :dataFinal OR :dataInicial = 0 OR :dataFinal = 0)
  AND (C.no = @VENDNO OR V.no = @VENDNO OR C.name LIKE CAST(CONCAT(:fornecedor, '%') AS CHAR) OR
       :fornecedor = '')
  AND (N.nfno = @NOTA OR :nota = '')
  AND (:fornecedor <> '' OR :nota <> '' OR (:dataInicial <> 0 AND :dataFinal <> 0))
  AND N.nfse = 1
  AND N.storeno IN (2, 3, 4, 5)
  AND C.name NOT LIKE '%ENGECOPI%'
  AND N.status <> 1
  AND N.tipo = 2
GROUP BY storeno, nfno, nfse;

DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.nfstoreno                AS storeno,
       N.nfno,
       N.nfse,
       CAST(N.dupno AS CHAR)      AS fatura,
       MAX(duedate)               AS vencimento,
       SUM(amtdue) - SUM(amtpaid) AS valorDevido
FROM sqldados.dup           AS D
  INNER JOIN sqldados.nfdup AS N
	       ON N.dupstoreno = D.storeno AND N.duptype = D.type AND N.dupno = D.dupno AND
		  N.dupse = D.dupse
  INNER JOIN TNF               NF
	       ON N.nfstoreno = NF.storeno AND N.nfno = NF.nfno AND N.nfse = NF.nfse
WHERE D.status <> 5
GROUP BY N.nfstoreno, N.nfno, N.nfse;

SELECT N.storeno                                 AS loja,
       N.pdvno                                   AS pdv,
       N.xano                                    AS transacao,
       N.eordno                                  AS pedido,
       cast(N.pedidoDate AS DATE)                AS dataPedido,
       cast(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')        AS fatura,
       cast(N.issuedate AS DATE)                 AS dataNota,
       N.fornecedor                              AS fornecedor,
       N.vendno                                  AS vendno,
       IFNULL(R.rmk, '')                         AS rmk
FROM TNF                      AS N
  LEFT JOIN sqldados.nfdevRmk AS R
	      USING (storeno, pdvno, xano)
  LEFT JOIN TDUP              AS D
	      ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
WHERE (IFNULL(D.valorDevido, 100) > 0)
GROUP BY loja, pdv, transacao
LIMIT 1000