create or replace PACKAGE "XHB_RCS_PKG" AS
 /**
  * CGI CREST to XHIBIT Program
  *
  * MODULE      : XHB_RCS_PKG
  *
  * DESCRIPTION : This package contains stored procedures for the Record Courtroom Statistics Module
  *
  * VERSION HISTORY:
  *
  * Date          Author            Version    Nature of Change
  * ----------    -------           --------   ----------------------------------------
  * 28/11/2018    Chris Vincent       1.0      First revision
  *
  **************************************************************************************/


PROCEDURE populate_judge_usage ( p_court_id  IN xhb_court.court_id%TYPE
								,p_from_date IN DATE
								,p_to_date   IN DATE);
													
FUNCTION courtroom_usage_exists_YN (p_courtroom_id IN XHB_COURT_ROOM.COURT_ROOM_ID%TYPE
								   ,p_sittingdate  IN DATE) RETURN VARCHAR2;
								   
FUNCTION judge_usage_exists_YN (p_sittingdate  IN DATE
							   ,p_judge_id	   IN XHB_REF_JUDGE.REF_JUDGE_ID%TYPE) RETURN VARCHAR2;
                           
END XHB_RCS_PKG;
/
show errors