/* Fixed various errors with this script */

insert into XHB_ORDER_TYPE(ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID) values
(29, 'BRSS', 'Breach of Suspended Sentence', null);

insert into XHB_ORDER_TYPE(ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID) values
(30, 'ACTCD', 'Action on Conditional Discharge', null);

insert into XHB_ORDER_TYPE(ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID) values
(31, 'NDS', 'Notice of Deferment of Sentence', null);

insert into XHB_ORDER_TYPE(ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID) values
(32, 'NBSS', 'Notice of Breach of Suspended Sentence Dealt With', null);

insert into XHB_ORDER_TYPE(ORDER_TYPE_ID, CODE, DESCRIPTION, REF_DISPOSAL_TYPE_ID) values
(33, 'BRCD', 'Breach of Conditional Discharge', null);

commit;
/