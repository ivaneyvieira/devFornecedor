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
DO @CDesp := :cdespesa;

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
  ncm VARCHAR(20)
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
       mfno_ref AS refPrd,
       groupno,
       mfno,
       taxno,
       lucroTributado
FROM sqldados.prd
       LEFT JOIN sqldados.prdalq
                 ON prdalq.prdno = prd.no
WHERE NOT (prd.no BETWEEN '          980000' AND '          999999')
  AND (prdalq.form_label LIKE CONCAT(@rotulo, '%') OR @rotulo = '')
  AND (prd.mfno = @mfno OR @mfno = 0);

DROP TEMPORARY TABLE IF EXISTS T_BAR;
CREATE TEMPORARY TABLE T_BAR
(
  PRIMARY KEY (prdno, grade)
)
SELECT prdno,
       grade,
       TRIM(GROUP_CONCAT(DISTINCT TRIM(B.barcode))) AS barcodes,
       IF((B.bits & POW(2, 1)) != 0, 'S', 'N')      AS gtin
FROM sqldados.prdbar AS B
       INNER JOIN sqldados.prd AS P
                  ON P.no = B.prdno
WHERE P.groupno != 10000
  AND (B.bits & POW(2, 1)) != 0
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_GTIN;
CREATE TEMPORARY TABLE T_GTIN
(
  PRIMARY KEY (prdno, grade)
)
SELECT prdno,
       grade,
       TRIM(GROUP_CONCAT(DISTINCT TRIM(B.barcode))) AS barcodes
FROM sqldados.prdbar AS B
       INNER JOIN sqldados.prd AS P
                  ON P.no = B.prdno
WHERE P.groupno != 10000
  AND (B.bits & POW(2, 1)) != 0
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS sqldados.T_QUERY;
CREATE TEMPORARY TABLE sqldados.T_QUERY
SELECT iprd2.storeno                                                                   AS lj,
       inv2.invno                                                                      AS ni,
       CAST(inv2.date AS DATE)                                                         AS data,
       CAST(inv2.issue_date AS DATE)                                                   AS dataEmissao,
       prd.mfno                                                                        AS fornCad,
       inv2.vendno                                                                     AS fornNota,
       iprd2.prdno                                                                     AS prod,
       IF(@comGrade = 'S', iprd2.grade, '')                                            AS grade,
       TRIM(MID(prd.name, 1, 37))                                                      AS descricao,
       TRIM(MID(prd.name, 37, 3))                                                      AS unidade,
       spedprd.ncm                                                                     AS ncmp,
       IFNULL(mfprd.ncm, spedprd.ncm)                                                  AS ncmn,
       IF(prd.taxno = '06', IF(iprd2.baseIcmsSubst = 0, 0, ROUND(((iprd2.baseIcmsSubst / 100) /
                                                                  (((iprd2.qtty / 1000) * (iprd2.dfob)) + (iprd2.ipiAmt / 100)) -
                                                                  1.00) * 100, 4)),
          ROUND(iprd2.lucroTributado / 100, 4))                                        AS mvan,
       ROUND(IF(prd.taxno = '00', 0, IFNULL(prd.lucroTributado, 0)) / 100, 2)          AS mvap,
       ROUND(iprd2.ipi / 100, 2)                                                       AS ipin,
       ROUND(IFNULL(prp.ipi, 0) / 100, 2)                                              AS ipip,
       IF(MID(iprd2.cstIcms, 2, 3) = '20',
          ROUND(iprd2.icms * 100.00 / (iprd2.fob * (iprd2.qtty / 1000)), 2), NULL)     AS icmsc,
       ROUND(iprd2.icmsAliq / 100, 2)                                                  AS icmsn,
       ROUND(IFNULL(prp.dicm, 0) * (-1) / 100, 2)                                      AS icmsp,
       prd.taxno                                                                       AS cstp,
       MID(iprd2.cstIcms, 2, 3)                                                        AS cstn,
       inv2.nfname                                                                     AS nfe,
       inv2.invse                                                                      AS serie,
       IF(MID(iprd2.cstIcms, 2, 3) = '20',
          ROUND(iprd2.baseIcms * 100.00 / (iprd2.fob * (iprd2.qtty / 1000)), 2), NULL) AS icmsd,
       IF(iprd2.grade = '' OR prd.groupno = 10000,
          CAST(CONCAT(IFNULL(TRIM(P2.gtin), ''), ',', IFNULL(G.barcodes, '')) AS CHAR),
          CAST(IFNULL(G.barcodes, '') AS CHAR))                                        AS barcodepl,
       IF(iprd2.grade = '' OR prd.groupno = 10000,
          CAST(CONCAT(TRIM(prd.barcode), ',', IFNULL(B.barcodes, '')) AS CHAR),
          CAST(CONCAT('G,', IFNULL(B.barcodes, '')) AS CHAR))                          AS barcodecl,
       TRIM(CAST(IFNULL(M.barcode, '') AS CHAR))                                       AS barcoden,
       TRIM(CAST(IFNULL(G.barcodes, '') AS CHAR))                                      AS barcodebp,
       TRIM(COALESCE(R.prdrefno, prd.refPrd, ''))                                      AS refPrdp,
       TRIM(COALESCE(M.refPrd, R.prdrefno, prd.refPrd, ''))                            AS refPrdn,
       IFNULL(prp.freight / 100, 0.00)                                                 AS fretep,
       inv2.freight * 100.00 / inv2.grossamt                                           AS freten,
       IF(inv2.weight = 0, NULL, (inv2.freight / 100) / inv2.weight * 1.00)            AS frete,
       cstIcms                                                                         AS cstIcms,
       cfop                                                                            AS cfop,
       ROUND(iprd2.dfob * iprd2.qtty / 1000, 2)                                        AS valor,
       ROUND(iprd2.discount / 100, 2)                                                  AS vlDesconto,
       ROUND(iprd2.dfob * iprd2.qtty / 1000, 2) - ROUND(iprd2.discount / 100, 2)       AS vlLiquido,
       ROUND(ROUND(iprd2.dfob * iprd2.qtty / 1000, 2) * iprd2.frete / 10000, 2)        AS vlFrete,
       ROUND(ROUND(iprd2.dfob * iprd2.qtty / 1000, 2) * iprd2.despesas / 10000, 2)     AS vlDespesas,
       ROUND(iprd2.icms / 100, 2)                                                      AS vlIcms,
       ROUND(iprd2.ipiAmt / 100, 2)                                                    AS vlIpi,
       ROUND(iprd2.baseIcmsSubst / 100, 2)                                             AS baseSubst,
       ROUND(iprd2.icmsSubst / 100, 2)                                                 AS vlIcmsSubst,
       IFNULL(N1.xmlNfe, N2.xmlNfe)                                                    AS xml,
       inv2.account                                                                    AS cDesp
FROM sqldados.iprd2
       INNER JOIN sqldados.inv2
                  USING (invno)
       INNER JOIN T_VEND AS vend
                  ON vend.no = inv2.vendno
       INNER JOIN T_PRD AS prd
                  ON (prd.no = iprd2.prdno)
       LEFT JOIN sqldados.prd2 AS P2
                 USING (prdno)
       LEFT JOIN sqldados.prdrefpq AS R
                 USING (prdno, grade)
       LEFT JOIN T_GTIN AS G
                 USING (prdno, grade)
       LEFT JOIN T_BAR AS B
                 USING (prdno, grade)
       LEFT JOIN T_MFPRD AS M
                 ON M.prdno = iprd2.prdno AND M.grade = IF(prd.groupno = 10000, '', iprd2.grade)
       LEFT JOIN sqldados.prp
                 ON (prp.prdno = iprd2.prdno AND prp.storeno = 10)
       INNER JOIN sqldados.cfo
                  ON (cfo.no = iprd2.cfop)
       LEFT JOIN sqldados.spedprd
                 ON (spedprd.prdno = prd.no)
       LEFT JOIN T_NCM AS mfprd
                 ON (iprd2.prdno = mfprd.prdnoRef)
       LEFT JOIN sqldados.store AS L
                 ON inv2.storeno = L.no
       LEFT JOIN sqldados.invnfe AS C
                 USING (invno)
       LEFT JOIN sqldados.notasEntradaNdd AS N1
                 ON N1.chave = CONCAT('NFe', C.nfekey)
                   AND N1.xmlNfe != 'NULL'
       LEFT JOIN sqldados.notasEntradaNdd AS N2
                 ON N2.cnpjDestinatario = L.cgc
                   AND N2.numero = inv2.nfname
                   AND N2.serie = inv2.invse
                   AND N2.xmlNfe != 'NULL'
WHERE inv2.date BETWEEN @di AND @df
  AND iprd2.storeno IN (1, 2, 3, 4, 5, 6, 7, 8)
  AND (iprd2.storeno = @storeno OR @storeno = 0)
  AND cfo.name1 NOT LIKE 'TRANSF%'
  AND cfo.name1 NOT LIKE 'DEVOL%'
  AND cfo.no NOT IN (2949, 2353, 2916)
  AND cfo.no NOT IN (1949, 1353, 1916)
  AND (iprd2.invno = @ni OR @ni = 0)
  AND (inv2.nfname = @nf OR @nf = '')
  AND (inv2.vendno = @vendno OR @vendno = 0)
  AND (iprd2.prdno = @prd OR @CODIGO = '')
  AND (inv2.account = @CDesp OR @CDesp = '')
GROUP BY iprd2.invno, iprd2.prdno, iprd2.grade;

DROP TABLE IF EXISTS sqldados.T_MAX;
CREATE TEMPORARY TABLE sqldados.T_MAX
(
  PRIMARY KEY (Prod, grade, NI)
)
SELECT Prod,
       grade,
       MAX(NI) AS NI
FROM sqldados.T_QUERY
GROUP BY Prod, grade;

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
       serie,
       fornCad,
       fornNota,
       prod,
       grade,
       descricao,
       unidade,
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
       0                                                                             AS quant,
       IF((CSTp = '06' AND CSTn = '10' OR CSTp = '06' AND CSTn = '60' OR CSTp = CSTn), 'S',
          'N')                                                                       AS cstDif,
       IF(ROUND(IF(CSTn = '20', ICMSc, ICMSn) * 100) = ROUND(ICMSp * 100), 'S', 'N') AS icmsDif,
       IF(ROUND(IPIn * 100) = ROUND(IPIp * 100), 'S', 'N')                           AS ipiDif,
       IF(ROUND(mvan * 100) = ROUND(mvap * 100), 'S', 'N')                           AS mvaDif,
       IF(NCMn = NCMp, 'S', 'N')                                                     AS ncmDif,
       barcodepl,
       barcodecl,
       barcoden,
       barcodebp,
       'S'                                                                           AS barcodeDif,
       refPrdn,
       refPrdp,
       IF(refPrdn = refPrdp, 'S', 'N')                                               AS refPrdDif,
       freten,
       fretep,
       IF(freten = fretep, 'S', IF(freten = fretep, 'DP', 'DN'))                     AS freteDif,
       frete,
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
       vlDesconto + vlLiquido + vlFrete + vlIcms + vlIpi + baseSubst                 AS vlTotal,
       xml,
       cDesp
FROM sqldados.T_QUERY
       INNER JOIN sqldados.T_MAX
                  USING (Prod, grade, NI)

