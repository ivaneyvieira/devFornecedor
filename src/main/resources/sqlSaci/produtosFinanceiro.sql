DROP TEMPORARY TABLE IF EXISTS T_NF;
CREATE TEMPORARY TABLE T_NF
SELECT X.storeno                       AS loja,
       X.pdvno                         AS pdv,
       X.xano                          AS transacao,
       X.prdno                         AS codigo,
       TRIM(MID(P.name, 1, 37))        AS descricao,
       X.grade                         AS grade,
       ROUND(X.qtty)                   AS qtde,
       X.price / 100                   AS valorUnitario,
       ROUND(X.qtty) * (X.price / 100) AS valorTotal,
       I.ipi / 10000                   AS ipiAliq,
       I.costdel3 / 10000              AS stAliq,
       IFNULL(B.barcode, P.barcode)    AS barcode,
       P.mfno_ref                      AS refFor,
       TRIM(MID(P.name, 37, 3))        AS un,
       P.taxno                         AS st
FROM sqldados.xaprd          AS X
  LEFT JOIN  sqldados.prp    AS I
	       ON I.storeno = 10 AND I.prdno = X.prdno
  LEFT JOIN  sqldados.prdbar AS B
	       ON B.prdno = X.prdno AND B.grade = X.grade
  INNER JOIN sqldados.prd    AS P
	       ON X.prdno = P.no
WHERE X.storeno = :loja
  AND X.pdvno = :pdv
  AND X.xano = :transacao
GROUP BY X.storeno, X.pdvno, X.xano, X.prdno, X.grade;

DROP TEMPORARY TABLE IF EXISTS T_PRD;
CREATE TEMPORARY TABLE T_PRD (
  PRIMARY KEY (prdno, grade)
)
SELECT codigo AS prdno, grade
FROM T_NF
GROUP BY codigo, grade;

DROP TEMPORARY TABLE IF EXISTS T_PRD_ULT;
CREATE TEMPORARY TABLE T_PRD_ULT (
  PRIMARY KEY (invno, prdno, grade)
)
SELECT prdno,
       grade,
       MAX(invno) AS invno
FROM sqldados.inv          AS I
  INNER JOIN sqldados.iprd AS P
	       USING (invno)
  INNER JOIN T_PRD
	       USING (prdno, grade)
WHERE I.bits & POW(2, 4) = 0
  AND I.auxShort13 & POW(2, 15) = 0
  AND I.cfo NOT IN (1910, 2910, 1916, 2916, 1949, 2949)
  AND I.type = 0
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_INV;
CREATE TEMPORARY TABLE T_INV (
  PRIMARY KEY (codigo, grade)
)
SELECT prdno                                                        AS codigo,
       grade,
       invno,
       vendno,
       IFNULL(R.form_label, '')                                     AS rotulo,
       CAST(CONCAT(I.storeno, ' ', I.nfname, '/', I.invse) AS CHAR) AS notaInv,
       CAST(I.issue_date AS DATE)                                   AS dateInv,
       P.fob / 100                                                  AS valorUnitInv,
       SUM(qtty / 1000)                                             AS quantInv,
       IFNULL(X.nfekey, '')                                         AS chaveUlt,
       cstIcms                                                      AS cst,
       cfop,
       IF(LENGTH(P.c1) < 30 AND P.c1 <> '', 'N', 'S')               AS sefazOk,
       P.c1                                                         AS chaveSefaz
FROM sqldados.iprd           AS P
  INNER JOIN sqldados.inv    AS I
	       USING (invno)
  LEFT JOIN  sqldados.invnfe AS X
	       USING (invno)
  INNER JOIN T_PRD_ULT
	       USING (invno, prdno, grade)
  LEFT JOIN  sqldados.prdalq AS R
	       USING (prdno)
GROUP BY prdno, grade;


SELECT loja,
       pdv,
       transacao,
       TRIM(codigo)                                                              AS codigo,
       refFor,
       descricao                                                                 AS refName,
       descricao,
       grade,
       qtde,
       valorUnitario,
       valorTotal,
       TRUNCATE(IFNULL(ipiAliq * valorTotal, 0.00), 2)                           AS ipi,
       0.00                                                                      AS baseSt,
       TRUNCATE(IFNULL(stAliq * valorTotal, 0.00), 2)                            AS vst,
       cst                                                                       AS cst,
       cfop                                                                      AS cfop,
       TRUNCATE(IFNULL(ipiAliq * valorTotal, 0.00), 2) +
       TRUNCATE(IFNULL(stAliq * valorTotal, 0.00), 2) + IFNULL(valorTotal, 0.00) AS valorTotalIpi,
       barcode,
       un,
       st,
       IFNULL(invno, 0)                                                          AS invno,
       IFNULL(vendno, 0)                                                         AS vendno,
       IFNULL(rotulo, '')                                                        AS rotulo,
       ROUND(IFNULL(quantInv, 0.00))                                             AS quantInv,
       IFNULL(notaInv, '')                                                       AS notaInv,
       dateInv                                                                   AS dateInv,
       IFNULL(valorUnitInv, 0.00)                                                AS valorUnitInv,
       IFNULL(valorUnitInv, 0.00) * qtde                                         AS valorTotalInv,
       chaveUlt                                                                  AS chaveUlt,
       ''                                                                        AS ncm,
       0.00                                                                      AS baseICMS,
       0.00                                                                      AS valorICMS,
       0.00                                                                      AS baseIPI,
       0.00                                                                      AS valorIPI,
       0.00                                                                      AS icmsAliq,
       0.00                                                                      AS ipiAliq,
       IFNULL(sefazOk, '')                                                       AS sefazOk,
       IFNULL(chaveSefaz, '')                                                    AS chaveSefaz
FROM T_NF
  LEFT JOIN T_INV
	      USING (codigo, grade)

