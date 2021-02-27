DELETE
FROM sqldados.nfdevFile
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano
  AND date = :date
  AND nome = :nome