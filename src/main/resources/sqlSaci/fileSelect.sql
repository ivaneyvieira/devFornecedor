SELECT storeno,
       pdvno,
       xano,
       CAST(date AS DATE) AS date,
       nome,
       file
FROM sqldados.nfdevFile
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano
ORDER BY storeno, pdvno, xano, date, nome
