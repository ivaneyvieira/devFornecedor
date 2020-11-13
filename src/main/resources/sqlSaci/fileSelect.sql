SELECT storeno,
       pdvno,
       xano,
       date,
       nome,
       file
FROM nfdevFile
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano
