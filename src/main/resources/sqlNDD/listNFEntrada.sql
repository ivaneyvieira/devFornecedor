SELECT N.OID                                                          AS id,
       IDE_NNF                                                        AS numero,
       IDE_SERIE                                                      as serie,
       CASE WHEN EXISTS(SELECT E.OID
                        FROM NDD_COLD.dbo.entrada_nfe_EVT AS E
                        WHERE E.OID = N.OID AND
                              TPEVENTO = 1 AND
                              DHREGEVENTO >= :dataInicial)
                THEN 'S'
            ELSE 'N' END                                              AS cancelado,
       IDE_DEMI                                                       AS dataEmissao,
       EMIT_CNPJ                                                      AS cnpjEmitente,
       CASE WHEN PATINDEX('%<xNome>%', XML_NFE) > 0 AND PATINDEX('%</xNome>%', XML_NFE) > 0
                THEN SUBSTRING(XML_NFE, PATINDEX('%<xNome>%', XML_NFE) + LEN('<xNome>'),
                               PATINDEX('%</xNome>%', XML_NFE) - PATINDEX('%<xNome>%', XML_NFE) -
                               LEN('<xNome>'))
            ELSE '' END                                               AS nomeFornecedor,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END                                               AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END                                               AS dataHoraRecebimento,
       DEST_CNPJ                                                      AS cnpjDestinatario,
       EMIT_IE                                                        AS ieEmitente,
       DEST_IE                                                        AS ieDestinatario,
       TOTAL_ICMSTOT_VBC                                              AS baseCalculoIcms,
       TOTAL_ICMSTOT_VBCST                                            AS baseCalculoSt,
       TOTAL_ICMSTOT_VPROD                                            AS valorTotalProdutos,
       TOTAL_ICMSTOT_VICMS                                            AS valorTotalIcms,
       TOTAL_ICMSTOT_VST                                              AS valorTotalSt,
       TOTAL_ISSQNTOT_VBC                                             AS baseCalculoIssqn,
       IDE_ID                                                         AS chave,
       STATUSNFE                                                      AS status,
       CASE WHEN XML_AUT IS NULL THEN '' ELSE XML_AUT END             AS xmlAut,
       CASE WHEN XML_CANC IS NULL THEN '' ELSE XML_CANC END           AS xmlCancelado,
       CASE WHEN XML_DADOSADIC IS NULL THEN '' ELSE XML_DADOSADIC END AS xmlDadosAdicionais,
       XML_NFE                                                        AS xmlFile
FROM NDD_COLD.dbo.entrada_nfe AS N
WHERE (EMIT_CNPJ NOT LIKE '07.483.654%') AND
      (DEST_CNPJ LIKE '07.483.654%') AND
      (IDE_DEMI BETWEEN :dataInicial AND :dataFinal) AND
      (IDE_NNF = :numero OR :numero = 0) AND
      (EMIT_CNPJ = :cnpj OR :cnpj = '') AND
      (DEST_CNPJ = :cnpjLoja OR :cnpjLoja = '') AND
      (CASE WHEN PATINDEX('%<xNome>%', XML_NFE) > 0 AND PATINDEX('%</xNome>%', XML_NFE) > 0
                THEN SUBSTRING(XML_NFE, PATINDEX('%<xNome>%', XML_NFE) + LEN('<xNome>'),
                               PATINDEX('%</xNome>%', XML_NFE) - PATINDEX('%<xNome>%', XML_NFE) -
                               LEN('<xNome>'))
            ELSE '' END LIKE (:fornecedor + '%') OR :fornecedor = '')
