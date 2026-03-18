--_____________________________
--Populate XHB_DISPLAY_DOCUMENT
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
INSERT INTO XHB_DISPLAY_DOCUMENT
(
    DISPLAY_DOCUMENT_ID,
    DESCRIPTION_CODE,
    DEFAULT_PAGE_DELAY,
    MULTIPLE_COURT_YN
)
VALUES
(
    1,
    'CourtDetail',
    10,
    'N'
);

INSERT INTO XHB_DISPLAY_DOCUMENT
(
    DISPLAY_DOCUMENT_ID,
    DESCRIPTION_CODE,
    DEFAULT_PAGE_DELAY,
    MULTIPLE_COURT_YN
)
VALUES
(
    2,
    'CourtList',
    10,
    'N'
);

INSERT INTO XHB_DISPLAY_DOCUMENT
(
    DISPLAY_DOCUMENT_ID,
    DESCRIPTION_CODE,
    DEFAULT_PAGE_DELAY,
    MULTIPLE_COURT_YN
)
VALUES
(
    3,
    'DailyList',
    10,
    'Y'
);

INSERT INTO XHB_DISPLAY_DOCUMENT
(
    DISPLAY_DOCUMENT_ID,
    DESCRIPTION_CODE,
    DEFAULT_PAGE_DELAY,
    MULTIPLE_COURT_YN
)
VALUES
(
    4,
    'AllCourtStatus',
    10,
    'Y'
);

INSERT INTO XHB_DISPLAY_DOCUMENT
(
    DISPLAY_DOCUMENT_ID,
    DESCRIPTION_CODE,
    DEFAULT_PAGE_DELAY,
    MULTIPLE_COURT_YN
)
VALUES
(
    5,
    'SummaryByName',
    10,
    'Y'
);

INSERT INTO XHB_DISPLAY_DOCUMENT
(
    DISPLAY_DOCUMENT_ID,
    DESCRIPTION_CODE,
    DEFAULT_PAGE_DELAY,
    MULTIPLE_COURT_YN
)
VALUES
(
    6,
    'JuryCurrentStatus',
    10,
    'Y'
);

COMMIT;

--_________________________
--Populate XHB_DISPLAY_TYPE
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
INSERT INTO XHB_DISPLAY_TYPE
(
    DISPLAY_TYPE_ID,
    DESCRIPTION_CODE
)
VALUES
(
    1,
    '18in'
);

INSERT INTO XHB_DISPLAY_TYPE
(
    DISPLAY_TYPE_ID,
    DESCRIPTION_CODE
)
VALUES
(
    2,
    '42in'
);

INSERT INTO XHB_DISPLAY_TYPE
(
    DISPLAY_TYPE_ID,
    DESCRIPTION_CODE
)
VALUES
(
    3,
    'browser'
);

COMMIT;

