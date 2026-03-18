CREATE TABLE XHB_COLLECTION_CENTRE (
       COLLECTION_CENTRE_ID     NUMBER(8)     NOT NULL,
       DISPLAY_NAME           	VARCHAR2(255) NULL,
       FULL_NAME           	VARCHAR2(255) NULL,
       DESCRIPTION        VARCHAR2(255) NULL,
       EMAIL_ADDRESS      VARCHAR2(255) NOT NULL,
       ADDRESS_ID	  NUMBER(8)     NOT NULL,
       VERSION            NUMBER(5)     NOT NULL,
       LAST_UPDATED_BY    VARCHAR2(30)  NOT NULL,
       CREATED_BY         VARCHAR2(30)  NOT NULL,
       CREATION_DATE      DATE          NOT NULL,
       LAST_UPDATE_DATE   DATE          NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 1M
                  NEXT 1M
                  PCTINCREASE 0);

-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_COLLECTION_CENTRE
  add constraint COLLECTION_CENTRE_PK primary key (COLLECTION_CENTRE_ID)
  using index 
  tablespace XHIBITD
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

ALTER TABLE XHB_COLLECTION_CENTRE ADD (CONSTRAINT XHB_COLL_CENTRE_ADDR_ID_FK FOREIGN KEY (ADDRESS_ID) REFERENCES XHB_ADDRESS (ADDRESS_ID));


-- Grant/Revoke object privileges 
grant select, insert, update on XHB_COLLECTION_CENTRE to PUBLIC;

create table AUD_COLLECTION_CENTRE (
       COLLECTION_CENTRE_ID     NUMBER(8)     NOT NULL,
       DISPLAY_NAME           	VARCHAR2(255) NULL,
       FULL_NAME           	VARCHAR2(255) NULL,
       DESCRIPTION        VARCHAR2(255) NULL,
       EMAIL_ADDRESS      VARCHAR2(255) NOT NULL,
       ADDRESS_ID	  NUMBER(8)     NOT NULL,
       VERSION            NUMBER(5)     NOT NULL,
       LAST_UPDATED_BY    VARCHAR2(30)  NOT NULL,
       CREATED_BY         VARCHAR2(30)  NOT NULL,
       CREATION_DATE      DATE          NOT NULL,
       LAST_UPDATE_DATE   DATE          NOT NULL,
	 insert_event            VARCHAR2(1)
)
tablespace AUDITD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

-- Grant/Revoke object privileges 
grant select, insert, update on AUD_COLLECTION_CENTRE to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_COLLECTION_CENTRE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_COLLECTION_CENTRE', 'AUD_COLLECTION_CENTRE', 'Y');


CREATE UNIQUE INDEX COLLECTION_CENTRE_IDX ON XHB_COLLECTION_CENTRE (COLLECTION_CENTRE_ID)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);

CREATE SEQUENCE XHB_COLLECTION_CENTRE_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;



CREATE OR REPLACE TRIGGER XHB_COLLECTION_CENTRE_BIR_TR
  BEFORE INSERT
  ON XHB_COLLECTION_CENTRE
  FOR EACH ROW

BEGIN

  IF :NEW.COLLECTION_CENTRE_ID IS NULL THEN

    SELECT XHB_COLLECTION_CENTRE_SEQ.NEXTVAL
    INTO   :NEW.COLLECTION_CENTRE_ID
    FROM   DUAL;

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

END;
/

CREATE OR REPLACE TRIGGER XHB_COLLECTION_CENTRE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COLLECTION_CENTRE
  FOR EACH ROW

DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COLLECTION_CENTRE') = 1) THEN

    INSERT INTO AUD_COLLECTION_CENTRE 
    VALUES (:old.COLLECTION_CENTRE_ID, 
            :old.DISPLAY_NAME, 
            :old.FULL_NAME, 
            :old.DESCRIPTION, 
            :old.EMAIL_ADDRESS,
            :old.ADDRESS,
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            l_trig_event);

  END IF;

END;
/
