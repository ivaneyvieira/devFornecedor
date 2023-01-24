SELECT id,
       CAST(date AS DATE) AS date,
       titulo,
       conteudo,
       concluido
FROM sqldados.agendaDemandas
WHERE concluido = :concluido