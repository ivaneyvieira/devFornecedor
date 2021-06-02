SELECT codigoFor,
       V.nome                                      AS nomeFor,
       VS.no                                       AS vendno,
       C.no                                        AS custno,
       N.storeno,
       CAST(CONCAT(nf.nfno, '/', nf.nfse) AS CHAR) AS nfSaci,
       numero,
       dataLancamento,
       dataVencimento,
       saldo,
       IFNULL(nf.grossamt / 100, 0.00)             AS saldoSaci
FROM sqldados.vendSap       AS V
  INNER JOIN sqldados.nfSap AS N
	       ON V.codigo = N.codigoFor
  INNER JOIN sqldados.vend  AS VS
	       ON VS.auxLong4 = V.codigo
  INNER JOIN sqldados.custp AS C
	       ON C.cpf_cgc = VS.cgc
  INNER JOIN sqldados.nf
	       ON (nf.storeno = N.storeno AND nf.nfno = N.numero * 1 AND nf.nfse = '1' AND
		   nf.custno = C.no)
WHERE (V.nome LIKE CONCAT('%', :filtro, '%') OR :filtro = '')
   OR (V.codigo LIKE CONCAT(:filtro, '%') OR :filtro = '')
UNION
SELECT codigoFor,
       V.nome                                      AS nomeFor,
       VS.no                                       AS vendno,
       C.no                                        AS custno,
       N.storeno,
       CAST(CONCAT(nf.nfno, '/', nf.nfse) AS CHAR) AS nfSaci,
       numero,
       dataLancamento,
       dataVencimento,
       saldo,
       IFNULL(nf.grossamt / 100, 0.00)             AS saldoSaci
FROM sqldados.vendSap       AS V
  INNER JOIN sqldados.nfSap    N
	       ON V.codigo = N.codigoFor
  INNER JOIN sqldados.vend  AS VS
	       ON VS.auxLong4 = V.codigo
  INNER JOIN sqldados.custp AS C
	       ON C.cpf_cgc = VS.cgc
  INNER JOIN sqldados.nf
	       ON (nf.storeno = N.storeno AND
		   SUBSTRING_INDEX(MID(nf.remarks, POSITION('SAP' IN nf.remarks) + 3, 100), ' ',
				   1) = N.numero AND nf.nfse = '1' AND nf.custno = C.no)
WHERE (V.nome LIKE CONCAT('%', :filtro, '%') OR :filtro = '')
   OR (V.codigo LIKE CONCAT(:filtro, '%') OR :filtro = '')

