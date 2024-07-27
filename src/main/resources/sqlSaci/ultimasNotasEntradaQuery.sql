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
DO @CDesp := :cdespesa;
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
       mfno,
       taxno,
       groupno,
       lucroTributado,
       mfno_ref                             AS refPrd,
       weight_g,
       IF(tipoGarantia = 2, garantia, NULL) AS mesesValidade,
       prdalq.form_label                    AS rotulo
FROM sqldados.prd
       LEFT JOIN sqldados.prdalq
                 ON prdalq.prdno = prd.no
WHERE NOT (prd.no BETWEEN '          980000' AND '          999999')
  AND (prdalq.form_label LIKE CONCAT(@rotulo, '%') OR @rotulo = '')
  AND (prd.mfno = @mfno OR @mfno = 0);

DROP TEMPORARY TABLE IF EXISTS T_STK;
CREATE TEMPORARY TABLE T_STK
(
  PRIMARY KEY (prdno)
)
SELECT prdno, SUM(qtty_varejo / 1000) AS estoque
FROM sqldados.stk AS S
       INNER JOIN T_PRD AS P
                  ON P.no = S.prdno
GROUP BY prdno;

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
SELECT iprd.storeno                                                           AS lj,
       inv.invno                                                              AS ni,
       CAST(inv.date AS DATE)                                                 AS data,
       CAST(inv.issue_date AS DATE)                                           AS dataEmissao,
       prd.mfno                                                               AS fornCad,
       inv.vendno                                                             AS fornNota,
       iprd.prdno                                                             AS prod,
       IF(@comGrade = 'S', iprd.grade, '')                                    AS grade,
       prd.rotulo                                                             AS rotulo,
       TRIM(MID(prd.name, 1, 37))                                             AS descricao,
       TRIM(MID(prd.name, 37, 3))                                             AS unidade,
       spedprd.ncm                                                            AS ncmp,
       IFNULL(mfprd.ncm, spedprd.ncm)                                         AS ncmn,
       IF(prd.taxno = '06', IF(iprd.baseIcmsSubst = 0, 0, ROUND(((iprd.baseIcmsSubst / 100) /
                                                                 (((iprd.qtty / 1000) * (iprd.dfob)) + (iprd.ipiAmt / 100)) -
                                                                 1.00) * 100, 4)),
          ROUND(iprd.lucroTributado / 100, 4))                                AS mvan,
       ROUND(IF(prd.taxno = '00', 0, IFNULL(prd.lucroTributado, 0)) / 100, 4) AS mvap,
       IF(iprd.baseIpi = 0, 0.00, ROUND(iprd.ipi / 100, 4))                   AS ipin,
       ROUND(IFNULL(prp.ipi, 0) / 100, 4)                                     AS ipip,
       IF(baseIcms = 0, 0.00, IF(MID(iprd.cstIcms, 2, 3) = '20',
                                 ROUND(iprd.icms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 4),
                                 NULL))                                       AS icmsc,
       IF(baseIcms = 0, 0.00, ROUND(iprd.icmsAliq / 100, 4))                  AS icmsn,
       ROUND(IFNULL(prp.dicm, 0) * (-1) / 100, 4)                             AS icmsp,
       prd.taxno                                                              AS cstp,
       MID(iprd.cstIcms, 2, 3)                                                AS cstn,
       inv.nfname                                                             AS nfe,
       inv.invse                                                              AS serie,
       IF(baseIcms = 0, 0.00, IF(MID(iprd.cstIcms, 2, 3) = '20',
                                 ROUND(iprd.baseIcms * 100.00 / (iprd.fob * (iprd.qtty / 1000)), 4),
                                 NULL))                                       AS icmsd,
       IF(iprd.grade = '' OR prd.groupno = 10000,
          CAST(CONCAT(IFNULL(TRIM(P2.gtin), ''), ',', IFNULL(G.barcodes, '')) AS CHAR),
          CAST(IFNULL(G.barcodes, '') AS CHAR))                               AS barcodepl,
       IF(iprd.grade = '' OR prd.groupno = 10000,
          CAST(CONCAT(TRIM(prd.barcode), ',', IFNULL(B.barcodes, '')) AS CHAR),
          CAST(CONCAT('G,', IFNULL(B.barcodes, '')) AS CHAR))                 AS barcodecl,
       TRIM(CAST(IFNULL(M.barcode, '') AS CHAR))                              AS barcoden,
       TRIM(CAST(IFNULL(G.barcodes, '') AS CHAR))                             AS barcodebp,
       TRIM(COALESCE(R.prdrefno, prd.refPrd, ''))                             AS refPrdp,
       TRIM(COALESCE(M.refPrd, R.prdrefno, prd.refPrd, ''))                   AS refPrdn,
       IFNULL(prp.freight / 100, 0.00)                                        AS fretep,
       inv.freight * 100.00 / inv.grossamt                                    AS freten,
       IF(inv.weight = 0, NULL, (inv.freight / 100) / inv.weight * 1.00)      AS frete,
       IFNULL(ROUND(oprd.cost, 2), 0.00)                                      AS precop,
       IFNULL(ROUND(prp.fob / 10000, 2), 0.00)                                AS precopc,
       IFNULL(ROUND(iprd.fob4 / 10000, 2), 0.00)                              AS precon,
       @PESO_BRUTO := IFNULL(prd.weight_g, NULL)                              AS pesoBruto,
       inv.ordno                                                              AS pedidoCompra,
       IF(inv.weight = 0, NULL, inv.weight)                                   AS pesoBrutoTotal,
       @PESO_BRUTO * (iprd.qtty / 1000)                                       AS pesoBrutoPrd,
       @FRETE := ROUND((inv.freight / 100) / (inv.weight), 4)                 AS freteKg,
       @FRETE_UN := ROUND(prd.weight_g * @FRETE, 4)                           AS freteUnit,
       ROUND(@FRETE_UN * 100 * 10000 / iprd.fob4, 2)                          AS fretePerNf,
       prp.freight / 100                                                      AS fretePerPrc,
       prp.refprice / 100                                                     AS precoRef,
       iprd.qtty / 1000                                                       AS quant,
       S.estoque                                                              AS estoque,
       mesesValidade                                                          AS mesesValidade,
       iprd.cstIcms                                                           AS cstIcms,
       iprd.cfop                                                              AS cfop,
       ROUND(iprd.dfob * iprd.qtty / 1000, 2)                                 AS valor,
       ROUND(iprd.discount / 100, 2)                                          AS vlDesconto,
       ROUND(iprd.dfob * iprd.qtty / 1000, 2) -
       ROUND(iprd.discount / 100, 2)                                          AS vlLiquido,
       ROUND(ROUND(iprd.dfob * iprd.qtty / 1000, 2) * iprd.frete / 10000, 2)  AS vlFrete,
       ROUND(ROUND(iprd.dfob * iprd.qtty / 1000, 2) * iprd.despesas / 10000,
             2)                                                               AS vlDespesas,
       ROUND(iprd.icms / 100, 2)                                              AS vlIcms,
       ROUND(iprd.ipiAmt / 100, 2)                                            AS vlIpi,
       ROUND(iprd.baseIcmsSubst / 100, 2)                                     AS baseSubst,
       ROUND(iprd.icmsSubst / 100, 2)                                         AS vlIcmsSubst,
       IFNULL(N1.xmlNfe, N2.xmlNfe)                                           AS xml,
       inv.account                                                            AS cDesp
FROM sqldados.iprd
       INNER JOIN sqldados.inv
                  USING (invno)
       LEFT JOIN T_STK AS S
                 USING (prdno)
       INNER JOIN T_VEND AS vend
                  ON vend.no = inv.vendno
       INNER JOIN T_PRD AS prd
                  ON (prd.no = iprd.prdno)
       LEFT JOIN sqldados.prd2 AS P2
                 USING (prdno)
       LEFT JOIN sqldados.prdrefpq AS R
                 USING (prdno, grade)
       LEFT JOIN T_GTIN AS G
                 USING (prdno, grade)
       LEFT JOIN T_BAR AS B
                 USING (prdno, grade)
       LEFT JOIN T_MFPRD AS M
                 ON M.prdno = iprd.prdno AND M.grade = IF(prd.groupno = 10000, '', iprd.grade)
       LEFT JOIN sqldados.prp
                 ON (prp.prdno = iprd.prdno AND prp.storeno = 10)
       INNER JOIN sqldados.cfo
                  ON (cfo.no = iprd.cfop)
       LEFT JOIN sqldados.spedprd
                 ON (spedprd.prdno = prd.no)
       LEFT JOIN T_NCM AS mfprd
                 ON (iprd.prdno = mfprd.prdnoRef)
       LEFT JOIN sqldados.oprd
                 ON (oprd.storeno = inv.storeno AND oprd.ordno = inv.ordno AND
                     oprd.prdno = iprd.prdno AND oprd.grade = iprd.grade)
       LEFT JOIN sqldados.store AS L
                 ON inv.storeno = L.no
       LEFT JOIN sqldados.invnfe AS C
                 USING (invno)
       LEFT JOIN sqldados.notasEntradaNdd AS N1
                 ON N1.chave = CONCAT('NFe', C.nfekey)
                   AND N1.xmlNfe != 'NULL'
       LEFT JOIN sqldados.notasEntradaNdd AS N2
                 ON N2.cnpjDestinatario = L.cgc
                   AND N2.numero = inv.nfname
                   AND N2.serie = inv.invse
                   AND N2.xmlNfe != 'NULL'
WHERE inv.date BETWEEN @di AND @df
  AND iprd.storeno IN (1, 2, 3, 4, 5, 6, 7, 8)
  AND (iprd.storeno = @storeno OR @storeno = 0)
  AND cfo.name1 NOT LIKE 'TRANSF%'
  AND cfo.name1 NOT LIKE 'DEVOL%'
  AND cfo.no NOT IN (2949, 2353, 2916)
  AND cfo.no NOT IN (1949, 1353, 1916)
  AND (iprd.invno = @ni OR @ni = 0)
  AND (inv.nfname = @nf OR @nf = '')
  AND (inv.vendno = @vendno OR @vendno = 0)
  AND (iprd.prdno = @prd OR @CODIGO = '')
  AND (FIND_IN_SET(TRIM(iprd.prdno), @listaProdutos) > 0 OR @listaProdutos = '')
  AND (inv.account = @CDesp OR @CDesp = '')
GROUP BY iprd.invno, iprd.prdno, iprd.grade;

DROP TABLE IF EXISTS sqldados.query1234567;
CREATE TABLE sqldados.query1234567
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
       TRIM(prod)                                                    AS prod,
       grade,
       rotulo,
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
       IF((CSTp = '06' AND CSTn = '10' OR CSTp = '06' AND CSTn = '60' OR CSTp = CSTn), 'S',
          'N')                                                       AS cstDif,
       IF(ROUND(IF(CSTn = '20', ICMSc, ICMSn) * 100) = ROUND(ICMSp * 100), 'S',
          'N')                                                       AS icmsDif,
       IF(ROUND(IPIn * 100) = ROUND(IPIp * 100), 'S', 'N')           AS ipiDif,
       IF(ROUND(mvan * 100) = ROUND(mvap * 100) OR (ABS(mvan - mvap) < 0.01), 'S',
          'N')                                                       AS mvaDif,
       IF(NCMn = NCMp, 'S', 'N')                                     AS ncmDif,
       barcodepl,
       barcodecl,
       barcoden,
       barcodebp,
       'S'                                                           AS barcodeDif,
       refPrdp,
       refPrdn,
       IF(refPrdp = refPrdn, 'S', 'N')                               AS refPrdDif,
       freten,
       fretep,
       IF(freten = fretep, 'S', IF(freten > fretep, 'DP', 'DN'))     AS freteDif,
       frete,
       precon,
       precon * quant                                                AS preconTotal,
       precop,
       precopc,
       IF(precon = precop, 'S', IF(precon > precop, 'DP', 'DN'))     AS precoDif,
       pesoBruto,
       pedidoCompra,
       pesoBrutoTotal,
       pesoBrutoPrd,
       freteKg,
       freteUnit,
       freteUnit * quant                                             AS freteTotal,
       fretePerNf,
       fretePerPrc,
       IF(IFNULL(fretePerNf, 0) = IFNULL(fretePerPrc, 0), 'S',
          IF(IFNULL(fretePerNf, 0) > IFNULL(fretePerPrc, 0), 'DP',
             'DN'))                                                  AS fretePerDif,
       quant,
       estoque,
       mesesValidade,
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
       vlDesconto + vlLiquido + vlFrete + vlIcms + vlIpi + baseSubst AS vlTotal,
       xml,
       cDesp,
       IF(precoRef >= 99000, precoRef, NULL)                         AS precoRef
FROM sqldados.T_QUERY
