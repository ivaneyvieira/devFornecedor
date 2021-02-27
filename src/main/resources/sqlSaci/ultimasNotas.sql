DROP TEMPORARY TABLE IF EXISTS T;
CREATE TEMPORARY TABLE T (
  PRIMARY KEY (prdno, grade)
)
SELECT X.prdno, X.grade
FROM sqldados.nf            AS N
  INNER JOIN sqldados.xaprd AS X
	       USING (storeno, pdvno, xano)
  LEFT JOIN  sqldados.custp AS C
	       ON C.no = N.custno
WHERE N.issuedate >= 20200101
  AND N.nfse = 66
  AND N.storeno IN (2, 3, 4, 5)
  AND C.name NOT LIKE '%ENGECOPI%'
  AND N.status <> 1
GROUP BY prdno, grade;

SELECT I.invno                                      AS ni,
       I.storeno                                    AS loja,
       CAST(CONCAT(I.nfname, '/', I.invse) AS CHAR) AS nfNumero,
       CAST(I.issue_date AS DATE)                   AS data,
       P.prdno                                      AS codigo,
       P.grade                                      AS grade,
       P.qtty / 1000                                AS qttd
FROM sqldados.inv          AS I
  INNER JOIN sqldados.iprd AS P
	       USING (invno)
  INNER JOIN T
	       USING (prdno, grade)
WHERE I.date > 20150101
  AND I.type = 0
  AND I.storeno IN (2, 3, 4, 5)
  AND I.bits & POW(2, 4) = 0
  AND I.auxShort13 & pow(2, 15) = 0