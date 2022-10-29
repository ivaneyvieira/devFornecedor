UPDATE sqldados.nf
SET c6  = MID(TRIM(IFNULL(:chaveDesconto, '')), 1, 40),
    c5  = MID(TRIM(IFNULL(:chaveDesconto, '')), 41, 40),
    c4  = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 1, 40),
    c3  = MID(TRIM(IFNULL(:observacaoAuxiliar, '')), 41, 40),
    l15 = :dataAgenda
WHERE storeno = :loja
  AND pdvno = :pdv
  AND xano = :transacao;


/*
CREATE TABLE sqldados.nfComplemento (
  storeno smallint(5) DEFAULT 0 NOT NULL,
  pdvno   smallint(5) DEFAULT 0 NOT NULL,
  xano    int(10)     DEFAULT 0 NOT NULL,
  pedidos varchar(160),
  PRIMARY KEY (storeno, pdvno, xano)
)
*/

REPLACE sqldados.nfComplemento(storeno, pdvno, xano, pedidos)
VALUES (:loja, :pdv, :transacao, :pedidos)