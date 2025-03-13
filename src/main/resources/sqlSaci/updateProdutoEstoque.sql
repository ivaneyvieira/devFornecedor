REPLACE INTO sqldados.prdConferencia(storeno, prdno, grade, dataConferencia, valorConferencia)
VALUE (:storeno, :prdno, :grade, IF(:dataConferencia = 0, null, :dataConferencia), :valorConferencia)