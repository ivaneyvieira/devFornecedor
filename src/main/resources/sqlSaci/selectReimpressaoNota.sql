SELECT CAST(data AS date)     AS data,
       hora,
       loja,
       nota,
       tipo,
       usuario,
       CAST(dataNota AS date) AS dataNota,
       codcli,
       nomecli,
       valor
FROM sqldados.reimpressaoNota
ORDER BY data DESC, hora DESC