INSERT INTO XHB_REF_DISPOSAL
VALUES
(-1, 'ORDTST1', 'Test disposal type for orders', 'CO', null, null,
null, null, null, 1, null, null, null, 'ORDERSTEST', null);

INSERT INTO XHB_REF_DISPOSAL
VALUES
(-2, 'ORDTST2', 'Test disposal type for orders', 'RS', null, null,
null, null, null, 1, null, null, null, 'ORDERSTEST', null);

INSERT INTO XHB_REF_DISPOSAL
VALUES
(-3, 'ORDTST3', 'Test disposal type for orders', 'RS', null, null,
null, null, null, 1, null, null, null, 'ORDERSTEST', null);

INSERT INTO XHB_REF_DISPOSAL
VALUES
(-4, 'ORDTST4', 'Test disposal type for orders', 'RS', null, null,
null, null, null, 1, null, null, null, 'ORDERSTEST', null);

INSERT INTO XHB_ORDER_DISPOSAL_XREF
VALUES
(-1, 1, -2, -1, null, null, null, 'ORDERSTEST', null);

INSERT INTO XHB_ORDER_DISPOSAL_XREF
VALUES
(-2, 1, -3, -1, null, null, null, 'ORDERSTEST', null);

INSERT INTO XHB_ORDER_DISPOSAL_XREF
VALUES
(-3, 1, -4, -1, null, null, null, 'ORDERSTEST', null);
