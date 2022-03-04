SELECT id,
       IFNULL(S.no, 0)                                              AS storeno,
       IFNULL(C.no, 0)                                              AS custno,
       IFNULL(V.name, N.nomeFornecedor)                             AS nome,
       IFNULL(V.no, 0)                                              AS codigoSaci,
       IFNULL(V.auxLong4, 0)                                        AS fornecedorSap,
       IFNULL(V.email, '')                                          AS email,
       IFNULL(V.remarks, '')                                        AS obs,
       numero,
       serie,
       dataEmissao,
       cnpjEmitente,
       cnpjDestinatario,
       ieEmitente,
       ieDestinatario,
       baseCalculoIcms,
       baseCalculoSt,
       valorTotalProdutos,
       valorTotalIcms,
       valorTotalSt,
       baseCalculoIssqn,
       chave,
       status,
       xmlAut,
       xmlCancelado,
       xmlNfe,
       xmlDadosAdicionais,
       IF(I.invno IS NULL, 'N', 'S')                                AS notaSaci,
       IFNULL(I.ordno, N.ordno)                                     AS ordno,
       IFNULL(TRIM(T.name), '')                                     AS transportadora,
       IFNULL(CAST(IF(I.auxLong2 = 0, '', I.auxLong2) AS CHAR), '') AS conhecimentoFrete,
       :temIPI                                                      AS temIPIS
FROM sqldados.notasEntradaNdd AS N
  LEFT JOIN sqldados.vend     AS V
	      ON V.cgc = N.cnpjEmitente
  LEFT JOIN sqldados.store    AS S
	      ON S.cgc = N.cnpjDestinatario
  LEFT JOIN sqldados.custp    AS C
	      ON C.cpf_cgc = N.cnpjDestinatario
  LEFT JOIN sqldados.inv      AS I
	      ON I.storeno = S.no AND I.nfname * 1 = N.numero * 1 AND I.invse = N.serie AND
		 I.vendno = V.no
  LEFT JOIN sqldados.carr     AS T
	      ON T.no = I.carrno
WHERE dataEmissao BETWEEN :dataInicial AND :dataFinal
  AND (chave = CONCAT('NFe', :chave) OR :chave = '')
  AND (IFNULL(V.name, N.nomeFornecedor) LIKE CONCAT(:filtro, '%') OR
       IFNULL(V.no, 0) = (:filtro * 1) OR :filtro = '')
HAVING CASE :tipo
	 WHEN 'RECEBER'
	   THEN notaSaci = 'N'
	 WHEN 'RECEBIDO'
	   THEN notaSaci = 'S'
	 WHEN 'TODOS'
	   THEN TRUE
	 ELSE FALSE
       END

