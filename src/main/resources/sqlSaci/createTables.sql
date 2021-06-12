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
  storeno        INT,
  numero         varchar(20),
  dataLancamento Date,
  dataVencimento DATE,
  saldo          DECIMAL(13, 4),
  PRIMARY KEY (codigoFor, numero)
);

DROP TABLE vendSap;
CREATE TABLE vendSap (
  codigo INT,
  nome   VARCHAR(100),
  PRIMARY KEY (codigo)
);

DROP TABLE nfSap;
CREATE TABLE nfSap (
  codigoFor      INT,
  storeno        INT,
  numero         VARCHAR(20),
  dataLancamento DATE,
  dataVencimento DATE,
  saldo          DECIMAL(13, 4),
  PRIMARY KEY (codigoFor, numero)
);


DROP TABLE sqldados.notasNdd;
CREATE TABLE sqldados.notasNdd (
  id                 int,
  numero             int,
  serie              int,
  dataEmissao        date,
  cnpjEmitente       varchar(20),
  cnpjDestinatario   varchar(20),
  ieEmitente         varchar(14),
  ieDestinatario     varchar(14),
  baseCalculoIcms    decimal(18, 2),
  baseCalculoSt      decimal(18, 2),
  valorTotalProdutos decimal(18, 2),
  valorTotalIcms     decimal(18, 2),
  valorTotalSt       decimal(18, 2),
  baseCalculoIssqn   decimal(18, 2),
  chave              varchar(50),
  status             int,
  xmlAut             longtext,
  xmlCancelado       longtext,
  xmlNfe             longtext,
  xmlDadosAdicionais longtext,
  PRIMARY KEY (id)
);