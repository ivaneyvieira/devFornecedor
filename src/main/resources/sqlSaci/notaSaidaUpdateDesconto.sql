UPDATE sqldados.nf
SET c6  = MID(TRIM(IFNULL(:chaveDesconto, '')), 1, 40),
    c5  = MID(TRIM(IFNULL(:chaveDesconto, '')), 41, 40),
    c4  = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 1, 40),
    c3  = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 41, 40),
    l15 = :dataAgenda
WHERE storeno = :loja
  AND pdvno = :pdv
  AND xano = :transacao;

REPLACE INTO sqldados.nfComplemento(storeno, pdvno, xano, chaveDesconto, observacaoAuxiliar,
				    dataAgenda, pedidos)
SELECT :loja      AS storeno,
       :pdv       AS pdvno,
       :transacao AS xano,
       :chaveDesconto,
       :observacaoAuxiliar,
       :dataAgenda,
       :pedidos
FROM DUAL

