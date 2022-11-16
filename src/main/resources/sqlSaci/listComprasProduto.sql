USE sqldados;

DO @LOJA := :loja;
DO @VENDNO := IF(:pesquisa REGEXP '^[0-9]+$]', :pesquisa * 1, 0);
DO @FORNECEDOR := :pesquisa;

DROP TABLE IF EXISTS T_BARCODE;
CREATE TEMPORARY TABLE T_BARCODE (
  PRIMARY KEY (prdno, grade)
)
SELECT prdno,
       grade,
       TRIM(barcode) AS barcode
FROM sqldados.prdbar
GROUP BY prdno, grade;

SELECT V.no                                                                         AS vendno,
       V.name                                                                       AS fornecedor,
       V.cgc                                                                        AS cnpj,
       O.storeno                                                                    AS loja,
       S.sname                                                                      AS sigla,
       O.no                                                                         AS numeroPedido,
       O.status                                                                     AS status,/*0 - Aberto,  1 - Entregue, 2 - Cancelado*/
       CAST(O.date AS DATE)                                                         AS dataPedido,
       CAST(ROUND(IF(O.dataEntrega = 0, DATE_ADD(O.date, INTERVAL O.deliv DAY) * 1,
		     O.dataEntrega)) AS DATE)                                       AS dataEntrega,
       O.remarks                                                                    AS obsercacaoPedido,
       TRIM(E.prdno)                                                                AS codigo,
       TRIM(MID(P.name, 1, 37))                                                     AS descricao,
       P.mfno_ref                                                                   AS refFab,
       E.grade                                                                      AS grade,
       MID(P.name, 38, 3)                                                           AS unidade,
       ROUND((E.qtty * E.mult) / 1000)                                              AS qtPedida,
       ROUND((E.qttyCancel * E.mult) / 1000)                                        AS qtCancelada,
       ROUND((E.qttyRcv * E.mult) / 1000)                                           AS qtRecebida,
       ROUND((E.qtty * E.mult - E.qttyCancel * E.mult - E.qttyRcv * E.mult) / 1000) AS qtPendente,
       E.cost                                                                       AS custoUnit,
       TRIM(IFNULL(B.barcode, P.barcode))                                           AS barcode
FROM sqldados.ords          AS O
  INNER JOIN sqldados.store AS S
	       ON S.no = O.storeno
  INNER JOIN sqldados.vend  AS V
	       ON O.vendno = V.no
  INNER JOIN sqldados.oprd  AS E
	       ON O.storeno = E.storeno AND O.no = E.ordno
  LEFT JOIN  T_BARCODE      AS B
	       ON E.prdno = B.prdno AND E.grade = B.grade
  INNER JOIN sqldados.prd   AS P
	       ON P.no = E.prdno
WHERE (O.storeno = :loja OR :loja = 0)
  AND (V.no = @VENDNO OR @VENDNO = 0)
  AND (V.sname LIKE CONCAT(@FORNECEDOR, '%'))
  AND O.status != 2
  AND O.date >= SUBDATE(CURRENT_DATE, INTERVAL 6 MONTH)
