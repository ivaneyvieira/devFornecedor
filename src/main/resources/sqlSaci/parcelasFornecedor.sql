SELECT I.invno                                                                    AS ni,
       CAST(CONCAT(I.nfname, IF(I.invse = '', '', CONCAT('/', I.invse))) AS CHAR) AS nota,
       CAST(duedate AS DATE)                                                      AS dtVencimento,
       X.amtdue / 100                                                             AS valor,
       X.remarks                                                                  AS observacao
FROM sqldados.inv           AS I
  INNER JOIN sqldados.invxa AS X
	       USING (invno)
  LEFT JOIN  sqldados.ords  AS O
	       ON O.storeno = I.storeno AND O.no = I.ordno
WHERE I.vendno = :vendno
  AND X.duedate >= CURRENT_DATE * 1
