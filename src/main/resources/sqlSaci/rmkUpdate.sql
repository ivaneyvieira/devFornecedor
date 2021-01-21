INSERT INTO sqldados.nfdevRmk(storeno, pdvno, xano, rmk)
VALUES (:storeno, :pdvno, :xano, :rmk)
ON DUPLICATE KEY UPDATE rmk = :rmk