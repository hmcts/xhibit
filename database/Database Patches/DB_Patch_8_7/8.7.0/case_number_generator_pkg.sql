--------------------------------------------------------
--  DDL for Package XHB_CASE_NUMBER_GENERATOR
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "XHIBIT"."XHB_CASE_NUMBER_GENERATOR" is

  -- Author  : WALTERSN
  -- Created : 16/05/2017 10:19:29
  -- Purpose : Generation of case numbers dependant on court id and case type
  

-- Procedure which returns the next case number in the sequence from
-- Xhb_case_number_seq_generator table and increments by 1.  
  PROCEDURE get_case_number(p_case_number OUT xhb_case_number_seq_generator.current_sequence%TYPE ,
			  p_court_id IN NUMBER,
			  p_case_type IN VARCHAR2);

end XHB_CASE_NUMBER_GENERATOR;

/

--------------------------------------------------------
--  DDL for Package Body XHB_CASE_NUMBER_GENERATOR
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "XHIBIT"."XHB_CASE_NUMBER_GENERATOR" is

PROCEDURE get_case_number(p_case_number OUT xhb_case_number_seq_generator.current_sequence%TYPE ,
			  p_court_id IN NUMBER,
			  p_case_type IN VARCHAR2) IS

BEGIN
	UPDATE xhb_case_number_seq_generator
	SET current_sequence = 1 + (
			Select current_sequence from xhb_case_number_seq_generator where court_id=p_court_id and case_type=p_case_type)
	where court_id = p_court_id and case_type=p_case_type
  RETURNING current_sequence INTO p_case_number;
	
END get_case_number;

end XHB_CASE_NUMBER_GENERATOR;

/
