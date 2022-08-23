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
WHERE (loja = :loja OR :loja = 0)
ORDER BY data DESC, hora DESC