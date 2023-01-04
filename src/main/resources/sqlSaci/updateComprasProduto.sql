UPDATE sqldados.oprd
SET padbyte = :confirmado,
    auxStr = :calcEmbalagem
WHERE storeno = :loja
  AND ordno = :numeroPedido
  AND prdno = LPAD(:codigo, 16, ' ')
  AND grade = :grade
  AND seqno = :seqno;

UPDATE sqldados.pedidosCompra
SET confirmado = :confirmado
WHERE loja = :loja
  AND numeroPedido = :numeroPedido
  AND codigo = :codigo
  AND grade = :grade
  AND seqno = :seqno