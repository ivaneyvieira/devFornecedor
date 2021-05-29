INSERT INTO sqldados.vendSap(codigo, nome) VALUE (:codigo, :nome)
ON DUPLICATE KEY UPDATE nome = :nome