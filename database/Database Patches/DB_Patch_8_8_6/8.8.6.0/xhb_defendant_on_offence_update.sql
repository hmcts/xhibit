/*    ------------------------------------------------------------------
*     UPDATE XHB_DEFENDANT_ON_OFFENCE TABLE
*     RECREATE TRIGGERS
*/    ------------------------------------------------------------------

ALTER TABLE XHB_DEFENDANT_ON_OFFENCE ADD (
  DAR_RETENTION_POLICY_ID   NUMBER(8)
);

ALTER TABLE AUD_DEFENDANT_ON_OFFENCE ADD (
  DAR_RETENTION_POLICY_ID   NUMBER(8)
);

-- Create/Recreate foreign key constraints
ALTER TABLE XHB_DEFENDANT_ON_OFFENCE ADD (
	CONSTRAINT XDOO_REF_RETENTION_POLICIES_FK
	FOREIGN KEY(DAR_RETENTION_POLICY_ID)
	REFERENCES XHB_DAR_RETENTION_POLICY(DAR_RETENTION_POLICY_ID));

/*    ------------------------------------------------------------------
*     RECREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_defendantonoffence_bur_tr.sql;

COMMIT;