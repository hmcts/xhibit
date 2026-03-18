/*
	CTX- 4361: Script checks for any entries in Chamber.
	Script checks for any entries in Chamber, Advocates and legal representatives that have been added since 17/03 to court 1
	(which is snaresbrook in live, used this as it's the last court for rollout so will be the best to use for existing non CTX). 
	It then checks these tables to see if there are entries for each pilot court and if not adds them.
	Has to insert advocate last as that has references to chamber and ref legal rep.
*/
CREATE OR REPLACE PROCEDURE xhb_populate_refCHLREPAd as
	--Get all legal rep added since 17/03 for snaresbrook
	CURSOR legalrep_c
	IS
	select *
	from xhb_ref_legal_representative 
	where TRUNC(creation_date) >= TO_DATE('2019/03/17','yyyy/mm/dd')
	and (nvl(obs_ind, 'N') <> 'Y' )  and court_id=1;
	
	--All pilot courts
    CURSOR courts_c
    IS
    SELECT court_id
    FROM   xhb_court
    WHERE  (nvl(obs_ind, 'N') <> 'Y' )
    AND    is_pilot = 'Y' 
    ORDER BY court_id;
	
	--All chambers added since 17/03
	CURSOR chamber_c
	IS
	SELECT *
	FROM xhb_ref_chamber
	where TRUNC(creation_date) >= TO_DATE('2019/03/17','yyyy/mm/dd')
	and (nvl(obs_ind, 'N') <> 'Y' ) 
	and is_global='Y'
	and court_id=1;
	
	--Declare variables 
	v_row_exist NUMBER;
	v_chamber_id xhb_ref_advocate.REF_CHAMBER_ID%TYPE;
	v_legal_rep_id xhb_ref_advocate.ref_legal_rep_id%TYPE;
	v_ref_advocate_val XHB_REF_ADVOCATE%ROWTYPE;
    v_err_code NUMBER;
    v_err_msg  VARCHAR2(100);
	
BEGIN

	--Insert chamber 
	FOR chamber_r in chamber_c
	LOOP
		FOR courts_r IN courts_c
		LOOP 
			SELECT count (*)
			INTO v_row_exist
			FROM xhb_ref_chamber
			WHERE court_id = courts_r.court_id
			AND crest_chamber_id = chamber_r.crest_chamber_id;
			IF v_row_exist = 0 THEN				
				INSERT INTO xhb_ref_chamber(OBS_IND, IS_GLOBAL, DX_REF, LOCATION_CODE, CREST_CHAMBER_ID, FIRM_NAME, ADDRESS_ID, COURT_ID, CREATED_BY, CLERK_NAME)
				VALUES ('N','Y', chamber_r.DX_REF, chamber_r.LOCATION_CODE, chamber_r.crest_chamber_id, chamber_r.FIRM_NAME, chamber_r.ADDRESS_ID, courts_r.court_id, 'XHIBIT', chamber_r.CLERK_NAME);
		   END IF;
		END LOOP;
	END LOOP;	
	--legal rep and advocates
	FOR legalrep_r in legalrep_c
	LOOP
		-- loop over all the pilot courts and check if its in there, if not then add it to here and advocates
		FOR courts_r in courts_c
			LOOP
			SELECT count (*)
			INTO v_row_exist
			FROM xhb_ref_legal_representative
			WHERE court_id = courts_r.court_id
			AND first_name = legalrep_r.first_name
			AND middle_name = legalrep_r.middle_name
			AND surname = legalrep_r.surname
			AND title = legalrep_r.title
			AND initials = legalrep_r.initials
			AND legal_rep_type = legalrep_r.legal_rep_type;
			
			IF v_row_exist = 0 THEN				
				INSERT INTO xhb_ref_legal_representative(FIRST_NAME, MIDDLE_NAME, SURNAME, TITLE, INITIALS, LEGAL_REP_TYPE, CREATED_BY, COURT_ID, OBS_IND )
				VALUES (legalrep_r.first_name, legalrep_r.middle_name, legalrep_r.surname, legalrep_r.title, legalrep_r.initials, legalrep_r.legal_rep_type, 'XHIBIT', courts_r.court_id, 'N' );
				
				-- get the value we have just added
				select XHB_REF_LEGAL_REP_SEQ.CURRVAL
				INTO v_legal_rep_id
				FROM dual;
				
				--also need to insert into ref advocate 
				select *
				INTO v_ref_advocate_val
				FROM xhb_ref_advocate 
				where ref_legal_rep_id = legalrep_r.ref_legal_rep_id;
				
				select ref_chamber_id 
				INTO v_chamber_id
				from xhb_ref_chamber
				where crest_chamber_id = v_ref_advocate_val.crest_chamber_id
				and court_id=courts_r.court_id;
				
				--INSERT into the ref advocate
				INSERT INTO xhb_ref_advocate(IS_GLOBAL, CREST_ADVOCATE_ID, CREST_CHAMBER_ID, OBS_IND, YEAR_OF_CALL, VAT_NO, BAR_NO, HONOURS, ADV_TYPE_IND, REF_LEGAL_REP_ID, REF_CHAMBER_ID, CREATED_BY)
				VALUES('Y',v_ref_advocate_val.CREST_ADVOCATE_ID, v_ref_advocate_val.CREST_CHAMBER_ID, 'N', v_ref_advocate_val.YEAR_OF_CALL, v_ref_advocate_val.VAT_NO, v_ref_advocate_val.BAR_NO, v_ref_advocate_val.HONOURS, v_ref_advocate_val.ADV_TYPE_IND, v_legal_rep_id, v_chamber_id, 'XHIBIT' );		
			END IF;

			END LOOP;
	END LOOP;
	COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001, 'Error in xhb_populate_ref_advocate_all :- ' || v_err_code || ' : ' || v_err_msg);
END xhb_populate_refCHLREPAd;
/
show errors
