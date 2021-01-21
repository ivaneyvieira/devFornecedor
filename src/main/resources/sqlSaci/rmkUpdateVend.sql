INSERT INTO sqldados.nfvendRmk(vendno, tipo, rmk)
VALUES (:vendno, :tipo, :rmk)
ON DUPLICATE KEY UPDATE rmk = :rmk