INSERT INTO XHB_ROTATION_SETS
(
    ROTATION_SET_ID,
    COURT_ID,
    DESCRIPTION,
    DEFAULT_YN
)
VALUES
(
    -1,
    1,
    'Test 1',
    'N'
);

INSERT INTO XHB_ROTATION_SETS
(
    ROTATION_SET_ID,
    COURT_ID,
    DESCRIPTION,
    DEFAULT_YN
)
VALUES
(
    -2,
    1,
    'Test 2',
    'Y'
);

INSERT INTO XHB_ROTATION_SETS
(
    ROTATION_SET_ID,
    COURT_ID,
    DESCRIPTION,
    DEFAULT_YN
)
VALUES
(
    -3,
    1,
    'Test 3',
    'Y'
);

INSERT INTO XHB_ROTATION_SETS
(
    ROTATION_SET_ID,
    COURT_ID,
    DESCRIPTION,
    DEFAULT_YN
)
VALUES
(
    -4,
    1,
    'Test 4',
    'N'
);

INSERT INTO XHB_ROTATION_SETS
(
    ROTATION_SET_ID,
    COURT_ID,
    DESCRIPTION,
    DEFAULT_YN
)
VALUES
(
    -5,
    3,
    'Test 5',
    'Y'
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -1,
    -1,
    1,
    11,
    1
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -2,
    -1,
    2,
    12,
    2
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -3,
    -1,
    3,
    13,
    3
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -4,
    -2,
    4,
    14,
    1
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -5,
    -2,
    5,
    15,
    2
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -6,
    -2,
    6,
    16,
    3
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -7,
    -3,
    1,
    16,
    1
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -8,
    -4,
    2,
    16,
    1
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -9,
    -5,
    4,
    5,
    1
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -10,
    -5,
    5,
    2,
    2
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -11,
    -5,
    6,
    5,
    3
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -12,
    -5,
    1,
    5,
    1
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -13,
    -5,
    2,
    10,
    2
);

INSERT INTO XHB_ROTATION_SET_DD
(
    ROTATION_SET_DD_ID,
    ROTATION_SET_ID,
    DISPLAY_DOCUMENT_ID,
    PAGE_DELAY,
    ORDERING
)
VALUES
(
    -14,
    -5,
    3,
    5,
    3
);

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
VALUES
(
    -1,
    'ENTRANCE_VESTIBULE',
    1
);

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
VALUES
(
    -2,
    'OUTSIDE_CR_1_AND_2',
    1
);

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
VALUES
(
    -3,
    'OUTSIDE_CR_10_AND_14',
    3
);

INSERT INTO XHB_DISPLAY
(
    DISPLAY_ID,
    DISPLAY_TYPE_ID,
    DISPLAY_LOCATION_ID,
    ROTATION_SET_ID,
    DESCRIPTION_CODE,
    LOCALE
)
VALUES
(
    -1,
    1,
    -1,
    -1,
    'Something.',
    'enGB'
);

INSERT INTO XHB_DISPLAY
(
    DISPLAY_ID,
    DISPLAY_TYPE_ID,
    DISPLAY_LOCATION_ID,
    ROTATION_SET_ID,
    DESCRIPTION_CODE,
    LOCALE
)
VALUES
(
    -2,
    2,
    -2,
    -1,
    'Something_x.',
    'cyGB'
);

INSERT INTO XHB_DISPLAY
(
    DISPLAY_ID,
    DISPLAY_TYPE_ID,
    DISPLAY_LOCATION_ID,
    ROTATION_SET_ID,
    DESCRIPTION_CODE,
    LOCALE
)
VALUES
(
    -3,
    1,
    -1,
    -2,
    'something_xy.',
    'enGB'
);

INSERT INTO XHB_DISPLAY
(
    DISPLAY_ID,
    DISPLAY_TYPE_ID,
    DISPLAY_LOCATION_ID,
    ROTATION_SET_ID,
    DESCRIPTION_CODE,
    LOCALE
)
VALUES
(
    -4,
    2,
    -2,
    -2,
    'something_xyz.',
    'enGB'
);

INSERT INTO XHB_DISPLAY
(
    DISPLAY_ID,
    DISPLAY_TYPE_ID,
    DISPLAY_LOCATION_ID,
    ROTATION_SET_ID,
    DESCRIPTION_CODE,
    LOCALE
)
VALUES
(
    -5,
    2,
    -3,
    -5,
    'UseThisForTests',
    'enGB'
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -1,
    1
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -1,
    2
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -1,
    3
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -2,
    1
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -2,
    2
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -2,
    3
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -3,
    4
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    1
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    2
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    3
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    4
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    5
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    6
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    7
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    8
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    9
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    10
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    11
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    12
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    13
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    14
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    15
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    16
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    17
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    18
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    19
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -4,
    20
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    31
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    32
);
INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    33
);
INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    34
);
INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    35
);
INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    36
);
INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    41
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    40
);

INSERT INTO XHB_DISPLAY_COURT_ROOM
(
    DISPLAY_ID,
    COURT_ROOM_ID
)
VALUES
(
    -5,
    44
);
