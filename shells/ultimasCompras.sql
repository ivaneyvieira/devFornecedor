SELECT I.invno                                      AS ni,
       I.storeno                                    AS loja,
       CAST(CONCAT(I.nfname, '/', I.invse) AS CHAR) AS nfNumero,
       CAST(I.issue_date AS DATE)                   AS data,
       TRIM(P.prdno)                                AS codigo,
       P.grade                                      AS grade,
       P.qtty / 1000                                AS qttd,
       vendno                                       AS vendno,
       IFNULL(R.form_label, '')                     AS rotulo
FROM sqldados.inv            AS I
  INNER JOIN sqldados.iprd   AS P
	       USING (invno)
  LEFT JOIN  sqldados.prdalq AS R
	       USING (prdno)
WHERE I.type = 0
  AND I.storeno IN (2, 3, 4, 5)
  AND I.cfo NOT IN (1910, 2910, 1916, 2916, 1949, 2949)
  AND I.bits & POW(2, 4) = 0
  AND I.auxShort13 & POW(2, 15) = 0
