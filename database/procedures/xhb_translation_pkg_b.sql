CREATE OR REPLACE PACKAGE BODY xhb_translation_pkg AS

   ----
   -- Get the current ref translation data for read, excludes audit and keys!
   ----
   FUNCTION get_ref_translation RETURN SYS_REFCURSOR

   IS

       l_return_cursor SYS_REFCURSOR;

   BEGIN

      OPEN l_return_cursor FOR
         SELECT 
            xrt.key,
            xrt.translation,
            xrt.context,
            xrt.exact_match,
            xrt.language,
            xrt.country
         FROM
            xhb_ref_translation xrt
         WHERE
            xrt.obs_ind = 'N';


      RETURN l_return_cursor;

   END get_ref_translation;

END xhb_translation_pkg;
/
show errors
