DO @storeno := :storeno;
DO @di := :di;
DO @df := :df;
DO @vendno := :vendno;
DO @mfno := :mfno;
DO @ni := :ni;
DO @nf := :nf;
DO @prd := LPAD(:prd, 16, ' ');
DO @CODIGO := :prd;
DO @cst := :cst;
DO @icms := :icms;
DO @ipi := :ipi;
DO @mva := :mva;
DO @ncm := :ncm;
DO @rotulo := :rotulo;

DROP TEMPORARY TABLE IF EXISTS T_NCM;
CREATE TEMPORARY TABLE T_NCM (
  PRIMARY KEY (prdnoRef),
  ncm varchar(20)
)
SELECT DISTINCT prdnoRef, MID(MAX(CONCAT(LPAD(seqnoAuto, 20, '0'), ncm)), 21, 20) AS ncm
FROM sqldados.mfprd
GROUP BY prdnoRef;

DROP TEMPORARY TABLE IF EXISTS T_VEND;
CREATE TEMPORARY TABLE T_VEND (
  PRIMARY KEY (no)
)
SELECT no, name
FROM sqldados.vend
WHERE name NOT LIKE 'ENGECOPI%';

DROP TEMPORARY TABLE IF EXISTS T_PRD;
CREATE TEMPORARY TABLE T_PRD (
  PRIMARY KEY (no)
)
SELECT no, name, mfno, taxno, lucroTributado
FROM sqldados.prd
  LEFT JOIN sqldados.prdalq
	      ON prdalq.prdno = prd.no
WHERE NOT (prd.no BETWEEN '          980000' AND '          999999')
  AND (prdalq.form_label LIKE CONCAT(@rotulo, '%') OR @rotulo = '')
  AND (prd.mfno = @mfno OR @mfno = 0);

DROP TEMPORARY TABLE IF EXISTS sqldados.T_QUERY;
CREATE TEMPORARY TABLE sqldados.T_QUERY
SELECT iprd.storeno                                                                                                 AS lj,
       inv.invno                                                                                                    AS ni,
       CAST(inv.date AS DATE)                                                                                       AS data,
       prd.mfno                                                                                                     AS fornCad,
       inv.vendno                                                                                                   AS fornNota,
       iprd.prdno                                                                                                   AS prod,
       TRIM(MID(prd.name, 1, 37))                                                                                   AS descricao,
       spedprd.ncm                                                                                                  AS ncmp,
       IFNULL(mfprd.ncm, spedprd.ncm)                                                                               AS ncmn,
       ROUND(iprd.lucroTributado / 100, 2)                                                                          AS mvan,
       ROUND(IF(prd.taxno = '00', 0, IFNULL(prd.lucroTributado, 0)) / 100,
	     2)                                                                                                     AS mvap,
       ROUND(iprd.ipi / 100, 2)                                                                                     AS ipin,
       ROUND(IFNULL(prp.ipi, 0) / 100, 2)                                                                           AS ipip,
       IF(MID(iprd.cstIcms, 2, 3) = '20', ROUND(iprd.icms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 2),
	  NULL)                                                                                                     AS icmsc,
       ROUND(iprd.icmsAliq / 100, 2)                                                                                AS icmsn,
       ROUND(IFNULL(prp.dicm, 0) * (-1) / 100, 2)                                                                   AS icmsp,
       prd.taxno                                                                                                    AS cstp,
       MID(iprd.cstIcms, 2, 3)                                                                                      AS cstn,
       inv.nfname                                                                                                   AS nfe,
       IF(MID(iprd.cstIcms, 2, 3) = '20', ROUND(iprd.baseIcms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 2),
	  NULL)                                                                                                     AS icmsd
FROM sqldados.iprd
  INNER JOIN sqldados.inv
	       USING (invno)
  INNER JOIN T_VEND AS vend
	       ON vend.no = inv.vendno
  INNER JOIN T_PRD AS prd
	       ON (prd.no = iprd.prdno)
  LEFT JOIN sqldados.prp
	      ON (prp.prdno = iprd.prdno AND prp.storeno = 10)
  INNER JOIN sqldados.cfo
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
  AND (inv.vendno = @vendno OR @vendno = 0)
  AND (iprd.prdno = @prd OR @CODIGO = '')
GROUP BY inv.invno, iprd.prdno;

DROP TABLE IF EXISTS sqldados.T_MAX;
CREATE TEMPORARY TABLE sqldados.T_MAX (
  PRIMARY KEY (Prod, NI)
)
SELECT Prod, MAX(NI) AS NI
FROM sqldados.T_QUERY
GROUP BY Prod;

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
       fornCad,
       fornNota,
       prod,
       descricao,
       icmsn,
       icmsc,
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
       IF((CSTp = '06' AND CSTn = '10' OR CSTp = '06' AND CSTn = '60' OR CSTp = CSTn), 'S',
	  'N')                                                                       AS cstDif,
       IF(ROUND(IF(CSTn = '20', ICMSc, ICMSn) * 100) = ROUND(ICMSp * 100), 'S', 'N') AS icmsDif,
       IF(ROUND(IPIn * 100) = ROUND(IPIp * 100), 'S', 'N')                           AS ipiDif,
       IF(ROUND(mvan * 100) = ROUND(mvap * 100), 'S', 'N')                           AS mvaDif,
       IF(NCMn = NCMp, 'S', 'N')                                                     AS ncmDif
FROM sqldados.T_QUERY
  INNER JOIN sqldados.T_MAX
	       USING (Prod, NI)

