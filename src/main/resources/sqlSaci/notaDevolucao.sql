SELECT N.storeno                                 AS loja,
       N.pdvno                                   AS pdv,
       N.xano                                    AS transacao,
       N.eordno                                  AS pedido,
       cast(O.date AS DATE)                      AS dataPedido,
       cast(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       cast(N.issuedate AS DATE)                 AS dataNota,
       cast(CONCAT(C.no, ' ', C.name) AS CHAR)   AS fornecedor,
       V.no                                      AS vendno
FROM sqldados.nf           AS N
  LEFT JOIN sqldados.eord  AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.custp AS C
	      ON C.no = N.custno
  LEFT JOIN sqldados.vend  AS V
	      ON C.cpf_cgc = V.cgc
WHERE N.issuedate >= :dataInicial
  AND N.nfse = 66
  AND N.storeno IN (2, 3, 4, 5)
  AND C.name NOT LIKE '%ENGECOPI%'
  AND N.status <> 1
GROUP BY loja, pdv, transacao