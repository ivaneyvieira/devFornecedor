DO @PRDNO := (select no from sqldados.prd where no = (:prdno * 1));

SELECT K.prdno_kit                   AS prdnoK,
       K.prdno                       AS prdno,
       TRIM(MID(P.name, 1, 37))      AS descricao,
       SUM(ROUND(K.qtty / 1000))     AS quant,
       SUM(ROUND(P.cost / 10000, 4)) AS custo
FROM
  sqldados.prdkit           AS K
    INNER JOIN sqldados.prd AS P
               ON K.prdno = P.no
WHERE prdno_kit = @PRDNO
GROUP BY prdno_kit, prdno