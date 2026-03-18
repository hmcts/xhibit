/*should fail as cpp formatting id is null*/
INSERT INTO XHB_CPP_FORMATTING_MERGE(FORMATTING_ID,COURT_ID,LANGUAGE) 
VALUES (828872,81,'cy');

/*should fail as cpp formatting id doesn't match anything in that table*/
INSERT INTO XHB_CPP_FORMATTING_MERGE(CPP_FORMATTING_ID,FORMATTING_ID,COURT_ID,LANGUAGE) 
VALUES (0,828872,81,'cy');

/*should fail as formatting id is null*/
INSERT INTO XHB_CPP_FORMATTING_MERGE(FORMATTING_ID,COURT_ID,LANGUAGE) 
VALUES (3,81,'cy');

/*should fail as formatting id doesn't match anything in xhb_formatting*/
INSERT INTO XHB_CPP_FORMATTING_MERGE(CPP_FORMATTING_ID,FORMATTING_ID,COURT_ID,LANGUAGE) 
VALUES (3,0,81,'cy');


/*should fail as court id is null*/
INSERT INTO XHB_CPP_FORMATTING_MERGE(CPP_FORMATTING_ID,FORMATTING_ID,LANGUAGE) 
VALUES (3,828872,'cy');

/*should fail as court id doesn't match anything in xhb_court */
INSERT INTO XHB_CPP_FORMATTING_MERGE(CPP_FORMATTING_ID,FORMATTING_ID,COURT_ID,LANGUAGE) 
VALUES (3,828872,0,'cy');

/*should fail as language is null*/
INSERT INTO XHB_CPP_FORMATTING_MERGE(CPP_FORMATTING_ID,FORMATTING_ID,COURT_ID) 
VALUES (3,828872,81);


/*should fail as random id entered into document clob id */
INSERT INTO XHB_CPP_FORMATTING_MERGE(CPP_FORMATTING_ID,FORMATTING_ID,COURT_ID,LANGUAGE,XHIBIT_CLOB_ID) 
VALUES (3,828872,81,'cy',0);