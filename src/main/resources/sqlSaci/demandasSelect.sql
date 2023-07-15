USE sqldados;

DO @QUERY := :pesquisa;
DO @QINT := IF(@QUERY REGEXP '^[0-9]+$', @QUERY * 1, NULL);
DO @QDOUBLE := IF(@QUERY REGEXP '^[0-9]+,[0-9]+$', REPLACE(@QUERY, ',', '.') * 1.00, NULL);
DO @QDATESTR := IF(@QUERY REGEXP '^[0-9]{2}/[0-9]{2}/[0-9]{4}$', @QUERY, NULL);
DO @QDATE := CONCAT(MID(@QDATESTR, 7, 4), MID(@QDATESTR, 4, 2), MID(@QDATESTR, 1, 2)) * 1;
DO @QTEXT := IF(@QINT IS NULL AND @QDOUBLE IS NULL AND @QDATE IS NULL, @QUERY, NULL);
DO @QTEXT_LIKE := CONCAT('%', @QTEXT, '%');

/*
SELECT @QUERY, @QINT, @QDOUBLE, @QDATESTR
*/

DROP TEMPORARY TABLE IF EXISTS T_FILE;
CREATE TEMPORARY TABLE T_FILE
(
  PRIMARY KEY (id)
)
SELECT xano AS id, COUNT(*) AS quantAnexo
FROM sqldados.nfdevFile
WHERE storeno = 1
  AND pdvno = 8888
GROUP BY xano;

SELECT id,
       CAST(date AS DATE)    AS date,
       titulo,
       conteudo,
       concluido,
       IFNULL(quantAnexo, 0) AS quantAnexo,
       vendno,
       destino,
       origem
FROM sqldados.agendaDemandas AS A
       LEFT JOIN T_FILE AS F
                 USING (id)
WHERE (concluido = :concluido OR :concluido = '')
  AND (titulo LIKE @QTEXT_LIKE OR conteudo LIKE @QTEXT_LIKE OR destino LIKE @QTEXT_LIKE OR
       @QTEXT IS NULL)
  AND (date = @QDATE OR @QDATE IS NULL)
  AND (vendno = 0)
