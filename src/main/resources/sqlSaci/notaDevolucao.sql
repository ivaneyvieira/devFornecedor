SELECT N.storeno                                                           AS loja,
       N.pdvno                                                             AS pdv,
       N.xano                                                              AS transacao,
       N.eordno                                                            AS pedido,
       cast(O.date AS DATE)                                                AS dataPedido,
       cast(CONCAT(N.nfno, '/', N.nfse) AS CHAR)                           AS nota,
       cast(N.issuedate AS DATE)                                           AS dataNota,
       cast(CONCAT(C.no, ' ', C.name) AS CHAR)                             AS fornecedor,
       GROUP_CONCAT(cast(CONCAT(R.no, ' ', R.name) AS CHAR) ORDER BY R.no) AS representante,
       GROUP_CONCAT(cast(CONCAT(TRIM(MID(R.ddd, 1, 5)), ' ', TRIM(MID(R.phone, 1, 10))) AS CHAR)
		    ORDER BY R.no)                                         AS telefone,
       GROUP_CONCAT(R.email ORDER BY R.no)                                 AS email
FROM sqldados.nf            AS N
  LEFT JOIN sqldados.eord   AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.custp  AS C
	      ON C.no = N.custno
  LEFT JOIN sqldados.vend   AS V
	      ON C.cpf_cgc = V.cgc
  LEFT JOIN sqldados.repven AS RV
	      ON RV.vendno = V.no
  LEFT JOIN sqldados.rep    AS R
	      ON R.no = RV.repno
WHERE N.issuedate >= :dataInicial
  AND N.nfse = 66
  AND N.storeno IN (2, 3, 4, 5)
  AND C.name NOT LIKE '%ENGECOPI%'
GROUP BY loja, pdv, transacao