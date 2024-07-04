SELECT *
FROM sqldados.userApp;

ALTER TABLE sqldados.userApp
  ADD COLUMN bitAcessoStr VARCHAR(400) DEFAULT '';

ALTER TABLE sqldados.userApp
  DROP COLUMN bitAcessoStr;

ALTER TABLE sqldados.userApp
  ADD COLUMN bitAcesso2 BIGINT DEFAULT 0 NULL;


SELECT userno, appName, bitAcesso, LENGTH(BIN(bitAcesso)) as bits, bitAcesso2
FROM sqldados.userApp;

