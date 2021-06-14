SELECT id,
       IFNULL(S.no, 0)                  AS storeno,
       IFNULL(C.no, 0)                  AS custno,
       IFNULL(V.name, N.nomeFornecedor) AS nome,
       IFNULL(V.no, 0)                  AS codigoSaci,
       IFNULL(V.auxLong4, 0)            AS fornecedorSap,
       IFNULL(V.email, '')              AS email,
       IFNULL(V.remarks, '')            AS obs,
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
       IF(I.invno IS NULL, 'N', 'S')    AS notaSaci,
       IFNULL(I.ordno, N.ordno)         AS ordno
FROM sqldados.notasEntradaNdd AS N
  LEFT JOIN sqldados.vend     AS V
	      ON V.cgc = N.cnpjEmitente
  LEFT JOIN sqldados.store    AS S
	      ON S.cgc = N.cnpjDestinatario
  LEFT JOIN sqldados.custp    AS C
	      ON C.cpf_cgc = N.cnpjDestinatario
  LEFT JOIN sqldados.inv      AS I
	      ON I.storeno = S.no AND I.nfname = N.numero AND I.invse = N.serie AND I.vendno = V.no
WHERE dataEmissao BETWEEN :dataInicial AND :dataFinal
HAVING CASE :tipo
	 WHEN 'RECEBER'
	   THEN notaSaci = 'N'
	 WHEN 'RECEBIDO'
	   THEN notaSaci = 'S'
	 WHEN 'TODOS'
	   THEN TRUE
	 ELSE FALSE
       END AND (nome LIKE CONCAT(:filtro, '%') OR codigoSaci = (:filtro * 1) OR :filtro = '')

