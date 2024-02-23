DO @LOJA := :loja;
DO @FILTRO := :filtro;
DO @FILTRO_INT := IF(TRIM(@FILTRO) REGEXP '^[0-9]+$', TRIM(@FILTRO) * 1, 0);

DROP TEMPORARY TABLE IF EXISTS TVEND;
CREATE TEMPORARY TABLE TVEND
(
  PRIMARY KEY (vendno)
)
SELECT V.no       AS vendno,
       C.no       AS custno,
       V.name     AS fornecedorNome,
       V.email,
       V.auxLong4 AS fornecedorSap
FROM sqldados.vend AS V
       LEFT JOIN sqldados.custp AS C
                 ON C.cpf_cgc = V.cgc
WHERE V.name NOT LIKE 'ENGECOPI%'
  AND ((V.name LIKE CONCAT('%', @FILTRO, '%') OR @FILTRO = '') AND
       (C.no = @FILTRO_INT OR V.no = @FILTRO_INT OR @FILTRO_INT = 0))
GROUP BY V.no;

DROP TEMPORARY TABLE IF EXISTS TINV;
CREATE TEMPORARY TABLE TINV
(
  PRIMARY KEY (invno)
)
SELECT I.*,
       SUM(X.amtpaid)  AS pagamento,
       MAX(X.duedate)  AS vencimento,
       TRIM(X.remarks) AS xObs
FROM sqldados.inv AS I
       INNER JOIN sqldados.invxa AS X
                  USING (invno)
WHERE X.amtpaid > 0
  AND X.paiddate > 0
  AND X.duedate >= 20100101
  AND I.invse = '1'
  AND I.type = 0
  AND (I.bits & POW(2, 4) = 0)
  AND (I.storeno IN (1, 2, 3, 4, 5, 6, 8))
  AND (I.storeno = @LOJA OR @LOJA = 0)
GROUP BY I.invno, I.grossamt
HAVING I.grossamt > pagamento;

SELECT N.storeno                                    AS loja,
       N.invno                                      AS ni,
       N.ordno                                      AS pedido,
       CAST(CONCAT(N.nfname, '/', N.invse) AS CHAR) AS nota,
       CAST(N.issue_date AS DATE)                   AS dataNota,
       CAST(N.vencimento AS DATE)                   AS vencimento,
       CAST(N.date AS DATE)                         AS dataEntrada,
       N.vendno                                     AS vendno,
       V.custno                                     AS custno,
       IFNULL(V.fornecedorNome, '')                 AS fornecedor,
       N.grossamt / 100                             AS valor,
       (N.grossamt - N.pagamento) / 100             AS desconto,
       N.pagamento / 100                            AS pagamento,
       IFNULL(N.xObs, '')                           AS obsNota,
       ''                                           AS chaveDesconto
FROM TINV AS N
       INNER JOIN TVEND AS V
                  ON N.vendno = V.vendno
