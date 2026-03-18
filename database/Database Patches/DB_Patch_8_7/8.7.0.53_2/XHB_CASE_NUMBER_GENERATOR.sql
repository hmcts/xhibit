create or replace package body XHB_CASE_NUMBER_GENERATOR is

PROCEDURE get_case_number(p_case_number OUT xhb_case_number_seq_generator.current_sequence%TYPE ,
			  p_court_id IN NUMBER,
			  p_case_type IN VARCHAR2) IS

BEGIN
  -- CTX-3656 - ADDING dbms_output to log incoming parameters
  dbms_output.put_line('get_case_number for p_court_id => '||p_court_id||' , p_case_type => '||p_case_type);
  
	UPDATE xhb_case_number_seq_generator
	SET current_sequence = 1 + (
			Select current_sequence from xhb_case_number_seq_generator where court_id=p_court_id and case_type=p_case_type)
  where court_id = p_court_id and case_type=p_case_type
  RETURNING current_sequence INTO p_case_number;
  
    -- CTX-3656 - ADDING dbms_output to log outgoing parameter
  dbms_output.put_line('generated case_number for p_court_id => '||p_court_id||', p_case_type => '||p_case_type||' is '||p_case_number);

END get_case_number;

end XHB_CASE_NUMBER_GENERATOR;
/
