
--- updates the XHB_SELECTORS_QUEUES table to register the correct queue for the document type

INSERT INTO XHB_SELECTOR_QUEUES (SELECTOR_QUEUE_ID,
                                  SELECTOR_ID,
                                  QUEUE_ID)
                                  VALUES (NULL,(SELECT SELECTOR_ID FROM XHB_SELECTORS WHERE SELECTOR='XHBTarget = ''EXISS'' AND XHBItemType = ''COC'''),3);

INSERT INTO XHB_SELECTOR_QUEUES (SELECTOR_QUEUE_ID,
                                  SELECTOR_ID,
                                  QUEUE_ID)
                                  VALUES (NULL,(SELECT SELECTOR_ID FROM XHB_SELECTORS WHERE SELECTOR='XHBTarget = ''EXISS'' AND XHBItemType = ''COD'''),3);

INSERT INTO XHB_SELECTOR_QUEUES (SELECTOR_QUEUE_ID,
                                  SELECTOR_ID,
                                  QUEUE_ID)
                                  VALUES (NULL,(SELECT SELECTOR_ID FROM XHB_SELECTORS WHERE SELECTOR='XHBTarget = ''EXISS'' AND XHBItemType = ''BWA'''),3);

INSERT INTO XHB_SELECTOR_QUEUES (SELECTOR_QUEUE_ID,
                                  SELECTOR_ID,
                                  QUEUE_ID)
                                  VALUES (NULL,(SELECT SELECTOR_ID FROM XHB_SELECTORS WHERE SELECTOR='XHBTarget = ''EXISS'' AND XHBItemType = ''BWB'''),3);

INSERT INTO XHB_SELECTOR_QUEUES (SELECTOR_QUEUE_ID,
                                  SELECTOR_ID,
                                  QUEUE_ID)
                                  VALUES (NULL,(SELECT SELECTOR_ID FROM XHB_SELECTORS WHERE SELECTOR='XHBTarget = ''EXISS'' AND XHBItemType = ''SSO'''),3);


Commit;
