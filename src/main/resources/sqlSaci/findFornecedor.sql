SELECT vendcust AS custno,
       name     AS fornecedor,
       no       AS vendno,
       auxLong4 AS fornecedorSap,
       email    AS email,
       remarks  AS obs
FROM sqldados.vend AS V
WHERE V.cgc = :cnpj