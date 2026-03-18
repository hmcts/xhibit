/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 22, 2003
 * Time: 9:37:03 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.components;

import uk.gov.courtservice.xhibit.client.order.exceptions.InvalidCurrencyCodeException;

// @todo replace this factory class with a properties file.
public class CurrencySymbolFactory {

    public static String getCurrencySymbol(String code) {
        if (code.equals("GBP")) {
            return " " + '£';
        } else if (code.equals("EURO")) {
            return " " + '€';
        } else {
            throw new InvalidCurrencyCodeException("Currency code " + code + " is not supported");
        }
    }
}
