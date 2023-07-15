USE sqldados;

DROP TEMPORARY TABLE IF EXISTS TNFSACI;
CREATE TEMPORARY TABLE TNFSACI
(
  PRIMARY KEY (storeno, pdvno, xano)
)
SELECT N.*
FROM sqldados.nf AS N
       INNER JOIN sqldados.custp AS C
                  ON C.no = N.custno AND C.name LIKE 'ENGECOPI%'
WHERE N.remarks LIKE 'GARANTIA %'
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 7
  AND N.cfo = 5949
  AND N.nfse = '1'
UNION
DISTINCT
SELECT *
FROM sqldados.nf AS N
WHERE (N.nfse = :serie)
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 2
UNION
DISTINCT
SELECT *
FROM sqldados.nf AS N
WHERE :serie = ''
  AND N.nfse IN ('1', '66')
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 2
UNION
DISTINCT
SELECT N.*
FROM sqldados.nf AS N
       INNER JOIN sqldados.dup AS D
                  USING (storeno, pdvno, xano)
WHERE D.storeno IN (2, 3, 4, 5)
  AND ((D.status = 5 AND D.remarks LIKE '%RETORNO%') OR bankno_paid = 121)
GROUP BY storeno, pdvno, xano;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF
(
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       V.no                                                       AS vendno,
       N.issuedate,
       N.eordno,
       O.date                                                     AS pedidoDate,
       C.no                                                       AS custno,
       C.name                                                     AS fornecedorNome,
       C.email                                                    AS email,
       V.auxLong4                                                 AS fornecedorSap,
       N.grossamt / 100                                           AS valor,
       CONCAT(TRIM(N.remarks), '\n',
              TRIM(IFNULL(R2.remarks__480, '')))                  AS obsNota,
       IF(N.remarks LIKE 'REJEI% NF% RETOR%' AND N.nfse = '1', 'S',
          'N')                                                    AS serie01Rejeitada,
       IF((N.remarks LIKE '%PAGO%' || N.remarks LIKE '%RETORNO%') AND N.nfse = '1', 'S',
          'N')                                                    AS serie01Pago,
       IF((N.remarks LIKE '%COLETA%') AND N.nfse = '1', 'S', 'N') AS serie01Coleta,
       IF((N.remarks LIKE '%PAGO%' || N.remarks LIKE '%RETORNO%') AND N.nfse = '66', 'S',
          'N')                                                    AS serie66Pago,
       IF((N.remarks LIKE '%REMESSA%CONSERTO%'), 'S', 'N')        AS remessaConserto,
       TRIM(N.remarks)                                            AS remarks,
       N.netamt / 100                                             AS baseIcms,
       N.icms_amt / 100                                           AS valorIcms,
       N.baseIcmsSubst / 100                                      AS baseIcmsSubst,
       N.icmsSubst / 100                                          AS icmsSubst,
       N.fre_amt / 100                                            AS valorFrete,
       N.sec_amt / 100                                            AS valorSeguro,
       N.discount / 100                                           AS valorDesconto,
       0.00                                                       AS outrasDespesas,
       N.ipi_amt / 100                                            AS valorIpi,
       grossamt / 100                                             AS valorTotal,
       TRIM(IFNULL(OBS.remarks__480, ''))                         AS obsPedido,
       IFNULL(X.nfekey, '')                                       AS chave,
       IFNULL(OP.name, '')                                        AS natureza,
       IFNULL(chaveDesconto, '')                                  AS chaveDesconto,
       IFNULL(observacaoAuxiliar, '')                             AS observacaoAuxiliar,
       CAST(IF(dataAgenda = 0, NULL, dataAgenda) AS DATE)         AS dataAgenda,
       IFNULL(nfAjuste, '')                                       AS nfAjuste,
       IFNULL(dataNfAjuste, 0)                                    AS dataNfAjuste,
       IFNULL(pedidos, '')                                        AS pedidos
FROM TNFSACI AS N
       LEFT JOIN sqldados.nfComplemento NC
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.natop AS OP
                 ON OP.no = N.natopno
       LEFT JOIN sqldados.nfes AS X
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.nfdevRmk AS R
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.nfrmk AS R2
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.eord AS O
                 ON O.storeno = N.storeno AND O.ordno = N.eordno
       LEFT JOIN sqldados.eordrk AS OBS
                 ON OBS.storeno = N.storeno AND OBS.ordno = N.eordno
       LEFT JOIN sqldados.custp AS C
                 ON C.no = N.custno AND
                    C.no NOT IN (306263, 312585, 901705, 21295, 120420, 478, 102773, 21333,
                                 709327, 108751)
       LEFT JOIN sqldados.vend AS V
                 ON C.cpf_cgc = V.cgc
WHERE (N.nfse = :serie OR (N.remarks LIKE 'GARANTIA%') OR (:serie = '' AND (N.nfse IN ('1', '66'))))
  AND (V.no = :vendno OR :vendno = 0)
GROUP BY N.storeno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP
(
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.nfstoreno                      AS storeno,
       N.nfno,
       N.nfse,
       D.status,
       D.bankno_paid,
       CASE D.status
         WHEN 1
           THEN 'Em cobranca'
         WHEN 2
           THEN 'Quitada'
         WHEN 3
           THEN 'Cartorio'
         WHEN 4
           THEN 'No advogado'
         WHEN 5
           THEN 'Cancelada'
         WHEN 6
           THEN 'Perda'
         WHEN 7
           THEN 'Protestada'
         WHEN 8
           THEN 'Outros'
         WHEN 9
           THEN 'Pago parcial'
         ELSE 'Desconhecido'
         END                            AS statusDup,
       D.remarks                        AS obsDup,
       CAST(N.dupno AS CHAR)            AS fatura,
       MAX(duedate)                     AS vencimento,
       SUM(amtdue - disc_amt - amtpaid) AS valorDevido
FROM sqldados.dup AS D
       INNER JOIN sqldados.nfdup AS N
                  ON N.dupstoreno = D.storeno AND N.duptype = D.type AND N.dupno = D.dupno AND
                     N.dupse = D.dupse
       INNER JOIN TNF NF
                  ON N.nfstoreno = NF.storeno AND N.nfno = NF.nfno AND N.nfse = NF.nfse
GROUP BY N.nfstoreno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS T;
CREATE TEMPORARY TABLE `T`
(
  `loja`               smallint(6)                    NOT NULL DEFAULT '0',
  `sigla`              char(2)                                 DEFAULT '',
  `pdv`                smallint(6)                    NOT NULL DEFAULT '0',
  `transacao`          int(11)                        NOT NULL DEFAULT '0',
  `pedido`             int(11)                        NOT NULL DEFAULT '0',
  `dataPedido`         date                                    DEFAULT NULL,
  `nota`               varchar(14) CHARACTER SET utf8          DEFAULT NULL,
  `fatura`             varchar(11) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `dataNota`           date                                    DEFAULT NULL,
  `custno`             bigint(11)                     NOT NULL DEFAULT '0',
  `fornecedor`         varchar(40)                    NOT NULL DEFAULT '',
  `email`              varchar(40)                    NOT NULL DEFAULT '',
  `fornecedorSap`      int(10)                                 DEFAULT '0',
  `vendno`             int(10),
  `rmk`                varchar(500)                   NOT NULL,
  `valor`              decimal(45, 4)                          DEFAULT NULL,
  `obsNota`            varchar(500)                   NOT NULL,
  `serie01Rejeitada`   varchar(1) CHARACTER SET utf8  NOT NULL DEFAULT '',
  `serie01Pago`        varchar(1) CHARACTER SET utf8  NOT NULL DEFAULT '',
  `serie01Coleta`      varchar(1) CHARACTER SET utf8  NOT NULL DEFAULT '',
  `serie66Pago`        varchar(1) CHARACTER SET utf8  NOT NULL DEFAULT '',
  `remessaConserto`    varchar(1) CHARACTER SET utf8  NOT NULL DEFAULT '',
  `remarks`            varchar(40)                             DEFAULT NULL,
  `baseIcms`           decimal(23, 4)                          DEFAULT NULL,
  `valorIcms`          decimal(23, 4)                          DEFAULT NULL,
  `baseIcmsSubst`      decimal(23, 4)                          DEFAULT NULL,
  `icmsSubst`          decimal(23, 4)                          DEFAULT NULL,
  `valorFrete`         decimal(23, 4)                          DEFAULT NULL,
  `valorSeguro`        decimal(23, 4)                          DEFAULT NULL,
  `outrasDespesas`     decimal(3, 2)                  NOT NULL DEFAULT '0.00',
  `valorIpi`           decimal(23, 4)                          DEFAULT NULL,
  `valorTotal`         decimal(23, 4)                          DEFAULT NULL,
  `obsPedido`          varchar(200),
  `tipo`               char(2)                        NOT NULL DEFAULT '',
  `rmkVend`            varchar(200)                   NOT NULL,
  `chave`              varchar(60)                    NOT NULL DEFAULT '',
  `natureza`           varchar(28)                    NOT NULL DEFAULT '',
  `chaveDesconto`      varchar(200)                   NOT NULL,
  `observacaoAuxiliar` varchar(200)                   NOT NULL,
  `dataAgenda`         date                                    DEFAULT NULL,
  `nfAjuste`           varchar(50)                    NOT NULL DEFAULT '',
  `dataNfAjuste`       date                                    DEFAULT NULL,
  `pedidos`            varchar(160)                   NOT NULL DEFAULT '',
  `situacaoFatura`     varchar(12) CHARACTER SET utf8          DEFAULT '',
  `obsFatura`          varchar(60)                             DEFAULT NULL,
  `banco`              int(10)                                 DEFAULT '0'
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
SELECT N.storeno                                              AS loja,
       S.sname                                                AS sigla,
       N.pdvno                                                AS pdv,
       N.xano                                                 AS transacao,
       N.eordno                                               AS pedido,
       CAST(N.pedidoDate AS DATE)                             AS dataPedido,
       CAST(CONCAT(N.nfno, '/', N.nfse) AS CHAR)              AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')                     AS fatura,
       CAST(N.issuedate AS DATE)                              AS dataNota,
       IFNULL(N.custno, 0)                                    AS custno,
       IFNULL(N.fornecedorNome, '')                           AS fornecedor,
       IFNULL(N.email, '')                                    AS email,
       N.fornecedorSap                                        AS fornecedorSap,
       N.vendno                                               AS vendno,
       IFNULL(MID(R.rmk, 1, 500), '')                         AS rmk,
       SUM(N.valor)                                           AS valor,
       IFNULL(MID(obsNota, 1, 500), '')                       AS obsNota,
       serie01Rejeitada                                       AS serie01Rejeitada,
       @PAGO := IF((D.bankno_paid = 121) OR ((D.valorDevido IS NOT NULL AND D.valorDevido <= 0) OR
                                             (D.status IS NOT NULL AND D.status = 2) OR
                                             (N.serie01Pago = 'S' AND D.storeno IS NULL)), 'S',
                   'N')                                       AS serie01Pago,
       serie01Coleta                                          AS serie01Coleta,
       serie66Pago                                            AS serie66Pago,
       remessaConserto                                        AS remessaConserto,
       remarks                                                AS remarks,
       baseIcms                                               AS baseIcms,
       valorIcms                                              AS valorIcms,
       baseIcmsSubst                                          AS baseIcmsSubst,
       icmsSubst                                              AS icmsSubst,
       valorFrete                                             AS valorFrete,
       valorSeguro                                            AS valorSeguro,
       outrasDespesas                                         AS outrasDespesas,
       valorIpi                                               AS valorIpi,
       valorTotal                                             AS valorTotal,
       N.obsPedido                                            AS obsPedido,
       N.nfse                                                 AS tipo,
       IFNULL(MID(RV.rmk, 1, 200), '')                        AS rmkVend,
       chave                                                  AS chave,
       natureza                                               AS natureza,
       IF(@PAGO = 'S', IFNULL(D.obsDup, ''), N.chaveDesconto) AS chaveDesconto,
       N.observacaoAuxiliar                                   AS observacaoAuxiliar,
       dataAgenda                                             AS dataAgenda,
       nfAjuste                                               AS nfAjuste,
       CAST(IF(dataNfAjuste = 0, NULL, dataNfAjuste) AS date) AS dataNfAjuste,
       pedidos                                                AS pedidos,
       D.statusDup                                            AS situacaoFatura,
       TRIM(MID(D.obsDup, 1, 60))                             AS obsFatura,
       D.bankno_paid                                          AS banco
FROM TNF AS N
       LEFT JOIN sqldados.store AS S
                 ON S.no = N.storeno
       LEFT JOIN sqldados.nfdevRmk AS R
                 USING (storeno, pdvno, xano)
       LEFT JOIN sqldados.nfvendRmk AS RV
                 ON RV.vendno = N.vendno AND RV.tipo = N.nfse
       LEFT JOIN TDUP AS D
                 ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
       LEFT JOIN sqldados.eordrk AS O
                 ON O.storeno = N.storeno AND O.ordno = N.eordno
WHERE (IFNULL(status, 0) <> 5)
  AND ((D.fatura IS NOT NULL OR serie01Pago = 'N') OR N.nfse = '66')
  AND N.fornecedorNome IS NOT NULL
GROUP BY loja, pdv, transacao, dataNota, custno;

SELECT loja,
       sigla,
       pdv,
       transacao,
       pedido,
       dataPedido,
       nota,
       fatura,
       dataNota,
       custno,
       fornecedor,
       email,
       fornecedorSap,
       vendno,
       rmk,
       valor,
       obsNota,
       serie01Rejeitada,
       serie01Pago,
       serie01Coleta,
       serie66Pago,
       remessaConserto,
       remarks,
       baseIcms,
       valorIcms,
       baseIcmsSubst,
       icmsSubst,
       valorFrete,
       valorSeguro,
       outrasDespesas,
       valorIpi,
       valorTotal,
       obsPedido,
       tipo,
       rmkVend,
       chave,
       natureza,
       chaveDesconto,
       observacaoAuxiliar,
       dataAgenda,
       nfAjuste,
       dataNfAjuste,
       pedidos,
       situacaoFatura,
       obsFatura,
       banco
FROM T