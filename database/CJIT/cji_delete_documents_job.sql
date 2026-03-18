/*
 * Script to set up the job for deleting documents marked
 * for deletion via the status flag.  The job needs to
 * be scheduled for 03:00am.
 *
 * The job calls the delete_cji_documents procedure in the
 * cji_delete_documents_pkg package.
 */

VARIABLE v_job_no NUMBER;

BEGIN
    -- Remove all previously created jobs for the deletion of documents...
    FOR rec IN (SELECT job FROM dba_jobs WHERE what = 'CJI_DELETE_DOCUMENTS_PKG.DELETE_CJI_DOCUMENTS;') LOOP
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