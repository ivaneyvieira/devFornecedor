USE sqldados;

DO @filtroStr := :filtro;
DO @FiltroNum := IF(:filtro REGEXP '^[0-9]+$', :filtro * 1, 0);

DROP TEMPORARY TABLE IF EXISTS T_PRDVEND;
CREATE TEMPORARY TABLE T_PRDVEND (
  PRIMARY KEY (vendno)
)
SELECT V.no             AS vendno,
       C.no             AS custno,
       V.name           AS nomeFornecedor,
       TRIM(V.auxChar1) AS nomeFantasia,
       V.cgc            AS cnpj,
       V.city           AS cidade,
       V.state          AS uf
FROM sqldados.vend         AS V
  LEFT JOIN sqldados.custp AS C
	      ON V.cgc = C.cpf_cgc
WHERE @filtroStr = ''
   OR V.name LIKE CONCAT('%', @filtroStr, '%')
   OR V.no = @FiltroNum
   OR C.no = @FiltroNum
   OR TRIM(V.auxChar1) LIKE CONCAT('%', @filtroStr, '%')
   OR V.cgc LIKE CONCAT(@filtroStr, '%')
   OR V.city LIKE CONCAT(@filtroStr, '%')
   OR V.state LIKE CONCAT(@filtroStr, '%')
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
       nomeFornecedor        AS nomeFornecedor,
       IFNULL(quantAnexo, 0) AS quantAnexo,
       nomeFantasia          AS nomeFantasia,
       cnpj,
       cidade,
       uf,
       IFNULL(C.texto, '')   AS texto
FROM T_PRDVEND
  LEFT JOIN T_FILE                   AS F
	      USING (vendno)
  LEFT JOIN sqldados.vendComplemento AS C
	      USING (vendno)
