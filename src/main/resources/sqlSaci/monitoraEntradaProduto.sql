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
SELECT invno,
       N.storeno,
       prdno,
       TRIM(prdno)              AS codigo,
       grade,
       N.date,
       N.vendno,
       V.sname                  AS fornecedor,
       TRIM(MID(P.name, 1, 37)) AS descricao,
       N.remarks
FROM sqldados.iprd AS I
       INNER JOIN T_PRD_PENDENTE AS T
                  USING (prdno, grade)
       INNER JOIN sqldados.inv AS N
                  USING (invno)
       INNER JOIN sqldados.vend AS V
                  ON V.no = N.vendno
       INNER JOIN sqldados.prd AS P
                  ON P.no = I.prdno
WHERE (N.date >= :dataInicial OR :dataInicial = 0)
  AND (N.date <= :dataFinal OR :dataFinal = 0)
  AND (N.storeno = :loja OR :loja = 0)
  AND V.name NOT LIKE 'ENGECOPI%';

DO @pesquisa := :pesquisa;
DO @pesquisaNum := IF(@pesquisa REGEXP '^[0-9]+$', @pesquisa * 1, 0);
DO @pesquisaLike := CONCAT(@pesquisa, '%');

SELECT invno              AS ni,
       storeno            AS loja,
       prdno              AS prdno,
       codigo             AS codigo,
       grade              AS grade,
       CAST(date AS DATE) AS dataEntrada,
       vendno             AS vendno,
       fornecedor         AS fornecedor,
       descricao          AS descricao,
       remarks            AS observacao
FROM T_QUERY
WHERE (@pesquisa = '' OR
       invno = @pesquisaNum OR
       codigo = @pesquisa OR
       grade = @pesquisa OR
       vendno = @pesquisaNum OR
       fornecedor LIKE @pesquisaLike OR
       descricao LIKE @pesquisaLike OR
       remarks LIKE @pesquisaLike)
