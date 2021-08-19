UPDATE sqldados.inv2
SET c1      = :data,
    c2      = :hora,
    auxStr6 = :recebedor,
    c3      = :dataRecbedor,
    c4      = :horaRecebedor,
    c5      = :conhecimento
WHERE invno = :invno
