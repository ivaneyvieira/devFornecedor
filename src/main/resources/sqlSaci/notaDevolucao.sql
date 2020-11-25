DO @SERIE := :serie;

DROP TEMPORARY TABLE IF EXISTS TNF;
CREATE TEMPORARY TABLE TNF (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.storeno,
       N.pdvno,
       N.xano,
       N.nfno,
       N.nfse,
       V.no                                                              AS vendno,
       N.issuedate,
       N.eordno,
       O.date                                                            AS pedidoDate,
       C.no                                                              AS custno,
       C.name                                                            AS fornecedorNome,
       N.grossamt / 100                                                  AS valor,
       CONCAT(TRIM(N.remarks), '\n', TRIM(IFNULL(R2.remarks__480, '')))  AS obsNota,
       IF(N.remarks LIKE 'REJEI% NF% RETOR%' AND N.nfse = '1', 'S', 'N') AS Serie01Rejeitada,
       IF((N.remarks LIKE '%PAGO%') AND N.nfse = '1', 'S', 'N')          AS Serie01Pago,
       TRIM(N.remarks)                                                   AS remarks,
       N.netamt / 100                                                    AS baseIcms,
       N.icms_amt / 100                                                  AS valorIcms,
       N.baseIcmsSubst / 100                                             AS baseIcmsSubst,
       N.icmsSubst / 100                                                 AS icmsSubst,
       N.fre_amt / 100                                                   AS valorFrete,
       N.sec_amt / 100                                                   AS valorSeguro,
       N.discount / 100                                                  AS valorDesconto,
       0.00                                                              AS outrasDespesas,
       N.ipi_amt / 100                                                   AS valorIpi,
       grossamt / 100                                                    AS valorTotal
FROM sqldados.nf              AS N
  LEFT JOIN sqldados.nfdevRmk AS R
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.nfrmk    AS R2
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.eord     AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.custp    AS C
	      ON C.no = N.custno
		AND C.no not in (306263, 312585, 901705, 21295, 120420, 478, 102773, 21333, 709327,108751)
  LEFT JOIN sqldados.vend     AS V
	      ON C.cpf_cgc = V.cgc
WHERE N.nfse = @SERIE
  AND N.storeno IN (2, 3, 4, 5)
  AND N.status <> 1
  AND N.tipo = 2
GROUP BY N.storeno, N.nfno, N.nfse;

DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP (
  PRIMARY KEY (storeno, nfno, nfse)
)
SELECT N.nfstoreno                      AS storeno,
       N.nfno,
       N.nfse,
       D.status,
       CAST(N.dupno AS CHAR)            AS fatura,
       MAX(duedate)                     AS vencimento,
       SUM(amtdue - disc_amt - amtpaid) AS valorDevido
FROM sqldados.dup           AS D
  INNER JOIN sqldados.nfdup AS N
	       ON N.dupstoreno = D.storeno AND N.duptype = D.type AND N.dupno = D.dupno AND
		  N.dupse = D.dupse
  INNER JOIN TNF               NF
	       ON N.nfstoreno = NF.storeno AND N.nfno = NF.nfno AND N.nfse = NF.nfse
GROUP BY N.nfstoreno, N.nfno, N.nfse;

SELECT N.storeno                                 AS loja,
       S.sname                                   AS sigla,
       N.pdvno                                   AS pdv,
       N.xano                                    AS transacao,
       N.eordno                                  AS pedido,
       cast(N.pedidoDate AS DATE)                AS dataPedido,
       cast(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       IFNULL(CAST(D.fatura AS CHAR), '')        AS fatura,
       cast(N.issuedate AS DATE)                 AS dataNota,
       N.custno                                  AS custno,
       N.fornecedorNome                          AS fornecedor,
       N.vendno                                  AS vendno,
       IFNULL(R.rmk, '')                         AS rmk,
       SUM(N.valor)                              AS valor,
       IFNULL(obsNota, '')                       AS obsNota,
       Serie01Rejeitada                          AS serie01Rejeitada,
       Serie01Pago                               AS serie01Pago,
       remarks                                   AS remarks,
       baseIcms                                  AS baseIcms,
       valorIcms                                 AS valorIcms,
       baseIcmsSubst                             AS baseIcmsSubst,
       icmsSubst                                 AS icmsSubst,
       valorFrete                                AS valorFrete,
       valorSeguro                               AS valorSeguro,
       outrasDespesas                            AS outrasDespesas,
       valorIpi                                  AS valorIpi,
       valorTotal                                AS valorTotal
FROM TNF                       AS N
  INNER JOIN sqldados.store    AS S
	       ON S.no = N.storeno
  LEFT JOIN  sqldados.nfdevRmk AS R
	       USING (storeno, pdvno, xano)
  LEFT JOIN  TDUP              AS D
	       ON D.storeno = N.storeno AND D.nfno = N.nfno AND D.nfse = N.nfse
WHERE (IFNULL(D.valorDevido, 100) > 0)
  AND (IFNULL(status, 0) <> 5)
  AND ((D.fatura IS NOT NULL OR Serie01Pago = 'N') OR N.nfse = '66')
GROUP BY loja, pdv, transacao, dataNota, custno
