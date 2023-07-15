SELECT storeno,
       pdvno,
       xano,
       idEmail,
       messageID,
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
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano