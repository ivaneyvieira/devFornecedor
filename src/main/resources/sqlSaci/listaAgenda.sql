DROP TABLE IF EXISTS sqldados.T_INV2;
CREATE TEMPORARY TABLE sqldados.T_INV2 /*T2*/
SELECT inv2.storeno                                                                     AS loja,
       TRUNCATE(CONCAT(RIGHT(inv2.c1, 4), MID(inv2.c1, 4, 2), LEFT(inv2.c1, 2)), 0) * 1 AS data,
       LEFT(inv2.c2, 8)                                                                 AS hora,
       MID(inv2.c2, 10, 2)                                                              AS agd,
       emp.sname                                                                        AS recebedor,
       inv2.invno                                                                       AS invno,
       inv2.vendno                                                                      AS forn,
       IFNULL(vend.sname, 'NAO ENCONTRADO')                                             AS abrev,
       IF(inv2.nfname = 0, CAST(inv2.invno AS CHAR), inv2.nfname)                       AS nf,
       inv.invno                                                                        AS ni,
       NULL                                                                             AS entrada,
       inv2.issue_date                                                                  AS emissao,
       inv2.ordno                                                                       AS pedido,
       CAST(inv2.l2 AS CHAR)                                                            AS conh,
       inv2.carrno                                                                      AS transp,
       IFNULL(carr.name, 'NAO ENCONT')                                                  AS nome,
       CAST(inv2.packages AS CHAR)                                                      AS volume,
       inv2.grossamt                                                                    AS total
FROM sqldados.inv2
  LEFT JOIN sqldados.vend
	      ON (vend.no = inv2.vendno)
  LEFT JOIN sqldados.carr AS carr
	      ON (carr.no = inv2.carrno)
  LEFT JOIN sqldados.inv  AS inv
	      ON (inv.vendno = inv2.vendno AND inv.nfname = inv2.nfname AND
		  inv.ordno = inv2.ordno AND inv.grossamt = inv2.grossamt)
  LEFT JOIN sqldados.emp
	      ON (emp.no = inv2.auxStr6)
WHERE inv.invno IS NULL;

DROP TABLE IF EXISTS sqldados.T_INVNEW;
CREATE TEMPORARY TABLE sqldados.T_INVNEW
SELECT DISTINCT invnew.storeno                                                     AS loja,
		invnew.date                                                        AS data,
		LEFT(invnew.auxStr1, 5)                                            AS hora,
		RIGHT(invnew.auxStr1, 2)                                           AS agd,
		''                                                                 AS recebedor,
		0                                                                  AS invno,
		invnew.vendno                                                      AS forn,
		IFNULL(vend.sname, 'NAO ENCONTRADO')                               AS abrev,
		IF(invnew.nfname = '0', CAST(invnew.invno AS CHAR), invnew.nfname) AS nf,
		IFNULL(inv.invno, 0)                                               AS ni,
		0                                                                  AS entrada,
		0                                                                  AS emissao,
		0                                                                  AS pedido,
		invnew.auxStr4                                                     AS conh,
		invnew.carrno                                                      AS transp,
		IFNULL(carr.sname, 'NAO ENCONT')                                   AS nome,
		invnew.auxStr5                                                     AS volume,
		invnew.grossamt                                                    AS total
FROM sqldados.invnew
  LEFT JOIN sqldados.vend
	      ON (vend.no = invnew.vendno)
  LEFT JOIN sqldados.vend AS carr
	      ON (carr.no = invnew.carrno)
  LEFT JOIN sqldados.inv
	      ON (inv.vendno = invnew.vendno AND inv.nfname = invnew.nfname /*AND inv.storeno = invnew.storeno AND inv.invse = invnew.invse*/)
WHERE (invnew.date > 20171031)
  AND inv.invno IS NULL;

DROP TABLE IF EXISTS sqldados.T_INV_UNION;
CREATE TEMPORARY TABLE sqldados.T_INV_UNION
SELECT loja,
       data,
       hora,
       agd,
       recebedor,
       invno,
       forn,
       abrev,
       emissao,
       nf,
       volume,
       total,
       transp,
       nome,
       pedido,
       ni,
       entrada,
       conh
FROM sqldados.T_INV2
UNION
SELECT DISTINCT loja,
		data,
		hora,
		agd,
		recebedor,
		invno,
		forn,
		abrev,
		emissao,
		nf,
		volume,
		total,
		transp,
		nome,
		pedido,
		ni,
		entrada,
		conh
FROM sqldados.T_INVNEW;

SELECT loja,
       CAST(IF(data = 0, NULL, data * 1) AS DATE)   AS data,
       IFNULL(hora, '')                             AS hora,
       IFNULL(agd, '')                              AS agd,
       IFNULL(recebedor, '')                        AS recebedor,
       IFNULL(CAST(invno AS CHAR), '')              AS invno,
       forn                                         AS fornecedor,
       abrev                                        AS abreviacao,
       CAST(IF(emissao = 0, NULL, emissao) AS DATE) AS emissao,
       IFNULL(nf, '')                               AS nf,
       IFNULL(volume, '')                           AS volume,
       IFNULL(total / 100, 0.00)                    AS total,
       transp                                       AS transp,
       nome                                         AS nome,
       pedido                                       AS pedido
FROM sqldados.T_INV_UNION
WHERE loja <> 0
HAVING IF(:agendado = 'S', data IS NOT NULL AND hora <> '', NOT (data IS NOT NULL AND hora <> ''))

