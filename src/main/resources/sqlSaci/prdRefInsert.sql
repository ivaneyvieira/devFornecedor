USE sqldados;

DROP
  TEMPORARY
  TABLE IF EXISTS T_REF;
CREATE TEMPORARY TABLE T_REF
SELECT prdno, grade, prdrefname, prdrefno
FROM sqldados.prdrefpq
WHERE prdno = LPAD(:codigo, 16, ' ')
  AND grade = :grade
  AND prdrefno = :prdrefno;

REPLACE INTO sqldados.prdrefpq(l1, l2, l3, l4, l5, l6, l7, l8, m1, m2, m3, m4, m5, m6, m7, m8, bits, padbyte, s1, s2,
                               s3,
                               s4, s5, s6, s7, s8, prdno, grade, prdrefno, prdrefname, c1, c2)
SELECT 0                      AS l1,
       0                      AS l2,
       0                      AS l3,
       0                      AS l4,
       0                      AS l5,
       0                      AS l6,
       0                      AS l7,
       0                      AS l8,
       0                      AS m1,
       0                      AS m2,
       0                      AS m3,
       0                      AS m4,
       0                      AS m5,
       0                      AS m6,
       0                      AS m7,
       0                      AS m8,
       0                      AS bits,
       0                      AS padbyte,
       0                      AS s1,
       0                      AS s2,
       0                      AS s3,
       0                      AS s4,
       0                      AS s5,
       0                      AS s6,
       0                      AS s7,
       0                      AS s8,
       LPAD(:codigo, 16, ' ') AS prdno,
       :grade                 AS grade,
       :prdrefno              AS prdrefno,
       :prdrefno              AS prdrefname,
       ''                     AS c1,
       ''                     AS c2
FROM dual
WHERE NOT EXISTS(SELECT * FROM T_REF)
  AND :grade <> '';

REPLACE INTO sqldados.prdref(vendno, auxLong1, auxLong2, auxMoney1, auxMoney2, l1, l2, l3, l4, m1, m2, m3, m4, bits,
                             bits2, auxShort1, auxShort2, auxShort3, auxShort4, s1, s2, s3, s4, prdno, grade, prdrefno,
                             prdrefname, auxChar1, auxChar2, c1, c2, c3, c4)
SELECT mfno                   AS vendno,
       0                      AS auxLong1,
       0                      AS auxLong2,
       0                      AS auxMoney1,
       0                      AS auxMoney2,
       CURDATE() * 1          AS l1,
       0                      AS l2,
       0                      AS l3,
       0                      AS l4,
       0                      AS m1,
       0                      AS m2,
       0                      AS m3,
       0                      AS m4,
       0                      AS bits,
       0                      AS bits2,
       0                      AS auxShort1,
       0                      AS auxShort2,
       0                      AS auxShort3,
       0                      AS auxShort4,
       0                      AS s1,
       0                      AS s2,
       0                      AS s3,
       0                      AS s4,
       no                     AS prdno,
       ''                     AS grade,
       mfno_ref                  prdrefno,
       TRIM(MID(name, 1, 37)) AS prdrefname,
       TRIM(MID(name, 37, 3)) AS auxChar1,
       ''                     AS auxChar2,
       ''                     AS c1,
       ''                     AS c2,
       ''                     AS c3,
       ''                     AS c4
FROM sqldados.prd
WHERE no = LPAD(:codigo, 16, ' ')
  AND :grade = '';

UPDATE sqldados.prd
SET mfno_ref = :prdrefno
WHERE no = LPAD(:codigo, 16, ' ')
  AND :grade = '';

UPDATE sqldados.mfprd
SET prdno = :prdrefno
WHERE prdnoRef = LPAD(:codigo, 16, ' ')
  AND grade = :grade