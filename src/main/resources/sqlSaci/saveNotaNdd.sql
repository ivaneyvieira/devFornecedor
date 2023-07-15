REPLACE INTO sqldados.notasEntradaNdd (id, numero, serie, dataEmissao, cnpjEmitente, nomeFornecedor,
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
                                                                                        :xmlDadosAdicionais);

DELETE
FROM sqldados.notasEntradaNdd
WHERE id = :id
  AND :cancelado = 'S';