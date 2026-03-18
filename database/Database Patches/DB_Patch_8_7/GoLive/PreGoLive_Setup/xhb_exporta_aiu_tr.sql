--ctx-2474 AFTER INSERT OR UPDATE of xhb_exporta
CREATE OR REPLACE TRIGGER xhb_exporta_aiu_tr 
AFTER INSERT OR UPDATE OF court_clerk_export,status_flag 
ON xhb_exporta 
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
BEGIN
 
 UPDATE xhb_def_hearing_record xdhr
 SET xdhr.forma_status = :NEW.status_flag
 ,   xdhr.forma_court_clerk = :NEW.court_clerk_export
 WHERE xdhr.hearing_id = :NEW.hearing_id;
 
 
END;
/