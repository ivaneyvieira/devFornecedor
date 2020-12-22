INSERT IGNORE INTO sqldados.nfdevEmail(storeno, pdvno, xano, idEmail)
VALUES (:storeno, :pdvno, :xano, :idEmail);

INSERT IGNORE INTO sqldados.devEmail(idEmail, email, assunto, msg, planilha, relatorio, anexos,
				     data, hora)
VALUES (:idEmail, :email, :assunto, :msg, :planilha, :relatorio, :anexos, current_date, current_time)