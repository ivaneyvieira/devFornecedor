DO @FILTRO := TRIM(:filtro);
DO @FILTRO_DATA := IF(@FILTRO REGEXP '^[0-9]{2}/[0-9]{2}/[0-9]{4}$', @FILTRO, '');
DO @FILTRO_NUM := IF(@FILTRO REGEXP '^[0-9]+$', @FILTRO * 1, 0);
DO @FILTRO_STR := CONCAT(@FILTRO, '%');


SELECT CAST(data AS date)     AS data,
       hora,
       loja,
       nota,
       tipo,
       usuario,
       CAST(dataNota AS date) AS dataNota,
       codcli,
       nomecli,
       valor
FROM sqldados.reimpressaoNota
WHERE (loja = :loja OR :loja = 0)
  AND ((DATE_FORMAT(data, '%d/%m/%Y') = @FILTRO_DATA AND @FILTRO_DATA != '') OR
       (DATE_FORMAT(dataNota, '%d/%m/%Y') = @FILTRO_DATA AND @FILTRO_DATA != '') OR
       (nota LIKE @FILTRO_STR AND @FILTRO != '') OR (tipo LIKE @FILTRO_STR AND @FILTRO != '') OR
       (usuario LIKE @FILTRO_STR AND @FILTRO != '') OR
       (nomecli LIKE @FILTRO_STR AND @FILTRO != '') OR
       (codcli = @FILTRO_NUM AND @FILTRO_NUM != 0) OR (@FILTRO = ''))
ORDER BY data DESC, hora DESC