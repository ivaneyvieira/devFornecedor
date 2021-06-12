SELECT OID                 AS id,
       IDE_NNF             AS numero,
       IDE_SERIE           AS serie,
       IDE_DEMI            AS dataEmissao,
       EMIT_CNPJ           AS cnpjEmitente,
       DEST_CNPJ           AS cnpjDestinatario,
       EMIT_IE             AS ieEmitente,
       DEST_IE             AS ieDestinatario,
       TOTAL_ICMSTOT_VBC   AS baseCalculoIcms,
       TOTAL_ICMSTOT_VBCST AS baseCalculoSt,
       TOTAL_ICMSTOT_VPROD AS valorTotalProdutos,
       TOTAL_ICMSTOT_VICMS AS valorTotalIcms,
       TOTAL_ICMSTOT_VST   AS valorTotalSt,
       TOTAL_ISSQNTOT_VBC  AS baseCalculoIssqn,
       IDE_ID              AS chave,
       STATUSNFE           AS status,
       ''                  AS xmlAut,
       ''                  AS xmlCancelado,
       ''                  AS xmlNfe,
       ''                  AS xmlDadosAdicionais
FROM NDD_COLD.dbo.entrada_nfe
WHERE EMIT_CNPJ NOT LIKE '07.483.654%'
  AND DEST_CNPJ LIKE '07.483.654%'
  AND IDE_DEMI >= '20210301'





