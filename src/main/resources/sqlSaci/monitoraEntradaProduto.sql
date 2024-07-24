USE sqldados;

DROP TEMPORARY TABLE IF EXISTS T_PRD_PENDENTE;
CREATE TEMPORARY TABLE T_PRD_PENDENTE
(
  PRIMARY KEY (prdno, grade)
)
SELECT prdno, grade
FROM sqldados.eord AS E
       INNER JOIN sqldados.eoprd AS I
                  USING (storeno, ordno)
WHERE E.paymno = 315
  AND custno NOT IN (478, 21295, 21333, 102773, 108751, 120420, 709327, 926520, 901705, 306263, 312585)
GROUP BY prdno, grade;

DROP TEMPORARY TABLE IF EXISTS T_QUERY;
CREATE TEMPORARY TABLE T_QUERY
SELECT N.invno                                         AS invno,
       N.storeno                                       AS storeno,
       I.prdno                                         AS prdno,
       TRIM(I.prdno)                                   AS codigo,
       I.grade                                         AS grade,
       N.date                                          AS date,
       CONCAT(N.nfname, '/', N.invse)                  AS nota,
       N.ordno                                         AS pedido,
       N.grossamt / 100                                AS valorNota,
       N.vendno                                        AS vendno,
       V.sname                                         AS fornecedor,
       IF(V.vendcust = 0, IFNULL(C.no, 0), V.vendcust) AS custno,
       TRIM(MID(P.name, 1, 37))                        AS descricao,
       N.remarks                                       AS remarks,
       I.fob / 100                                     AS valorUnitInv,
       SUM(I.qtty / 1000)                              AS quantInv
FROM sqldados.iprd AS I
       INNER JOIN T_PRD_PENDENTE AS T
                  USING (prdno, grade)
       INNER JOIN sqldados.inv AS N
                  USING (invno)
       INNER JOIN sqldados.vend AS V
                  ON V.no = N.vendno
       LEFT JOIN sqldados.custp AS C
                 ON C.cpf_cgc = V.cgc
       INNER JOIN sqldados.prd AS P
                  ON P.no = I.prdno
WHERE (N.date >= :dataInicial OR :dataInicial = 0)
  AND (N.date <= :dataFinal OR :dataFinal = 0)
  AND (N.storeno = :loja OR :loja = 0)
  AND V.name NOT LIKE 'ENGECOPI%'
  AND N.type = 0
GROUP BY I.invno, I.prdno, I.grade;

DO @pesquisa := :pesquisa;
DO @pesquisaNum := IF(@pesquisa REGEXP '^[0-9]+$', @pesquisa * 1, 0);
DO @pesquisaLike := CONCAT(@pesquisa, '%');

SELECT invno                   AS ni,
       storeno                 AS loja,
       prdno                   AS prdno,
       codigo                  AS codigo,
       grade                   AS grade,
       CAST(date AS DATE)      AS dataEntrada,
       nota                    AS nota,
       pedido                  AS pedido,
       valorNota               AS valorNota,
       vendno                  AS vendno,
       custno                  AS custno,
       fornecedor              AS fornecedor,
       descricao               AS descricao,
       remarks                 AS observacao,
       valorUnitInv            AS valorUnit,
       quantInv                AS quant,
       valorUnitInv * quantInv AS valorTotal
FROM T_QUERY
WHERE (@pesquisa = '' OR
       invno = @pesquisaNum OR
       codigo = @pesquisa OR
       grade = @pesquisa OR
       vendno = @pesquisaNum OR
       fornecedor LIKE @pesquisaLike OR
       descricao LIKE @pesquisaLike OR
       remarks LIKE @pesquisaLike)
