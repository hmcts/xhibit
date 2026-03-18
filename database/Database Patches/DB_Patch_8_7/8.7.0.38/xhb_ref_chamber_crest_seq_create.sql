/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_REF_CHAMBER
*/	------------------------------------------------------------------

-- Create sequence table for XHB_SITTING_ON_LIST
declare seqNo NUMBER;
doesExist NUMBER;
begin
	select MAX(CREST_CHAMBER_ID)+1 into seqNo from XHB_REF_CHAMBER;
	IF seqNo > 0 then		
		select count(*)
		INTO doesExist		
		from ALL_OBJECTS 
		where object_type='SEQUENCE' 
		and object_name='XHB_REF_CHAMBER_CREST_SEQ';
		
		if doesExist>0 then
			execute immediate 'DROP SEQUENCE XHB_REF_CHAMBER_CREST_SEQ';
		end if;
		execute immediate 'CREATE SEQUENCE XHB_REF_CHAMBER_CREST_SEQ MAXVALUE 999999999999999999999999999 START WITH ' || seqNo || 'INCREMENT BY 1 NOCACHE';
	end if;
end;
/
/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGER INSERT
*/    ------------------------------------------------------------------
@@xhb_ref_chamber_bir_tr.sql;

commit;
