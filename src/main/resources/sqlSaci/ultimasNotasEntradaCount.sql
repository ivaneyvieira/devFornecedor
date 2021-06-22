DO @storeno := :storeno;
DO @di := :di;
DO @df := :df;
DO @vendno := :vendno;
DO @ni := :ni;
DO @nf := :nf;
DO @prd := LPAD(:prd, 16, ' ');
DO @cst := :cst;
DO @icms := :icms;
DO @ipi := :ipi;
DO @mva := :mva;
DO @ncm := :ncm;

DROP TABLE IF EXISTS sqldados.T_MAX;
CREATE TEMPORARY TABLE sqldados.T_MAX (
  PRIMARY KEY (Prod, NI, cstDif, icmsDif, ipiDif, mvaDif, ncmDif)
)
SELECT Prod,
       IF((CSTp = '06' AND CSTn = '10' OR CSTp = '06' AND CSTn = '60' OR CSTp = CSTn), 'S',
	  'N')                                               AS cstDif,
       IF(ROUND(ICMSn * 100) = ROUND(ICMSp * 100), 'S', 'N') AS icmsDif,
       IF(ROUND(IPIn * 100) = ROUND(IPIp * 100), 'S', 'N')   AS ipiDif,
       IF(ROUND(mvan * 100) = ROUND(mvap * 100), 'S', 'N')   AS mvaDif,
       IF(NCMn = NCMp, 'S', 'N')                             AS ncmDif,
       MAX(NI)                                               AS NI
FROM sqldados.query1234567
GROUP BY Prod, cstDif, icmsDif, ipiDif, mvaDif, ncmDif;

SELECT COUNT(*)
FROM sqldados.query1234567
  INNER JOIN sqldados.T_MAX
	       USING (Prod, NI)
WHERE @cst = cstDif
   OR @icms = icmsDif
   OR @ipi = ipiDif
   OR @mva = mvaDif
   OR @ncm = ncmDif
   OR (@cst = 'T' AND @icms = 'T' AND @ipi = 'T' AND @mva = 'T' AND @ncm = 'T')

