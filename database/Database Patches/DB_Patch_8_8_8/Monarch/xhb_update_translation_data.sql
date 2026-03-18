DECLARE

	replaceKey			XHB_REF_TRANSLATION.KEY%TYPE;
	replaceTranslation	XHB_REF_TRANSLATION.TRANSLATION%TYPE;
	debug 				BOOLEAN := FALSE;
	success 			NUMBER := 0;
	failed 				NUMBER := 0;

	CURSOR c_ref_translation IS
	SELECT ref_translation_id, key, translation, context, exact_match, language, country
	FROM xhb_ref_translation
	WHERE key LIKE '%QC%' OR key LIKE '%Q.C%'
	AND obs_ind = 'N';
	
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
	
	FUNCTION get_new_value (currentValue VARCHAR2, valueMode VARCHAR2) 
	RETURN VARCHAR2 IS
		newValue	XHB_REF_TRANSLATION.TRANSLATION%TYPE;
	BEGIN
		IF valueMode = 'QC' THEN
			-- want the replacement value for the English key
			IF ok_to_replace(currentValue, 'QC') THEN
				newValue := REPLACE(currentValue, 'QC', 'KC');
			ELSIF ok_to_replace(currentValue, 'Q.C') THEN
				newValue := REPLACE(currentValue, 'Q.C', 'K.C');
			ELSE
				newValue := currentValue;
			END IF;
		ELSIF valueMode = 'CF' THEN
			-- want the replacement value for the Welsh translation
			IF ok_to_replace(currentValue, 'CF') THEN
				newValue := REPLACE(currentValue, 'CF', 'CB');
			ELSIF ok_to_replace(currentValue, 'C.F') THEN
				newValue := REPLACE(currentValue, 'C.F', 'C.B');
			ELSE
				newValue := currentValue;
			END IF;
		ELSE
			-- valueMode not recognised, return the original value
			newValue := currentValue;
		END IF;
		RETURN newValue;
	END get_new_value;

BEGIN

	DBMS_OUTPUT.PUT_LINE('Processing XHB_REF_TRANSLATION data');
	FOR rec IN c_ref_translation LOOP
		replaceKey := get_new_value(rec.key, 'QC');
		replaceTranslation := get_new_value(rec.translation, 'CF');
		IF replaceTranslation = rec.translation THEN
			-- Could not find the Welsh equivalent of QC in translation so seartioch for QC instead
			replaceTranslation := get_new_value(rec.translation, 'QC');
		END IF;
		
		IF replaceKey <> rec.key THEN
			-- Ok to insert new record
			IF debug THEN
				DBMS_OUTPUT.PUT_LINE('New record: Old Key: ' || rec.key || ' New Key: ' || replaceKey);
				DBMS_OUTPUT.PUT_LINE('New record: Old Translation: ' || rec.translation || ' New Translation: ' || replaceTranslation);
			ELSE
				INSERT INTO xhb_ref_translation (key, translation, context, exact_match, language, country)
				VALUES (replaceKey, replaceTranslation, rec.context, rec.exact_match, rec.language, rec.country);
			END IF;
			
			IF replaceTranslation = rec.translation THEN
				-- New record create, but alert in log if could no find a QC/CF reference in the translation
				DBMS_OUTPUT.PUT_LINE('WARNING: New record created for id: ' || rec.ref_translation_id || ' but no matching QC/CF reference could be found in the translation');
			END IF;
			IF debug THEN
				-- To separate inserts when debugging
				DBMS_OUTPUT.PUT_LINE('----------');
			END IF;
			success := success + 1;
		ELSE
			-- Cannot update - log it
			DBMS_OUTPUT.PUT_LINE('Could not perform an insert for key: ' || rec.key || ' on translation id: ' || rec.ref_translation_id);
			failed := failed + 1;
		END IF;
	END LOOP;
	DBMS_OUTPUT.PUT_LINE('Inserted: ' || success || ' failed: ' || failed);
	COMMIT;

END;
/