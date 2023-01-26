INSERT INTO sqldados.agendaDemandas(date, titulo, conteudo, concluido, vendno)
VALUE (:date, :titulo, :conteudo, 'N', :vendno)