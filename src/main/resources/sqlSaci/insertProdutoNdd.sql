SET SQL_MODE = '';
INSERT IGNORE INTO sqldados.produtosNdd(id, nItem, cProd, cEAN, xProd, ncm, cfop, uCom, qCom, vUnCom, vProd, indTot,
                                        cstIcms, baseIcms, percIcms, valorIcms, baseIpi, percIpi, valorIpi, basePis,
                                        percPis, valorPis, baseCofins, percCofins, valorCofins)
  VALUE (:id, :nItem, :cProd, :cEAN, :xProd, :ncm, :cfop, :uCom, :qCom, :vUnCom, :vProd, :indTot, :cstIcms, :baseIcms,
         :percIcms, :valorIcms, :baseIpi, :percIpi, :valorIpi, :basePis, :percPis, :valorPis, :baseCofins, :percCofins,
         :valorCofins)