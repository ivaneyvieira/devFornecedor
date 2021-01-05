DROP TEMPORARY TABLE IF EXISTS T;
CREATE TEMPORARY TABLE T (
  PRIMARY KEY (nfekey)
)
select nfekey,
       nfnumero * 1                                                        as nfnumero,
       nfserie,
       nfdata * 1                                                          as nfdata,
       nfhora,
       CONCAT(MID(rementente, 1, 2), '.', MID(rementente, 3, 3), '.', MID(rementente, 6, 3), '/',
	      MID(rementente, 9, 4), '-', MID(rementente, 13, 2))          as rementente,
       CONCAT(MID(destinatario, 1, 2), '.', MID(destinatario, 3, 3), '.', MID(destinatario, 6, 3),
	      '/', MID(destinatario, 9, 4), '-', MID(destinatario, 13, 2)) as destinatario
from sqldados.notasPendentes;

select nfekey               as nfekey,
       nfnumero             as nfno,
       nfserie              as nfse,
       cast(nfdata as date) as dataNota,
       nfhora               as horaNota,
       V.no                 as vendno,
       S.no                 as storeno,
       V.name               as fornecedor,
       S.sname              as loja
from T
  left join sqldados.vend  AS V
	      ON V.cgc = rementente
  left join sqldados.store AS S
	      ON S.cgc = destinatario;