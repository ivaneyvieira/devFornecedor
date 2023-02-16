USE sqldados;

DO @LOJA := :loja;
DO @DI := :di;
DO @DF := :df;
DO @DD := DATE_SUB(@DI, INTERVAL 6 MONTH) * 1;
DO @VEND := :vend;
DO @NI := :ni;
DO @NFNO := TRIM(:nfno);

DROP TEMPORARY TABLE IF EXISTS T_CTE;
CREATE TEMPORARY TABLE T_CTE (
  PRIMARY KEY (storeno, cte)
)
SELECT invno,
       vendno,
       storeno,
       date,
       nfname * 1                 AS cte,
       type,
       carrno,
       account,
       CAST(I.issue_date AS DATE) AS emissao,
       CAST(I.date AS DATE)       AS entrada,
       grossamt / 100             AS valorNF,
       COUNT(*)                   AS qt
FROM sqldados.inv AS I
WHERE nfname != ''
  AND account = '2.01.40'
  AND type = 0
  AND carrno = 0
  AND (storeno = @LOJA OR @LOJA = 0)
  AND (date >= @DD)
GROUP BY storeno, cte;

DROP TABLE IF EXISTS sqldados.queryCte1234567;
CREATE TABLE sqldados.queryCte1234567
SELECT I.storeno                                                                                 AS loja,
       I.invno                                                                                   AS ni,
       I.nfname                                                                                  AS nf,
       CAST(I.issue_date AS DATE)                                                                AS emissao,
       CAST(I.date AS DATE)                                                                      AS entrada,
       I.vendno                                                                                  AS vendno,
       I.prdamt / 100                                                                            AS totalPrd,
       grossamt / 100                                                                            AS valorNF,
       I.carrno                                                                                  AS carrno,
       SUBSTRING_INDEX(T.name, ' ', 1)                                                           AS carrName,
       I.auxLong2                                                                                AS cte,
       C.emissao                                                                                 AS emissaoCte,
       C.entrada                                                                                 AS entradaCte,
       C.valorNF                                                                                 AS valorCte,
       weight                                                                                    AS pesoBruto,
       @STRCUB := REPLACE(IF(LOCATE(' CUB ', I.remarks) > 0,
			     SUBSTRING_INDEX(MID(I.remarks, LOCATE('CUB ', I.remarks) + 4, 100),
					     ' ', 1), ''), ',',
			  '.')                                                                   AS strCub,
       @CUB := IF(@STRCUB = '', 1.00, @STRCUB * 1.00)                                            AS cub,
       @CUB * F.valorLivre2 / 100                                                                AS pesoFat,
       @FPESO := ROUND(@CUB * (F.valorLivre2 / 100) * (F.fretePeso / 100),
		       2)                                                                        AS fPeso,
       @ADVALORE :=
	 ROUND((grossamt / 100) * (F.freteValor / 1000), 2)                                      AS adValore,
       @GRIS := ROUND((grossamt / 100) * (F.freteGRIS / 1000), 2)                                AS gris,
       @TAXA := freteDespacho / 100                                                              AS taxa,
       @OUTRO := 0.00                                                                            AS outro,
       @ALIQUOTA := F.valorLivre1 / 100                                                          AS aliquota,
       @ICMS := ROUND((@FPESO + @ADVALORE + @GRIS + @TAXA + @OUTRO) / (100 - @ALIQUOTA) * @ALIQUOTA,
		      2)                                                                         AS icms,
       ROUND(@FPESO + @ADVALORE + @GRIS + @TAXA + @OUTRO + @ICMS, 2)                             AS totalFrete
FROM sqldados.inv            AS I
  INNER JOIN sqldados.carr      T
	       ON T.no = I.carrno
  INNER JOIN T_CTE           AS C
	       ON C.cte = I.auxLong2 AND C.storeno = I.storeno
  INNER JOIN sqldados.carrfr AS F
	       ON F.carrno = I.carrno AND F.tabelano = T.auxShort1
WHERE (I.storeno = @LOJA OR @LOJA = 0)
  AND (I.date BETWEEN @DI AND @DF)
  AND (I.vendno = @VEND OR @VEND = 0)
  AND (I.invno = @NI OR @NI = 0)
  AND (I.nfname = @NFNO OR @NFNO = '')