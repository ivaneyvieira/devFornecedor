UPDATE sqldados.nf
SET c6 = TRIM(IFNULL(:chaveDesconto, ''))
WHERE storeno = :loja
  AND pdvno = :pdv
  AND xano = :transacao