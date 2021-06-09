select OID                         as id,
       IDE_NNF                     as numero,
       IDE_SERIE                   as serie,
       IDE_DEMI                    as dataEmissao,
       EMIT_CNPJ                   as cnpjEmitente,
       DEST_CNPJ                   as cnpjDestinatario,
       EMIT_IE                     as ieEmitente,
       DEST_IE                     as ieDestinatario,
       TOTAL_ICMSTOT_VBC           as baseCalculoIcms,
       TOTAL_ICMSTOT_VBCST         as baseCalculoSt,
       TOTAL_ICMSTOT_VPROD         as valorTotalProdutos,
       TOTAL_ICMSTOT_VICMS         as valorTotalIcms,
       TOTAL_ICMSTOT_VST           as valorTotalSt,
       TOTAL_ISSQNTOT_VBC          as baseCalculoIssqn,
       IDE_ID                      as chave,
       STATUSNFE                   as status,
       coalesce(XML_AUT, '')       as xmlAut,
       coalesce(XML_CANC, '')      as xmlCancelado,
       coalesce(XML_NFE, '')       as xmlNfe,
       coalesce(XML_DADOSADIC, '') as xmlDadosAdicionais
from NDD_COLD.dbo.entrada_nfe
where EMIT_CNPJ NOT LIKE '07.483.654%'
  AND DEST_CNPJ LIKE '07.483.654%'
  AND IDE_DEMI >= '20210601'




