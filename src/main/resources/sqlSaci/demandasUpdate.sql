UPDATE sqldados.agendaDemandas
SET date     = :date,
    titulo   = :titulo,
    conteudo = :conteudo,
    concluido = :concluido,
    vendno = :vendno
WHERE id = :id