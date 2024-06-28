UPDATE sqldados.agendaDemandas
SET date      = :date,
    titulo    = :titulo,
    conteudo  = :conteudo,
    concluido = :concluido,
    vendno    = :vendno,
    destino   = :destino,
    origem    = :origem,
    userno    = :userno
WHERE id = :id