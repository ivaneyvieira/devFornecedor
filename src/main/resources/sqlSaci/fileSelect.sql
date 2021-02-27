SELECT storeno,
       pdvno,
       xano,
       cast(date AS DATE) AS date,
       nome,
       file
FROM sqldados.nfdevFile
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano
