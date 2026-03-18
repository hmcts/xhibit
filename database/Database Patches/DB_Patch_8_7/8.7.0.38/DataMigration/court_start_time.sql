select court_id
, court_start_time
from xhb_court
where court_id = 95
;

"COURT_ID"                    "COURT_START_TIME"            
"95"                          "10:0"                        

UPDATE xhb_court
SET court_start_time = '10:00'
WHERE court_id = 95;

select court_id
, court_start_time
from xhb_court
where court_id = 95
;

"COURT_ID"                    "COURT_START_TIME"            
"95"                          "10:00"                       

commit;
