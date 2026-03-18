CREATE OR REPLACE PROCEDURE TEST_GENERATE_PDDA_LOAD (
    p_total_records       IN NUMBER,
    p_records_per_batch   IN NUMBER
)
AS
    v_guid             VARCHAR2(36);
    v_type_id          XHB_PDDA_MESSAGE.PDDA_MESSAGE_TYPE_ID%TYPE;
    v_data_id          XHB_PDDA_MESSAGE.PDDA_MESSAGE_DATA_ID%TYPE;
    v_court_room_id    XHB_PDDA_MESSAGE.COURT_ROOM_ID%TYPE;
    v_batch_id         XHB_PDDA_BATCH.PDDA_BATCH_ID%TYPE;
BEGIN
    FOR i IN 1..p_total_records LOOP
        IF MOD(i - 1, p_records_per_batch) = 0 THEN
            SELECT xhb_pdda_batch_seq.NEXTVAL INTO v_batch_id FROM dual;

            INSERT INTO xhb_pdda_batch (
                pdda_batch_id,
                no_of_records_in_batch,
                batch_opened_datetime,
                batch_closed_datetime,
                batch_status_id,
                batch_no_resends
            ) VALUES (
                v_batch_id,
                p_records_per_batch,
                SYSDATE,
                SYSDATE,
                4,
                1
            );
        END IF;

        SELECT LOWER(RAWTOHEX(SYS_GUID())) INTO v_guid FROM dual;

        SELECT ref_pdda_message_type_id
        INTO v_type_id
        FROM (
            SELECT ref_pdda_message_type_id
            FROM xhb_ref_pdda_message_type
            ORDER BY DBMS_RANDOM.VALUE
        )
        WHERE ROWNUM = 1;

        SELECT pdda_message_data_id
        INTO v_data_id
        FROM (
            SELECT DISTINCT pdda_message_data_id
            FROM xhb_pdda_message
            WHERE pdda_message_data_id IS NOT NULL
            ORDER BY DBMS_RANDOM.VALUE
        )
        WHERE ROWNUM = 1;

        v_court_room_id := CASE WHEN DBMS_RANDOM.VALUE < 0.5 THEN 8156 ELSE NULL END;

        INSERT INTO xhb_pdda_message (
            pdda_message_id,
            court_id,
            court_room_id,
            pdda_message_guid,
            pdda_message_type_id,
            pdda_message_data_id,
            pdda_batch_id
        ) VALUES (
            xhb_pdda_message_seq.NEXTVAL,
            95,
            v_court_room_id,
            v_guid,
            v_type_id,
            v_data_id,
            v_batch_id
        );
    END LOOP;

    COMMIT;
END;
/