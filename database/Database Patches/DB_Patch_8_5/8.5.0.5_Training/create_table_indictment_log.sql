/******************************************************************************
   NAME:       create_table_indictment_log.sql
   PURPOSE:    Holds indictment log comments.
               RFC 2867.
   REVISIONS:
   Ver        Date               Author           Description
   ---------  ----------       ---------------  --------------------------------
----
   1.0        20-May-2011       D Field         Creation
*******************************************************************************/



DROP TABLE ops$crest.indictment_log;


CREATE TABLE ops$crest.indictment_log
(case_type VARCHAR2(1) NOT NULL
,case_no   NUMBER      NOT NULL
,seq_no    NUMBER      NOT NULL
,ind_comments VARCHAR2(78) NOT NULL
)
TABLESPACE crst_data;

ALTER TABLE ops$crest.indictment_log ADD CONSTRAINT indictment_log_pk PRIMARY KE
Y (case_type,case_no,seq_no) USING INDEX TABLESPACE crst_index;

CREATE PUBLIC SYNONYM indictment_log FOR ops$crest.indictment_log;

GRANT ALL ON ops$crest.indictment_log TO PUBLIC;

INSERT INTO hk_stats
(table_name
,export_Level
,deleted_since_export
,grand_total_deleted
,deleted_last_hk_run
)
VALUES
('INDICTMENT_LOG'
,99999999
,0
,0
,0
);

COMMIT;
