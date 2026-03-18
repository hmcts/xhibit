update xhb_selectors set selector = replace(selector,'ARS','AR') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''ARS''';

update xhb_selectors set selector = replace(selector,'CHG','CH') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''CHG''';

update xhb_selectors set selector = replace(selector,'CPRO','CPR') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''CPRO''';

update xhb_selectors set selector = replace(selector,'PDL','DLP') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''PDL''';

update xhb_selectors set selector = replace(selector,'CRS','SR') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''CRS''';

update xhb_selectors set selector = replace(selector,'TRS','TR') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''TRS''';

update xhb_selectors set selector = replace(selector,'YOO','YOI') where selector = 'XHBTarget = ''EXISS'' AND XHBItemType = ''YOO''';

commit;