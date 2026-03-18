-------------------------------------------------------------------------------
--
-- This SQL sets the enabled column in XHB_SELECTORS to 'N'so that the 
-- listed message types are not sent to ExISS
-- 
-- Part of CCN1388
--
-------------------------------------------------------------------------------


-- The default is to enable ALL message types to be sent to ExISS
UPDATE XHB_SELECTORS SET ENABLED='Y' where SELECTOR_ID>0;

-- Now disable those message types which have been agreed not to be sent to ExSS
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Generic EXISS selector%';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Community Order';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Community Punishment Order';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Community Punishment Rehabilitation Order';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Imprisonment Order';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Remand Order';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Young Offender Order';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Prison Daily List';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Skeleton Schedule';
UPDATE XHB_SELECTORS SET ENABLED='N' where DESCRIPTION like 'Selector for Pre-Sentence Reports';

-- Add a selector record for the new message types

INSERT INTO XHB_SELECTORS (SELECTOR_ID,
                           SELECTOR,
                           DESCRIPTION,
                           ENABLED,
                           PRECEDENCE)
                           VALUES (NULL,'XHBTarget = ''EXISS'' AND XHBItemType = ''COC''','Selector for Custodial Order5044C','N',1);

INSERT INTO XHB_SELECTORS (SELECTOR_ID,
                           SELECTOR,
                           DESCRIPTION,
                           ENABLED,
                           PRECEDENCE)
                           VALUES (NULL,'XHBTarget = ''EXISS'' AND XHBItemType = ''COD''','Selector for Custodial Order5044D','N',1);

INSERT INTO XHB_SELECTORS (SELECTOR_ID,
                           SELECTOR,
                           DESCRIPTION,
                           ENABLED,
                           PRECEDENCE)
                           VALUES (NULL,'XHBTarget = ''EXISS'' AND XHBItemType = ''BWA''','Selector for Bench Warrant5061A','N',1);

INSERT INTO XHB_SELECTORS (SELECTOR_ID,
                           SELECTOR,
                           DESCRIPTION,
                           ENABLED,
                           PRECEDENCE)
                           VALUES (NULL,'XHBTarget = ''EXISS'' AND XHBItemType = ''BWB''','Selector for Bench Warrant5061B','N',1);

INSERT INTO XHB_SELECTORS (SELECTOR_ID,
                           SELECTOR,
                           DESCRIPTION,
                           ENABLED,
                           PRECEDENCE)
                           VALUES (NULL,'XHBTarget = ''EXISS'' AND XHBItemType = ''SSO''','Selector for Suspended Sentence','N',1);

Commit;