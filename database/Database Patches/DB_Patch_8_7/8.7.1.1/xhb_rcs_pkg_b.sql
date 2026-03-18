create or replace PACKAGE BODY "XHB_RCS_PKG" AS
  /**
  * CGI CREST to XHIBIT Program
  *
  * MODULE      : XHB_RCS_PKG
  *
  * DESCRIPTION : This package contains stored procedures for the Record Courtroom Statistics Module
  *
  **************************************************************************************/

/**
 * CGI DREST TO XHIBIT Program
 *
 * MODULE      : populate_judge_usage
 *
 * DESCRIPTION : Populate XHB_JUDGE_USAGE tables from hearing and sitting tables * 
 **/
PROCEDURE populate_judge_usage (p_court_id  IN xhb_court.court_id%TYPE
                                                    ,p_from_date IN DATE
                                                    ,p_to_date   IN DATE) AS 

	TYPE xhb_rcs_rec IS RECORD
    (court_room_id            xhibit.xhb_court_room.court_room_id%TYPE
    , sitting_date             xhibit.xhb_scheduled_hearing.start_time%TYPE 
    , ref_judge_id             xhibit.xhb_sitting.ref_judge_id%TYPE
    );

    TYPE xhb_rcs_type IS TABLE OF xhb_rcs_rec;
    xhb_rcs_tt  xhb_rcs_type;

	CURSOR xhb_rcs_cur IS
	select 	xcr.court_room_id
			,xsh.start_time AS sitting_date
			,xs.ref_judge_id
	FROM 	xhb_court xcrt
			,xhb_court_site xcs
			,xhb_court_room xcr
			,xhb_sitting xs
			,xhb_scheduled_hearing xsh
	WHERE xcrt.court_id = p_court_id
	AND trunc(xsh.start_time) BETWEEN p_from_date AND p_to_date
	AND xcrt.court_id = xcs.court_id
	AND xcs.court_site_id = xcr.court_site_id
	AND xs.court_room_id = xcr.court_room_id
	AND xs.court_site_id = xcs.court_site_id
	AND xsh.sitting_id = xs.sitting_id
	AND NVL(xcrt.obs_ind,'-') <> 'Y'
	AND NVL(xcs.obs_ind,'-') <> 'Y'
	AND NVL(xcr.obs_ind,'-') <> 'Y'
	ORDER BY ref_judge_id, sitting_date, court_room_id;

	v_curr_ref_judge_id  xhb_judge_usage.ref_judge_id%TYPE :=0;
	v_next_ref_judge_id  xhb_judge_usage.ref_judge_id%TYPE :=0;
	v_curr_sitting_date  xhibit.xhb_scheduled_hearing.start_time%TYPE :=(sysdate-100); 
	v_next_sitting_date  xhibit.xhb_scheduled_hearing.start_time%TYPE :=(sysdate-100); 
	v_ju_row_cnt         NUMBER:=0;
 
BEGIN
  
	OPEN xhb_rcs_cur;
	LOOP
		FETCH xhb_rcs_cur BULK COLLECT INTO xhb_rcs_tt LIMIT 1000;

		IF xhb_rcs_tt IS NOT NULL AND xhb_rcs_tt.COUNT > 0 THEN
			FOR i IN xhb_rcs_tt.FIRST .. xhb_rcs_tt.LAST LOOP
				--1st iteration set the variables
				v_curr_ref_judge_id  := NVL(xhb_rcs_tt(i).ref_judge_id,00000);
				v_curr_sitting_date  := trunc(xhb_rcs_tt(i).sitting_date);

				IF 	v_curr_ref_judge_id = v_next_ref_judge_id AND 
					v_curr_sitting_date = v_next_sitting_date THEN
					-- Prevent duplicates from being entered
					NULL;

				ELSE             
					-- Check that the current row is not in the table as it may have been created outside of this run
					SELECT count(*)
					INTO v_ju_row_cnt
					FROM xhb_judge_usage
					WHERE trunc(sitting_date) = trunc(xhb_rcs_tt(i).sitting_date)
					AND ref_judge_id = xhb_rcs_tt(i).ref_judge_id;

					IF v_ju_row_cnt = 0 
						AND xhb_rcs_tt(i).ref_judge_id IS NOT NULL 
						AND judge_usage_exists_YN(xhb_rcs_tt(i).sitting_date, xhb_rcs_tt(i).ref_judge_id) = 'N' THEN
						-- Load into the xhb_judge_usage table but do not create duplicates
						
						INSERT INTO xhb_judge_usage (judge_usage_id
							,court_room_id
							,sitting_date
							,court_chambers_ind
							,ref_judge_id)
						VALUES (xhb_judge_usage_seq.nextval
							, xhb_rcs_tt(i).court_room_id
							, trunc(xhb_rcs_tt(i).sitting_date)
							, 'CRT'
							, xhb_rcs_tt(i).ref_judge_id
						);
					END IF;
				END IF; 

				v_next_ref_judge_id  := NVL(xhb_rcs_tt(i).ref_judge_id,00000);
				v_next_sitting_date  := trunc(xhb_rcs_tt(i).sitting_date);               

			END LOOP;
		END IF;
		EXIT WHEN xhb_rcs_cur%NOTFOUND;
	END LOOP;

	CLOSE xhb_rcs_cur;                           

EXCEPTION
	WHEN OTHERS THEN
		ROLLBACK;
		DBMS_OUTPUT.PUT_LINE('!!! AN ERROR OCCURRED populate_judge_usage: '||p_court_id||' ERROR: '||SUBSTR(SQLERRM,1,110));
		
END populate_judge_usage;

FUNCTION courtroom_usage_exists_YN (p_courtroom_id IN XHB_COURT_ROOM.COURT_ROOM_ID%TYPE
								   ,p_sittingdate  IN DATE) RETURN VARCHAR2 IS
								   
	v_result VARCHAR2(1);
	CURSOR C_xcru IS
	SELECT 	DECODE(COUNT(*), 0, 'N', 'Y')
	FROM 	XHB_COURT_ROOM_USAGE
	WHERE 	COURT_ROOM_ID = p_courtroom_id 
	AND		TRUNC(SITTING_DATE) = TRUNC(p_sittingdate)
	AND 	NVL(OBS_IND,'N') = 'N';
	
BEGIN

	OPEN C_xcru;
	FETCH C_xcru INTO v_result;
	CLOSE C_xcru;
	RETURN v_result;
	
END courtroom_usage_exists_YN;

FUNCTION judge_usage_exists_YN (p_sittingdate  IN DATE
							   ,p_judge_id	   IN XHB_REF_JUDGE.REF_JUDGE_ID%TYPE) RETURN VARCHAR2 IS
								   
	v_result VARCHAR2(1);
	CURSOR C_xju IS
	SELECT 	DECODE(COUNT(*), 0, 'N', 'Y')
	FROM 	XHB_JUDGE_USAGE
	WHERE 	REF_JUDGE_ID = p_judge_id
	AND		TRUNC(SITTING_DATE) = TRUNC(p_sittingdate)
	AND 	NVL(OBS_IND,'N') = 'N';
	
BEGIN

	OPEN C_xju;
	FETCH C_xju INTO v_result;
	CLOSE C_xju;
	RETURN v_result;
	
END judge_usage_exists_YN;

END XHB_RCS_PKG;
/
show errors