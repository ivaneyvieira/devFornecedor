UPDATE sqldados.agendaDemandas
SET date      = :date,
    titulo    = :titulo,
    conteudo  = :conteudo,
    concluido = :concluido,
    vendno    = :vendno,
    destino   = :destino,
    origem    = :origem
WHERE id = :id