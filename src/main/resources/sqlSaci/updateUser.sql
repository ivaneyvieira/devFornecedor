DO @NO:=(SELECT MAX(no) FROM sqldados.users WHERE login = :login);

INSERT  INTO sqldados.userApp(userno, appName, bitAcesso)
VALUES(@NO, 'devFornecedor', :bitAcesso)
ON DUPLICATE KEY UPDATE bitAcesso = :bitAcesso