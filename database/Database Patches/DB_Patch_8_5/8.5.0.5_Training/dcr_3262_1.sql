rem Filename : dcr_3262_l.sql
rem
rem Purpose
rem =======
rem RFC 2867 - dcr 3262 copy indictment log records from case to indictment_log
table
rem
rem Change Control
rem ==============
rem
rem Version    Date       Description                Control No.      Author
rem -------    ----       -----------                -----------      ------
rem   1.0         16/05/2011 New script              RFC2867          Peter Berr
ell
rem ----------------------------------------------------------------------------
---

set define off
set termout on
set serveroutput on

DECLARE
   CURSOR case_ind_comments
rem Filename : dcr_3262_l.sql
rem
rem Purpose
rem =======
rem RFC 2867 - dcr 3262 copy indictment log records from case to indictment_log
table
rem
rem Change Control
rem ==============
rem
rem Version    Date       Description                Control No.      Author
rem -------    ----       -----------                -----------      ------
rem   1.0         16/05/2011 New script              RFC2867          Peter Berr
ell
rem ----------------------------------------------------------------------------
---

set define off
set termout on
set serveroutput on

DECLARE
   CURSOR case_ind_comments
   IS
      SELECT
      case_type, case_no,
      ind_comments1, ind_comments2, ind_comments3, ind_comments4, ind_comments5,
 ind_comments6
      FROM pl_case
      WHERE
      ind_comments1 IS NOT NULL OR
      ind_comments2 IS NOT NULL OR
      ind_comments3 IS NOT NULL OR
      ind_comments4 IS NOT NULL OR
      ind_comments5 IS NOT NULL OR
      ind_comments6 IS NOT NULL ;

   CURSOR c_ind_log
   IS
      SELECT DISTINCT
      case_type, case_no
      FROM indictment_log;

   comment_count NUMBER := 0;
   total_count NUMBER := 0;
   log_count NUMBER := 0;

   PROCEDURE insert_ind_log(l_case_type IN pl_case.case_type%TYPE,
                            l_case_no IN pl_case.case_no%TYPE,
                            l_seq_no IN indictment_log.seq_no%TYPE,
                            l_ind_comments IN indictment_log.ind_comments%TYPE
                            ) IS
   BEGIN

      INSERT INTO indictment_log(
      case_type, case_no, seq_no, ind_comments
      ) VALUES (
      l_case_type, l_case_no, l_seq_no, l_ind_comments
      );

   END;

BEGIN

   DBMS_OUTPUT.ENABLE (100000);

   total_count := 0;
   comment_count := 0;
   FOR c_case IN case_ind_comments
   LOOP
      comment_count := 0;
      IF c_case.ind_comments1 IS NOT NULL
      THEN
         comment_count := comment_count + 1;
         insert_ind_log(c_case.case_type, c_case.case_no, comment_count, c_case.
ind_comments1);
      END IF;
      IF c_case.ind_comments2 IS NOT NULL
      THEN
         comment_count := comment_count + 1;
         insert_ind_log(c_case.case_type, c_case.case_no, comment_count, c_case.
ind_comments2);
      END IF;
      IF c_case.ind_comments3 IS NOT NULL
      THEN
         comment_count := comment_count + 1;
         insert_ind_log(c_case.case_type, c_case.case_no, comment_count, c_case.
ind_comments3);
      END IF;
      IF c_case.ind_comments4 IS NOT NULL
      THEN
         comment_count := comment_count + 1;
         insert_ind_log(c_case.case_type, c_case.case_no, comment_count, c_case.
ind_comments4);
      END IF;
      IF c_case.ind_comments5 IS NOT NULL
      THEN
         comment_count := comment_count + 1;
         insert_ind_log(c_case.case_type, c_case.case_no, comment_count, c_case.
ind_comments5);
      END IF;
      IF c_case.ind_comments6 IS NOT NULL
      THEN
         comment_count := comment_count + 1;
         insert_ind_log(c_case.case_type, c_case.case_no, comment_count, c_case.
ind_comments6);
      END IF;

      total_count := total_count + comment_count;

      IF total_count >= 1000
      THEN
         dbms_output.put_line(to_char(total_count) || ' indictment_log records c
ommitted');
         total_count := 0;
         COMMIT;
      END IF;
   END LOOP;

   dbms_output.put_line(to_char(total_count) || ' indictment_log records committ
ed');
   COMMIT;

   log_count := 0;
   FOR c_log IN c_ind_log
   LOOP

      UPDATE pl_case SET
      ind_comments1 = NULL, ind_comments2 = NULL, ind_comments3 = NULL,
      ind_comments4 = NULL, ind_comments5 = NULL, ind_comments6 = NULL
      WHERE case_type = c_log.case_type
      AND case_no = c_log.case_no;

      log_count := log_count + 1;

      IF log_count > 1000
      THEN
         dbms_output.put_line(to_char(log_count) || ' CASE records committed');
         log_count := 0;
         COMMIT;
      END IF;
   END LOOP;

   dbms_output.put_line(to_char(log_count) || ' case records committed');
   COMMIT;

   EXCEPTION
   WHEN OTHERS THEN
   dbms_output.put_line(to_char(sqlcode) || ' ' || substr(sqlerrm, 1, 100));

END;
/
