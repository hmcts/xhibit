DECLARE
	
	CURSOR c_find_duplicate_ju_rows IS
	SELECT TRUNC(sitting_date) AS sitting_date, ref_judge_id, MIN(judge_usage_id) as original_id, COUNT(*)
	FROM xhb_judge_usage
	GROUP BY TRUNC(sitting_date), ref_judge_id
	HAVING COUNT(*) > 1;

BEGIN
	
	-- Delete the duplicate xhb_judge_usage records, keeping the earliest record
	FOR ju_rec IN c_find_duplicate_ju_rows LOOP
		DELETE FROM xhb_judge_usage
		WHERE TRUNC(sitting_date) = ju_rec.sitting_date
		AND ref_judge_id = ju_rec.ref_judge_id
		AND judge_usage_id > ju_rec.original_id;
	END LOOP;
	COMMIT;

END;

/