select storeno,
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
from sqldados.nfdevEmail       AS NE
  inner join sqldados.devEmail AS E
	       USING (idEmail)
WHERE storeno = :storeno
  AND pdvno = :pdvno
  AND xano = :xano