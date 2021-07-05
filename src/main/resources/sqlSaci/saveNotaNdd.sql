INSERT INTO sqldados.notasEntradaNdd (id, numero, serie, dataEmissao, cnpjEmitente, nomeFornecedor,
				      cnpjDestinatario, ieEmitente, ieDestinatario, baseCalculoIcms,
				      baseCalculoSt, valorTotalProdutos, valorTotalIcms,
				      valorTotalSt, baseCalculoIssqn, chave, status, xmlAut,
				      xmlCancelado, xmlNfe, xmlDadosAdicionais) VALUE (:id, :numero,
										       :serie,
										       :dataEmissao,
										       :cnpjEmitente,
										       :nomeFornecedor,
										       :cnpjDestinatario,
										       :ieEmitente,
										       :ieDestinatario,
										       :baseCalculoIcms,
										       :baseCalculoSt,
										       :valorTotalProdutos,
										       :valorTotalIcms,
										       :valorTotalSt,
										       :baseCalculoIssqn,
										       :chave,
										       :status,
										       :xmlAut,
										       :xmlCancelado,
										       :xmlNfe,
										       :xmlDadosAdicionais)
ON DUPLICATE KEY UPDATE numero             = :numero,
			serie              = :serie,
			dataEmissao        = :dataEmissao,
			cnpjEmitente       = :cnpjEmitente,
			nomeFornecedor     = :nomeFornecedor,
			cnpjDestinatario   = :cnpjDestinatario,
			ieEmitente         = :ieEmitente,
			ieDestinatario     = :ieDestinatario,
			baseCalculoIcms    = :baseCalculoIcms,
			baseCalculoSt      = :baseCalculoSt,
			valorTotalProdutos = :valorTotalProdutos,
			valorTotalIcms     = :valorTotalIcms,
			valorTotalSt       = :valorTotalSt,
			baseCalculoIssqn   = :baseCalculoIssqn,
			chave              = :chave,
			status             = :status,
			xmlAut             = :xmlAut,
			xmlCancelado       = :xmlCancelado,
			xmlNfe             = :xmlNfe,
			xmlDadosAdicionais = :xmlDadosAdicionais