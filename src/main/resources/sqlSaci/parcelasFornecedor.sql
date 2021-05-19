SELECT I.invno                                                                    AS ni,
       CAST(CONCAT(I.nfname, IF(I.invse = '', '', CONCAT('/', I.invse))) AS CHAR) AS nota,
       cast(duedate AS DATE)                                                      AS dtVencimento,
       X.amtdue / 100                                                             AS valor
FROM sqldados.inv           AS I
  INNER JOIN sqldados.invxa AS X
	       USING (invno)
WHERE I.vendno = :vendno
  AND X.duedate >= current_date * 1
