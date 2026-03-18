/* This script will be used to update Production from 0_4p to the latest level - 0_4q */


alter table CJI_EVENT add BITS_REGISTERING_LOCATION VARCHAR2 ( 50 ) null;

