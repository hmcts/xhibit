#!/bin/bash
. ~/.bash_profile

sqlplus -s xhibit/xhibit@csdbprd11<<EOF

SET head OFF
SET pages 0
SET trimspool ON
SET echo OFF
SET feedback OFF

select COURT_ID from XHB_COURT
ORDER by COURT_ID ASC;

