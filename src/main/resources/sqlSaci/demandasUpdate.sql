UPDATE sqldados.agendaDemandas
SET date     = :date,
    titulo   = :titulo,
    conteudo = :conteudo
WHERE id = :id