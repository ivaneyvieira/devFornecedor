SELECT O.storeno            AS loja,
       O.no                 AS pedido,
       CAST(O.date AS DATE) AS data,
       O.amt / 100          AS total,
       O.remarks            AS observacao
FROM sqldados.ords AS O
WHERE vendno = :vendno
  AND O.amt > 0