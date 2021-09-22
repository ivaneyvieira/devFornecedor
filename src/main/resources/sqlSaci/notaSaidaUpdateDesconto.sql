UPDATE sqldados.nf
SET c6  = MID(TRIM(IFNULL(:chaveDesconto, '')), 1, 40),
    c5  = MID(TRIM(IFNULL(:chaveDesconto, '')), 41, 40),
    l15 = :dataAgenda
WHERE storeno = :loja
  AND pdvno = :pdv
  AND xano = :transacao