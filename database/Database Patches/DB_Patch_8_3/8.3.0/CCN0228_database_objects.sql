/**************************************************
* CCN0228_database_objects.sql
*
* Version Date       Author  Description
* 1.0     25/03/2009 J Powell Creation
* 1.1     30/03/2009 J Powell Added DATE_EXPORTED to AUD_DEFENDANT_ON_CASE and updated XHB_DEFENDANTONCASE_BUR_TR
*
***************************************************/

--Create new column on XHB_DEFENDANT_ON_CASE
ALTER TABLE XHB_DEFENDANT_ON_cASE
ADD DATE_EXPORTED DATE;

ALTER TABLE AUD_DEFENDANT_ON_cASE
ADD DATE_EXPORTED DATE;

-- the defendant on case trigger will be updated in a separate file

--Create New roles
INSERT INTO XHB_SECURITY_ROLE VALUES('XHBViewUnauthorisedCaseStatus','N');

INSERT INTO XHB_SECURITY_GROUP_ROLE (GROUP_NAME,ROLE_NAME,IS_ENABLED,IS_ENABLED_BY_DEFAULT) VALUES ('CS Admin','XHBViewUnauthorisedCaseStatus','Y','Y');

COMMIT;

--Create new package
@xhb_unauthorised_cases_pkg_h.sql
@xhb_unauthorised_cases_pkg_b.sql
