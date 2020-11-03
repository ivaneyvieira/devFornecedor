SELECT X.storeno                AS loja,
       X.pdvno                  AS pdv,
       X.xano                   AS transacao,
       X.prdno                  AS codigo,
       TRIM(MID(P.name, 1, 37)) AS descricao,
       X.grade                  AS grade,
       X.qtty / 1000            AS qtde
FROM sqldados.xaprd2      AS X
  INNER JOIN sqldados.prd AS P
	       ON X.prdno = P.no
WHERE X.storeno = : loja
  AND X.prdno = :pdv
  AND X.xano = :transacao