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
  PRIMARY KEY (prdnoRef)
)
SELECT DISTINCT prdnoRef, ncm
FROM sqldados.mfprd
GROUP BY prdnoRef;

DROP TABLE IF EXISTS sqldados.T_QUERY;
CREATE TEMPORARY TABLE sqldados.T_QUERY
SELECT iprd.storeno                                    AS lj,
       inv.invno                                       AS ni,
       CAST(inv.date AS DATE)                          AS data,
       prd.mfno                                        AS forn,
       iprd.prdno                                      AS prod,
       TRIM(MID(prd.name, 1, 37))                      AS descricao,
       spedprd.ncm                                     AS ncmp,
       IFNULL(mfprd.ncm, '')                           AS ncmn,
       iprd.lucroTributado / 100                       AS mvan,
       IF(prd.taxno = 00, 0, prd.lucroTributado) / 100 AS mvap,
       iprd.ipi / 100                                  AS ipin,
       prp.ipi / 100                                   AS ipip,
       iprd.icmsAliq / 100                             AS icmsn,
       prp.dicm * (-1) / 100                           AS icmsp,
       prd.taxno                                       AS cstp,
       MID(iprd.cstIcms, 2, 3)                         AS cstn,
       inv.nfname                                      AS nfe
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
  AND cfo.no NOT IN (2949, 2353)
  AND cfo.no NOT IN (1949, 1353)
  AND (iprd.invno = @ni OR @ni = 0)
  AND (inv.nfname = @nf OR @nf = '')
  AND (prd.mfno = @vendno OR @vendno = 0)
  AND vend.name NOT LIKE 'ENGECOPI%'
  AND (iprd.prdno LIKE @prd OR TRIM(@prd) LIKE '')
  AND NOT (prd.no BETWEEN 980000 AND 999999)
GROUP BY inv.invno, iprd.prdno;

DROP TABLE IF EXISTS sqldados.T_MAX;
CREATE TEMPORARY TABLE sqldados.T_MAX (
  PRIMARY KEY (Prod, NI)
)
SELECT Prod, MAX(NI) AS NI
FROM sqldados.T_QUERY
GROUP BY Prod;

SELECT lj,
       ni,
       data,
       nfe,
       forn,
       prod,
       descricao,
       icmsn,
       icmsp,
       ipin,
       ipip,
       cstn,
       cstp,
       mvan,
       mvap,
       ncmn,
       ncmp
FROM sqldados.T_QUERY
  INNER JOIN sqldados.T_MAX
	       USING (Prod, NI)
WHERE CASE @cst
	WHEN 'S'
	  THEN (CSTp = '06' AND CSTn = '10' OR CSTp = '06' AND CSTn = '60' OR CSTp = CSTn)
	WHEN 'N'
	  THEN NOT (CSTp = '06' AND CSTn = '10' OR CSTp = '06' AND CSTn = '60' OR CSTp = CSTn)
	WHEN 'T'
	  THEN TRUE
	ELSE FALSE
      END
  AND CASE @icms
	WHEN 'S'
	  THEN ICMSn = ICMSp
	WHEN 'N'
	  THEN ICMSn != ICMSp
	WHEN 'T'
	  THEN TRUE
	ELSE FALSE
      END
  AND CASE @ipi
	WHEN 'S'
	  THEN IPIn = IPIp
	WHEN 'N'
	  THEN IPIn != IPIp
	WHEN 'T'
	  THEN TRUE
	ELSE FALSE
      END
  AND CASE @mva
	WHEN 'S'
	  THEN MVAn = MVAp
	WHEN 'N'
	  THEN MVAn != MVAp
	WHEN 'T'
	  THEN TRUE
	ELSE FALSE
      END
  AND CASE @ncm
	WHEN 'S'
	  THEN (NCMn = NCMp)
	WHEN 'N'
	  THEN (NCMn != NCMp)
	WHEN 'T'
	  THEN TRUE
	ELSE FALSE
      END

