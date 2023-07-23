SELECT MIN(dataEmissao * 1) AS data
FROM sqldados.notasEntradaNdd
       INNER JOIN sqldados.produtosNdd USING (id)