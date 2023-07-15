DROP TEMPORARY TABLE IF EXISTS T;
CREATE TEMPORARY TABLE T
(
  PRIMARY KEY (nfekey)
)
SELECT nfekey,
       nfnumero * 1                                                        AS nfnumero,
       nfserie,
       nfdata * 1                                                          AS nfdata,
       nfhora,
       CONCAT(MID(rementente, 1, 2), '.', MID(rementente, 3, 3), '.', MID(rementente, 6, 3), '/',
              MID(rementente, 9, 4), '-', MID(rementente, 13, 2))          AS rementente,
       CONCAT(MID(destinatario, 1, 2), '.', MID(destinatario, 3, 3), '.', MID(destinatario, 6, 3),
              '/', MID(destinatario, 9, 4), '-', MID(destinatario, 13, 2)) AS destinatario
FROM sqldados.notasPendentes;

SELECT nfekey               AS nfekey,
       nfnumero             AS nfno,
       nfserie              AS nfse,
       CAST(nfdata AS date) AS dataNota,
       nfhora               AS horaNota,
       V.no                 AS vendno,
       S.no                 AS storeno,
       V.name               AS fornecedor,
       S.sname              AS loja
FROM T
       LEFT JOIN sqldados.vend AS V
                 ON V.cgc = rementente
       LEFT JOIN sqldados.store AS S
                 ON S.cgc = destinatario
WHERE V.name IS NOT NULL;