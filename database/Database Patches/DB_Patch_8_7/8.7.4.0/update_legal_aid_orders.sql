UPDATE xhb_legal_aid_order SET obs_ind = 'Y' WHERE date_of_revocation IS NOT NULL;
COMMIT;