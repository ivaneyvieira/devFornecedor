DO @storeno := :storeno;
DO @di := :di;
DO @df := :df;
DO @vendno := :vendno;
DO @mfno := :mfno;
DO @ni := :ni;
DO @nf := :nf;
DO @prd := LPAD(:prd, 16, ' ');
DO @CODIGO := :prd;
DO @rotulo := '';

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
SELECT no,
       name,
       mfno,
       taxno,
       lucroTributado
FROM sqldados.prd
  LEFT JOIN sqldados.prdalq
	      ON prdalq.prdno = prd.no
WHERE NOT (prd.no BETWEEN '          980000' AND '          999999')
  AND (prdalq.form_label LIKE CONCAT(@rotulo, '%') OR @rotulo = '')
  AND (prd.mfno = @mfno OR @mfno = 0);

SELECT iprd.storeno                                                                 AS lj,
       inv.invno                                                                    AS ni,
       CAST(inv.date AS DATE)                                                       AS data,
       prd.mfno                                                                     AS fornCad,
       inv.vendno                                                                   AS fornNota,
       TRIM(iprd.prdno)                                                             AS prod,
       TRIM(MID(prd.name, 1, 37))                                                   AS descricao,
       ROUND(iprd.lucroTributado / 100, 2)                                          AS mvan,
       ROUND(iprd.lucroTributado * (iprd.baseIcms / 100) / 100, 2)                  AS mvanv,
       ROUND(iprd.ipi / 100, 2)                                                     AS ipin,
       ROUND(iprd.ipi * (iprd.baseIpi / 100) / 100, 2)                              AS ipinv,
       IF(MID(iprd.cstIcms, 2, 3) = '20',
	  ROUND(iprd.icms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 2), NULL)     AS icmsc,
       IF(MID(iprd.cstIcms, 2, 3) = '20',
	  ROUND(iprd.icms * 100.00 * (iprd.baseIcms / 100) / (iprd.fob * (iprd.qtty / 1000)), 2),
	  NULL)                                                                     AS icmscv,
       ROUND(iprd.icmsAliq / 100, 2)                                                AS icmsn,
       ROUND(iprd.icmsAliq * (iprd.baseIcms / 100) / 100, 2)                        AS icmsnv,
       MID(iprd.cstIcms, 2, 3)                                                      AS cstn,
       inv.nfname                                                                   AS nfe,
       IF(MID(iprd.cstIcms, 2, 3) = '20',
	  ROUND(iprd.baseIcms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 2), NULL) AS icmsd,
       IF(MID(iprd.cstIcms, 2, 3) = '20',
	  ROUND(iprd.baseIcms * 100.00 * (iprd.baseIcms / 100) / (iprd.fob * (iprd.qtty / 1000)),
		2), NULL)                                                           AS icmsdv
FROM sqldados.iprd
  INNER JOIN sqldados.inv
	       USING (invno)
  INNER JOIN T_VEND AS vend
	       ON vend.no = inv.vendno
  INNER JOIN T_PRD  AS prd
	       ON (prd.no = iprd.prdno)
  INNER JOIN sqldados.cfo
	       ON (cfo.no = iprd.cfop)
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
GROUP BY inv.invno, iprd.prdno


