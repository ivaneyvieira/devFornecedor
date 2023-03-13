USE sqldados;

SET @VENDNO := :vendno;
SET @QUERY := :query;
SET @QUERY_LIKE := CONCAT('%', @QUERY, '%');

DROP TEMPORARY TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS
SELECT storeno                                  AS loja,
       invno                                    AS ni,
       CAST(CONCAT(nfname, '/', invse) AS char) AS nf,
       CAST(issue_date AS DATE)                 AS emissao,
       CAST(date AS DATE)                       AS entrada,
       grossamt / 100                           AS valorNota,
       remarks                                  AS obs
FROM sqldados.inv AS I
WHERE vendno = @VENDNO
ORDER BY invno DESC;

SELECT loja,
       ni,
       nf,
       emissao,
       entrada,
       valorNota,
       obs
FROM T_NOTAS TN
WHERE @QUERY = ''
   OR loja = @QUERY
   OR ni = @QUERY
   OR nf LIKE @QUERY
   OR DATE_FORMAT(emissao, '%d/%m/%Y') = @QUERY
   OR DATE_FORMAT(entrada, '%d/%m/%Y') = @QUERY
   OR obs LIKE @QUERY_LIKE
