--------------------------------------------------------
--  DDL for Package XHB_CASE_GROUP_GENERATOR
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "XHIBIT"."XHB_CASE_GROUP_GENERATOR" is

  -- Author  : m.newman
  -- Created : 08/06/2018 14:04
  -- Purpose : To allow generation of case group numbers
  

-- Procedure which returns the next case group number in the sequence from
-- XHB_CASE_GROUP_SEQ_GENERATOR table and increments by 1.  
  PROCEDURE get_case_group_number(p_case_group_number OUT xhb_case_group_seq_generator.current_sequence%TYPE ,
			  p_court_id IN NUMBER);

end XHB_CASE_GROUP_GENERATOR;

/

--------------------------------------------------------
--  DDL for Package Body XHB_CASE_GROUP_GENERATOR
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "XHIBIT"."XHB_CASE_GROUP_GENERATOR" is

PROCEDURE get_case_group_number(p_case_group_number OUT xhb_case_group_seq_generator.current_sequence%TYPE ,
			  p_court_id IN NUMBER) IS

BEGIN
	UPDATE xhb_case_group_seq_generator
	SET current_sequence = 1 + (
			Select current_sequence from xhb_case_group_seq_generator where court_id=p_court_id)
	where court_id = p_court_id
  RETURNING current_sequence INTO p_case_group_number;
	
END get_case_group_number;

end XHB_CASE_GROUP_GENERATOR;

/
