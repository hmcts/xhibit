CREATE OR REPLACE PACKAGE dar_housekeeping_pkg AS

   --DVR-17
  PROCEDURE get_darts_messages(p_start_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE - 1)
								 ,p_end_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE));
								 
  PROCEDURE get_darts_messages(p_start_date IN VARCHAR2
								 ,p_end_date IN VARCHAR2);

  PROCEDURE get_darts_errors(p_start_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE - 1)
								 ,p_end_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE));
								 
  PROCEDURE get_darts_errors(p_start_date IN VARCHAR2
								 ,p_end_date IN VARCHAR2);

END dar_housekeeping_pkg;
/