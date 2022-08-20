SELECT CAST(data AS date) AS data,
       hora,
       loja,
       nota,
       tipo,
       usuario
FROM sqldados.reimpressaoNota
ORDER BY data DESC, hora DESC