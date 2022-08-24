DELETE FROM sqldados.reimpressaoNota
WHERE loja = :loja
  AND nota = :numero
  AND usuario = :login