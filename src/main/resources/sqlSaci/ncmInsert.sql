UPDATE sqldados.spedprd
SET genero = MID(:ncm, 1, 2),
    ncm    = :ncm
WHERE prdno = LPAD(:codigo, 16, ' ')