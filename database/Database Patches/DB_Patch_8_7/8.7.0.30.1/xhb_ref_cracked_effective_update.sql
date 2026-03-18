UPDATE xhb_ref_cracked_effective set trial_code_type = 'C' where code in ('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L');
UPDATE xhb_ref_cracked_effective set trial_code_type = 'E' where code = 'E1';
UPDATE xhb_ref_cracked_effective set trial_code_type = 'I' where code in ('M1', 'M2', 'M3', 'N1', 'N2', 'N3', 'O1', 'O2', 'P', 'Q1', 'Q2', 'Q3', 'R', 'S1', 'S2', 'S3', 'S4', 'T', 'U1', 'U2', 'V', 'W1', 'W2', 'W3', 'W4', 'W5', 'X', 'Y', 'Z');

commit;
