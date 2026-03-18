/*xlc2-312*/
UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup1 = 'B'
WHERE disposal_code = 'DISOBLG'
AND dil_seq_no IN (130,140,150)
AND NVL(mcgroup1,'A') = 'A';

UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup1 = 'C'
WHERE disposal_code = 'DISOBLG'
AND dil_seq_no IN (160)
AND mcgroup1 = 'B';

/*xlc2-315*/
UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup2 = 'A'
WHERE disposal_code = 'DISOBLG'
AND dil_seq_no IN (200,220,240,260)
AND NVL(mcgroup2,'~') != 'A';

UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup2 = 'B'
WHERE disposal_code = 'DISOBLG'
AND dil_seq_no IN (280,300,320)
AND NVL(mcgroup2,'~') != 'B';

UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup2 = 'A'
WHERE disposal_code = 'DISDISC'
AND dil_seq_no IN (200,220)
AND NVL(mcgroup2,'~') != 'A';

UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup2 = 'B'
WHERE disposal_code = 'DISDISC'
AND dil_seq_no IN (280,300,320)
AND NVL(mcgroup2,'~') != 'B';

UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup2 = 'A'
WHERE disposal_code = 'DISTOT'
AND dil_seq_no IN (200,220)
AND NVL(mcgroup2,'~') != 'A';

UPDATE XHB_REF_DISPOSAL_LINE
SET mcgroup2 = 'B'
WHERE disposal_code = 'DISTOT'
AND dil_seq_no IN (280,300,320)
AND NVL(mcgroup2,'~') != 'B';

commit;