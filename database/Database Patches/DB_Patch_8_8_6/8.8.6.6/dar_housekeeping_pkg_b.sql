CREATE OR REPLACE PACKAGE BODY dar_housekeeping_pkg AS

	START_GREATER_END EXCEPTION;
	PRAGMA EXCEPTION_INIT(START_GREATER_END, -20105);

	DAYS_LIMIT_EXCEEDED EXCEPTION;
	PRAGMA EXCEPTION_INIT(DAYS_LIMIT_EXCEEDED, -20110);


	--DVR-17
	PROCEDURE get_darts_messages(p_start_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE - 1)
							,p_end_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE)) IS
	DELIMITER CONSTANT VARCHAR2(1) := ',';
	SPEECHMARKS CONSTANT VARCHAR2(1) := '"';
	DATE_FORMAT CONSTANT VARCHAR2(10) := 'DD/MM/YYYY';
	
	BEGIN
		DBMS_OUTPUT.PUT_LINE(SPEECHMARKS || 'DARTS Messages from ' || TO_CHAR(p_start_date, DATE_FORMAT) || ' to ' || TO_CHAR(p_end_date, DATE_FORMAT) || SPEECHMARKS);
		
		IF (p_start_date > p_end_date) THEN
			RAISE START_GREATER_END;
		ELSIF (p_end_date - p_start_date > 60) THEN
			RAISE DAYS_LIMIT_EXCEEDED;
		END IF;
		
		DBMS_OUTPUT.PUT_LINE(SPEECHMARKS || 'Date' || SPEECHMARKS || DELIMITER || SPEECHMARKS || 'DARTS Successful Messages' || SPEECHMARKS || DELIMITER || SPEECHMARKS || 'DARTS Failed Messages' || SPEECHMARKS || DELIMITER || SPEECHMARKS || 'DARTS Not Processed' || SPEECHMARKS);
		
		FOR rec IN (
		SELECT TRUNC(dms.creation_date) darts_date,
			SUM(CASE status_code WHEN 'S' THEN 1 ELSE 0 END) Success,
			SUM(CASE status_code WHEN 'F' THEN 1 ELSE 0 END) Failure,
			SUM(CASE status_code WHEN 'N' THEN 1 ELSE 0 END) Not_Processed
		FROM DAR_MESSAGE_STORE dms
		WHERE dms.creation_date IS NOT NULL
		AND dms.creation_date BETWEEN p_start_date AND p_end_date
		AND (status_code = 'F' AND NOT status_detail LIKE '404 : Courthouse Not Found%')
		AND (status_code = 'F' AND NOT status_detail LIKE '404 : Handler Not Found%')
		GROUP BY TRUNC(dms.creation_date)
		ORDER BY 1 DESC) loop
		DBMS_OUTPUT.PUT_LINE(SPEECHMARKS || TO_CHAR(rec.darts_date, DATE_FORMAT) || SPEECHMARKS || DELIMITER || SPEECHMARKS || rec.Success || SPEECHMARKS || DELIMITER || SPEECHMARKS || rec.Failure || SPEECHMARKS || DELIMITER || SPEECHMARKS || rec.Not_Processed || SPEECHMARKS);
		END LOOP;
	END get_darts_messages;
  
  
	PROCEDURE get_darts_messages(p_start_date IN VARCHAR2
								,p_end_date IN VARCHAR2) IS 
	DATE_FORMAT CONSTANT VARCHAR2(10) := 'DD/MM/YYYY';
	v_start_date DATE;
	v_end_date DATE;
	
	BEGIN
		IF p_start_date IS NOT NULL AND TRIM(p_start_date) != ' ' THEN
			v_start_date := TO_DATE(p_start_date, DATE_FORMAT);
		ELSE
			v_start_date := TRUNC(SYSDATE) - 1;
		END IF;

		IF p_end_date IS NOT NULL AND TRIM(p_end_date) != ' ' THEN
			v_end_date := TO_DATE(p_end_date, DATE_FORMAT);
		ELSE
			v_end_date := TRUNC(SYSDATE);
		END IF;
		
		get_darts_messages(v_start_date, v_end_date);
	END get_darts_messages;
	
	
	PROCEDURE get_darts_errors(p_start_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE - 1)
							,p_end_date IN DAR_MESSAGE_STORE.LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE)) IS
	ERROR_OUTPUT VARCHAR2(6);
	BEGIN
		IF (p_start_date > p_end_date) THEN
			RAISE START_GREATER_END;
		ELSIF (p_end_date - p_start_date > 60) THEN
			RAISE DAYS_LIMIT_EXCEEDED;
		END IF;
		
		SELECT COUNT(status_code)
		INTO ERROR_OUTPUT
		FROM DAR_MESSAGE_STORE
		WHERE status_detail = '404 : Handler Not Found'
		AND creation_date IS NOT NULL
		AND creation_date BETWEEN p_start_date AND p_end_date;
		
		DBMS_OUTPUT.PUT_LINE(ERROR_OUTPUT);
	END get_darts_errors;
	
	
	PROCEDURE get_darts_errors(p_start_date IN VARCHAR2
							,p_end_date IN VARCHAR2) IS 
	DATE_FORMAT CONSTANT VARCHAR2(10) := 'DD/MM/YYYY';
	v_start_date DATE;
	v_end_date DATE;
	
	BEGIN
		IF p_start_date IS NOT NULL AND TRIM(p_start_date) != ' ' THEN
			v_start_date := TO_DATE(p_start_date, DATE_FORMAT);
		ELSE
			v_start_date := TRUNC(SYSDATE) - 1;
		END IF;

		IF p_end_date IS NOT NULL AND TRIM(p_end_date) != ' ' THEN
			v_end_date := TO_DATE(p_end_date, DATE_FORMAT);
		ELSE
			v_end_date := TRUNC(SYSDATE);
		END IF;
		
		get_darts_errors(v_start_date, v_end_date);
	END get_darts_errors;

END dar_housekeeping_pkg;
/


