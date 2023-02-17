SELECT tabelano,
       carrno,
       nome
FROM sqldados.carrfr AS C
WHERE tabelano = :tabelano
  AND carrno = :carrno