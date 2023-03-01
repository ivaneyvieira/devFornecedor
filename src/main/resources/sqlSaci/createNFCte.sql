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
       I.storeno,
       date,
       nfname * 1                                   AS cte,
       I.type,
       carrno,
       account,
       CAST(I.issue_date AS DATE)                   AS emissao,
       CAST(I.date AS DATE)                         AS entrada,
       grossamt / 100                               AS valorNF,
       COUNT(*)                                     AS qt,
       IF(SUM(X.amtdue) > SUM(X.amtpaid), 'A', 'P') AS status
FROM sqldados.inv           AS I
  INNER JOIN sqldados.invxa AS X
	       USING (invno)
WHERE nfname != ''
  AND account = '2.01.40'
  AND I.type = 0
  AND carrno = 0
  AND (I.storeno = @LOJA OR @LOJA = 0)
  AND (date >= @DD)
  AND (invno = @NICTE OR @NICTE = 0)
  AND (nfname = @CTE OR @CTE = 0)
GROUP BY I.storeno, cte;

DROP TEMPORARY TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS
SELECT I.storeno                                                                               AS loja,
       I.invno                                                                                 AS ni,
       I.nfname                                                                                AS nf,
       CAST(I.issue_date AS DATE)                                                              AS emissao,
       CAST(I.date AS DATE)                                                                    AS entrada,
       I.vendno                                                                                AS vendno,
       (I.prdamt / 100)                                                                        AS totalPrd,
       (I.grossamt / 100)                                                                      AS valorNF,
       I.carrno                                                                                AS carrno,
       @CARR01 := SUBSTRING_INDEX(T.name, ' ', 1)                                              AS carrName01,
       @CARR02 := SUBSTRING_INDEX(T.name, ' ', 2)                                              AS carrName02,
       @CARR03 := SUBSTRING_INDEX(T.name, ' ', 3)                                              AS carrName03,
       IFNULL(IF(LENGTH(@CARR01) > 3, @CARR01, IF(LENGTH(@CARR02) > 3, @CARR02, @CARR03)),
	      '')                                                                              AS carrName,
       I.auxLong2                                                                              AS cte,
       C.status                                                                                AS status,
       C.emissao                                                                               AS emissaoCte,
       C.entrada                                                                               AS entradaCte,
       IFNULL(C.valorNF, 0.00)                                                                 AS valorCte,
       I.weight                                                                                AS pesoBrutoUnit,
       @STRCUB := REPLACE(IF(LOCATE('CUB ', I.remarks) > 0,
			     SUBSTRING_INDEX(MID(I.remarks, LOCATE('CUB ', I.remarks) + 4, 100),
					     ' ', 1), ''), ',',
			  '.')                                                                 AS strCub,
       IF(@STRCUB = '', 0.00, @STRCUB * 1.00)                                                  AS cub,
       IFNULL(F.valorLivre2 / 100, 0.00)                                                       AS fatorCub,
       IFNULL(F.freteValor / 1000, 0.000)                                                      AS freteValor,
       IFNULL(F.freteGRIS / 1000, 0.000)                                                       AS freteGRIS,
       IFNULL(F.freteDespacho / 100, 0.00)                                                     AS taxa,
       0.00                                                                                    AS outro,
       IFNULL(F.valorLivre1 / 100, 0.00)                                                       AS aliquota,
       T.fretePerc / 100                                                                       AS fretePerc,
       IFNULL(F.valorLivre3 / 100, 0.00)                                                       AS freteMinimo,
       IFNULL(F.valorLivre4 / 100, 0.00)                                                       AS pesoMinimo,
       (F.fretePeso / 100)                                                                     AS fretePeso
FROM sqldados.inv            AS I
  LEFT JOIN  sqldados.carr      T
	       ON T.no = I.carrno
  INNER JOIN T_CTE           AS C
	       ON C.cte = I.auxLong2 AND C.storeno = I.storeno
  LEFT JOIN  sqldados.carrfr AS F
	       ON F.carrno = I.carrno AND F.tabelano = I.carrno
WHERE (I.storeno = @LOJA OR @LOJA = 0)
  AND (I.date BETWEEN @DI AND @DF)
  AND (I.vendno = @VEND OR @VEND = 0)
  AND (I.invno = @NI OR @NI = 0)
  AND (I.nfname = @NFNO OR @NFNO = '')
  AND (I.carrno = @CARRNO OR @CARRNO = 0)
  AND (I.freight != 0)
  AND (I.auxLong2 != 0)
  AND (I.auxLong2 = @CTE OR @CTE = 0);

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
       status                                                              AS status,
       cte,
       emissaoCte,
       entradaCte,
       valorCte,
       SUM(pesoBrutoUnit)                                                  AS pesoBruto,
       cub,
       freteMinimo,
       pesoMinimo,
       fretePeso,
       IFNULL(cub * fatorCub, 0.00)                                        AS pesoCub,
       freteValor,
       freteGRIS,
       taxa,
       outro,
       aliquota,
       fretePerc
FROM T_NOTAS
GROUP BY carrno, cte;

DROP TEMPORARY TABLE IF EXISTS T_NOTAS_CTE01;
CREATE TEMPORARY TABLE T_NOTAS_CTE01
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
       status,
       cte,
       emissaoCte,
       entradaCte,
       valorCte,
       pesoBruto,
       cub,
       freteMinimo,
       pesoMinimo,
       pesoCub,
       IFNULL(ROUND(IF(pesoBruto > pesoCub, pesoBruto, pesoCub) * fretePeso, 2),
	      0.00) AS freteNormal,
       fretePeso,
       freteValor,
       freteGRIS,
       taxa,
       outro,
       aliquota,
       fretePerc
FROM T_NOTAS_CTE;

DROP TEMPORARY TABLE IF EXISTS T_NOTAS_CTE02;
CREATE TEMPORARY TABLE T_NOTAS_CTE02
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
       status,
       cte,
       emissaoCte,
       entradaCte,
       valorCte,
       pesoBruto,
       cub,
       pesoCub,
       CASE
	 WHEN freteMinimo > 0
	   THEN IF(pesoBruto > 100.00 || pesoCub > 100.00, freteNormal, freteMinimo)
	 WHEN pesoMinimo > 0
	   THEN IF(pesoBruto > pesoMinimo || pesoCub > pesoMinimo, freteNormal,
		   pesoMinimo * fretePeso)
	 ELSE freteNormal
       END AS fretePeso,
       freteValor,
       freteGRIS,
       taxa,
       outro,
       aliquota,
       fretePerc
FROM T_NOTAS_CTE01;

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
       status                                                                                   AS status,
       cte,
       emissaoCte,
       entradaCte,
       valorCte,
       pesoBruto,
       cub,
       pesoCub,
       fretePeso,
       @ADVALORE := ROUND(valorNF * freteValor, 2)                                              AS adValore,
       @GRIS := ROUND(valorNF * freteGRIS, 2)                                                   AS gris,
       taxa                                                                                     AS taxa,
       outro                                                                                    AS outro,
       aliquota                                                                                 AS aliquota,
       @ICMS := ROUND((fretePeso + @ADVALORE + @GRIS + taxa + outro) / (100 - aliquota) * aliquota,
		      2)                                                                        AS icms,
       IF(fretePerc = 0, ROUND(fretePeso + @ADVALORE + @GRIS + taxa + outro + @ICMS, 2),
	  ROUND(valorNF * fretePerc / 100, 2))                                                  AS totalFrete
FROM T_NOTAS_CTE02
