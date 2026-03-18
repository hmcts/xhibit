insert into dar_darts_config (DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY)
values ('darts.resend.history_cutoff_limit_hrs', '24', sysdate, sysdate, 'DARTS', 'DARTS');

insert into dar_darts_config (DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY)
values ('darts.resend.max.message_batch', '500', sysdate, sysdate, 'DARTS', 'DARTS');

insert into dar_darts_config (DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY)
values ('darts.resend.dontrun_period', '07-19', sysdate, sysdate, 'DARTS', 'DARTS');

COMMIT;
/