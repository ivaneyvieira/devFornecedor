use sqldados;

DROP TABLE IF EXISTS prdConferencia;
CREATE TABLE prdConferencia
(
  storeno          int         NOT NULL,
  prdno            varchar(16) NOT NULL,
  grade            varchar(10) NOT NULL,
  dataConferencia  date        NULL,
  valorConferencia Int         NULL,
  PRIMARY KEY (storeno, prdno, grade)
);