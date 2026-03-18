#!/bin/bash
#################################################################################
# disable_constraint.sh								#
#                                                                               #
# Script to disable constraints when truncating tables				#
#                                                                               #
#                                                                               #
#                                                                               #
#################################################################################

# wmb_debug affects the wmb_debug_clob table
# xhb_disposal_reference affects teh xhb_disposal table

. ${COMMON_VAR}/.common-variables

${SQLPLUS} -s ${ORA}<<endsql
set serveroutput on
alter table wmb_debug disable constraint WMB_DEBUG_CREST_RESPONSE_ID_FK;
alter table wmb_debug disable constraint WMB_DEBUG_XHIBIT_CALL_ID_FK;
alter table xhb_disposal_reference disable constraint DISPOSAL_REFERENCE_DISP_ID_FK;
endsql


echo "Constraints disabled"
exit
