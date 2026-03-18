INSERT INTO EXI_REF_TYPE 
(internal_code, group_id, operation_id, internal_name, schema_name, version) 
SELECT subqry.internal_code, 23 group_id, 2 operation_id, subqry.internal_name, 'http://www.courtservice.gov.uk/schemas/courtservice' schema_name, '5.9' version
FROM (
	SELECT '11532' internal_code, 'Serious Terrorism Sentence (18 to 21)' internal_name FROM DUAL UNION
	SELECT '12811' internal_code, 'Alternative Count: Jury Discharged from Verdict' internal_name FROM DUAL UNION
	SELECT '12812' internal_code, 'Jury Discharged: Other than disagreement' internal_name FROM DUAL UNION
	SELECT '12813' internal_code, 'Defendant Discharged' internal_name FROM DUAL UNION
	SELECT '12814' internal_code, 'Not Guilty by Direction' internal_name FROM DUAL UNION
	SELECT '12815' internal_code, 'Not Guilty by Reason of Insanity' internal_name FROM DUAL UNION
	SELECT '12816' internal_code, 'Pros Offer No Evidence (Not a Summary offence)' internal_name FROM DUAL UNION
	SELECT '12817' internal_code, 'Pros Offer No Evidence (Summary offences only)' internal_name FROM DUAL UNION
	SELECT '12818' internal_code, 'Jury Discharged: Unable to Agree' internal_name FROM DUAL UNION
	SELECT '12514' internal_code, 'Confiscation Order under S1(5) DTOA 1986' internal_name FROM DUAL
) subqry
WHERE NOT EXISTS (SELECT 1 FROM EXI_REF_TYPE x2
                  WHERE  x2.internal_code = subqry.internal_code);
commit;