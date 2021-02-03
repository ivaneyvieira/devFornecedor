DROP TEMPORARY TABLE IF EXISTS TVEND;
CREATE TEMPORARY TABLE TVEND (
  PRIMARY KEY (vendno)
)
select V.no   as vendno,
       C.no   AS custno,
       V.name AS fornecedorNome,
       V.email
from sqldados.vend         AS V
  LEFT JOIN sqldados.custp AS C
	      ON C.cpf_cgc = V.cgc
WHERE V.name NOT LIKE 'ENGECOPI%'
GROUP BY V.no;

SELECT N.storeno                                    AS loja,
       S.sname                                      AS sigla,
       9999                                         AS pdv,
       N.invno                                      AS transacao,
       N.ordno                                      AS pedido,
       cast(NULL AS DATE)                           AS dataPedido,
       cast(CONCAT(N.nfname, '/', N.invse) AS CHAR) AS nota,
       ''                                           AS fatura,
       cast(N.issue_date AS DATE)                   AS dataNota,
       V.custno                                     AS custno,
       V.fornecedorNome                             AS fornecedor,
       V.email                                      as email,
       N.vendno                                     AS vendno,
       IFNULL(R.rmk, '')                            AS rmk,
       SUM(N.grossamt / 100)                        AS valor,
       IFNULL(N.remarks, '')                        AS obsNota,
       'N'                                          AS serie01Rejeitada,
       'N'                                          AS serie01Pago,
       'N'                                          AS serie66Pago,
       remarks                                      AS remarks,
       0                                            AS baseIcms,
       0                                            AS valorIcms,
       0                                            AS baseIcmsSubst,
       0                                            AS icmsSubst,
       0                                            AS valorFrete,
       0                                            AS valorSeguro,
       0                                            AS outrasDespesas,
       0                                            AS valorIpi,
       0                                            AS valorTotal,
       ''                                           AS obsPedido,
       'ENT'                                        AS tipo,
       IFNULL(RV.rmk, '')                           AS rmkVend
FROM sqldados.inv               AS N
  INNER JOIN sqldados.store     AS S
	       ON S.no = N.storeno
  LEFT JOIN  sqldados.nfdevRmk  AS R
	       ON N.storeno = R.storeno AND R.pdvno = 9999 AND N.invno = R.xano
  LEFT JOIN  sqldados.nfvendRmk AS RV
	       ON RV.vendno = N.vendno AND RV.tipo = N.invse
  INNER JOIN TVEND              AS V
	       ON N.vendno = V.vendno
  INNER JOIN sqldados.eord      AS O
	       ON O.storeno = N.storeno AND O.ordno = N.ordno AND O.paymno = 316
WHERE N.invse = '66'
  AND (N.bits & POW(2, 4) = 0)
GROUP BY loja, pdv, transacao, dataNota, custno