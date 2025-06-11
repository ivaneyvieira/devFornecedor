DO @PRDNO := ( SELECT no
               FROM
                 sqldados.prd
               WHERE no = (:prdno * 1) );

SELECT K.prdno_kit                    AS prdnoK,
       TRUNCATE(S.cm_real / 10000, 2) AS custoK,
       K.prdno                        AS prdno,
       TRIM(MID(P.name, 1, 37))       AS descricao,
       SUM(ROUND(K.qtty / 1000))      AS quant,
       SUM(ROUND(P.cost / 10000, 4))  AS custo
FROM
  sqldados.prdkit           AS K
    INNER JOIN sqldados.prd AS P
               ON K.prdno = P.no
    LEFT JOIN  sqldados.stk AS S
               ON K.prdno_kit = S.prdno
                 AND S.storeno = 4
WHERE prdno_kit = @PRDNO
GROUP BY prdno_kit, prdno