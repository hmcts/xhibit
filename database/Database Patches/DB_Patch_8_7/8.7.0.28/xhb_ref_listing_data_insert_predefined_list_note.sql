
UPDATE XHB_REF_LISTING_DATA SET OBS_IND = 'Y' WHERE REF_DATA_TYPE = 'PREDEFINED_LIST_NOTE';

INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under s39, CYPA 1933', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under s39, CYPA 1933 (witnesses only)', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under Contempt of Court Act 1981', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Orders made under s39, CYPA 1933 '||'&'||' Contempt of Court Act 1981', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Orders made under s39, CYPA 1933 (witnesses only) '||'&'||' Contempt of Court Act 1981', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under s45, Youth Justice and Criminal Evidence Act 1999', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under s45a, Youth Justice and Criminal Evidence Act 1999', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under s46, Youth Justice and Criminal Evidence Act 1999', 'N');
INSERT INTO XHB_REF_LISTING_DATA (ref_listing_data_id, ref_data_type, ref_data_value, obs_ind) VALUES (XHB_REF_LISTING_DATA_SEQ.NEXTVAL, 'PREDEFINED_LIST_NOTE', 'Order made under s49, Children and Young Persons Act 1933', 'N');



 COMMIT;