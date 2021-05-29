SELECT codigoFor,
       V.nome                          AS nomeFor,
       VS.no                           AS vendno,
       N.storeno,
       numero,
       dataLancamento,
       dataVencimento,
       saldo,
       IFNULL(nf.grossamt / 100, 0.00) AS saldoSaci
FROM sqldados.vendSap      AS V
  INNER JOIN sqldados.nfSap   N
	       ON V.codigo = N.codigoFor
  LEFT JOIN  sqldados.vend AS VS
	       ON VS.auxLong4 = V.codigo
  LEFT JOIN  sqldados.nf
	       ON nf.storeno = N.storeno AND nf.nfno = N.numero * 1 AND nf.nfse = '1'
WHERE ROUND(saldo * 100) != IFNULL(nf.grossamt, 0)
  AND (V.nome LIKE CONCAT('%', :filtro, '%') OR :filtro = '')
  AND (V.codigo LIKE CONCAT(:filtro, '%') OR :filtro = '')

