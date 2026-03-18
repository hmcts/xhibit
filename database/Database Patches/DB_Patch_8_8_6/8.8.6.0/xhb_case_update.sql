/*    ------------------------------------------------------------------
*     UPDATE XHB_CASE TABLE
*     RECREATE TRIGGERS
*/    ------------------------------------------------------------------

ALTER TABLE XHB_CASE ADD (
  DAR_RETENTION_POLICY_ID   NUMBER(8),
  CRP_LAST_UPDATE_DATE      DATE
);

ALTER TABLE AUD_CASE ADD (
  DAR_RETENTION_POLICY_ID   NUMBER(8),
  CRP_LAST_UPDATE_DATE      DATE
);

-- Create/Recreate foreign key constraints
ALTER TABLE XHB_CASE ADD (
	CONSTRAINT XC_DAR_RETENTION_POLICY_FK
	FOREIGN KEY(DAR_RETENTION_POLICY_ID)
	REFERENCES XHB_DAR_RETENTION_POLICY(DAR_RETENTION_POLICY_ID));

/*    ------------------------------------------------------------------
*     RECREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_case_ai_tr.sql;
@xhb_case_bur_tr.sql;

COMMIT;