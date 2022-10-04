SELECT N.invno                  AS invno,
       IFNULL(N.storeno, 0)     AS storeno,
       V.name                   AS nomefornecedor,
       IFNULL(V.no, 0)          AS codigoFornecedor,
       IFNULL(V.auxLong4, 0)    AS fornecedorSap,
       IFNULL(V.email, '')      AS email,
       IFNULL(V.remarks, '')    AS obs,
       N.nfname                 AS numero,
       N.invse                  AS serie,
       DATE(N.issue_date)       AS dataEmissao,
       N.ordno                  AS ordno,
       IFNULL(CHAVE.nfekey, '') AS chave
FROM sqldados.inv           AS N
  LEFT JOIN sqldados.vend   AS V
	      ON V.no = N.vendno
  LEFT JOIN sqldados.invnfe AS CHAVE
	      ON N.invno = CHAVE.invno
WHERE N.issue_date BETWEEN :dataInicial AND :dataFinal
  AND (CHAVE.nfekey = :chave OR :chave = '')
  AND (V.name LIKE CONCAT(:filtro, '%') OR IFNULL(V.no, 0) = (:filtro * 1) OR :filtro = '')
  AND N.invse != ''
  AND N.type = 0

