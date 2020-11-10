DROP TEMPORARY TABLE IF EXISTS TDUP;
CREATE TEMPORARY TABLE TDUP (
  PRIMARY KEY (storeno, pdvno, xano)
)
SELECT storeno,
       pdvno,
       xano,
       dupno
FROM sqldados.dup
WHERE xano > 0
  AND issuedate > 20190101
GROUP BY storeno, pdvno, xano;

SELECT N.storeno                                 AS loja,
       N.pdvno                                   AS pdv,
       N.xano                                    AS transacao,
       N.eordno                                  AS pedido,
       cast(O.date AS DATE)                      AS dataPedido,
       cast(CONCAT(N.nfno, '/', N.nfse) AS CHAR) AS nota,
       IFNULL(CAST(D.dupno AS CHAR), '')         AS fatura,
       cast(N.issuedate AS DATE)                 AS dataNota,
       cast(CONCAT(C.no, ' ', C.name) AS CHAR)   AS fornecedor,
       V.no                                      AS vendno,
       IFNULL(R.rmk, '')                         AS rmk
FROM sqldados.nf              AS N
  LEFT JOIN sqldados.nfdevRmk AS R
	      USING (storeno, pdvno, xano)
  LEFT JOIN TDUP              AS D
	      USING (storeno, pdvno, xano)
  LEFT JOIN sqldados.eord     AS O
	      ON O.storeno = N.storeno AND O.ordno = N.eordno
  LEFT JOIN sqldados.custp    AS C
	      ON C.no = N.custno
  LEFT JOIN sqldados.vend     AS V
	      ON C.cpf_cgc = V.cgc
WHERE (N.issuedate BETWEEN :dataInicial AND :dataFinal OR :fornecedor <> '' OR :nota <> '' OR
       :dataInicial = 0 OR :dataFinal = 0)
  AND (C.no = :fornecedor * 1 OR V.no = :fornecedor * 1 OR C.name LIKE CONCAT(:fornecedor, '%') OR
       :fornecedor = '')
  AND (N.nfno = :nota OR :nota = '')
  AND (:fornecedor <> '' OR :nota <> '' OR (:dataInicial <> 0 AND :dataFinal <> 0))
  AND N.nfse = 1
  AND N.storeno IN (2, 3, 4, 5)
  AND C.name NOT LIKE '%ENGECOPI%'
  AND N.status <> 1
  AND N.tipo = 2
GROUP BY loja, pdv, transacao
LIMIT 1000