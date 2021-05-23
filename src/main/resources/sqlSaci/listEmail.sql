SELECT email
FROM sqldados.vend
WHERE no = :vendno
UNION
DISTINCT
SELECT R.email AS email
FROM sqldados.repven     AS RV
  LEFT JOIN sqldados.rep AS R
	      ON R.no = RV.repno
WHERE RV.vendno = :vendno
ORDER BY 1