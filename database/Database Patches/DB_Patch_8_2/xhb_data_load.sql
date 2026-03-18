rem XHB_VERSION
rem ===========
update xhb_version
set    schema_version = '8.2',
       updated_by     = 'DEVELOPMENT';

commit;



rem XHB_1745_DATA_MIGRATION
rem =======================
insert into
xhb_1745_data_migration_totals
select court_id,
       0,
       0,
       null
from   xhb_court;

commit;



rem XHB_REF_OFFENCE
rem ===============
insert into
xhb_ref_offence (
    offence_code,
    offence_desc,
    court_id
)
select 'ZZ99998',
       'Original Charge',
        court_id
from    xhb_court c
where  'ZZ99998' not in (
    select r.offence_code 
    from xhb_ref_offence    r 
    where r.court_id      = c.court_id
);

commit;