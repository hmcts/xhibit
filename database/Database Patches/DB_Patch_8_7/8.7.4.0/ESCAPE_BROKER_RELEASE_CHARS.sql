/* IMPORTANT - READ BEFORE REDEPLOYING!
 * Whenever this function is deployed some extra steps must be included so the | characters are deployed correctly:
 * 1) Navigate to the directory where the SQL files are stored
 * 2) Open Command Prompt here
 * 3) Type chcp65001
 * 4) Type SET NLS_LANG=.AL32UTF8
 * 5) Type @nameOfSqlFile.sql;
*/
CREATE OR REPLACE FUNCTION ESCAPE_BROKER_RELEASE_CHARS(p_string_to_escape VARCHAR2) RETURN VARCHAR2 AS

    v_escaped_string VARCHAR2(4000);
    BEGIN
    v_escaped_string := REPLACE(p_string_to_escape, '!', '!!');
    v_escaped_string := REPLACE(v_escaped_string, '¦', '!¦');

    RETURN REPLACE(v_escaped_string, '|', '!|');
END ESCAPE_BROKER_RELEASE_CHARS;
/