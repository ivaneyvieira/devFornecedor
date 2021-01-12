DROP TABLE IF EXISTS sqldados.T_INV2;
CREATE TEMPORARY TABLE sqldados.T_INV2 /*T2*/
SELECT inv2.storeno                                                                     AS loja,
       TRUNCATE(CONCAT(RIGHT(inv2.c1, 4), MID(inv2.c1, 4, 2), LEFT(inv2.c1, 2)), 0) * 1 AS data,
       LEFT(inv2.c2, 8)                                                                 AS hora,
       IFNULL(emp.no, 0)                                                                as empno,
       IFNULL(emp.sname, '')                                                            AS recebedor,
       inv2.invno                                                                       AS invno,
       inv2.vendno                                                                      AS forn,
       IFNULL(vend.sname, 'NAO ENCONTRADO')                                             AS abrev,
       IF(inv2.nfname = 0, CAST(inv2.invno AS CHAR), inv2.nfname)                       AS nf,
       inv2.issue_date                                                                  AS emissao,
       inv2.ordno                                                                       AS pedido,
       CAST(inv2.l2 AS CHAR)                                                            AS conh,
       inv2.carrno                                                                      AS transp,
       CAST(IFNULL(LEFT(carr.name, 10), 'NAO ENCONT') AS CHAR)                          AS nome,
       CAST(inv2.packages AS CHAR)                                                      AS volume,
       inv2.grossamt                                                                    AS total,
       IF(TRIM(inv2.c1) <> '' AND TRIM(LEFT(inv2.c2, 8)) <> '', 'S', 'N')               AS agendado,
       IF(emp.sname IS NULL, 'N', 'S')                                                  AS recebido
FROM sqldados.inv2
  LEFT JOIN sqldados.vend
	      ON (vend.no = inv2.vendno)
  LEFT JOIN sqldados.carr AS carr
	      ON (carr.no = inv2.carrno)
  LEFT JOIN sqldados.inv  AS inv
	      ON (inv.vendno = inv2.vendno AND inv.nfname = inv2.nfname AND
		  inv.ordno = inv2.ordno AND inv.grossamt = inv2.grossamt)
  LEFT JOIN sqldados.emp
	      ON (emp.no = inv2.auxStr6 AND emp.no <> 0)
WHERE inv.invno IS NULL
  AND inv2.storeno > 0;

SELECT loja,
       CAST(IF(data = 0, NULL, data * 1) AS DATE)   AS data,
       IFNULL(hora, '')                             AS hora,
       CAST(empno as unsigned)                      as empno,
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
FROM sqldados.T_INV2
WHERE loja <> 0
  AND agendado = :agendado
  AND recebido = :recebido

