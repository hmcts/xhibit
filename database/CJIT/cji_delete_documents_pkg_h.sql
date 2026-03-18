/*
 * Procedure for deleting document records from vaious CJI tables
 * that are marked with a status of Delete.  Called from an Oracle job
 * at a predefined time (3 in the morning).
 */

CREATE OR REPLACE PACKAGE cji_delete_documents_pkg AS

  PROCEDURE delete_cji_documents;

END cji_delete_documents_pkg;
/
show errors
