INSERT INTO sqldados.agendaDemandas(date, titulo, conteudo, concluido, vendno, destino, origem, userno)
  VALUE (:date, :titulo, :conteudo, 'N', :vendno, :destino, :origem, :userno)