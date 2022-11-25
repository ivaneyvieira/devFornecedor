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
DO @barcode := :barcode;
DO @refPrd := :refPrd;
DO @frete := :frete;
DO @preco := :preco;

SELECT lj,
       ni,
       data,
       dataEmissao,
       nfe,
       fornCad,
       fornNota,
       prod,
       grade,
       descricao,
       icmsn,
       icmsp,
       icmsd,
       icmsc,
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
       ncmDif,
       barcodep,
       barcoden,
       barcodeDif,
       refPrdp,
       refPrdn,
       refPrdDif,
       freten,
       fretep,
       freteDif,
       frete,
       precon,
       precop,
       precoDif
FROM sqldados.query1234567
WHERE @cst = cstDif
   OR @icms = icmsDif
   OR @ipi = ipiDif
   OR @mva = mvaDif
   OR @ncm = ncmDif
   OR @barcode = barcodeDif
   OR @refPrd = refPrdDif
   OR @frete = freteDif
   OR @preco = precoDif
   OR (@cst = 'T' AND @icms = 'T' AND @ipi = 'T' AND @mva = 'T' AND @ncm = 'T' AND
       @barcode = 'T' AND @refPrd = 'T' AND @frete = 'T' AND @preco= 'T')

