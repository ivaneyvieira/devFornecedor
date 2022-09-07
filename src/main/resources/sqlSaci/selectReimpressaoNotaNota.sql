SELECT CAST(data AS date) AS data,
       hora,
       loja,
       nota,
       tipo,
       usuario
FROM sqldados.reimpressaoNotaNovo
WHERE loja = :loja
  AND nota = :numero
ORDER BY data DESC, hora DESC