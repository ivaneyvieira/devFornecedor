DROP TEMPORARY TABLE IF EXISTS T_NF;
CREATE TEMPORARY TABLE T_NF
SELECT X.storeno                       AS loja,
       X.pdvno                         AS pdv,
       X.xano                          AS transacao,
       X.prdno                         AS codigo,
       TRIM(MID(P.name, 1, 37))        AS descricao,
       X.grade                         AS grade,
       ROUND(X.qtty)                   AS qtde,
       X.price / 100                   AS valorUnitario,
       ROUND(X.qtty) * (X.price / 100) AS valorTotal,
       IFNULL(B.barcode, P.barcode)    AS barcode,
       TRIM(MID(P.name, 37, 3))        AS un
FROM sqldados.xaprd          AS X
  LEFT JOIN  sqldados.prdbar AS B
	       USING (prdno, grade)
  INNER JOIN sqldados.prd    AS P
	       ON X.prdno = P.no
WHERE X.storeno = :loja
  AND X.pdvno = :pdv
  AND X.xano = :transacao;

DROP TEMPORARY TABLE IF EXISTS T_PRD;
CREATE TEMPORARY TABLE T_PRD (
  PRIMARY KEY (prdno, grade)
)
SELECT codigo AS prdno, grade
FROM T_NF greoup
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
  AND I.auxShort13 & pow(2, 15) = 0
  AND I.cfo NOT IN (1910, 2910, 1916, 2916, 1949, 2949)
  AND I.type = 0
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_INV;
CREATE TEMPORARY TABLE T_INV (
  PRIMARY KEY (codigo, grade)
)
SELECT prdno            AS codigo,
       grade,
       invno,
       SUM(qtty / 1000) AS quantInv
FROM iprd
  INNER JOIN T_PRD_ULT
	       USING (invno, prdno, grade)
GROUP BY prdno, grade;

SELECT loja,
       pdv,
       transacao,
       codigo,
       descricao,
       grade,
       qtde,
       valorUnitario,
       valorTotal,
       barcode,
       un,
       invno,
       ROUND(quantInv) AS quantInv
FROM T_NF
  LEFT JOIN T_INV
	      USING (codigo, grade)

