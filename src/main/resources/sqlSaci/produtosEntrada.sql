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