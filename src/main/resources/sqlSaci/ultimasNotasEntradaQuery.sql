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

DROP TEMPORARY TABLE IF EXISTS T_NCM;
CREATE TEMPORARY TABLE T_NCM (
  PRIMARY KEY (prdnoRef),
  ncm varchar(20)
)
SELECT DISTINCT prdnoRef, MID(MAX(CONCAT(LPAD(seqnoAuto, 20, '0'), ncm)), 21, 20) AS ncm
FROM sqldados.mfprd
WHERE ncm <> ''
GROUP BY prdnoRef;

DROP TEMPORARY TABLE IF EXISTS sqldados.T_QUERY;
CREATE TEMPORARY TABLE sqldados.T_QUERY
SELECT iprd.storeno                                                                 AS lj,
       inv.invno                                                                    AS ni,
       CAST(inv.date AS DATE)                                                       AS data,
       prd.mfno                                                                     AS forn,
       iprd.prdno                                                                   AS prod,
       TRIM(MID(prd.name, 1, 37))                                                   AS descricao,
       spedprd.ncm                                                                  AS ncmp,
       IFNULL(mfprd.ncm, spedprd.ncm)                                               AS ncmn,
       ROUND(iprd.lucroTributado / 100, 2)                                          AS mvan,
       ROUND(IF(prd.taxno = '00', 0, IFNULL(prd.lucroTributado, 0)) / 100, 2)       AS mvap,
       ROUND(iprd.ipi / 100, 2)                                                     AS ipin,
       ROUND(IFNULL(prp.ipi, 0) / 100, 2)                                           AS ipip,
       IF(MID(iprd.cstIcms, 2, 3) = '20',
	  ROUND(iprd.icms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 2),
	  ROUND(iprd.icmsAliq / 100, 2))                                            AS icmsn,
       ROUND(IFNULL(prp.dicm, 0) * (-1) / 100, 2)                                   AS icmsp,
       prd.taxno                                                                    AS cstp,
       MID(iprd.cstIcms, 2, 3)                                                      AS cstn,
       inv.nfname                                                                   AS nfe,
       IF(MID(iprd.cstIcms, 2, 3) = '20',
	  ROUND(iprd.baseIcms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 2), NULL) AS icmsd
FROM sqldados.iprd
  LEFT JOIN sqldados.inv
	      USING (invno)
  LEFT JOIN sqldados.vend
	      ON (vend.no = inv.vendno)
  LEFT JOIN sqldados.prd
	      ON (prd.no = iprd.prdno)
  LEFT JOIN sqldados.prp
	      ON (prp.prdno = iprd.prdno AND prp.storeno = 10)
  LEFT JOIN sqldados.cfo
	      ON (cfo.no = iprd.cfop)
  LEFT JOIN sqldados.spedprd
	      ON (spedprd.prdno = prd.no)
  LEFT JOIN T_NCM AS mfprd
	      ON (iprd.prdno = mfprd.prdnoRef)
WHERE inv.date BETWEEN @di AND @df
  AND iprd.storeno IN (1, 2, 3, 4, 5, 6, 7)
  AND (iprd.storeno = @storeno OR @storeno = 0)
  AND cfo.name1 NOT LIKE 'TRANSF%'
  AND cfo.name1 NOT LIKE 'DEVOL%'
  AND cfo.name1 NOT LIKE '%BONIF%'
  AND cfo.no NOT IN (2949, 2353, 2916)
  AND cfo.no NOT IN (1949, 1353, 1916)
  AND (iprd.invno = @ni OR @ni = 0)
  AND (inv.nfname = @nf OR @nf = '')
  AND (vend.no = @vendno OR @vendno = 0)
  AND vend.name NOT LIKE 'ENGECOPI%'
  AND (iprd.prdno LIKE @prd OR TRIM(@prd) LIKE '')
  AND NOT (prd.no BETWEEN '          980000' AND '          999999')
GROUP BY inv.invno, iprd.prdno;

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
FROM sqldados.T_QUERY
GROUP BY Prod, cstDif, icmsDif, ipiDif, mvaDif, ncmDif;

DROP TABLE IF EXISTS sqldados.query1234567;
CREATE TABLE sqldados.query1234567 (
  INDEX (cstDif),
  INDEX (icmsDif),
  INDEX (ipiDif),
  INDEX (mvaDif),
  INDEX (ncmDif)
)
SELECT lj,
       ni,
       data,
       nfe,
       forn,
       prod,
       descricao,
       icmsn,
       icmsp,
       icmsd,
       ipin,
       ipip,
       cstn,
       cstp,
       mvan,
       mvap,
       ncmn,
       ncmp,
       cstDif,
       icmsDif,
       ipiDif,
       mvaDif,
       ncmDif
FROM sqldados.T_QUERY
  INNER JOIN sqldados.T_MAX
	       USING (Prod, NI)
