DROP TEMPORARY TABLE IF EXISTS T_PEDIDO;
CREATE TEMPORARY TABLE T_PEDIDO
SELECT X.storeno                              AS loja,
       X.ordno                                AS pedido,
       X.prdno                                AS codigo,
       TRIM(MID(P.name, 1, 37))               AS descricao,
       X.grade                                AS grade,
       ROUND(X.qtty / 1000)                   AS qtde,
       X.price / 100                          AS valorUnitario,
       ROUND(X.qtty / 1000) * (X.price / 100) AS valorTotal,
       IFNULL(B.barcode, P.barcode)           AS barcode,
       TRIM(MID(P.name, 37, 3))               AS un
FROM sqldados.eoprd          AS X
  LEFT JOIN  sqldados.prdbar AS B
	       USING (prdno, grade)
  INNER JOIN sqldados.prd    AS P
	       ON X.prdno = P.no
WHERE X.storeno = :loja
  AND X.ordno = :pedido
GROUP BY X.storeno, X.ordno, X.prdno, X.grade;

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

select loja,
       0                             as pdv,
       0                             as transacao,
       codigo,
       descricao,
       grade,
       qtde,
       valorUnitario,
       valorTotal,
       barcode,
       un,
       IFNULL(invno, 0)              as invno,
       ROUND(IFNULL(quantInv, 0.00)) AS quantInv
from T_PEDIDO
  LEFT JOIN T_INV
	      USING (codigo, grade)
