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
