DO @storeno := :storeno;
DO @di := :di;
DO @df := :df;
DO @vendno := :vendno;
DO @mfno := :mfno;
DO @ni := :ni;
DO @nf := :nf;
DO @listaProdutos := TRIM(:listaProdutos);
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

SELECT D.storeno                                    AS lj,
       I.invno                                      AS ni,
       CAST(I.date AS DATE)                         AS data,
       CAST(CONCAT(I.nfname, '/', I.invse) AS CHAR) AS nfe,
       P.mfno                                       AS fornCad,
       I.vendno                                     AS fornNota,
       TRIM(D.prdno)                                AS prod,
       TRIM(MID(P.name, 1, 37))                     AS descricao,
       IFNULL(S.ncm, '')                            AS ncm,
       D.cstIcms                                    AS cst,
       D.cfop                                       AS cfop,
       TRIM(MID(P.name, 38, 3))                     AS un,
       D.qtty / 1000                                AS quant,
       D.fob / 100                                  AS valorUnit,
       (D.qtty / 1000) * (D.fob / 100)              AS valorTotal,
       D.baseIcms / 100                             AS baseIcms,
       D.ipiAmt / 100                               AS valorIPI,
       IF(D.baseIpi = 0, 0.00, D.ipi / 100)         AS aliqIpi,
       D.icms / 100                                 AS valorIcms,
       IF(D.baseIcms = 0, 0.00, D.icmsAliq / 100)   AS aliqIcms
FROM sqldados.iprd            AS D
  INNER JOIN sqldados.inv     AS I
	       USING (invno)
  INNER JOIN T_VEND           AS V
	       ON V.no = I.vendno
  INNER JOIN T_PRD            AS P
	       ON (P.no = D.prdno)
  INNER JOIN sqldados.cfo     AS C
	       ON (C.no = D.cfop)
  LEFT JOIN  sqldados.spedprd AS S
	       ON (S.prdno = P.no)
WHERE I.date BETWEEN @di AND @df
  AND D.storeno IN (1, 2, 3, 4, 5, 6, 7)
  AND (D.storeno = @storeno OR @storeno = 0)
  AND C.name1 NOT LIKE 'TRANSF%'
  AND C.name1 NOT LIKE 'DEVOL%'
  AND C.name1 NOT LIKE '%BONIF%'
  AND C.no NOT IN (2949, 2353, 2916)
  AND C.no NOT IN (1949, 1353, 1916)
  AND (D.invno = @ni OR @ni = 0)
  AND (I.nfname = @nf OR @nf = '')
  AND (I.vendno = @vendno OR @vendno = 0)
  AND (FIND_IN_SET(TRIM(D.prdno), @listaProdutos) > 0 OR @listaProdutos = '')
GROUP BY D.invno, D.prdno
ORDER BY prod, data


