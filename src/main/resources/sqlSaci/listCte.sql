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
       fPeso,
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