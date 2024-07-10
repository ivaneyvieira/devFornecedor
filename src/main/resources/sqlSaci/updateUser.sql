DO @NO := (SELECT MAX(no)
           FROM sqldados.users
           WHERE login = :login
             AND (bits1 & POW(2, 0)) = 0);

UPDATE sqldados.users
SET auxLong1 = :loja
WHERE no = @NO;

REPLACE INTO sqldados.userApp(userno, appName, bitAcesso, bitAcesso2, senhaPrint)
VALUES (@NO, :appName, :bitAcesso, :bitAcesso2, :senhaPrint)
