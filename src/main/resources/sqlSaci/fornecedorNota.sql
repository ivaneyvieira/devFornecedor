USE sqldados;

SET @VENDNO := :vendno;
SET @LOJA := :loja;
SET @QUERY := :query;
SET @QUERY_LIKE := CONCAT('%', @QUERY, '%');


DROP TEMPORARY TABLE IF EXISTS T_PARCELA_ULT;
CREATE TEMPORARY TABLE T_PARCELA_ULT (
  PRIMARY KEY (invno),
  chave varchar(20)
)
SELECT invno, MAX(CONCAT(LPAD(instno, 10, ' '), LPAD(duedate, 10, ' '))) AS chave
FROM sqldados.invxa       AS X
  INNER JOIN sqldados.inv AS I
	       USING (invno)
WHERE I.vendno = @VENDNO
GROUP BY invno;

DROP TEMPORARY TABLE IF EXISTS T_PARCELA_ULT2;
CREATE TEMPORARY TABLE T_PARCELA_ULT2 (
  PRIMARY KEY (invno, instno, duedate)
)
SELECT invno,
       TRIM(MID(chave, 1, 10)) * 1  AS instno,
       TRIM(MID(chave, 11, 10)) * 1 AS duedate
FROM T_PARCELA_ULT;

DROP TEMPORARY TABLE IF EXISTS T_FILES;
CREATE TEMPORARY TABLE T_FILES (
  PRIMARY KEY (invno)
)
SELECT xano AS invno, COUNT(*) AS qt
FROM nfdevFile
WHERE storeno = 77
  AND pdvno = 7777
GROUP BY xano;

DROP TEMPORARY TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS (
  nf varchar(20)
)
SELECT I.storeno                                          AS loja,
       invno                                              AS ni,
       IF(invse = '', nfname, CONCAT(nfname, '/', invse)) AS nf,
       CAST(issue_date AS DATE)                           AS emissao,
       CAST(date AS DATE)                                 AS entrada,
       grossamt / 100                                     AS valorNota,
       I.remarks                                          AS obs,
       CAST(X.duedate AS date)                            AS vencimento,
       CASE X.status
	 WHEN 0
	   THEN 'Em Aberto'
	 WHEN 1
	   THEN 'Pgto.Total'
	 WHEN 2
	   THEN 'Pgto.Parcial'
	 WHEN 3
	   THEN 'Pgto.c/Desconto'
	 WHEN 4
	   THEN 'Pgto.+ Juros'
	 WHEN 5
	   THEN 'Cancelada'
	 WHEN 6
	   THEN 'Pgto.c/Devolucao'
	 WHEN 7
	   THEN 'Consignacao'
	 ELSE ''
       END                                                AS situacao,
       X.remarks                                          AS obsParcela,
       IFNULL(F.qt, 0)                                    AS quantAnexo
FROM sqldados.inv          AS I
  LEFT JOIN T_FILES        AS F
	      USING (invno)
  LEFT JOIN T_PARCELA_ULT2 AS U
	      USING (invno)
  LEFT JOIN sqldados.invxa AS X
	      USING (invno, instno, duedate)
WHERE (vendno = @VENDNO)
  AND (I.storeno = @LOJA OR @LOJA = 0)
ORDER BY invno DESC;



SELECT loja,
       ni,
       nf,
       emissao,
       entrada,
       valorNota,
       obs,
       vencimento,
       situacao,
       obsParcela,
       quantAnexo
FROM T_NOTAS TN
WHERE @QUERY = ''
   OR loja = @QUERY
   OR ni = @QUERY
   OR nf LIKE @QUERY
   OR DATE_FORMAT(emissao, '%d/%m/%Y') = @QUERY
   OR DATE_FORMAT(entrada, '%d/%m/%Y') = @QUERY
   OR obs LIKE @QUERY_LIKE
