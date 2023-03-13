USE sqldados;

SET @VENDNO := :vendno;

SELECT storeno                                  AS loja,
       invno                                    AS ni,
       CAST(CONCAT(nfname, '/', invse) AS char) AS nf,
       CAST(issue_date AS DATE)                 AS emissao,
       CAST(date AS DATE)                       AS entrada,
       grossamt / 100                           AS valorNota,
       remarks                                  AS obs
FROM sqldados.inv AS I
WHERE vendno = @VENDNO
ORDER BY invno DESC