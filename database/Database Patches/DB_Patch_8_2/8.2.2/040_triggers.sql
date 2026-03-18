CREATE OR REPLACE TRIGGER xhb_rs_case_bir_tr
    before insert on xhb_rs_case
    for each row
begin
    if :new.rs_case_id is null then
        select xhb_rs_case_seq.nextval
        into  :new.rs_case_id
        from   dual;
    end if;


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




CREATE OR REPLACE TRIGGER xhb_pros_ref_sol_firm_bir_tr
    before insert on xhb_prosecutor_ref_sol_firm
    for each row
begin
    if :new.prosecutor_ref_sol_firm_id is null then
        select xhb_pros_ref_sol_firm_seq.nextval
        into  :new.prosecutor_ref_sol_firm_id
        from   dual;
    end if;

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



CREATE OR REPLACE TRIGGER xhb_indictment_history_bir_tr
    before insert on xhb_indictment_history
    for each row
begin
    if :new.indictment_history_id is null then
        select xhb_indictment_history_seq.nextval
        into  :new.indictment_history_id
        from   dual;
    end if;

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




CREATE OR REPLACE TRIGGER xhb_bail_application_bir_tr
    before insert on xhb_bail_application
    for each row
begin
    if :new.bail_application_id is null then
        select xhb_bail_application_seq.nextval
        into  :new.bail_application_id
        from   dual;
    end if;

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