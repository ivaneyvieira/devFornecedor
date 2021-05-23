INSERT IGNORE INTO sqldados.nfdevEmail(storeno, pdvno, xano, idEmail)
VALUES (:storeno, :pdvno, :xano, :idEmail);

INSERT IGNORE INTO sqldados.devEmail(idEmail, email, assunto, msg, planilha, relatorio, anexos,
				     data, hora, messageID)
VALUES (:idEmail, :email, :assunto, :msg, :planilha, :relatorio, :anexos, CURRENT_DATE,
	CURRENT_TIME, :messageID)