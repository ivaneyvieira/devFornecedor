DROP TEMPORARY TABLE IF EXISTS T_PEDIDO;
CREATE TEMPORARY TABLE T_PEDIDO
SELECT X.storeno                            AS loja,
       N.ordno                              AS pedido,
       X.prdno                              AS codigo,
       P.mfno_ref                           AS refFor,
       TRIM(MID(P.name, 1, 37))             AS descricao,
       X.grade                              AS grade,
       ROUND(X.qtty / 1000)                 AS qtde,
       X.fob / 100                          AS valorUnitario,
       ROUND(X.qtty / 1000) * (X.fob / 100) AS valorTotal,
       I.ipi / 10000                        AS ipiAliq,
       I.costdel3 / 10000                   AS stAliq,
       IFNULL(B.barcode, P.barcode)         AS barcode,
       TRIM(MID(P.name, 37, 3))             AS un,
       P.taxno                              AS st
FROM sqldados.iprd           AS X
  LEFT JOIN  sqldados.inv    AS N
	       ON N.invno = X.invno
  LEFT JOIN  sqldados.prp    AS I
	       ON I.storeno = 10 AND I.prdno = X.prdno
  LEFT JOIN  sqldados.prdbar AS B
	       ON B.prdno = X.prdno AND B.grade = X.grade
  INNER JOIN sqldados.prd    AS P
	       ON X.prdno = P.no
WHERE X.storeno = :loja
  AND X.invno = :invno
GROUP BY X.storeno, X.invno, X.prdno, X.grade;

DROP TEMPORARY TABLE IF EXISTS T_PRD;
CREATE TEMPORARY TABLE T_PRD (
  PRIMARY KEY (prdno, grade)
)
SELECT codigo AS prdno, grade
FROM T_PEDIDO
GROUP BY codigo, grade;


DROP TEMPORARY TABLE IF EXISTS T_PRD_ULT;
CREATE TEMPORARY TABLE T_PRD_ULT (
  PRIMARY KEY (invno, prdno, grade)
)
SELECT prdno,
       grade,
       MAX(invno) AS invno
FROM sqldados.inv          AS I
  INNER JOIN sqldados.iprd AS P
	       USING (invno)
  INNER JOIN T_PRD
	       USING (prdno, grade)
WHERE I.bits & POW(2, 4) = 0
  AND I.auxShort13 & POW(2, 15) = 0
  AND I.cfo NOT IN (1910, 2910, 1916, 2916, 1949, 2949)
  AND I.type = 0
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_INV;
CREATE TEMPORARY TABLE T_INV (
  PRIMARY KEY (codigo, grade)
)
SELECT prdno                                                        AS codigo,
       grade,
       invno,
       vendno,
       IFNULL(R.form_label, '')                                     AS rotulo,
       CAST(CONCAT(I.storeno, ' ', I.nfname, '/', I.invse) AS CHAR) AS notaInv,
       CAST(I.issue_date AS DATE)                                   AS dateInv,
       P.fob / 100                                                  AS valorUnitInv,
       SUM(qtty / 1000)                                             AS quantInv,
       IFNULL(X.nfekey, '')                                         AS chaveUlt,
       cstIcms                                                      AS cst,
       cfop,
       IF(LENGTH(P.c1) < 30, 'N', 'S')                              AS sefazOk
FROM sqldados.iprd           AS P
  INNER JOIN sqldados.inv    AS I
	       USING (invno)
  LEFT JOIN  sqldados.invnfe AS X
	       USING (invno)
  INNER JOIN T_PRD_ULT
	       USING (invno, prdno, grade)
  LEFT JOIN  sqldados.prdalq AS R
	       USING (prdno)
GROUP BY prdno, grade;

SELECT loja,
       0                                                 AS pdv,
       0                                                 AS transacao,
       codigo,
       refFor,
       descricao,
       grade,
       qtde,
       valorUnitario,
       valorTotal,
       IFNULL(ipiAliq * valorTotal, 0.00)                AS ipi,
       0.00                                              AS baseSt,
       IFNULL(stAliq * valorTotal, 0.00)                 AS vst,
       cst                                               AS cst,
       cfop                                              AS cfop,
       IFNULL((ipiAliq + stAliq + 1) * valorTotal, 0.00) AS valorTotalIpi,
       barcode,
       un,
       st,
       IFNULL(invno, 0)                                  AS invno,
       IFNULL(vendno, 0)                                 AS vendno,
       IFNULL(rotulo, '')                                AS rotulo,
       ROUND(IFNULL(quantInv, 0.00))                     AS quantInv,
       IFNULL(notaInv, '')                               AS notaInv,
       dateInv                                           AS dateInv,
       IFNULL(valorUnitInv, 0.00)                        AS valorUnitInv,
       IFNULL(valorUnitInv, 0.00) * qtde                 AS valorTotalInv,
       chaveUlt                                          AS chaveUlt,
       ''                                                AS ncm,
       0.00                                              AS baseICMS,
       0.00                                              AS valorICMS,
       0.00                                              AS baseIPI,
       0.00                                              AS valorIPI,
       0.00                                              AS icmsAliq,
       0.00                                              AS ipiAliq,
       sefazOk                                           AS sefazOk
FROM T_PEDIDO
  LEFT JOIN T_INV
	      USING (codigo, grade)