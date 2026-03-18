/* This script will be used to update the CJIT database from 0_4q (in CVS) to the latest level - 0_4v */

-- Create indexes for all of the foreign keys on the CJIT database, this will
-- prevent full-table locks when deleting from related tables...
CREATE INDEX CJI_AHM_DEVICE_ID_FK_IDX ON CJI_AHM(DEVICE_ID);
CREATE INDEX CJI_AHM_STATUS_ID_FK_IDX ON CJI_AHM(STATUS_ID);
CREATE INDEX CJI_AHM_AGG_STAT_ST_ID_FK_IDX ON CJI_AHM_AGG_STATUS(STATUS_ID);
CREATE INDEX CJI_AHM_REPLY_STATUS_ID_FK_IDX ON CJI_AHM_REPLY(STATUS_ID);
CREATE INDEX CJI_AHM_REPLY_MSG_ID_FK_IDX ON CJI_AHM_REPLY(MESSAGE_ID);
CREATE INDEX CJI_BITS_MSG_DOC_ID_FK_IDX ON CJI_BITS_MESSAGE(DOCUMENT_ID);
CREATE INDEX CJI_BITS_MSG_REQUEST_ID_FK_IDX ON CJI_BITS_MESSAGE(REQUEST_ID);
CREATE INDEX CJI_BITS_MSG_OP_TYPE_ID_FK_IDX ON CJI_BITS_MESSAGE(OPERATION_TYPE_ID);
CREATE INDEX CJI_CJIP_MSG_EVENT_ID_FK_IDX ON CJI_CJIP_MESSAGE(EVENT_ID);
CREATE INDEX CJI_CJIP_MSG_DOC_ID_FK_IDX ON CJI_CJIP_MESSAGE(DOCUMENT_ID);
CREATE INDEX CJI_CJIP_MSG_STATUS_ID_FK_IDX ON CJI_CJIP_MESSAGE(STATUS_ID);
CREATE INDEX CJI_CJIP_RQST_STATUS_ID_FK_IDX ON CJI_CJIP_REQUEST(STATUS_ID);
CREATE INDEX CJI_DOC_DOC_TYPE_ID_FK_IDX ON CJI_DOCUMENT(DOCUMENT_TYPE_ID);
CREATE INDEX CJI_DOC_STATUS_ID_FK_IDX ON CJI_DOCUMENT(STATUS_ID);
CREATE INDEX CJI_DOC_SEC_CJO_ROLE_ID_FK_IDX ON CJI_DOCUMENT_SECURITY(CJO_ROLE_ID);
CREATE INDEX CJI_DOC_SEC_DOC_TYPE_ID_FK_IDX ON CJI_DOCUMENT_SECURITY(DOCUMENT_TYPE_ID);
CREATE INDEX CJI_EVENT_OP_ID_FK_IDX ON CJI_EVENT(OPERATION_ID);
CREATE INDEX CJI_EVENT_STATUS_ID_FK_IDX ON CJI_EVENT(STATUS_ID);

-- Create unique constraints for the status text fields on the lookup tables to prevent
-- duplicates and provide the CBO additional information about the data,,,
CREATE UNIQUE INDEX CJI_AHM_REPLY_STAT_TXT_IDX ON CJI_AHM_REPLY_STATUS(STATUS_TEXT);
CREATE UNIQUE INDEX CJI_AHM_STAT_TXT_IDX ON CJI_AHM_STATUS(STATUS_TEXT);
CREATE UNIQUE INDEX CJI_CJIP_MESSAGE_STAT_TXT_IDX ON CJI_CJIP_MESSAGE_STATUS(STATUS_TEXT);
CREATE UNIQUE INDEX CJI_CJIP_REQUEST_STAT_TXT_IDX ON CJI_CJIP_REQUEST_STATUS(STATUS_TEXT);
CREATE UNIQUE INDEX CJI_DOCUMENT_STAT_TXT_IDX ON CJI_DOCUMENT_STATUS(STATUS_TEXT);
CREATE UNIQUE INDEX CJI_EVENT_STAT_TXT_IDX ON CJI_EVENT_STATUS(STATUS_TEXT);

/*
 * Procedure for deleting document records from vaious CJI tables
 * that are marked with a status of Delete.  Called from an Oracle job
 * at a predefined time (3 in the morning).
 */
CREATE OR REPLACE PACKAGE cji_delete_documents_pkg AS

  PROCEDURE delete_cji_documents;

END cji_delete_documents_pkg;
/
show errors;

CREATE OR REPLACE PACKAGE BODY cji_delete_documents_pkg
AS
    /*
     * Procedure for deleting all of the cji documents and events that
     * have been marked for deletion.
     */
    PROCEDURE delete_cji_documents
    IS
        l_ahm_delete_id          CJI_AHM_STATUS.status_id%TYPE;
        l_ahm_reply_delete_id    CJI_AHM_REPLY_STATUS.status_id%TYPE;
        l_cjip_message_delete_id CJI_CJIP_MESSAGE_STATUS.status_id%TYPE;
        l_cjip_request_delete_id CJI_CJIP_REQUEST_STATUS.status_id%TYPE;
        l_document_delete_id     CJI_DOCUMENT_STATUS.status_id%TYPE;
        l_event_delete_id        CJI_EVENT_STATUS.status_id%TYPE;
    BEGIN
        -- Acquire all of the Delete status event ids, this will do a cartesian
        -- join between all of the listed tables, but only one row should be returned.
        SELECT cas.status_id,             
               cars.status_id,     
               ccms.status_id, 
               ccrs.status_id,
               cds.status_id,    
               ces.status_id
        INTO   l_ahm_delete_id,
               l_ahm_reply_delete_id,
               l_cjip_message_delete_id,
               l_cjip_request_delete_id,
               l_document_delete_id,
               l_event_delete_id
        FROM   CJI_AHM_STATUS cas,
               CJI_AHM_REPLY_STATUS cars,
               CJI_CJIP_MESSAGE_STATUS ccms,
               CJI_CJIP_REQUEST_STATUS ccrs,
               CJI_DOCUMENT_STATUS cds,
               CJI_EVENT_STATUS ces
        WHERE  cas.status_text  = 'Delete'
        AND    cars.status_text = 'Delete'
        AND    ccms.status_text = 'Delete'
        AND    ccrs.status_text = 'Delete'
        AND    cds.status_text  = 'Delete'
        AND    ces.status_text  = 'Delete';

        -------------------------------------------------------------------------
        -- Delete all of the ad-hoc messages marked for deletion...
        -------------------------------------------------------------------------

        DELETE FROM CJI_AHM_REPLY car1 WHERE EXISTS (
        SELECT 1
        FROM   CJI_AHM_REPLY car,
               CJI_AHM ca
        WHERE  ca.message_id(+) = car.message_id
        AND    car.ROWID = car1.ROWID
        AND    (car.status_id = l_ahm_reply_delete_id
        OR      ca.status_id  = l_ahm_delete_id));

        DELETE FROM CJI_AHM WHERE status_id = l_ahm_delete_id;

        -------------------------------------------------------------------------
        -- Now delete from the other tables those that are marked for deletion...
        -------------------------------------------------------------------------

        DELETE FROM CJI_BITS_MESSAGE cbm1 WHERE EXISTS ( 
        SELECT 1
        FROM   CJI_BITS_MESSAGE cbm,
               CJI_DOCUMENT cd,
               CJI_CJIP_REQUEST cjr
        WHERE  cd.document_id = cbm.document_id
        AND    cbm.request_id = cjr.request_id(+)
        AND    cbm.ROWID = cbm1.ROWID
        AND    (cd.status_id  = l_document_delete_id
        OR      cjr.status_id = l_cjip_request_delete_id));

        DELETE FROM CJI_CJIP_MESSAGE ccm1 WHERE EXISTS (
        SELECT 1
        FROM   CJI_CJIP_MESSAGE ccm,
               CJI_DOCUMENT cd,
               CJI_EVENT ce
        WHERE  ccm.document_id = cd.document_id(+)
        AND    ccm.event_id = ce.event_id(+)
        AND    ccm.ROWID = ccm1.ROWID
        AND    (ccm.status_id = l_cjip_message_delete_id
        OR      cd.status_id  = l_document_delete_id
        OR      ce.status_id  = l_event_delete_id));

        DELETE FROM CJI_CJIP_REQUEST WHERE status_id = l_cjip_request_delete_id;

        DELETE FROM CJI_EVENT WHERE status_id = l_event_delete_id;

        DELETE FROM CJI_DOCUMENT WHERE status_id = l_document_delete_id; 

        -------------------------------------------------------------------------
        -- Now delete all of the orphaned rows in the CJI_EVENT and CJI_CJIP_REQUEST tables...
        -------------------------------------------------------------------------

        DELETE FROM CJI_EVENT ce
        WHERE NOT EXISTS (SELECT 1
                          FROM CJI_CJIP_MESSAGE ccm
                          WHERE ccm.event_id = ce.event_id);

        DELETE FROM CJI_CJIP_REQUEST ccr
        WHERE NOT EXISTS (SELECT 1
                          FROM CJI_BITS_MESSAGE cbm
                          WHERE cbm.request_id = ccr.request_id);

        COMMIT;
    END delete_cji_documents;
END cji_delete_documents_pkg;
/
show errors;

VARIABLE v_job_no NUMBER;

BEGIN
    -- Remove all previously created jobs for the deletion of documents...
    FOR rec IN (SELECT job FROM user_jobs WHERE what = 'CJI_DELETE_DOCUMENTS_PKG.DELETE_CJI_DOCUMENTS;') LOOP
        DBMS_JOB.remove(rec.job);
    END LOOP;

    -- Submit a new job...
    DBMS_JOB.SUBMIT(:v_job_no,
                    'CJI_DELETE_DOCUMENTS_PKG.DELETE_CJI_DOCUMENTS;',
                    trunc(sysdate) + (3 / 24) + 1,
                    'trunc(sysdate) + (3 / 24) + 1');
    COMMIT;
END;
/

PRINT v_job_no;
