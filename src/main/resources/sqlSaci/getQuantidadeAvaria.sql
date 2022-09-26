SELECT id,
       numeroProtocolo,
       codigo,
       codBarra,
       quantidade
FROM sqldados.quantAvaria qA
WHERE id = :id
  AND numeroProtocolo = :numeroProtocolo
  AND codigo = :codigo
  AND codBarra = :codBarra