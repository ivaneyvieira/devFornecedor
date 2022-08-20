SELECT CAST(data AS date) AS data,
       hora,
       loja,
       nota,
       tipo,
       usuario
FROM sqldados.reimpressaoNota
WHERE loja = :loja
  AND nota = :numero
  AND usuario = :login
ORDER BY data DESC, hora DESC