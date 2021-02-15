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
       CAST(CONCAT(I.storeno, ' ', I.nfname, '/', I.invse) AS CHAR) AS notaInv,
       CAST(I.issue_date AS DATE)                                   AS dateInv,
       P.fob / 100                                                  AS valorUnitInv,
       SUM(qtty / 1000)                                             AS quantInv,
       IFNULL(X.nfekey, '')                                         AS chaveUlt
FROM sqldados.iprd           AS P
  INNER JOIN sqldados.inv    AS I
	       USING (invno)
  LEFT JOIN  sqldados.invnfe AS X
	       USING (invno)
  INNER JOIN T_PRD_ULT
	       USING (invno, prdno, grade)
GROUP BY prdno, grade;

SELECT loja,
       pdv,
       transacao,
       codigo,
       refFor,
       descricao,
       grade,
       qtde,
       valorUnitario,
       valorTotal,
       TRUNCATE(IFNULL(ipiAliq * valorTotal, 0.00), 2)                           AS ipi,
       TRUNCATE(IFNULL(stAliq * valorTotal, 0.00), 2)                            AS vst,
       TRUNCATE(IFNULL(ipiAliq * valorTotal, 0.00), 2) +
       TRUNCATE(IFNULL(stAliq * valorTotal, 0.00), 2) + IFNULL(valorTotal, 0.00) AS valorTotalIpi,
       barcode,
       un,
       st,
       IFNULL(invno, 0)                                                          AS invno,
       ROUND(IFNULL(quantInv, 0.00))                                             AS quantInv,
       IFNULL(notaInv, '')                                                       AS notaInv,
       dateInv                                                                   AS dateInv,
       IFNULL(valorUnitInv, 0.00)                                                AS valorUnitInv,
       IFNULL(valorUnitInv, 0.00) * qtde                                         AS valorTotalInv,
       chaveUlt                                                                  AS chaveUlt
FROM T_NF
  LEFT JOIN T_INV
	      USING (codigo, grade)

