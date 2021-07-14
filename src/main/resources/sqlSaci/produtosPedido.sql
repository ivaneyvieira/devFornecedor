DROP TEMPORARY TABLE IF EXISTS T_REF;
CREATE TEMPORARY TABLE T_REF (
  PRIMARY KEY (codigo, grade)
)
SELECT prdno                                                               AS codigo,
       grade                                                               AS grade,
       CAST(MID(MAX(CONCAT(LPAD(l1, 10, '0'), prdrefno)), 11, 20) AS char) AS refno
FROM sqldados.prdref
GROUP BY prdno, grade
HAVING COUNT(*) > 1;

DROP TEMPORARY TABLE IF EXISTS T_PEDIDO;
CREATE TEMPORARY TABLE T_PEDIDO
SELECT X.storeno                    AS loja,
       X.ordno                      AS pedido,
       X.prdno                      AS codigo,
       P.mfno_ref                   AS refFor,
       TRIM(MID(P.name, 1, 37))     AS descricao,
       X.grade                      AS grade,
       ROUND(X.qtty / 1000)         AS qtde,
       I.ipi / 10000                AS ipiAliq,
       I.costdel3 / 10000           AS stAliq,
       IFNULL(B.barcode, P.barcode) AS barcode,
       TRIM(MID(P.name, 37, 3))     AS un,
       P.taxno                      AS st,
       IFNULL(S.ncm, '')            AS ncm
FROM sqldados.eoprd           AS X
  LEFT JOIN  sqldados.prp     AS I
	       ON I.storeno = 10 AND I.prdno = X.prdno
  LEFT JOIN  sqldados.prdbar  AS B
	       ON B.prdno = X.prdno AND B.grade = X.grade
  INNER JOIN sqldados.prd     AS P
	       ON X.prdno = P.no
  LEFT JOIN  sqldados.spedprd AS S
	       ON S.prdno = X.prdno
WHERE X.storeno = :loja
  AND X.ordno = :pedido
GROUP BY X.storeno, X.ordno, X.prdno, X.grade;

DROP TEMPORARY TABLE IF EXISTS T_PRD;
CREATE TEMPORARY TABLE T_PRD (
  PRIMARY KEY (prdno, grade)
)
SELECT codigo    AS prdno,
       grade,
       SUM(qtde) AS qtde
FROM T_PEDIDO
GROUP BY codigo, grade;

DROP TEMPORARY TABLE IF EXISTS T_PRD_ULT;
CREATE TEMPORARY TABLE T_PRD_ULT (
  PRIMARY KEY (invno, prdno, grade)
)
SELECT prdno,
       grade,
       MAX(invno)                                 AS invno,
       MAX(IF(P.qtty >= T_PRD.qtde, invno, NULL)) AS invnoQ
FROM sqldados.inv          AS I
  INNER JOIN sqldados.iprd AS P
	       USING (invno)
  INNER JOIN T_PRD
	       USING (prdno, grade)
WHERE I.bits & POW(2, 4) = 0
  AND I.storeno = 4
  AND I.auxShort13 & POW(2, 15) = 0
  AND I.cfo NOT IN (1916, 2916, 1949, 2949) /*1910, 2910 - bonificação,  */
  AND I.type = 0
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_MFINV;
CREATE TEMPORARY TABLE T_MFINV (
  PRIMARY KEY (invno, prdno, grade)
)
SELECT I.invno,
       P.prdnoRef AS prdno,
       P.grade,
       P.cst,
       P.cfop
FROM sqldados.mfinv         AS I
  INNER JOIN sqldados.mfprd AS P
	       ON P.mfinv_seqno = I.seqnoAuto
WHERE invno <> 0
GROUP BY I.invno, P.prdnoRef, P.grade;

DROP TEMPORARY TABLE IF EXISTS T_INV;
CREATE TEMPORARY TABLE T_INV (
  PRIMARY KEY (codigo, grade)
)
SELECT P.prdno                                      AS codigo,
       P.grade,
       P.invno,
       vendno,
       IFNULL(R.form_label, '')                     AS rotulo,
       CAST(CONCAT(I.nfname, '/', I.invse) AS CHAR) AS notaInv,
       CAST(I.issue_date AS DATE)                   AS dateInv,
       P.fob / 100                                  AS valorUnitInv,
       SUM(qtty / 1000)                             AS quantInv,
       IFNULL(X.nfekey, '')                         AS chaveUlt,
       IFNULL(M.cst, cstIcms)                       AS cst,
       IFNULL(M.cfop, P.cfop)                       AS cfopNota,
       CASE IFNULL(R.form_label, '')
	 WHEN 'NORMAL..'
	   THEN CASE
		  WHEN V.state IS NULL
		    THEN 0
		  WHEN V.state = 'PI'
		    THEN 5202
		  ELSE 6202
		END
	 WHEN 'SUBSTI00'
	   THEN CASE
		  WHEN V.state IS NULL
		    THEN 0
		  WHEN V.state = 'PI'
		    THEN 5411
		  ELSE 6411
		END
	 WHEN 'REDUZI88'
	   THEN CASE
		  WHEN V.state IS NULL
		    THEN 0
		  WHEN V.state = 'PI'
		    THEN 5202
		  ELSE 6202
		END
	 WHEN 'REDUZI56'
	   THEN CASE
		  WHEN V.state IS NULL
		    THEN 0
		  WHEN V.state = 'PI'
		    THEN 5202
		  ELSE 6202
		END
	 WHEN 'ISENTO..'
	   THEN CASE
		  WHEN V.state IS NULL
		    THEN 0
		  WHEN V.state = 'PI'
		    THEN 5202
		  ELSE 6202
		END
	 WHEN 'NAO_TRIB'
	   THEN 0
	 ELSE 0
       END                                          AS cfop,
       P.icmsAliq / 100                             AS icmsAliq,
       P.ipi / 100                                  AS ipiAliq
FROM sqldados.iprd           AS P
  INNER JOIN sqldados.inv    AS I
	       USING (invno)
  LEFT JOIN  sqldados.vend   AS V
	       ON V.no = I.vendno
  LEFT JOIN  sqldados.invnfe AS X
	       USING (invno)
  INNER JOIN T_PRD_ULT       AS U
	       ON P.invno = IFNULL(U.invnoQ, U.invno) AND P.prdno = U.prdno AND P.grade = U.grade
  LEFT JOIN  T_MFINV         AS M
	       ON P.invno = M.invno AND P.prdno = M.prdno AND M.grade = U.grade
  LEFT JOIN  sqldados.prdalq AS R
	       ON R.prdno = P.prdno
GROUP BY P.prdno, P.grade;

SELECT P.loja,
       0                                                                  AS pdv,
       0                                                                  AS transacao,
       P.codigo                                                           AS codigo,
       IFNULL(R.refno, P.refFor)                                          AS refFor,
       P.descricao                                                        AS descricao,
       P.grade                                                            AS grade,
       P.qtde                                                             AS qtde,
       IFNULL(N.valorUnitInv, 0.00)                                       AS valorUnitario,
       IFNULL(P.qtde * N.valorUnitInv, 0.00)                              AS valorTotal,
       IFNULL(P.ipiAliq * P.qtde * N.valorUnitInv, 0.00)                  AS ipi,
       IFNULL(P.stAliq * P.qtde * N.valorUnitInv, 0.00)                   AS vst,
       CAST(N.cst AS char)                                                AS cst,
       CAST(IFNULL(N.cfop, '') AS char)                                   AS cfop,
       IFNULL((P.ipiAliq + P.stAliq + 1) * P.qtde * N.valorUnitInv, 0.00) AS valorTotalIpi,
       P.barcode                                                          AS barcode,
       P.un                                                               AS un,
       P.st                                                               AS st,
       IFNULL(N.invno, 0)                                                 AS invno,
       IFNULL(N.vendno, 0)                                                AS vendno,
       IFNULL(N.rotulo, '')                                               AS rotulo,
       ROUND(IFNULL(N.quantInv, 0.00))                                    AS quantInv,
       IFNULL(N.notaInv, '')                                              AS notaInv,
       N.dateInv                                                          AS dateInv,
       IFNULL(N.valorUnitInv, 0.00)                                       AS valorUnitInv,
       IFNULL(N.valorUnitInv, 0.00) * P.qtde                              AS valorTotalInv,
       IFNULL(N.chaveUlt, '')                                             AS chaveUlt,
       P.ncm                                                              AS ncm,
       IFNULL(P.qtde * N.valorUnitInv, 0.00)                              AS baseICMS,
       IFNULL(P.qtde * N.valorUnitInv * N.icmsAliq / 100, 0.00)           AS valorICMS,
       IFNULL(P.qtde * N.valorUnitInv, 0.00)                              AS baseIPI,
       IFNULL(P.qtde * N.valorUnitInv * N.ipiAliq / 100, 0.00)            AS valorIPI,
       IFNULL(N.icmsAliq, 0.00)                                           AS icmsAliq,
       IFNULL(N.ipiAliq, 0.00)                                            AS ipiAliq
FROM T_PEDIDO     AS P
  LEFT JOIN T_INV AS N
	      USING (codigo, grade)
  LEFT JOIN T_REF AS R
	      USING (codigo, grade)


