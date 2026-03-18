/* This script will be used to update Production from 0_4p to the latest level - 0_4q */


alter table CJI_DOCUMENT add SRC_DOCUMENT_KEY VarChar2(255) null;
