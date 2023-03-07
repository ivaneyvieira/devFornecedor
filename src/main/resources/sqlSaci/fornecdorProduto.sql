USE sqldados;

DO @filtroStr := :filtro;
DO @FiltroNum := IF(:filtro REGEXP '^[0-9]+$', :filtro * 1, 0);

DROP TEMPORARY TABLE IF EXISTS T_PRDEND;
CREATE TEMPORARY TABLE T_PRDEND (
  PRIMARY KEY (prdno, vendno)
)
SELECT I.prdno,
       N.vendno,
       MAX(N.invno) AS invno
FROM sqldados.iprd        AS I
  INNER JOIN sqldados.inv AS N
	       USING (invno)
WHERE N.type = 0
  AND N.bits & POW(2, 4) = 0
  AND N.vendno != 0
  AND FALSE
GROUP BY I.prdno, N.vendno;

DROP TEMPORARY TABLE IF EXISTS T_PRDDATA;
CREATE TEMPORARY TABLE T_PRDDATA (
  PRIMARY KEY (vendno)
)
SELECT P.vendno,
       CAST(MAX(I.issue_date) AS DATE)              AS data,
       I.invno,
       CAST(CONCAT(I.nfname, '/', I.invse) AS CHAR) AS nota
FROM T_PRDEND             AS P
  INNER JOIN sqldados.inv AS I
	       USING (invno)
GROUP BY vendno;

DROP TEMPORARY TABLE IF EXISTS T_PRDVEND;
CREATE TEMPORARY TABLE T_PRDVEND (
  PRIMARY KEY (vendno)
)
SELECT V.no                         AS vendno,
       C.no                         AS custno,
       V.name                       AS nomeFornecedor,
       IFNULL(TRIM(C.id_sname), '') AS nomeFantasiaC,
       TRIM(V.auxChar1)             AS nomeFantasiaV,
       V.cgc                        AS cnpj,
       V.city                       AS cidade,
       V.state                      AS uf
FROM sqldados.vend         AS V
  LEFT JOIN sqldados.custp AS C
	      ON V.cgc = C.cpf_cgc
GROUP BY V.no;

DROP TEMPORARY TABLE IF EXISTS T_FILE;
CREATE TEMPORARY TABLE T_FILE (
  PRIMARY KEY (vendno)
)
SELECT xano AS vendno, COUNT(*) AS quantAnexo
FROM sqldados.nfdevFile
WHERE storeno = 88
  AND pdvno = 8888
GROUP BY xano;

SELECT vendno,
       custno,
       nomeFornecedor                                                                  AS nomeFornecedor,
       data,
       invno,
       nota,
       IFNULL(quantAnexo, 0)                                                           AS quantAnexo,
       IF(nomeFantasiaV = '', IF(nomeFantasiaC = '', '', nomeFantasiaC),
	  IF(nomeFantasiaC = '', nomeFantasiaV,
	     CONCAT(nomeFantasiaV, nomeFantasiaC)))                                    AS nomeFantasia,
       cnpj,
       cidade,
       uf,
       IFNULL(C.texto, '')                                                             AS texto
FROM T_PRDVEND
  LEFT JOIN T_PRDDATA
	      USING (vendno)
  LEFT JOIN T_FILE                   AS F
	      USING (vendno)
  LEFT JOIN sqldados.vendComplemento AS C
	      USING (vendno)
WHERE (@filtroStr = '' OR nomeFornecedor LIKE CONCAT('%', @filtroStr, '%'))
   OR vendno = @FiltroNum
   OR custno = @FiltroNum
GROUP BY vendno