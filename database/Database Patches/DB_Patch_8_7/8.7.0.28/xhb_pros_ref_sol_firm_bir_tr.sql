create or replace TRIGGER "XHIBIT".xhb_pros_ref_sol_firm_bir_tr
    before insert on xhb_prosecutor_ref_sol_firm
    for each row

	DECLARE v_prosrefsolfirmid NUMBER;

begin
    if :new.prosecutor_ref_sol_firm_id is null then
        select xhb_pros_ref_sol_firm_seq.nextval
        into  v_prosrefsolfirmid
        from   dual;
		
		:NEW.prosecutor_ref_sol_firm_id :=v_prosrefsolfirmid;
		ELSE
			v_prosrefsolfirmid := :NEW.prosecutor_ref_sol_firm_id;
    end if;
	
	IF :NEW.CREST_CPF_ID IS NULL THEN
    :NEW.CREST_CPF_ID :=v_prosrefsolfirmid;
  END IF;

    IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
        (:NEW.CREATED_BY IS NULL)) THEN

        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
               SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :NEW.LAST_UPDATED_BY,
               :NEW.CREATED_BY
        FROM   DUAL;

    END IF;

    SELECT SYSDATE,
           SYSDATE,
           1
    INTO   :NEW.LAST_UPDATE_DATE,
           :NEW.CREATION_DATE,
           :NEW.VERSION
    FROM   DUAL;
end;
/