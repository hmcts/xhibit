INSERT INTO XHB_REF_AGGRAVATING_REASONS
(REASON_DESCRIPTION)
SELECT subqry.* FROM (
SELECT 'Aggravated due to assaults on workers providing a public service,'||
       ' performing a public duty or providing a service to the public' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated by a terrorist connection'                      REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated due to emergency worker offence'                REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated due to hostility based on disability of victim' REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated due to sexual orientation'                      REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated due to sexual orientation of victim'            REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated due to transgender'                             REASON_DESCRIPTION FROM DUAL UNION
SELECT 'Aggravated due to transgender of victim'                   REASON_DESCRIPTION FROM DUAL
) subqry 
WHERE NOT EXISTS (SELECT NULL FROM XHB_REF_AGGRAVATING_REASONS x1 
                   WHERE x1.REASON_DESCRIPTION = subqry.REASON_DESCRIPTION AND ROWNUM = 1);

commit;
/