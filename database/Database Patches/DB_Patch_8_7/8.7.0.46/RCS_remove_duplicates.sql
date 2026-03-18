DECLARE

	CURSOR c_find_duplicate_cru_rows IS
	SELECT court_room_id, sitting_date, MIN(court_room_usage_id) as original_id, COUNT(*)
	FROM xhb_court_room_usage
	GROUP BY court_room_id, sitting_date
	HAVING COUNT(*) > 1;
	
	CURSOR c_find_duplicate_ju_rows IS
	SELECT court_room_id, sitting_date, ref_judge_id, MIN(judge_usage_id) as original_id, COUNT(*)
	FROM xhb_judge_usage
	GROUP BY court_room_id, sitting_date, ref_judge_id
	HAVING COUNT(*) > 1;

BEGIN

	-- Delete the duplicate xhb_court_room_usage records, keeping the earliest record
	FOR cru_rec IN c_find_duplicate_cru_rows LOOP
		DELETE FROM xhb_court_room_usage
		WHERE court_room_id = cru_rec.court_room_id
		AND sitting_date = cru_rec.sitting_date
		AND court_room_usage_id > cru_rec.original_id;
	END LOOP;
	COMMIT;
	
	-- Delete the duplicate xhb_judge_usage records, keeping the earliest record
	FOR ju_rec IN c_find_duplicate_ju_rows LOOP
		DELETE FROM xhb_judge_usage
		WHERE court_room_id = ju_rec.court_room_id
		AND sitting_date = ju_rec.sitting_date
		AND ref_judge_id = ju_rec.ref_judge_id
		AND judge_usage_id > ju_rec.original_id;
	END LOOP;
	COMMIT;

END;

/