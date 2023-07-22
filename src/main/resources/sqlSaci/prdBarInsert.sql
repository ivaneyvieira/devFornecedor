USE sqldados;

DROP
  TEMPORARY
  TABLE IF EXISTS T_BAR;
CREATE TEMPORARY TABLE T_BAR
SELECT prdno, grade, barcode
FROM sqldados.prdbar
WHERE prdno = LPAD(:codigo, 16, ' ')
  AND grade = :grade
  AND barcode = LPAD(:barcode, 16, ' ');

INSERT INTO sqldados.prdbar(auxLong1, auxLong2, l1, l2, l3, l4, l5, l6, l7, l8, m1, m2, m3, m4, m5, m6, m7, m8, bits,
                            bits2, s1, s2, s3, s4, s5, s6, s7, s8, prdno, grade, barcode, barcode48, c1, c2, c3, c4)
SELECT 0                       AS auxLong1,
       0                       AS auxLong2,
       0                       AS l1,
       0                       AS l2,
       0                       AS l3,
       0                       AS l4,
       0                       AS l5,
       0                       AS l6,
       0                       AS l7,
       0                       AS l8,
       0                       AS m1,
       0                       AS m2,
       0                       AS m3,
       0                       AS m4,
       0                       AS m5,
       0                       AS m6,
       0                       AS m7,
       0                       AS m8,
       0                       AS bits,
       0                       AS bits2,
       0                       AS s1,
       0                       AS s2,
       0                       AS s3,
       0                       AS s4,
       0                       AS s5,
       0                       AS s6,
       0                       AS s7,
       0                       AS s8,
       LPAD(:codigo, 16, ' ')  AS prdno,
       :grade                  AS grade,
       LPAD(:barcode, 16, ' ') AS barcode,
       ''                      AS barcode48,
       ''                      AS c1,
       ''                      AS c2,
       ''                      AS c3,
       ''                      AS c4
FROM dual
WHERE NOT EXISTS(SELECT * FROM T_BAR)
