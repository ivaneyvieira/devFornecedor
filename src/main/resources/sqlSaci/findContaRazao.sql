USE sqldados;

SET SQL_MODE = '';

DO @DATA_CORTE := SUBDATE(CURRENT_DATE, INTERVAL 6 MONTH) * 1;

DROP TEMPORARY TABLE IF EXISTS T_INV;
CREATE TEMPORARY TABLE T_INV
(
  PRIMARY KEY (invno)
)
SELECT account,
       invno,
       storeno,
       nfname,
       invse,
       issue_date,
       date,
       grossamt,
       vendno,
       remarks
FROM sqldados.inv AS I
WHERE date BETWEEN :dataInicial AND :dataFinal
  AND storeno IN (1, 2, 3, 4, 5, 6, 7, 8);

DROP TEMPORARY TABLE IF EXISTS T_ACC_NO;
CREATE TEMPORARY TABLE T_ACC_NO
(
  PRIMARY KEY (account)
)
SELECT DISTINCT account
FROM T_INV
WHERE account != '';

DROP TEMPORARY TABLE IF EXISTS T_ACC;
CREATE TEMPORARY TABLE T_ACC
(
  PRIMARY KEY (account)
)
SELECT no AS account, name AS descricao
FROM sqldados.acc AS A
       INNER JOIN T_ACC_NO AS AC
                  ON AC.account = A.no;


DROP TEMPORARY TABLE IF EXISTS T_PARCELA_ULT;
CREATE TEMPORARY TABLE T_PARCELA_ULT
(
  PRIMARY KEY (invno),
  chave VARCHAR(20)
)
SELECT invno, MAX(CONCAT(LPAD(instno, 10, ' '), LPAD(duedate, 10, ' '))) AS chave
FROM sqldados.invxa AS X
       INNER JOIN sqldados.inv AS I
                  USING (invno)
WHERE I.vendno = @VENDNO
GROUP BY invno;

DROP TEMPORARY TABLE IF EXISTS T_PARCELA_ULT2;
CREATE TEMPORARY TABLE T_PARCELA_ULT2
(
  PRIMARY KEY (invno, instno, duedate)
)
SELECT invno,
       TRIM(MID(chave, 1, 10)) * 1  AS instno,
       TRIM(MID(chave, 11, 10)) * 1 AS duedate
FROM T_PARCELA_ULT;

DROP TEMPORARY TABLE IF EXISTS T_FILES;
CREATE TEMPORARY TABLE T_FILES
(
  PRIMARY KEY (invno)
)
SELECT xano AS invno, COUNT(*) AS qt
FROM nfdevFile
WHERE storeno = 77
  AND pdvno = 7777
GROUP BY xano;

DROP TEMPORARY TABLE IF EXISTS T_NOTAS;
CREATE TEMPORARY TABLE T_NOTAS
(
  nf VARCHAR(20)
)
SELECT I.storeno                                          AS loja,
       invno                                              AS ni,
       IF(invse = '', nfname, CONCAT(nfname, '/', invse)) AS nf,
       CAST(issue_date AS DATE)                           AS emissao,
       CAST(date AS DATE)                                 AS entrada,
       grossamt / 100                                     AS valorNota,
       V.no                                               AS vendno,
       V.sname                                            AS fornecedor,
       I.remarks                                          AS obs,
       CAST(X.duedate AS DATE)                            AS vencimento,
       CASE X.status
         WHEN 0
           THEN 'Em Aberto'
         WHEN 1
           THEN 'Pgto.Total'
         WHEN 2
           THEN 'Pgto.Parcial'
         WHEN 3
           THEN 'Pgto.c/Desconto'
         WHEN 4
           THEN 'Pgto.+ Juros'
         WHEN 5
           THEN 'Cancelada'
         WHEN 6
           THEN 'Pgto.c/Devolucao'
         WHEN 7
           THEN 'Consignacao'
         ELSE ''
       END                                                AS situacao,
       X.remarks                                          AS obsParcela,
       IFNULL(F.qt, 0)                                    AS quantAnexo,
       IFNULL(A.account, '0')                             AS numeroConta,
       IFNULL(A.descricao, 'SEM CONTA')                   AS descricaoConta
FROM T_INV AS I
       LEFT JOIN T_ACC AS A
                 USING (account)
       LEFT JOIN T_FILES AS F
                 USING (invno)
       LEFT JOIN T_PARCELA_ULT2 AS U
                 USING (invno)
       LEFT JOIN sqldados.invxa AS X
                 USING (invno, instno, duedate)
       LEFT JOIN sqldados.vend AS V
                 ON V.no = I.vendno
WHERE issue_date >= 20240301
ORDER BY invno DESC;

SELECT numeroConta,
       descricaoConta,
       loja,
       ni,
       nf,
       emissao,
       entrada,
       valorNota,
       vendno,
       fornecedor,
       obs,
       vencimento,
       situacao,
       obsParcela,
       quantAnexo
FROM T_NOTAS TN
WHERE (:query = '' OR
       numeroConta LIKE CONCAT(:query, '%') OR
       descricaoConta LIKE CONCAT(:query, '%'))
