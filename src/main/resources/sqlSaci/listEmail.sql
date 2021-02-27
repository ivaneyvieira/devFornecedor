select email
from sqldados.vend
where no = :vendno
union
distinct
SELECT R.email AS email
FROM sqldados.repven     AS RV
  LEFT JOIN sqldados.rep AS R
	      ON R.no = RV.repno
WHERE RV.vendno = :vendno
order by 1