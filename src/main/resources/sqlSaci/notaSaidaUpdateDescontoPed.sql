UPDATE sqldados.eord
SET c4         = MID(TRIM(IFNULL(:chaveDesconto, '')), 1, 40),
    c5         = MID(TRIM(IFNULL(:chaveDesconto, '')), 41, 40),
    auxString  = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 1, 20),
    auxString2 = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 21, 40),
    auxString4 = :pedidos,
    l11        = :dataAgenda
WHERE storeno = :loja
  AND ordno = :pedido;

/*
UPDATE sqldados.nfComplemento
SET pedidos            = :pedidos,
    chaveDesconto      = :chaveDesconto,
    observacaoAuxiliar = :observacaoAuxiliar,
    dataAgenda         = :dataAgenda
WHERE storeno = :loja
  AND xano = :pedido
  AND pdvno = 980
 */

REPLACE INTO sqldados.nfComplemento(storeno,
                                    pdvno,
                                    xano,
                                    pedidos,
                                    chaveDesconto,
                                    observacaoAuxiliar,
                                    dataAgenda)
SELECT :loja               AS storeno,
       980                 AS pdvno,
       :pedido             AS xano,
       :pedidos            AS pedidos,
       :chaveDesconto      AS chaveDesconto,
       :observacaoAuxiliar AS observacaoAuxiliar,
       :dataAgenda         AS dataAgenda
FROM DUAL