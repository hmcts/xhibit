#!/bin/bash
#################################################################################
# disable_constraint.sh								#
#                                                                               #
# Script to disable constraints when truncating tables				#
#                                                                               #
#                                                                               #
#                                                                               #
#################################################################################

# gdg_messages affects gdg_inbound_clobs

. ${COMMON_VAR}/.common-variables

${SQLPLUS} -s ${ORA2}<<endsql
set serveroutput on
ALTER TABLE GDG_INBOUND_MESSAGES DISABLE CONSTRAINTS INBOUND_MSG_ID_FK;
endsql


echo "Constraints disabled"
exit
