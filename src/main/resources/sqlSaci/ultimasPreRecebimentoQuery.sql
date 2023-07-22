USE sqldados;

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
DO @comGrade := :comGrade;
DO @listaProdutos := TRIM(:listaProdutos);

DROP TEMPORARY TABLE IF EXISTS T_MFPRD;
CREATE TEMPORARY TABLE T_MFPRD
(
  PRIMARY KEY (prdno, grade)
)
SELECT prdnoRef                                                           AS prdno,
       grade,
       MID(MAX(CONCAT(LPAD(seqnoAuto, 10, '0'), TRIM(barcode))), 11, 100) AS barcode,
       MID(MAX(CONCAT(LPAD(seqnoAuto, 10, '0'), TRIM(prdno))), 11, 100)   AS refPrd
FROM sqldados.mfprd
WHERE TRIM(prdnoRef) <> ''
GROUP BY prdnoRef, grade;

DROP TEMPORARY TABLE IF EXISTS T_NCM;
CREATE TEMPORARY TABLE T_NCM
(
  PRIMARY KEY (prdnoRef),
  ncm varchar(20)
)
SELECT DISTINCT prdnoRef, MID(MAX(CONCAT(LPAD(seqnoAuto, 20, '0'), ncm)), 21, 20) AS ncm
FROM sqldados.mfprd
GROUP BY prdnoRef;

DROP TEMPORARY TABLE IF EXISTS T_VEND;
CREATE TEMPORARY TABLE T_VEND
(
  PRIMARY KEY (no)
)
SELECT no, name
FROM sqldados.vend
WHERE name NOT LIKE 'ENGECOPI%';

DROP TEMPORARY TABLE IF EXISTS T_PRD;
CREATE TEMPORARY TABLE T_PRD
(
  PRIMARY KEY (no)
)
SELECT no,
       name,
       barcode,
       mfno,
       taxno,
       lucroTributado,
       mfno_ref AS refPrd,
       weight_g
FROM sqldados.prd
       LEFT JOIN sqldados.prdalq
                 ON prdalq.prdno = prd.no
WHERE NOT (prd.no BETWEEN '          980000' AND '          999999')
  AND (prdalq.form_label LIKE CONCAT(@rotulo, '%') OR @rotulo = '')
  AND (prd.mfno = @mfno OR @mfno = 0);

DROP TEMPORARY TABLE IF EXISTS sqldados.T_QUERY;
CREATE TEMPORARY TABLE sqldados.T_QUERY
SELECT iprd2.storeno                                                                     AS lj,
       inv2.invno                                                                        AS ni,
       CAST(inv2.date AS DATE)                                                           AS data,
       CAST(inv2.issue_date AS DATE)                                                     AS dataEmissao,
       prd.mfno                                                                          AS fornCad,
       inv2.vendno                                                                       AS fornNota,
       iprd2.prdno                                                                       AS prod,
       IF(@comGrade = 'S', iprd2.grade, '')                                              AS grade,
       TRIM(MID(prd.name, 1, 37))                                                        AS descricao,
       spedprd.ncm                                                                       AS ncmp,
       IFNULL(mfprd.ncm, spedprd.ncm)                                                    AS ncmn,
       IF(prd.taxno = '06', IF(iprd.baseIcmsSubst = 0, 0, ROUND(((iprd.baseIcmsSubst / 100) /
                                                                 (((iprd.qtty / 1000) * (iprd.dfob)) + (iprd.ipiAmt / 100)) -
                                                                 1.00) * 100, 4)),
          ROUND(iprd.lucroTributado / 100, 4))                                           AS mvan,
       ROUND(IF(prd.taxno = '00', 0, IFNULL(prd.lucroTributado, 0)) / 100, 4)            AS mvap,
       IF(iprd2.baseIpi = 0, 0.00, ROUND(iprd2.ipi / 100, 4))                            AS ipin,
       ROUND(IFNULL(prp.ipi, 0) / 100, 4)                                                AS ipip,
       IF(baseIcms = 0, 0.00, IF(MID(iprd2.cstIcms, 2, 3) = '20',
                                 ROUND(iprd2.icms * 100.00 / (iprd2.fob * (iprd2.qtty / 1000)), 4),
                                 NULL))                                                  AS icmsc,
       IF(baseIcms = 0, 0.00, ROUND(iprd2.icmsAliq / 100, 4))                            AS icmsn,
       ROUND(IFNULL(prp.dicm, 0) * (-1) / 100, 4)                                        AS icmsp,
       prd.taxno                                                                         AS cstp,
       MID(iprd2.cstIcms, 2, 3)                                                          AS cstn,
       inv2.nfname                                                                       AS nfe,
       IF(baseIcms = 0, 0.00, IF(MID(iprd2.cstIcms, 2, 3) = '20',
                                 ROUND(iprd2.baseIcms * 100.00 / (iprd2.fob * (iprd2.qtty / 1000)),
                                       4), NULL))                                        AS icmsd,
       CAST(TRIM(COALESCE(GROUP_CONCAT(TRIM(B.barcode)), P2.gtin, prd.barcode)) AS CHAR) AS barcodepl,
       TRIM(IFNULL(M.barcode, ''))                                                       AS barcoden,
       TRIM(IFNULL(prd.refPrd, ''))                                                      AS refPrdp,
       TRIM(IFNULL(M.refPrd, ''))                                                        AS refPrdn,
       IFNULL(prp.freight / 100, 0.00)                                                   AS fretep,
       inv2.freight * 100.00 / inv2.grossamt                                             AS freten,
       IF(inv2.weight = 0, NULL, (inv2.freight / 100) / inv2.weight * 1.00)              AS frete,
       IFNULL(ROUND(oprd.cost, 2), 0.00)                                                 AS precop,
       IFNULL(ROUND(prp.fob / 10000, 2), 0.00)                                           AS precopc,
       IFNULL(ROUND(iprd2.fob4 / 10000, 2), 0.00)                                        AS precon,
       IFNULL(prd.weight_g, 0.00)                                                        AS pesoBruto,
       inv2.ordno                                                                        AS pedidoCompra,
       iprd2.qtty / 1000                                                                 AS quant,
       cstIcms                                                                           AS cstIcms,
       cfop                                                                              AS cfop,
       ROUND(iprd2.dfob * iprd2.qtty / 1000, 2)                                          AS valor,
       ROUND(iprd2.discount / 100, 2)                                                    AS vlDesconto,
       ROUND(iprd2.dfob * iprd2.qtty / 1000, 2) - ROUND(iprd2.discount / 100, 2)         AS vlLiquido,
       ROUND(ROUND(iprd2.dfob * iprd2.qtty / 1000, 2) * iprd2.frete / 10000, 2)          AS vlFrete,
       ROUND(ROUND(iprd2.dfob * iprd2.qtty / 1000, 2) * iprd2.despesas / 10000, 2)       AS vlDespesas,
       ROUND(iprd2.icms / 100, 2)                                                        AS vlIcms,
       ROUND(iprd2.ipiAmt / 100, 2)                                                      AS vlIpi,
       ROUND(iprd2.baseIcmsSubst / 100, 2)                                               AS baseSubst,
       ROUND(iprd2.icmsSubst / 100, 2)                                                   AS vlIcmsSubst
FROM sqldados.iprd2
       INNER JOIN sqldados.inv2
                  USING (invno)
       INNER JOIN T_VEND AS vend
                  ON vend.no = inv2.vendno
       INNER JOIN T_PRD AS prd
                  ON (prd.no = iprd2.prdno)
       LEFT JOIN sqldados.prd2 AS P2
                 USING (prdno)
       LEFT JOIN sqldados.prd2 AS P2
                 USING (prdno)
       LEFT JOIN T_MFPRD AS M
                 USING (prdno, grade)
       LEFT JOIN sqldados.prdbar AS B
                 ON B.prdno = iprd2.prdno AND B.grade = iprd2.grade AND B.grade != ''
       LEFT JOIN sqldados.prp
                 ON (prp.prdno = iprd2.prdno AND prp.storeno = 10)
       INNER JOIN sqldados.cfo
                  ON (cfo.no = iprd2.cfop)
       LEFT JOIN sqldados.spedprd
                 ON (spedprd.prdno = prd.no)
       LEFT JOIN T_NCM AS mfprd
                 ON (iprd2.prdno = mfprd.prdnoRef)
       LEFT JOIN sqldados.oprd
                 ON (oprd.storeno = inv2.storeno AND oprd.ordno = inv2.ordno AND
                     oprd.prdno = iprd2.prdno AND oprd.grade = iprd2.grade)
WHERE inv2.date BETWEEN @di AND @df
  AND iprd2.storeno IN (1, 2, 3, 4, 5, 6, 7)
  AND (iprd2.storeno = @storeno OR @storeno = 0)
  AND cfo.name1 NOT LIKE 'TRANSF%'
  AND cfo.name1 NOT LIKE 'DEVOL%'
  AND cfo.name1 NOT LIKE '%BONIF%'
  AND cfo.no NOT IN (2949, 2353, 2916)
  AND cfo.no NOT IN (1949, 1353, 1916)
  AND (iprd2.invno = @ni OR @ni = 0)
  AND (inv2.nfname = @nf OR @nf = '')
  AND (inv2.vendno = @vendno OR @vendno = 0)
  AND (iprd2.prdno = @prd OR @CODIGO = '')
  AND (FIND_IN_SET(TRIM(iprd2.prdno), @listaProdutos) > 0 OR @listaProdutos = '')
GROUP BY inv2.invno, iprd2.prdno, grade;

DROP TABLE IF EXISTS sqldados.query99999;
CREATE TABLE sqldados.query99999
(
  INDEX (cstDif),
  INDEX (icmsDif),
  INDEX (ipiDif),
  INDEX (mvaDif),
  INDEX (ncmDif),
  INDEX (barcodeDif)
)
SELECT lj,
       ni,
       data,
       dataEmissao,
       nfe,
       fornCad,
       fornNota,
       TRIM(prod)                                                    AS prod,
       grade,
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
          'N')                                                       AS cstDif,
       IF(ROUND(IF(CSTn = '20', ICMSc, ICMSn) * 100) = ROUND(ICMSp * 100), 'S',
          'N')                                                       AS icmsDif,
       IF(ROUND(IPIn * 100) = ROUND(IPIp * 100), 'S', 'N')           AS ipiDif,
       IF(ROUND(mvan * 100) = ROUND(mvap * 100) OR (ABS(mvan - mvap) < 0.01), 'S',
          'N')                                                       AS mvaDif,
       IF(NCMn = NCMp, 'S', 'N')                                     AS ncmDif,
       barcodepl,
       barcoden,
       'S'                                                           AS barcodeDif,
       refPrdp,
       refPrdn,
       IF(refPrdp = refPrdn, 'S', 'N')                               AS refPrdDif,
       freten,
       fretep,
       IF(freten = fretep, 'S', IF(freten > fretep, 'DP', 'DN'))     AS freteDif,
       frete,
       precon,
       precop,
       precopc,
       IF(precon = precop, 'S', IF(precon > precop, 'DP', 'DN'))     AS precoDif,
       pesoBruto,
       pedidoCompra,
       quant                                                         AS quant,
       cstIcms,
       cfop,
       valor,
       vlDesconto,
       vlLiquido,
       vlFrete,
       vlDespesas,
       vlIcms,
       vlIpi,
       baseSubst,
       vlIcmsSubst,
       vlDesconto + vlLiquido + vlFrete + vlIcms + vlIpi + baseSubst AS vlTotal
FROM sqldados.T_QUERY
