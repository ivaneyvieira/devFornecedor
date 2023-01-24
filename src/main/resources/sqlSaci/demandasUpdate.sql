UPDATE sqldados.agendaDemandas
SET date     = :date,
    titulo   = :titulo,
    conteudo = :conteudo,
    concluido = :concluido
WHERE id = :id