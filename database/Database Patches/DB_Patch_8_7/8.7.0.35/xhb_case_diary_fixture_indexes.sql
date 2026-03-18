/* Add indexes to columns regularly used for retrieving fixtures by */
CREATE INDEX XHB_CDF_COURT_SITE_FK ON xhb_case_diary_fixture(COURT_SITE_ID);
CREATE INDEX XHB_CDF_LISTING_DATE ON xhb_case_diary_fixture(LISTING_DATE);