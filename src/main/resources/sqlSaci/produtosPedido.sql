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

select loja,
       codigo,
       descricao,
       grade,
       qtde,
       valorUnitario,
       valorTotal,
       barcode,
       un
from T_PEDIDO
