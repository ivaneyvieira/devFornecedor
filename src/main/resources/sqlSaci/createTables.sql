DROP TABLE vendSap;
CREATE TABLE vendSap
(
  codigo          Int,
  nome            varchar(100),
  quantidadeNotas Int,
  PRIMARY KEY (codigo)
);

DROP TABLE nfSap;
CREATE TABLE nfSap
(
  codigoFor      Int,
  storeno        INT,
  numero         varchar(20),
  dataLancamento Date,
  dataVencimento DATE,
  saldo          DECIMAL(13, 4),
  PRIMARY KEY (codigoFor, numero)
);

DROP TABLE vendSap;
CREATE TABLE vendSap
(
  codigo INT,
  nome   VARCHAR(100),
  PRIMARY KEY (codigo)
);

DROP TABLE nfSap;
CREATE TABLE nfSap
(
  codigoFor      INT,
  storeno        INT,
  numero         VARCHAR(20),
  dataLancamento DATE,
  dataVencimento DATE,
  saldo          DECIMAL(13, 4),
  PRIMARY KEY (codigoFor, numero)
);


DROP TABLE sqldados.notasEntradaNdd;
CREATE TABLE sqldados.notasEntradaNdd
(
  id                 int,
  numero             int,
  serie              int,
  dataEmissao        date,
  cnpjEmitente       varchar(20),
  nomeFornecedor     varchar(100),
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
  ordno              int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  INDEX i1 (dataEmissao)
);

ALTER TABLE sqldados.notasEntradaNdd
  MODIFY COLUMN NUMERO VARCHAR(32);

ALTER TABLE sqldados.notasEntradaNdd
  MODIFY COLUMN SERIE VARCHAR(4);

CREATE TABLE sqldados.reimpressaoNota
(
  data    int,
  hora    varchar(10),
  loja    int,
  nota    varchar(20),
  tipo    varchar(20),
  usuario varchar(30)
);

ALTER TABLE sqldados.reimpressaoNota
  ADD INDEX (loja, nota, usuario);

ALTER TABLE sqldados.reimpressaoNota
  ADD dataNota int;

ALTER TABLE sqldados.reimpressaoNota
  ADD codcli int;

ALTER TABLE sqldados.reimpressaoNota
  ADD nomecli varchar(100);

ALTER TABLE sqldados.reimpressaoNota
  ADD valor double;

TRUNCATE TABLE sqldados.reimpressaoNota;

ALTER TABLE sqldados.userApp
  ADD senhaPrint varchar(40) DEFAULT '';


DROP TABLE IF EXISTS sqldados.quantAvaria;
CREATE TABLE sqldados.quantAvaria
(
  id              Int,
  numeroProtocolo VARCHAR(50),
  codigo          VARCHAR(30),
  codBarra        VARCHAR(30),
  quantidade      DOUBLE,
  PRIMARY KEY (id, numeroProtocolo, codigo, codBarra)
);

DROP TABLE IF EXISTS sqldados.pedidosCompra;
CREATE TABLE sqldados.pedidosCompra
(
  `origem`           varchar(4)           DEFAULT '',
  `vendno`           int(10)     NOT NULL,
  `fornecedor`       char(40)    NOT NULL DEFAULT '',
  `cnpj`             char(20)    NOT NULL DEFAULT '',
  `loja`             smallint(5) NOT NULL DEFAULT '0',
  `sigla`            char(2)     NOT NULL DEFAULT '',
  `numeroPedido`     int(10)     NOT NULL DEFAULT '0',
  `status`           smallint(5) NOT NULL DEFAULT '0',
  `dataPedido`       date                 DEFAULT NULL,
  `dataEntrega`      date                 DEFAULT NULL,
  `obsercacaoPedido` char(36)    NOT NULL DEFAULT '',
  `codigo`           varchar(16) NOT NULL DEFAULT '',
  `seqno`            smallint(5) NOT NULL DEFAULT '0',
  `descricao`        varchar(37)          DEFAULT NULL,
  `refFab`           char(48)    NOT NULL DEFAULT '',
  `grade`            char(8)     NOT NULL DEFAULT '',
  `unidade`          varchar(3)           DEFAULT NULL,
  `refno`            varchar(40),
  `refname`          varchar(40)          DEFAULT NULL,
  `qtPedida`         double(17, 0)        DEFAULT NULL,
  `qtCancelada`      double(17, 0)        DEFAULT NULL,
  `qtRecebida`       double(17, 0)        DEFAULT NULL,
  `qtPendente`       double(17, 0)        DEFAULT NULL,
  `custoUnit`        double               DEFAULT NULL,
  `barcode`          varchar(16)          DEFAULT NULL,
  PRIMARY KEY (`loja`, `numeroPedido`, `codigo`, `grade`, `seqno`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

ALTER TABLE sqldados.pedidosCompra
  ADD COLUMN confirmado char(1);

/*
Pesquisa pedidos sem valor
*/
SELECT *
FROM sqldados.oprd
WHERE ordno = 404632
  AND storeno = 4
  AND prdno = 120528;

/*** Tabelas da agenda ***/

DROP TABLE IF EXISTS sqldados.agendaDemandas;
CREATE TABLE sqldados.agendaDemandas
(
  id       int NOT NULL AUTO_INCREMENT,
  date     int(10),
  titulo   VARCHAR(100),
  conteudo TEXT,
  PRIMARY KEY (id)
);

ALTER TABLE sqldados.agendaDemandas
  ADD concluido VARCHAR(1) DEFAULT 'N';

ALTER TABLE sqldados.agendaDemandas
  ADD vendno INT DEFAULT 0;

CREATE INDEX i1 ON sqldados.agendaDemandas (vendno);

/*************************/

ALTER TABLE sqldados.nfComplemento
  ADD chaveDesconto      TEXT,
  ADD observacaoAuxiliar TEXT,
  ADD dataAgenda         int(10) DEFAULT 0;

REPLACE INTO sqldados.nfComplemento(storeno, pdvno, xano, chaveDesconto, observacaoAuxiliar,
                                    dataAgenda, pedidos)
SELECT storeno,
       pdvno,
       xano,
       CONCAT(c6, c5)      AS chaveDesconto,
       CONCAT(c4, c3)      AS observacaoAuxiliar,
       l15                 AS dataAgenda,
       IFNULL(pedidos, '') AS pedidos
FROM sqldados.nf
       LEFT JOIN sqldados.nfComplemento
                 USING (storeno, pdvno, xano)
WHERE c6 != ''
   OR c4 != ''
   OR l15 != 0;
/****************************************************/

ALTER TABLE sqldados.agendaDemandas
  ADD destino VARCHAR(100) DEFAULT '';

ALTER TABLE sqldados.agendaDemandas
  ADD origem VARCHAR(100) DEFAULT '';

/**********************************************************/

DROP TABLE IF EXISTS T_DADOS;
CREATE TEMPORARY TABLE T_DADOS
SELECT E.storeno                         AS storeno,
       E.ordno                           AS xano,
       980                               AS pdvno,
       CONCAT(E.c4, E.c5)                AS chaveDesconto,
       CONCAT(E.auxString, E.auxString2) AS observacaoAuxiliar,
       E.l11                             AS dataAgenda,
       E.auxLong4                        AS pedidos
FROM sqldados.eord AS E
WHERE TRIM(CONCAT(E.c4, E.c5)) != ''
   OR TRIM(CONCAT(E.auxString, E.auxString2)) != ''
   OR E.l11 != 0
   OR E.auxLong4 != 0;

REPLACE INTO sqldados.nfComplemento(storeno, pdvno, xano, pedidos, chaveDesconto,
                                    observacaoAuxiliar,
                                    dataAgenda)
SELECT storeno,
       pdvno,
       xano,
       pedidos,
       chaveDesconto,
       observacaoAuxiliar,
       dataAgenda
FROM T_DADOS;

/*************************************************************************/

SELECT *
FROM sqldados.agendaDemandas
WHERE vendno != 0;


SELECT *
FROM sqldados.nfdevFile
WHERE pdvno = 8888;

/*************************************************************************/

CREATE TABLE sqldados.vendComplemento
(
  vendno int(10) NOT NULL,
  texto  text,
  PRIMARY KEY (vendno)
);

/*************************************************************************/
/* Localiza o campo cte na tabela inv */
SELECT I.auxLong2 AS cte
FROM sqldados.inv AS I;


/*************************************************************************/
/* Adicona campo nfAjuste na tabela nfComplemento */
ALTER TABLE sqldados.nfComplemento
  ADD nfAjuste VARCHAR(50) DEFAULT '';

ALTER TABLE sqldados.nfComplemento
  ADD dataNfAjuste int(10) DEFAULT 0;


CREATE INDEX i2
  ON sqldados.notasEntradaNdd (chave);


DROP TABLE IF EXISTS sqldados.produtosNdd;
CREATE TABLE sqldados.produtosNdd
(
  id          int NOT NULL,
  nItem       int NOT NULL,
  cProd       VARCHAR(60),
  cEAN        VARCHAR(14),
  xProd       VARCHAR(120),
  ncm         VARCHAR(8),
  cfop        VARCHAR(4),
  uCom        VARCHAR(6),
  qCom        DECIMAL(13, 4),
  vUnCom      DECIMAL(13, 4),
  vProd       DECIMAL(13, 4),
  indTot      VARCHAR(1),
  cstIcms     VARCHAR(3),
  baseIcms    DECIMAL(13, 4),
  percIcms    DECIMAL(13, 4),
  valorIcms   DECIMAL(13, 4),
  baseIpi     DECIMAL(13, 4),
  percIpi     DECIMAL(13, 4),
  valorIpi    DECIMAL(13, 4),
  basePis     DECIMAL(13, 4),
  percPis     DECIMAL(13, 4),
  valorPis    DECIMAL(13, 4),
  baseCofins  DECIMAL(13, 4),
  percCofins  DECIMAL(13, 4),
  valorCofins DECIMAL(13, 4),
  PRIMARY KEY (id, nItem, cProd)
);

alter TABLE sqldados.produtosNdd
  ADD COLUMN   cEANTrib    VARCHAR(14) AFTER cEAN;


REPLACE INTO sqldados.prdref(vendno, auxLong1, auxLong2, auxMoney1, auxMoney2, l1, l2, l3, l4, m1, m2, m3, m4, bits,
                             bits2, auxShort1, auxShort2, auxShort3, auxShort4, s1, s2, s3, s4, prdno, grade, prdrefno,
                             prdrefname, auxChar1, auxChar2, c1, c2, c3, c4)
SELECT mfno                   AS vendno,
       0                      AS auxLong1,
       0                      AS auxLong2,
       0                      AS auxMoney1,
       0                      AS auxMoney2,
       CURDATE() * 1          AS l1,
       0                      AS l2,
       0                      AS l3,
       0                      AS l4,
       0                      AS m1,
       0                      AS m2,
       0                      AS m3,
       0                      AS m4,
       0                      AS bits,
       0                      AS bits2,
       0                      AS auxShort1,
       0                      AS auxShort2,
       0                      AS auxShort3,
       0                      AS auxShort4,
       0                      AS s1,
       0                      AS s2,
       0                      AS s3,
       0                      AS s4,
       no                     AS prdno,
       ''                     AS grade,
       mfno_ref                  prdrefno,
       TRIM(MID(name, 1, 37)) AS prdrefname,
       TRIM(MID(name, 37, 3)) AS auxChar1,
       ''                     AS auxChar2,
       ''                     AS c1,
       ''                     AS c2,
       ''                     AS c3,
       ''                     AS c4
FROM sqldados.prd
WHERE grade_l = '0         0         0         0         0         0         0         0';


CREATE INDEX id1 ON sqldados.notasEntradaNdd (cnpjDestinatario, numero, serie);