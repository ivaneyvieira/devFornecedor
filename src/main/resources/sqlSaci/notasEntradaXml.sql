DROP TEMPORARY TABLE IF EXISTS T1;
CREATE TEMPORARY TABLE T1
SELECT id,
       xmlNfe AS xml
FROM sqldados.notasEntradaNdd AS N
       LEFT JOIN sqldados.invnfe AS CHAVE
                 ON N.chave = CONCAT('NFe', CHAVE.nfekey)
WHERE (invno = :ni AND xmlNfe != 'NULL');

DROP TEMPORARY TABLE IF EXISTS T2;
CREATE TEMPORARY TABLE T2
SELECT id,
       xmlNfe AS xml
FROM sqldados.notasEntradaNdd AS N
       INNER JOIN sqldados.store AS S
                  ON N.cnpjDestinatario = S.cgc AND S.no = :loja
WHERE N.NUMERO = :numero
  AND N.SERIE = :serie
  AND N.xmlNfe != 'NULL'
  AND NOT EXISTS(SELECT * FROM T1);

SELECT id, xml
FROM T1
UNION
SELECT id, xml
FROM T2
