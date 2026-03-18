-- Data for Witness Facilities
-- Doug Climie
-- 25/03/03
--
--
--Modified to add prompts for Case Id and date of first day KJW 31.3.03


PROMPT Remove existing data

DELETE FROM XHB_WITNESS
/
DELETE FROM XHB_SKELETON_SESSION
/
DELETE FROM XHB_SKELETON_DAY
/
DELETE FROM XHB_SKELETON_SCHEDULE
/

commit
/


PROMPT New records - real "skeleton" witness
PROMPT
PROMPT Enter Case_id &&Case1


INSERT INTO XHB_SKELETON_SCHEDULE
(skeleton_id,
last_update_date,
creation_date,
created_by,
last_updated_by,  
version,
skeleton_delivery_status_id,
CASE_ID,
DELIVERABLE )
VALUES
(1,
sysdate,
sysdate,
user,
user,
1,
1,
&&Case1,
'Y')
/
INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(1,
1,
null,
sysdate,
sysdate,
user,
user,
1,
1,
1 )
/
INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(1,
'M',
'First witness for first day',
sysdate,
sysdate,
user,
user,
1,
1,
1 )
/
INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(1,
'Basil Fawlty',
'Juvenile',
12,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
1,
sysdate,
sysdate,
user,
user,
&&Case1,
12)
/
PROMPT  New witness - same skeleton, day,session
PROMPT

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(2,
'Arthur Askey',
'Police Officer',
48,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
1,
sysdate,
sysdate,
user,
user,
1,
&&Case1)
/

commit
/
PROMPT Non-"skeleton" data - new record
PROMPT
PROMPT Enter Case_id &&Case2
--

INSERT INTO XHB_SKELETON_SCHEDULE
(skeleton_id,
last_update_date,
creation_date,
created_by,
last_updated_by,  
version,
skeleton_delivery_status_id,
CASE_ID,
DELIVERABLE )
VALUES
(2,
sysdate,
sysdate,
user,
user,
1,
null,
&&Case2,
'N')
/

INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(2,
1,
null,
sysdate,
sysdate,
user,
user,
1,
1,
2 )
/
INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(2,
'M',
null,
sysdate,
sysdate,
user,
user,
1,
2,
2 )
/
INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(3,
 'Atilla the Hun',
'Ordinary',
null,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 15:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
'Watch out - history of unstable behaviour',
null,
2,
sysdate,
sysdate,
user,
user,
1,
&&Case2)
/

commit
/

PROMPT Add witness for same skeleton (2), case, day, session

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(4,
'Sybil Boggins',
'Ordinary',
37,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 11:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
2,
sysdate,
sysdate,
user,
user,
1,
&&Case2)
/

commit
/

PROMPT Add witness for same skeleton (2), case, new session (3)

INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(3,
'A',
null,
sysdate,
sysdate,
user,
user,
1,
2,
2 )
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(5,
'Leonard Nimoy',
'Ordinary',
61,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 14:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
3,
sysdate,
sysdate,
user,
user,
1,
&&Case2)
/

commit
/

PROMPT Non-"skeleton" (3) data - new record
PROMPT
PROMPT Enter Case_id &&Case3
--

INSERT INTO XHB_SKELETON_SCHEDULE
(skeleton_id,
last_update_date,
creation_date,
created_by,
last_updated_by,  
version,
skeleton_delivery_status_id,
CASE_ID,
DELIVERABLE )
VALUES
(3,
sysdate,
sysdate,
user,
user,
1,
null,
&&Case3,
'N')
/


INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(4,
1,
null,
sysdate,
sysdate,
user,
user,
1,
1,
3 )
/
INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(4,
'M',
null,
sysdate,
sysdate,
user,
user,
1,
4,
3 )
/
INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(6,
'Carmina Detroit',
'Expert',
18,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
4,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/

PROMPT Add witness for same skeleton (3), case, day, session

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(7,
'Horace Walpole',
'Ordinary',
286,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:00', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
4,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/

PROMPT Add witness for same skeleton (3), case, new session (5)

INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(5,
'A',
null,
sysdate,
sysdate,
user,
user,
1,
4,
3 )
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(8,
'Daphne Jane Doe',
'Ordinary',
21,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 16:30', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
null,
null,
5,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/

PROMPT 
PROMPT Add witness for same skeleton (3), case, new day (5), session (6)


INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(5,
2,
null,
sysdate,
sysdate,
user,
user,
1,
1,
3 )
/

INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(6,
'M',
null,
sysdate,
sysdate,
user,
user,
1,
5,
3 )
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(9,
'Terry Thatcher',
'Expert',
null,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 12:30', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
'Shifty type - could be MI6',
null,
6,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/


PROMPT Add witness for same skeleton (3), case, new day (6), session (7)

INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(6,
3,
null,
sysdate,
sysdate,
user,
user,
1,
1,
3 )
/

INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(7,
'A',
null,
sysdate,
sysdate,
user,
user,
1,
6,
3 )
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(10,
'Doug Climie',
'Ordinary',
45,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 16:30', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
'Honest upright person doing their duty',
null,
7,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/


PROMPT Add witness for same skeleton (3), case, new day (7), session (8)

INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(7,
4,
null,
sysdate,
sysdate,
user,
user,
1,
1,
3 )
/

INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(8,
'M',
null,
sysdate,
sysdate,
user,
user,
1,
7,
3 )
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(11,
'Louis Pasteur',
'Expert',
181,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:30', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
'Clean-living type',
null,
8,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/

PROMPT Add witness for same skeleton (3), case, new day (8), session (9)

INSERT INTO XHB_SKELETON_DAY
(skeleton_day_id,
day_number, 
skeleton_date,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
WEEK_NUMBER,
skeleton_id )
VALUES
(8,
5,
null,
sysdate,
sysdate,
user,
user,
1,
1,
3 )
/

INSERT INTO XHB_SKELETON_SESSION (
skeleton_session_id,
morning_or_afternoon,
notes,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
skeleton_day_id,
skeleton_id )             
VALUES
(9,
'M',
null,
sysdate,
sysdate,
user,
user,
1,
8,
3 )
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(12,
'Michael Jackson',
'Expert',
181,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:30', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
'Clean-living type',
null,
8,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

INSERT INTO XHB_WITNESS (
witness_id,
name,
status,
age,
witness_type,
actual_arrival_date_time,
EXPECTED_ARRIVAL_TIME,
released_date_time,
mobileNumber,
pagerNumber,
pagerNet,
notes,
calculated_witness_time,
session_id,
last_update_date,
creation_date,
created_by,
last_updated_by,
version,
CASE_ID)
VALUES
(13,
'Michael Jacksons son',
'Expert',
18,
'PROSECUTION',
null,
to_date(trunc(sysdate)||', 10:30', 'DD-MON-RRRR, HH24:MI'),
null,
null,
null,
null,
'Clean-living type',
null,
8,
sysdate,
sysdate,
user,
user,
1,
&&Case3)
/

commit
/
update xhb_skeleton_day
set skeleton_date = trunc(sysdate)
where day_number = 1
/
update xhb_skeleton_day
set skeleton_date = trunc(sysdate) + 1
where day_number = 2
/
update xhb_skeleton_day
set skeleton_date = trunc(sysdate) + 2
where day_number = 3
/
update xhb_skeleton_day
set skeleton_date = trunc(sysdate) + 3
where day_number = 4
/
update xhb_skeleton_day
set skeleton_date = trunc(sysdate) + 4
where day_number = 5
/
update xhb_witness
set expected_arrival_time  = expected_arrival_time + 1
where session_id in (
select sess.skeleton_session_id from xhb_skeleton_session sess, xhb_skeleton_day d
where d.day_number = 2
and d.skeleton_day_id = sess.skeleton_day_id)
/
update xhb_witness
set expected_arrival_time  = expected_arrival_time + 2
where session_id in (
select sess.skeleton_session_id from xhb_skeleton_session sess, xhb_skeleton_day d
where d.day_number = 3
and d.skeleton_day_id = sess.skeleton_day_id)
/
update xhb_witness
set expected_arrival_time  = expected_arrival_time + 3
where session_id in (
select sess.skeleton_session_id from xhb_skeleton_session sess, xhb_skeleton_day d
where d.day_number = 4
and d.skeleton_day_id = sess.skeleton_day_id)
/
update xhb_witness
set expected_arrival_time  = expected_arrival_time + 4
where session_id in (
select sess.skeleton_session_id from xhb_skeleton_session sess, xhb_skeleton_day d
where d.day_number = 5
and d.skeleton_day_id = sess.skeleton_day_id)
/
commit
/
