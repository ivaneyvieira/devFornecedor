select prdno, grade, prdrefname, prdrefno
from sqldados.prdrefpq
where prdno = LPAD(:codigo, 16, ' ')
  AND grade = :grade




