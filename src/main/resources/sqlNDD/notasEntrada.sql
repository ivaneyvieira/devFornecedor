SELECT OID                                                     AS id,
       IDE_NNF                                                 AS numero,
       IDE_SERIE                                               AS serie,
       IDE_DEMI                                                AS dataEmissao,
       EMIT_CNPJ                                               AS cnpjEmitente,
       CASE WHEN PATINDEX('%<xNome>%', XML_NFE) > 0 AND PATINDEX('%</xNome>%', XML_NFE) > 0
                THEN SUBSTRING(XML_NFE, PATINDEX('%<xNome>%', XML_NFE) + len('<xNome>'),
                               PATINDEX('%</xNome>%', XML_NFE) - PATINDEX('%<xNome>%', XML_NFE) -
                               len('<xNome>'))
            ELSE '' END                                        AS nomeFornecedor,
       DEST_CNPJ                                               AS cnpjDestinatario,
       EMIT_IE                                                 AS ieEmitente,
       DEST_IE                                                 AS ieDestinatario,
       TOTAL_ICMSTOT_VBC                                       AS baseCalculoIcms,
       TOTAL_ICMSTOT_VBCST                                     AS baseCalculoSt,
       TOTAL_ICMSTOT_VPROD                                     AS valorTotalProdutos,
       TOTAL_ICMSTOT_VICMS                                     AS valorTotalIcms,
       TOTAL_ICMSTOT_VST                                       AS valorTotalSt,
       TOTAL_ISSQNTOT_VBC                                      AS baseCalculoIssqn,
       IDE_ID                                                  AS chave,
       STATUSNFE                                               AS status,
       CASE WHEN XML_AUT IS NULL THEN 'NULL' ELSE '' END       AS xmlAut,
       CASE WHEN XML_CANC IS NULL THEN 'NULL' ELSE '' END      AS xmlCancelado,
       CASE WHEN XML_NFE IS NULL THEN 'NULL' ELSE '' END       AS xmlNfe,
       CASE WHEN XML_DADOSADIC IS NULL THEN 'NULL' ELSE '' END AS xmlDadosAdicionais
FROM NDD_COLD.dbo.entrada_nfe
WHERE EMIT_CNPJ NOT LIKE '07.483.654%' AND
      DEST_CNPJ LIKE '07.483.654%' AND
      IDE_DEMI >= :dataInicial





