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
SELECT I.storeno                                                     AS loja,
       I.invno                                                       AS ni,
       I.nfname                                                      AS nf,
       CAST(I.issue_date AS DATE)                                    AS emissao,
       CAST(I.date AS DATE)                                          AS entrada,
       I.vendno                                                      AS vendno,
       (I.prdamt / 100)                                              AS totalPrd,
       (I.grossamt / 100)                                            AS valorNF,
       I.carrno                                                      AS carrno,
       IFNULL(SUBSTRING_INDEX(T.name, ' ', 1), '')                   AS carrName,
       I.auxLong2                                                    AS cte,
       C.status                                                      AS status,
       C.emissao                                                     AS emissaoCte,
       C.entrada                                                     AS entradaCte,
       IFNULL(C.valorNF, 0.00)                                       AS valorCte,
       (I.weight)                                                    AS pesoBruto,
       @STRCUB := REPLACE(IF(LOCATE(' CUB ', I.remarks) > 0,
			     SUBSTRING_INDEX(MID(I.remarks, LOCATE('CUB ', I.remarks) + 4, 100),
					     ' ', 1), ''), ',', '.') AS strCub,
       @CUB := IF(@STRCUB = '', 0.00, @STRCUB * 1.00)                AS cub,
       IFNULL(@CUB * F.valorLivre2 / 100, 0.00)                      AS pesoCub,
       IFNULL(ROUND(IF(@CUB = 0, 1.00, @CUB * (F.valorLivre2 / 100)) * (F.fretePeso / 100), 2),
	      0.00)                                                  AS fPeso,
       IFNULL(F.freteValor / 1000, 0.000)                            AS freteValor,
       IFNULL(F.freteGRIS / 1000, 0.000)                             AS freteGRIS,
       IFNULL(F.freteDespacho / 100, 0.00)                           AS taxa,
       0.00                                                          AS outro,
       IFNULL(F.valorLivre1 / 100, 0.00)                             AS aliquota
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
       status                                                                               AS status,
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
