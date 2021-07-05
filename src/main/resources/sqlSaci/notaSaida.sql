SELECT N.storeno                                 AS loja,
       N.nfno                                    AS numero,
       N.nfse * 1                                AS serie,
       CAST(CONCAT(N.nfno, '/', N.nfse) AS char) AS nota,
       N.custno                                  AS codigoCliente,
       C.name                                    AS nomeCliente,
       N.eordno                                  AS pedido,
       CAST(N.issuedate AS date)                 AS data,
       N.grossamt / 100                          AS valor,
       IFNULL(K.nfekey, '')                      AS chave
FROM sqldados.nf            AS N
  LEFT JOIN  sqldados.nfes  AS K
	       USING (storeno, pdvno, xano)
  INNER JOIN sqldados.custp AS C
	       ON C.no = N.custno
WHERE N.status <> 1
  AND N.tipo = 0
  AND (N.storeno = :loja)
  AND (N.nfno = :numero OR :numero = 0)
  AND (N.nfse = :serie OR :serie = '')
  AND (N.issuedate BETWEEN :dataI AND :dataF)
  AND (N.custno = :codigoCliente OR :codigoCliente = 0)
  AND (C.name LIKE CONCAT('%', :nomeCliente, '%') OR :nomeCliente = '')

