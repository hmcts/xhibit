DECLARE

	replaceString	VARCHAR2(80);
	debug 			BOOLEAN := FALSE;
	success 		NUMBER := 0;
	failed 			NUMBER := 0;

	CURSOR c_ref_advocate IS
	SELECT ref_advocate_id, honours
	FROM xhb_ref_advocate
	WHERE honours IS NOT NULL
	AND (honours LIKE '%QC%' OR honours LIKE '%Q.C%');
	
	CURSOR c_ref_judge_honours IS
	SELECT ref_judge_id, honours
	FROM xhb_ref_judge
	WHERE honours IS NOT NULL
	AND (honours LIKE '%QC%' OR honours LIKE '%Q.C%');
	
	CURSOR c_ref_judge_title IS
	SELECT ref_judge_id, title
	FROM xhb_ref_judge
	WHERE title IS NOT NULL
	AND (title LIKE '%QC%' OR title LIKE '%Q.C%');
	
	CURSOR c_ref_judge_full_list_title1 IS
	SELECT ref_judge_id, full_list_title1
	FROM xhb_ref_judge
	WHERE full_list_title1 IS NOT NULL
	AND (full_list_title1 LIKE '%QC%' OR full_list_title1 LIKE '%Q.C%');
	
	CURSOR c_ref_judge_full_list_title2 IS
	SELECT ref_judge_id, full_list_title2
	FROM xhb_ref_judge
	WHERE full_list_title2 IS NOT NULL
	AND (full_list_title2 LIKE '%QC%' OR full_list_title2 LIKE '%Q.C%');
	
	CURSOR c_ref_judge_full_list_title3 IS
	SELECT ref_judge_id, full_list_title3
	FROM xhb_ref_judge
	WHERE full_list_title3 IS NOT NULL
	AND (full_list_title3 LIKE '%QC%' OR full_list_title3 LIKE '%Q.C%');
	
	FUNCTION ok_to_replace (currentValue VARCHAR2, searchString VARCHAR2) 
	RETURN BOOLEAN IS
		instrValue		NUMBER;
		strLength		NUMBER;
		leadingChar		VARCHAR2(1);
		trailingChar	VARCHAR2(1);
		ok 				BOOLEAN := FALSE;
	BEGIN
		strLength := LENGTH(searchString);
		instrValue := INSTR(currentValue, searchString);
		IF instrValue = 1 THEN
			-- searchString is at start of currentValue so don't check for leading characters
			trailingChar := SUBSTR(currentValue, instrValue + strLength, 1);
			IF trailingChar IS NULL OR REGEXP_INSTR(trailingChar,'[a-zA-Z]') = 0 THEN
				-- The trailing character is not another letter of the alphabet
				ok := TRUE;
			END IF;
		ELSIF instrValue > 1 THEN
			-- searchString is not at the start of currentValue so check leading and trailing characters
			leadingChar := SUBSTR(currentValue, instrValue - 1, 1);
			trailingChar := SUBSTR(currentValue, instrValue + strLength, 1);
			IF (leadingChar IS NULL OR REGEXP_INSTR(leadingChar,'[a-zA-Z]') = 0)
			   AND (trailingChar IS NULL OR REGEXP_INSTR(trailingChar,'[a-zA-Z]') = 0) THEN
				-- The leading and trailing characters are not letters of the alphabet
				ok := TRUE;
			END IF;
		END IF;
		RETURN ok;
	END ok_to_replace;
	
	FUNCTION get_new_value (currentValue VARCHAR2) 
	RETURN VARCHAR2 IS
		newValue	VARCHAR2(80);
	BEGIN
		IF ok_to_replace(currentValue, 'QC') THEN
			newValue := REPLACE(currentValue, 'QC', 'KC');
		ELSIF ok_to_replace(currentValue, 'Q.C') THEN
			newValue := REPLACE(currentValue, 'Q.C', 'K.C');
		ELSE
			newValue := currentValue;
		END IF;
		RETURN newValue;
	END get_new_value;

BEGIN

	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_ADVOCATE.HONOURS data');
	FOR rec IN c_ref_advocate LOOP
		replaceString := get_new_value(rec.honours);
		IF replaceString <> rec.honours THEN
			-- Ok to update with the new value
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE(rec.honours || ' replaced with ' || replaceString);
			ELSE
				UPDATE xhb_ref_advocate SET honours = replaceString WHERE ref_advocate_id = rec.ref_advocate_id;
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform the replace update on XHB_REF_ADVOCATE.HONOURS ' || rec.honours || ' for id ' || rec.ref_advocate_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Updated: ' || success || ' failed: ' || failed);
	COMMIT;
	success := 0;
	failed := 0;

	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_JUDGE.HONOURS data');
	FOR rec IN c_ref_judge_honours LOOP
		replaceString := get_new_value(rec.honours);
		IF replaceString <> rec.honours THEN
			-- Ok to update with the new value
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE(rec.honours || ' replaced with ' || replaceString);
			ELSE
				UPDATE xhb_ref_judge SET honours = replaceString WHERE ref_judge_id = rec.ref_judge_id;
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform the replace update on XHB_REF_JUDGE.HONOURS ' || rec.honours || ' for id ' || rec.ref_judge_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Updated: ' || success || ' failed: ' || failed);
	COMMIT;
	success := 0;
	failed := 0;
	
	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_JUDGE.TITLE data');
	FOR rec IN c_ref_judge_title LOOP
		replaceString := get_new_value(rec.title);
		IF replaceString <> rec.title THEN
			-- Ok to update with the new value
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE(rec.title || ' replaced with ' || replaceString);
			ELSE
				UPDATE xhb_ref_judge SET title = replaceString WHERE ref_judge_id = rec.ref_judge_id;
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform the replace update on XHB_REF_JUDGE.TITLE ' || rec.title || ' for id ' || rec.ref_judge_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Updated: ' || success || ' failed: ' || failed);
	COMMIT;
	success := 0;
	failed := 0;
	
	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_JUDGE.FULL_LIST_TITLE1 data');
	FOR rec IN c_ref_judge_full_list_title1 LOOP
		replaceString := get_new_value(rec.full_list_title1);
		IF replaceString <> rec.full_list_title1 THEN
			-- Ok to update with the new value
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE(rec.full_list_title1 || ' replaced with ' || replaceString);
			ELSE
				UPDATE xhb_ref_judge SET full_list_title1 = replaceString WHERE ref_judge_id = rec.ref_judge_id;
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform the replace update on XHB_REF_JUDGE.FULL_LIST_TITLE1 ' || rec.full_list_title1 || ' for id ' || rec.ref_judge_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Updated: ' || success || ' failed: ' || failed);
	COMMIT;
	success := 0;
	failed := 0;
	
	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_JUDGE.FULL_LIST_TITLE2 data');
	FOR rec IN c_ref_judge_full_list_title2 LOOP
		replaceString := get_new_value(rec.full_list_title2);
		IF replaceString <> rec.full_list_title2 THEN
			-- Ok to update with the new value
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE(rec.full_list_title2 || ' replaced with ' || replaceString);
			ELSE
				UPDATE xhb_ref_judge SET full_list_title2 = replaceString WHERE ref_judge_id = rec.ref_judge_id;
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform the replace update on XHB_REF_JUDGE.FULL_LIST_TITLE2 ' || rec.full_list_title2 || ' for id ' || rec.ref_judge_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Updated: ' || success || ' failed: ' || failed);
	COMMIT;
	success := 0;
	failed := 0;
	
	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_JUDGE.FULL_LIST_TITLE3 data');
	FOR rec IN c_ref_judge_full_list_title3 LOOP
		replaceString := get_new_value(rec.full_list_title3);
		IF replaceString <> rec.full_list_title3 THEN
			-- Ok to update with the new value
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE(rec.full_list_title3 || ' replaced with ' || replaceString);
			ELSE
				UPDATE xhb_ref_judge SET full_list_title3 = replaceString WHERE ref_judge_id = rec.ref_judge_id;
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform the replace update on XHB_REF_JUDGE.FULL_LIST_TITLE3 ' || rec.full_list_title3 || ' for id ' || rec.ref_judge_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Updated: ' || success || ' failed: ' || failed);
	COMMIT;

END;
/