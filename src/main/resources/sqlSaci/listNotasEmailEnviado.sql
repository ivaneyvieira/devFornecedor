SELECT storeno,
       pdvno,
       xano,
       idEmail,
       data,
       hora,
       email,
       assunto,
       msg,
       planilha,
       relatorio,
       anexos
FROM sqldados.nfdevEmail AS NE
       INNER JOIN sqldados.devEmail AS E
                  USING (idEmail)
WHERE E.idEmail = :idEmail