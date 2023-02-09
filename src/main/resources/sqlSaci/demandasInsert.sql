INSERT INTO sqldados.agendaDemandas(date, titulo, conteudo, concluido, vendno, destino)
VALUE (:date, :titulo, :conteudo, 'N', :vendno, :destino)