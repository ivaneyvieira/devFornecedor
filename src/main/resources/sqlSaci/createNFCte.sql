USE sqldados;

DO @LOJA := :loja;
DO @DI := :di;
DO @DF := :df;
DO @DD := DATE_SUB(@DI, INTERVAL 6 MONTH) * 1;
DO @VEND := :vend;
DO @NI := :ni;
DO @NFNO := TRIM(:nfno);
DO @CARRNO := :carrno;
DO @NICTE := :niCte;
DO @CTE := :cte;
DO @TABNO := :tabno;

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
  AND (invno = @NICTE OR @NICTE = 0)
  AND (nfname = @CTE OR @CTE = 0)
GROUP BY storeno, cte;

DROP TEMPORARY TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS
SELECT I.storeno                                                                        AS loja,
       I.invno                                                                          AS ni,
       I.nfname                                                                         AS nf,
       CAST(I.issue_date AS DATE)                                                       AS emissao,
       CAST(I.date AS DATE)                                                             AS entrada,
       I.vendno                                                                         AS vendno,
       (I.prdamt / 100)                                                                 AS totalPrd,
       (grossamt / 100)                                                                 AS valorNF,
       I.carrno                                                                         AS carrno,
       SUBSTRING_INDEX(T.name, ' ', 1)                                                  AS carrName,
       I.auxLong2                                                                       AS cte,
       C.emissao                                                                        AS emissaoCte,
       C.entrada                                                                        AS entradaCte,
       C.valorNF                                                                        AS valorCte,
       (I.weight)                                                                       AS pesoBruto,
       @STRCUB := REPLACE(IF(LOCATE(' CUB ', I.remarks) > 0,
			     SUBSTRING_INDEX(MID(I.remarks, LOCATE('CUB ', I.remarks) + 4, 100),
					     ' ', 1), ''), ',', '.')                    AS strCub,
       @CUB := IF(@STRCUB = '', 0.00, @STRCUB * 1.00)                                   AS cub,
       @CUB * F.valorLivre2 / 100                                                       AS pesoCub,
       ROUND(IF(@CUB = 0, 1.00, @CUB * (F.valorLivre2 / 100)) * (F.fretePeso / 100), 2) AS fPeso,
       (F.freteValor / 1000)                                                            AS freteValor,
       F.freteGRIS / 1000                                                               AS freteGRIS,
       F.freteDespacho / 100                                                            AS taxa,
       0.00                                                                             AS outro,
       F.valorLivre1 / 100                                                              AS aliquota
FROM sqldados.inv            AS I
  INNER JOIN sqldados.carr      T
	       ON T.no = I.carrno
  INNER JOIN T_CTE           AS C
	       ON C.cte = I.auxLong2 AND C.storeno = I.storeno
  INNER JOIN sqldados.carrfr AS F
	       ON F.carrno = I.carrno AND F.tabelano = IF(@TABNO = 0, T.auxShort1, @TABNO)
WHERE (I.storeno = @LOJA OR @LOJA = 0)
  AND (I.date BETWEEN @DI AND @DF)
  AND (I.vendno = @VEND OR @VEND = 0)
  AND (I.invno = @NI OR @NI = 0)
  AND (I.nfname = @NFNO OR @NFNO = '')
  AND (I.carrno = @CARRNO OR @CARRNO = 0);

DROP TEMPORARY TABLE IF EXISTS T_NOTAS_CTE;
CREATE TEMPORARY TABLE T_NOTAS_CTE
SELECT loja,
       CAST(GROUP_CONCAT(DISTINCT ni ORDER BY ni SEPARATOR ' - ') AS char) AS ni,
       CAST(GROUP_CONCAT(DISTINCT nf ORDER BY ni SEPARATOR ' - ') AS char) AS nf,
       emissao,
       entrada,
       vendno,
       SUM(totalPrd)                                                       AS totalPrd,
       SUM(valorNF)                                                        AS valorNF,
       carrno,
       carrName,
       cte,
       emissaoCte,
       entradaCte,
       valorCte,
       SUM(pesoBruto)                                                      AS pesoBruto,
       cub,
       pesoCub,
       fPeso,
       freteValor,
       freteGRIS,
       taxa,
       outro,
       aliquota
FROM T_NOTAS
GROUP BY carrno, cte;

DROP TABLE IF EXISTS sqldados.queryCte1234567;
CREATE TABLE sqldados.queryCte1234567
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
       @ADVALORE := ROUND(valorNF * freteValor, 2)                                          AS adValore,
       @GRIS := ROUND(valorNF * freteGRIS, 2)                                               AS gris,
       taxa                                                                                 AS taxa,
       outro                                                                                AS outro,
       aliquota                                                                             AS aliquota,
       @ICMS :=
	 ROUND((fPeso + @ADVALORE + @GRIS + taxa + outro) / (100 - aliquota) * aliquota, 2) AS icms,
       ROUND(fPeso + @ADVALORE + @GRIS + taxa + outro + @ICMS, 2)                           AS totalFrete
FROM T_NOTAS_CTE
