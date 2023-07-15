INSERT INTO sqldados.agendaDemandas(date, titulo, conteudo, concluido, vendno, destino, origem)
  VALUE (:date, :titulo, :conteudo, 'N', :vendno, :destino, :origem)