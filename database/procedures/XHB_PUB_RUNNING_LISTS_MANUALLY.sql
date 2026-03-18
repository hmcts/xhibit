/**
 * Description: This procedure can be used to publish the running lists for all
 * the active courts. It simulates what happens when the user presses the Publish
 * button except its for all courts.
 */
create or replace procedure XHB_PUB_RUNNING_LISTS_MANUALLY
IS
  lprev_running_list_id number;
  
  cursor get_all_courts_cur is
    select court_id from xhb_court where obs_ind='N';
    
  lcourt get_all_courts_cur%ROWTYPE;
  running_list_cases_crt SYS_REFCURSOR;
  c_record SYS_REFCURSOR;
  no_cases_crt number;
  v_next_pub_running_list_id number;
  l_validation_id VARCHAR(50);
  v_return_xml CLOB;
  v_list_court XHB_COURT%ROWTYPE;
  v_unique_id VARCHAR(50);
  v_today_date VARCHAR2(10);
  v_end_date VARCHAR2(20);
  v_list_name VARCHAR2(100);
  v_document_title VARCHAR2(255);
  v_document_name VARCHAR2(50);
  v_document_description VARCHAR2(200);
  
begin

  open get_all_courts_cur;
  
  loop
    fetch get_all_courts_cur into lcourt;
    exit when get_all_courts_cur%NOTFOUND;
    
    -- Get the max previous running list id
    select max(pub_running_list_id) into lprev_running_list_id from xhb_pub_running_list where court_id=lcourt.court_id;
    dbms_output.put_line('Courtid: '||lcourt.court_id||' PrevRunningListId: '||lprev_running_list_id);
    
    -- Any cases for this court?
    select count(*) into no_cases_crt
    FROM XHB_CASE xc,
        XHB_DEFENDANT_ON_CASE xdoc,
        XHB_DEFENDANT xd,
        XHB_REF_COURT xcrt
        WHERE xc.COURT_ID = lcourt.court_id
        AND xhb_report_pkg.is_valid_case_status(xc.case_status) = 'Y'
        AND xc.CASE_ID = xdoc.CASE_ID (+)
        AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID (+)
        AND NVL(xdoc.OBS_IND (+),'N') <> 'Y'
        AND xc.REF_COURT_ID = xcrt.REF_COURT_ID(+)
        AND ((lprev_running_list_id = 0 AND xc.PUB_RUNNING_LIST_ID IS NULL)
        OR (lprev_running_list_id > 0 AND xc.PUB_RUNNING_LIST_ID = lprev_running_list_id));
        
      -- Check to see if there any cases for this court, and therefore a running list can be published  
      if no_cases_crt > 0 then
        -- Create a new entry in XHB_PUB_RUNNING_LIST for this running list for this court
        SELECT XHB_PUB_RUNNING_LIST_SEQ.nextval INTO v_next_pub_running_list_id FROM dual;
        INSERT INTO XHB_PUB_RUNNING_LIST (PUB_RUNNING_LIST_ID, COURT_ID,PUBLISHED_DATE) VALUES (v_next_pub_running_list_id, lcourt.court_id, sysdate);
    
        -- Get all the running list cases for this court and process each one individually
        -- Functionality replicated from the xhb_report_pkg.get_prlis_report procedure
        for c_record in (SELECT /* get_prlis_report */
               xc.CASE_ID
            FROM XHB_CASE xc,
            XHB_DEFENDANT_ON_CASE xdoc,
            XHB_DEFENDANT xd,
            XHB_REF_COURT xcrt
            WHERE xc.COURT_ID = lcourt.court_id
            AND xhb_report_pkg.is_valid_case_status(xc.case_status) = 'Y'
            AND xc.CASE_ID = xdoc.CASE_ID (+)
            AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID (+)
            AND NVL(xdoc.OBS_IND (+),'N') <> 'Y'
            AND xc.REF_COURT_ID = xcrt.REF_COURT_ID(+)
            AND ((lprev_running_list_id = 0 AND xc.PUB_RUNNING_LIST_ID IS NULL)
            OR (lprev_running_list_id > 0 AND xc.PUB_RUNNING_LIST_ID = lprev_running_list_id)))
        loop
          dbms_output.put_line('case_id'||c_record.case_id);
          
          -- Functionality replicated from the xhb_report_pkg.publish_running_list procedure to update all cases with the new pub_running_list_id
          -- and send the data to the portal
          UPDATE XHB_CASE SET PUB_RUNNING_LIST_ID = v_next_pub_running_list_id WHERE CASE_ID = TO_NUMBER(c_record.case_id);
        
          SELECT XHB_VALIDATION_SEQ.NEXTVAL INTO l_validation_id FROM DUAL;
          v_unique_id := 'CSDRL' || LPAD(l_validation_id, 15, 0);
          dbms_output.put_line('v_unique_id'||v_unique_id);
          v_return_xml:= XHB_GET_XML_REPORTS.GET_RUNNING_LIST(lcourt.court_id, v_next_pub_running_list_id, v_unique_id);
        
          SELECT * INTO v_list_court FROM XHB_COURT xc WHERE xc.COURT_ID = lcourt.court_id;
          v_today_date := TO_CHAR(sysdate, 'DD/MM/YY');
          v_end_date := TO_CHAR(sysdate, 'MON DD, YYYY');
          v_list_name := 'Running List';
          v_document_title:= v_list_name || ' ending ' || v_end_date;
          v_document_name := 'RL ' || v_today_date || ' ending ' || v_end_date;
          v_document_description := v_list_name || ', ' || INITCAP(v_list_court.COURT_NAME) || ' ' || INITCAP(v_list_court.COURT_PREFIX)
                                    || ' on ' || v_today_date || ' ending ' || v_end_date;
          XHB_LIST_DISTRIBUTION_PKG.STORE_XML_DOCUMENT(v_return_xml, v_document_title, trunc(sysdate), 'RL', v_list_court.COURT_ID, v_list_court.COUNTRY, v_list_court.LANGUAGE);
          XHB_LIST_DISTRIBUTION_PKG.STORE_VALIDATION_DOCUMENT(v_return_xml, v_document_name, v_document_description, 'RL', l_validation_id, v_unique_id, 17, v_list_court.CREST_COURT_ID);
          
          XHB_REPORT_PKG.UPDATE_REPORT_LOG(lcourt.court_id, 'PRLIS', 'Running List');
        end loop;
    
      end if; -- If there are cases for this court for the running lsti
    
  end loop;
  
  close get_all_courts_cur; -- close cursor
  
end XHB_PUB_RUNNING_LISTS_MANUALLY; -- end procedure