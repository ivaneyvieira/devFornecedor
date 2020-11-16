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
  AND X.xano = :transacao