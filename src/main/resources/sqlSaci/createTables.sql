DROP TABLE vendSap;
CREATE TABLE vendSap (
  codigo          Int,
  nome            varchar(100),
  quantidadeNotas Int,
  PRIMARY KEY (codigo)
);

DROP TABLE nfSap;
CREATE TABLE nfSap (
  codigoFor      Int,
  numero         varchar(20),
  dataLancamento Date,
  dataVencimento DATE,
  saldo          DECIMAL(13, 4),
  PRIMARY KEY (codigoFor, numero)
)