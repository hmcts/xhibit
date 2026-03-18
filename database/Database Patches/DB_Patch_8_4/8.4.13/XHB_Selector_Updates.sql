--
-- Disable all Exiss messages apart from UC,NC,DC,DL,TR,AR,CR,DELIVERERROR
--
UPDATE XHB_SELECTORS SET ENABLED = 'N' WHERE SELECTOR NOT IN
('XHBTarget = ''EXISS'' AND XHBItemType = ''UPDCASE''',
'XHBTarget = ''EXISS'' AND XHBItemType = ''NEWCASE''',
'XHBTarget = ''EXISS'' AND XHBItemType = ''DL''',
'XHBTarget = ''EXISS'' AND XHBItemType = ''TR''',
'XHBTarget = ''EXISS'' AND XHBItemType = ''AR''',
'XHBTarget = ''EXISS'' AND XHBItemType = ''SR''',
'XHBTarget = ''EXISS'' AND XHBItemType = ''DELIVERERROR'''
);

DELETE FROM XHB_SELECTOR_QUEUES WHERE QUEUE_ID = 2 AND SELECTOR_ID IN (
  SELECT SELECTOR_ID FROM XHB_SELECTORS WHERE SELECTOR NOT IN
  ('XHBTarget = ''EXISS'' AND XHBItemType = ''UPDCASE''',
  'XHBTarget = ''EXISS'' AND XHBItemType = ''NEWCASE''',
  'XHBTarget = ''EXISS'' AND XHBItemType = ''DL''',    
  'XHBTarget = ''EXISS'' AND XHBItemType = ''TR''',
  'XHBTarget = ''EXISS'' AND XHBItemType = ''AR''',
  'XHBTarget = ''EXISS'' AND XHBItemType = ''SR''',
  'XHBTarget = ''EXISS'' AND XHBItemType = ''DELIVERERROR'''
  )
);
COMMIT;