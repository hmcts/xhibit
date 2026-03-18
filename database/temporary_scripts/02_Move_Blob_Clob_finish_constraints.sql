-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                 Add not null constraints where appropriate                --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------


-- VERY IMPORTANT NOTE:::
-- If this script fails then re-run the 01 script must be run again to copy over all of the data...



-- new blob/clob columns must be changed to not nullable where appropriate...
ALTER TABLE XHB_FORMATTING   MODIFY (XML_DOCUMENT_CLOB_ID NOT NULL);
ALTER TABLE XHB_XML_DOCUMENT MODIFY (XML_DOCUMENT_CLOB_ID NOT NULL);
ALTER TABLE XHB_EMAIL        MODIFY (MIME_BODY_BLOB_ID    NOT NULL);





-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                 Create the foreign key references                         --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- Blobs...
ALTER TABLE XHB_EMAIL            ADD CONSTRAINT XHB_EMAIL_MIME_BODY_BLOB_ID_FK FOREIGN KEY (mime_body_blob_id)          REFERENCES XHB_BLOB(blob_id);
ALTER TABLE XHB_DOCUMENT_CONTROL ADD CONSTRAINT XHB_DOC_CONT_FORMAT_BLOB_ID_FK FOREIGN KEY (formatted_document_blob_id) REFERENCES XHB_BLOB(blob_id);
ALTER TABLE XHB_FORMATTING       ADD CONSTRAINT XHB_FORMAT_FORMAT_BLOB_ID_FK   FOREIGN KEY (formatted_document_blob_id) REFERENCES XHB_BLOB(blob_id);

-- Clobs...
ALTER TABLE XHB_FORMATTING    ADD CONSTRAINT XHB_FORMAT_XML_DOC_CLOB_ID_FK  FOREIGN KEY (xml_document_clob_id) REFERENCES XHB_CLOB(clob_id);
ALTER TABLE XHB_INTERNET_HTML ADD CONSTRAINT XHB_INT_HTML_HTML_CLOB_ID_FK   FOREIGN KEY (html_clob_id)         REFERENCES XHB_CLOB(clob_id);
ALTER TABLE XHB_XML_DOCUMENT  ADD CONSTRAINT XHB_XML_DOC_XML_DOC_CLOB_ID_FK FOREIGN KEY (xml_document_clob_id) REFERENCES XHB_CLOB(clob_id);





-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                 Create the foreign key indexes                            --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
CREATE INDEX EMAIL_BLOB_FK_IDX            ON XHB_EMAIL(mime_body_blob_id)                     TABLESPACE XHB_LOB_IDX;
CREATE INDEX DOCUMENT_CONTROL_BLOB_FK_IDX ON XHB_DOCUMENT_CONTROL(formatted_document_blob_id) TABLESPACE XHB_LOB_IDX;
CREATE INDEX FORMATTING_BLOB_FK_IDX       ON XHB_FORMATTING(formatted_document_blob_id)       TABLESPACE XHB_LOB_IDX;

CREATE INDEX FORMATTING_CLOB_FK_IDX    ON XHB_FORMATTING(xml_document_clob_id)   TABLESPACE XHB_LOB_IDX;
CREATE INDEX INTERNET_HTML_CLOB_FK_IDX ON XHB_INTERNET_HTML(html_clob_id)        TABLESPACE XHB_LOB_IDX;
CREATE INDEX XML_DOCUMENT_CLOB_FK_IDX  ON XHB_XML_DOCUMENT(xml_document_clob_id) TABLESPACE XHB_LOB_IDX;





-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                 Allow old audit columns to be nullable                    --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------


ALTER TABLE AUD_FORMATTING MODIFY (XML_DOCUMENT NULL);
ALTER TABLE AUD_XML_DOCUMENT MODIFY (XML_DOCUMENT NULL);
ALTER TABLE AUD_EMAIL MODIFY (MIME_BODY NULL);
