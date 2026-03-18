DELETE FROM xhb_display_court_room
WHERE display_id < 0;

DELETE FROM xhb_display
WHERE display_id < 0;


DELETE FROM xhb_display_location
WHERE display_location_id < 0;


DELETE FROM xhb_rotation_set_dd
WHERE rotation_set_dd_id < 0;


DELETE FROM xhb_rotation_sets
WHERE rotation_set_id < 0;
