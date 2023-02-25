SELECT loja,
       ni,
       nf,
       emissao,
       entrada,
       vendno,
       totalPrd,
       valorNF,
       carrno,
       carrName,
       cte,
       emissaoCte,
       entradaCte,
       valorCte,
       pesoBruto,
       cub,
       pesoCub,
       fretePeso,
       adValore,
       gris,
       taxa,
       outro,
       aliquota,
       icms,
       totalFrete,
       status
FROM sqldados.queryCte1234567
WHERE CASE :status
	WHEN 'T'
	  THEN TRUE
	WHEN 'A'
	  THEN status = 'A'
	WHEN 'P'
	  THEN status = 'P'
	ELSE FALSE
      END
  AND CASE :diferenca
	WHEN 'T'
	  THEN TRUE
	WHEN '>'
	  THEN ROUND(valorCte, 2) > ROUND(totalFrete, 2)
	WHEN '<'
	  THEN ROUND(valorCte, 2) < ROUND(totalFrete, 2)
	WHEN '='
	  THEN ROUND(valorCte, 2) = ROUND(totalFrete, 2)
	ELSE FALSE
      END