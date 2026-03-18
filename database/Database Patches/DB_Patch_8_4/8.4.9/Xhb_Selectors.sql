/*
* This resets all the documents so that they'll be picked up for processing
*/
update xhb_selectors set enabled='Y' where selector<>'XHBTarget = ''EXISS'''
