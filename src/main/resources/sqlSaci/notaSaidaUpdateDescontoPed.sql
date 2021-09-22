UPDATE sqldados.eord
SET c4  = MID(TRIM(IFNULL(:chaveDesconto, '')), 1, 40),
    c5  = MID(TRIM(IFNULL(:chaveDesconto, '')), 41, 40),
    l11 = :dataAgenda
WHERE storeno = :loja
  AND ordno = :pedido