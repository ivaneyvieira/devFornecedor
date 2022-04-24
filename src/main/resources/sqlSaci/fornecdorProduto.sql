DROP TEMPORARY TABLE IF EXISTS T_PRDEND;
CREATE TEMPORARY TABLE T_PRDEND (
  PRIMARY KEY (prdno, vendno)
)
SELECT I.prdno,
       N.vendno,
       MAX(N.invno) AS invno
FROM sqldados.iprd        AS I
  INNER JOIN sqldados.inv AS N
	       USING (invno)
WHERE N.type = 0
  AND N.bits & POW(2, 4) = 0
  AND N.vendno != 0
  AND FALSE
GROUP BY I.prdno, N.vendno;

DROP TEMPORARY TABLE IF EXISTS T_PRDDATA;
CREATE TEMPORARY TABLE T_PRDDATA (
  PRIMARY KEY (vendno)
)
SELECT P.vendno,
       CAST(MAX(I.issue_date) AS DATE)              AS data,
       I.invno,
       CAST(CONCAT(I.nfname, '/', I.invse) AS CHAR) AS nota
FROM T_PRDEND             AS P
  INNER JOIN sqldados.inv AS I
	       USING (invno)
GROUP BY vendno;

DROP TEMPORARY TABLE IF EXISTS T_PRDVEND;
CREATE TEMPORARY TABLE T_PRDVEND (
  PRIMARY KEY (vendno, prdno)
)
SELECT V.no AS vendno,
       C.no AS custno,
       V.name,
       P.no AS prdno
FROM sqldados.prd           AS P
  INNER JOIN sqldados.vend  AS V
	       ON P.mfno = V.no
  LEFT JOIN  sqldados.custp AS C
	       ON V.cgc = C.cpf_cgc
GROUP BY V.no, prdno;

SELECT vendno,
       custno,
       name AS nomeFornecedor,
       data,
       invno,
       nota
FROM T_PRDVEND
  LEFT JOIN T_PRDDATA
	      USING (vendno)
GROUP BY vendno