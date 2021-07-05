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

SELECT lj,
       ni,
       data,
       nfe,
       fornCad,
       fornNota,
       prod,
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
       ncmDif
FROM sqldados.query1234567
WHERE @cst = cstDif
   OR @icms = icmsDif
   OR @ipi = ipiDif
   OR @mva = mvaDif
   OR @ncm = ncmDif
   OR (@cst = 'T' AND @icms = 'T' AND @ipi = 'T' AND @mva = 'T' AND @ncm = 'T')

