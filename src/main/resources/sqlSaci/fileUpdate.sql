UPDATE nfdevFile
SET date = :DATE,
    nome = :nome,
    file = :file
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano
  AND date = :date
  AND nome = :nome
