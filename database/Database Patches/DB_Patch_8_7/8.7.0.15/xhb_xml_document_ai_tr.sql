/*CTX-630*/
CREATE OR REPLACE TRIGGER XHB_XML_DOCUMENT_AI_TR
 AFTER INSERT ON XHB_XML_DOCUMENT
 REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
 
 DECLARE
 
  BEGIN
   IF :NEW.document_type IN ('DL','RL','DLP','FL','WL')
    THEN
     INSERT INTO XHB_COURTEL_LIST (courtel_list_id
                                  ,xml_document_id
                                  )
     VALUES (XHB_COURTEL_list_seq.nextval
            ,:NEW.xml_document_id
            );
   END IF; 
  END xhb_xml_document_ai_tr;
/