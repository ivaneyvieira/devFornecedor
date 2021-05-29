INSERT INTO sqldados.nfSap(codigoFor, storeno, numero, dataLancamento, dataVencimento,
			   saldo) VALUE (:codigoFor,
					 :storeno,
					 :numero,
					 :dataLancamento,
					 :dataVencimento,
					 :saldo)
ON DUPLICATE KEY UPDATE dataLancamento = :dataLancamento,
			dataVencimento = :dataVencimento,
			saldo          = :saldo