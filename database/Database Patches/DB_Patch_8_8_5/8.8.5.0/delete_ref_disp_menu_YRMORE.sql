-- Move the YRMORE menu into YRO menu
UPDATE XHB_REF_DISPOSAL_MENU item
SET item.parent = 2698 --YRO
WHERE item.parent = 2711 --YRMORE
;

-- Delete the YRMORE menu (if empty)
DELETE XHB_REF_DISPOSAL_MENU menu
WHERE menu.menu_item_id = 2711 --YRMORE
AND NOT EXISTS (SELECT 1 FROM XHB_REF_DISPOSAL_MENU item 
                 WHERE item.parent = menu.menu_item_id);

COMMIT;
/