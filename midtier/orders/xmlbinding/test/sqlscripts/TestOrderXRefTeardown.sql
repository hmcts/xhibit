DELETE FROM XHB_ORDER_DISPOSAL_XREF
WHERE order_disposal_xref_id < 0;

DELETE FROM XHB_REF_DISPOSAL
WHERE ref_disposal_id < 0;
