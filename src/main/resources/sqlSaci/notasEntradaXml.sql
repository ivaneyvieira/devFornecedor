SELECT id,
       xmlNfe
FROM sqldados.notasEntradaNdd AS N
       LEFT JOIN sqldados.invnfe AS CHAVE
                 ON N.chave = CONCAT('NFe', CHAVE.nfekey)
WHERE (invno = :invno)