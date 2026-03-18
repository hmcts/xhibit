/*CTX-2280*/
-- Introduce new DATE_VACATED column to the XHB_CASE_DIARY_FIXTURE table
ALTER TABLE xhb_case_diary_fixture 
 ADD (date_vacated	DATE
	 );

ALTER TABLE aud_case_diary_fixture 
 ADD (date_vacated	DATE
	 );


-- Rebuild the database trigger to include the new column
@@xhb_case_diary_fixture_bur_tr.sql;