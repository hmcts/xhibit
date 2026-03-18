CREATE OR REPLACE PACKAGE xhb_list_distribution_pkg AS

    ----
    -- TO BE REMOVED: get_wll_unsub_rec_by_court_id
    ----
    PROCEDURE get_wll_unsub_rec_by_court_id (
                                              p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                              p_court_id        IN     NUMBER
                                            );


    ----
    -- TO BE REMOVED: get_dist_stat_by_court_id
    ----
    PROCEDURE get_dist_stat_by_court_id (
                                          p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                          p_court_id      IN     NUMBER
                                        );


    ----
    -- TO BE REMOVED: get_wll_dist_stat_by_court_id
    ----
    PROCEDURE get_wll_dist_stat_by_court_id (
                                              p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                              p_court_id          IN     NUMBER
                                            );

    ----
    -- get_sub_wll_rec_by_court_id
    ----
    --      Returns all subscribed wll_recipients with their document_distribution data
    ----
    FUNCTION get_sub_wll_rec_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR;

    ----
    -- get_unsub_wll_rec_by_court_id
    ----
    --      Returns all unsubscribed wll_recipients
    ----
    FUNCTION get_unsub_wll_rec_by_court_id(
                                           p_court_id    IN     NUMBER
                                          )
                                          RETURN SYS_REFCURSOR;

    ----
    -- get_wll_control_by_pk
    ----
    --      Returns wll_control and the status of its letters
    ----
    FUNCTION get_wll_control_by_pk(
                                   p_wll_control_id    IN     NUMBER
                                  )
                                  RETURN SYS_REFCURSOR;

    ----
    -- get_wll_control_by_court_id
    ----
    --      Returns wll_control and the status of its letters
    ----
    FUNCTION get_wll_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR;

    ----
    -- get_doc_control_by_court_id
    ----
    --      Returns all the docment_control records with their recipient data
    ----
    FUNCTION get_doc_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR;

    ---
    -- get_letter_xml
    ---
    --      Returns clobs containing the letters for the specified list
    ---
    FUNCTION get_letter_xml(
                             p_wll_control_id IN   NUMBER,
                             p_include_post   IN   NUMBER,
                             p_include_email  IN   NUMBER,
                             p_include_fax    IN   NUMBER
                           )
                           RETURN SYS_REFCURSOR;
    ----
    -- get_next_control_id
    ----
    --     Returns the id of the next control to process
    ----
    FUNCTION get_next_control_id RETURN XHB_WLL_CONTROL.wll_control_id%TYPE;


    ----
    -- update_control_status
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to update
    --     p_success_in        - 0 represents false, anything else is true
    ----
    PROCEDURE update_control_status (
                                      p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                      p_success_in        IN NUMBER
                                    );


    ----
    -- get_xml_document
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to get
    --                           the details for
    ----
    FUNCTION get_xml_document (
                                p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE
                              )
                              RETURN SYS_REFCURSOR;


    ----
    -- create_list_letter
    ----
    --     p_wll_control_id_in - The wll_control_id of the original list the letter was part of
    --     p_court_id_in       - The court id the letter belongs to
    --     p_document_type_in  - The type of the letter to be inserted
    --     p_document_title_in - The title of the letter to be inserted
    --     p_major_schema_version_in - The major schema version of the letter to be inserted
    --     p_major_schema_version_in - The minor schema version of the letter to be inserted 
    --     p_language_in       - The language of the letter to be inserted 
    --     p_country_in        - The country of the letter to be inserted 
    --     p_recipient_id_in   - The id of the letter recipient
    --     p_recipient_type_in - The type of the recipient
    --
    --     Return the clob that the letter can be written to externally inside of a cursor
    ----
    FUNCTION create_list_letter (
                                  p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                  p_court_id_in       IN XHB_XML_DOCUMENT.court_id%TYPE,
                                  p_document_type_in  IN XHB_XML_DOCUMENT.document_type%TYPE,
                                  p_document_title_in IN XHB_XML_DOCUMENT.document_title%TYPE,
                                  p_major_schema_version_in IN XHB_XML_DOCUMENT.major_schema_version%TYPE,
                                  p_minor_schema_version_in IN XHB_XML_DOCUMENT.minor_schema_version%TYPE,
                                  p_language_in IN XHB_XML_DOCUMENT.language%TYPE,
                                  p_country_in IN XHB_XML_DOCUMENT.country%TYPE,
                                  p_recipient_id_in   IN XHB_WLL_RECIPIENT.crest_solicitor_firm_id%TYPE,
                                  p_recipient_type_in IN XHB_WLL_RECIPIENT.recipient_type%TYPE
                                )
                                RETURN SYS_REFCURSOR;

  PROCEDURE PUBLISH_LIST(p_list_id XHB_LIST.LIST_ID%TYPE);

    ---
    -- get_blob_data
    ---
    --      Returns blob containing the internet web page
    ---
    FUNCTION get_blob_data(
                             p_blob_id IN NUMBER
                           )
                           RETURN SYS_REFCURSOR;

END xhb_list_distribution_pkg;
/
show errors
