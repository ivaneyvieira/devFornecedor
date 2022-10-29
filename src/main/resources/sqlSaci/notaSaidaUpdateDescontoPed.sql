UPDATE sqldados.eord
SET c4         = MID(TRIM(IFNULL(:chaveDesconto, '')), 1, 40),
    c5         = MID(TRIM(IFNULL(:chaveDesconto, '')), 41, 40),
    auxString  = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 1, 20),
    auxString2 = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 21, 40),
    auxString4 = :pedidos,
    l11        = :dataAgenda
WHERE storeno = :loja
  AND ordno = :pedido