SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj1 AS C
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 1
UNION ALL
SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj2
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 2
UNION ALL
SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj03
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 3
UNION ALL
SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj04
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 4
UNION ALL
SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj05
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 5
UNION ALL
SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj06
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 6
UNION ALL
SELECT OID              AS id,
       XML_NFE          AS xmlNfe,
       CASE WHEN PATINDEX('%<nProt>%', XML_AUT) > 0 AND PATINDEX('%</nProt>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<nProt>%', XML_AUT) + len('<nProt>'),
                               PATINDEX('%</nProt>%', XML_AUT) - PATINDEX('%<nProt>%', XML_AUT) -
                               len('<nProt>'))
            ELSE '' END AS numeroProtocolo,
       CASE WHEN PATINDEX('%<dhRecbto>%', XML_AUT) > 0 AND PATINDEX('%</dhRecbto>%', XML_AUT) > 0
                THEN SUBSTRING(XML_AUT, PATINDEX('%<dhRecbto>%', XML_AUT) + len('<dhRecbto>'),
                               PATINDEX('%</dhRecbto>%', XML_AUT) -
                               PATINDEX('%<dhRecbto>%', XML_AUT) - len('<dhRecbto>'))
            ELSE '' END AS dataHoraRecebimento
FROM NDD_COLD.dbo.cold_lj07
WHERE IDE_NNF = :numero AND
      IDE_SERIE = :serie AND
      :storeno = 7