package uk.gov.courtservice.xhibit.client.im.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p/> Title: AdHoc message service class.
 * </p>
 * <p/> Description:
 * </p>
 * <p/> Utility class containing methods to support Ad Hoc messaging.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class AdHocMessageServices {
    private static HashMap htmlEscapes = new HashMap();

    private static final Logger log = CSServices.getLogger(AdHocMessageServices.class);

    static {
        htmlEscapes.put("nbsp", new Integer(160));
        htmlEscapes.put("iexcl", new Integer(161));
        htmlEscapes.put("cent", new Integer(162));
        htmlEscapes.put("pound", new Integer(163));
        htmlEscapes.put("curren", new Integer(164));
        htmlEscapes.put("yen", new Integer(165));
        htmlEscapes.put("brvbar", new Integer(166));
        htmlEscapes.put("sect", new Integer(167));
        htmlEscapes.put("uml", new Integer(168));
        htmlEscapes.put("copy", new Integer(169));
        htmlEscapes.put("ordf", new Integer(170));
        htmlEscapes.put("laquo", new Integer(171));
        htmlEscapes.put("not", new Integer(172));
        htmlEscapes.put("shy", new Integer(173));
        htmlEscapes.put("reg", new Integer(174));
        htmlEscapes.put("macr", new Integer(175));
        htmlEscapes.put("deg", new Integer(176));
        htmlEscapes.put("plusmn", new Integer(177));
        htmlEscapes.put("sup2", new Integer(178));
        htmlEscapes.put("sup3", new Integer(179));
        htmlEscapes.put("acute", new Integer(180));
        htmlEscapes.put("micro", new Integer(181));
        htmlEscapes.put("para", new Integer(182));
        htmlEscapes.put("middot", new Integer(183));
        htmlEscapes.put("cedil", new Integer(184));
        htmlEscapes.put("sup1", new Integer(185));
        htmlEscapes.put("ordm", new Integer(186));
        htmlEscapes.put("raquo", new Integer(187));
        htmlEscapes.put("frac14", new Integer(188));
        htmlEscapes.put("frac12", new Integer(189));
        htmlEscapes.put("frac34", new Integer(190));
        htmlEscapes.put("iquest", new Integer(191));
        htmlEscapes.put("Agrave", new Integer(192));
        htmlEscapes.put("Aacute", new Integer(193));
        htmlEscapes.put("Acirc", new Integer(194));
        htmlEscapes.put("Atilde", new Integer(195));
        htmlEscapes.put("Auml", new Integer(196));
        htmlEscapes.put("Aring", new Integer(197));
        htmlEscapes.put("AElig", new Integer(198));
        htmlEscapes.put("Ccedil", new Integer(199));
        htmlEscapes.put("Egrave", new Integer(200));
        htmlEscapes.put("Eacute", new Integer(201));
        htmlEscapes.put("Ecirc", new Integer(202));
        htmlEscapes.put("Euml", new Integer(203));
        htmlEscapes.put("Igrave", new Integer(204));
        htmlEscapes.put("Iacute", new Integer(205));
        htmlEscapes.put("Icirc", new Integer(206));
        htmlEscapes.put("Iuml", new Integer(207));
        htmlEscapes.put("ETH", new Integer(208));
        htmlEscapes.put("Ntilde", new Integer(209));
        htmlEscapes.put("Ograve", new Integer(210));
        htmlEscapes.put("Oacute", new Integer(211));
        htmlEscapes.put("Ocirc", new Integer(212));
        htmlEscapes.put("Otilde", new Integer(213));
        htmlEscapes.put("Ouml", new Integer(214));
        htmlEscapes.put("times", new Integer(215));
        htmlEscapes.put("Oslash", new Integer(216));
        htmlEscapes.put("Ugrave", new Integer(217));
        htmlEscapes.put("Uacute", new Integer(218));
        htmlEscapes.put("Ucirc", new Integer(219));
        htmlEscapes.put("Uuml", new Integer(220));
        htmlEscapes.put("Yacute", new Integer(221));
        htmlEscapes.put("THORN", new Integer(222));
        htmlEscapes.put("szlig", new Integer(223));
        htmlEscapes.put("agrave", new Integer(224));
        htmlEscapes.put("aacute", new Integer(225));
        htmlEscapes.put("acirc", new Integer(226));
        htmlEscapes.put("atilde", new Integer(227));
        htmlEscapes.put("auml", new Integer(228));
        htmlEscapes.put("aring", new Integer(229));
        htmlEscapes.put("aelig", new Integer(230));
        htmlEscapes.put("ccedil", new Integer(231));
        htmlEscapes.put("egrave", new Integer(232));
        htmlEscapes.put("eacute", new Integer(233));
        htmlEscapes.put("ecirc", new Integer(234));
        htmlEscapes.put("euml", new Integer(235));
        htmlEscapes.put("igrave", new Integer(236));
        htmlEscapes.put("iacute", new Integer(237));
        htmlEscapes.put("icirc", new Integer(238));
        htmlEscapes.put("iuml", new Integer(239));
        htmlEscapes.put("eth", new Integer(240));
        htmlEscapes.put("ntilde", new Integer(241));
        htmlEscapes.put("ograve", new Integer(242));
        htmlEscapes.put("oacute", new Integer(243));
        htmlEscapes.put("ocirc", new Integer(244));
        htmlEscapes.put("otilde", new Integer(245));
        htmlEscapes.put("ouml", new Integer(246));
        htmlEscapes.put("divide", new Integer(247));
        htmlEscapes.put("oslash", new Integer(248));
        htmlEscapes.put("ugrave", new Integer(249));
        htmlEscapes.put("uacute", new Integer(250));
        htmlEscapes.put("ucirc", new Integer(251));
        htmlEscapes.put("uuml", new Integer(252));
        htmlEscapes.put("yacute", new Integer(253));
        htmlEscapes.put("thorn", new Integer(254));
        htmlEscapes.put("yuml", new Integer(255));
        htmlEscapes.put("fnof", new Integer(402));
        htmlEscapes.put("Alpha", new Integer(913));
        htmlEscapes.put("Beta", new Integer(914));
        htmlEscapes.put("Gamma", new Integer(915));
        htmlEscapes.put("Delta", new Integer(916));
        htmlEscapes.put("Epsilon", new Integer(917));
        htmlEscapes.put("Zeta", new Integer(918));
        htmlEscapes.put("Eta", new Integer(919));
        htmlEscapes.put("Theta", new Integer(920));
        htmlEscapes.put("Iota", new Integer(921));
        htmlEscapes.put("Kappa", new Integer(922));
        htmlEscapes.put("Lambda", new Integer(923));
        htmlEscapes.put("Mu", new Integer(924));
        htmlEscapes.put("Nu", new Integer(925));
        htmlEscapes.put("Xi", new Integer(926));
        htmlEscapes.put("Omicron", new Integer(927));
        htmlEscapes.put("Pi", new Integer(928));
        htmlEscapes.put("Rho", new Integer(929));
        htmlEscapes.put("Sigma", new Integer(931));
        htmlEscapes.put("Tau", new Integer(932));
        htmlEscapes.put("Upsilon", new Integer(933));
        htmlEscapes.put("Phi", new Integer(934));
        htmlEscapes.put("Chi", new Integer(935));
        htmlEscapes.put("Psi", new Integer(936));
        htmlEscapes.put("Omega", new Integer(937));
        htmlEscapes.put("alpha", new Integer(945));
        htmlEscapes.put("beta", new Integer(946));
        htmlEscapes.put("gamma", new Integer(947));
        htmlEscapes.put("delta", new Integer(948));
        htmlEscapes.put("epsilon", new Integer(949));
        htmlEscapes.put("zeta", new Integer(950));
        htmlEscapes.put("eta", new Integer(951));
        htmlEscapes.put("theta", new Integer(952));
        htmlEscapes.put("iota", new Integer(953));
        htmlEscapes.put("kappa", new Integer(954));
        htmlEscapes.put("lambda", new Integer(955));
        htmlEscapes.put("mu", new Integer(956));
        htmlEscapes.put("nu", new Integer(957));
        htmlEscapes.put("xi", new Integer(958));
        htmlEscapes.put("omicron", new Integer(959));
        htmlEscapes.put("pi", new Integer(960));
        htmlEscapes.put("rho", new Integer(961));
        htmlEscapes.put("sigmaf", new Integer(962));
        htmlEscapes.put("sigma", new Integer(963));
        htmlEscapes.put("tau", new Integer(964));
        htmlEscapes.put("upsilon", new Integer(965));
        htmlEscapes.put("phi", new Integer(966));
        htmlEscapes.put("chi", new Integer(967));
        htmlEscapes.put("psi", new Integer(968));
        htmlEscapes.put("omega", new Integer(969));
        htmlEscapes.put("thetasym", new Integer(977));
        htmlEscapes.put("upsih", new Integer(978));
        htmlEscapes.put("piv", new Integer(982));
        htmlEscapes.put("bull", new Integer(8226));
        htmlEscapes.put("hellip", new Integer(8230));
        htmlEscapes.put("prime", new Integer(8242));
        htmlEscapes.put("Prime", new Integer(8243));
        htmlEscapes.put("oline", new Integer(8254));
        htmlEscapes.put("frasl", new Integer(8260));
        htmlEscapes.put("weierp", new Integer(8472));
        htmlEscapes.put("image", new Integer(8465));
        htmlEscapes.put("real", new Integer(8476));
        htmlEscapes.put("trade", new Integer(8482));
        htmlEscapes.put("alefsym", new Integer(8501));
        htmlEscapes.put("larr", new Integer(8592));
        htmlEscapes.put("uarr", new Integer(8593));
        htmlEscapes.put("rarr", new Integer(8594));
        htmlEscapes.put("darr", new Integer(8595));
        htmlEscapes.put("harr", new Integer(8596));
        htmlEscapes.put("crarr", new Integer(8629));
        htmlEscapes.put("lArr", new Integer(8656));
        htmlEscapes.put("uArr", new Integer(8657));
        htmlEscapes.put("rArr", new Integer(8658));
        htmlEscapes.put("dArr", new Integer(8659));
        htmlEscapes.put("hArr", new Integer(8660));
        htmlEscapes.put("forall", new Integer(8704));
        htmlEscapes.put("part", new Integer(8706));
        htmlEscapes.put("exist", new Integer(8707));
        htmlEscapes.put("empty", new Integer(8709));
        htmlEscapes.put("nabla", new Integer(8711));
        htmlEscapes.put("isin", new Integer(8712));
        htmlEscapes.put("notin", new Integer(8713));
        htmlEscapes.put("ni", new Integer(8715));
        htmlEscapes.put("prod", new Integer(8719));
        htmlEscapes.put("sum", new Integer(8721));
        htmlEscapes.put("minus", new Integer(8722));
        htmlEscapes.put("lowast", new Integer(8727));
        htmlEscapes.put("radic", new Integer(8730));
        htmlEscapes.put("prop", new Integer(8733));
        htmlEscapes.put("infin", new Integer(8734));
        htmlEscapes.put("ang", new Integer(8736));
        htmlEscapes.put("and", new Integer(8743));
        htmlEscapes.put("or", new Integer(8744));
        htmlEscapes.put("cap", new Integer(8745));
        htmlEscapes.put("cup", new Integer(8746));
        htmlEscapes.put("int", new Integer(8747));
        htmlEscapes.put("there4", new Integer(8756));
        htmlEscapes.put("sim", new Integer(8764));
        htmlEscapes.put("cong", new Integer(8773));
        htmlEscapes.put("asymp", new Integer(8776));
        htmlEscapes.put("ne", new Integer(8800));
        htmlEscapes.put("equiv", new Integer(8801));
        htmlEscapes.put("le", new Integer(8804));
        htmlEscapes.put("ge", new Integer(8805));
        htmlEscapes.put("sub", new Integer(8834));
        htmlEscapes.put("sup", new Integer(8835));
        htmlEscapes.put("nsub", new Integer(8836));
        htmlEscapes.put("sube", new Integer(8838));
        htmlEscapes.put("supe", new Integer(8839));
        htmlEscapes.put("oplus", new Integer(8853));
        htmlEscapes.put("otimes", new Integer(8855));
        htmlEscapes.put("perp", new Integer(8869));
        htmlEscapes.put("sdot", new Integer(8901));
        htmlEscapes.put("lceil", new Integer(8968));
        htmlEscapes.put("rceil", new Integer(8969));
        htmlEscapes.put("lfloor", new Integer(8970));
        htmlEscapes.put("rfloor", new Integer(8971));
        htmlEscapes.put("lang", new Integer(9001));
        htmlEscapes.put("rang", new Integer(9002));
        htmlEscapes.put("loz", new Integer(9674));
        htmlEscapes.put("spades", new Integer(9824));
        htmlEscapes.put("clubs", new Integer(9827));
        htmlEscapes.put("hearts", new Integer(9829));
        htmlEscapes.put("diams", new Integer(9830));
        htmlEscapes.put("quot", new Integer(34));
        htmlEscapes.put("amp", new Integer(38));
        htmlEscapes.put("lt", new Integer(60));
        htmlEscapes.put("gt", new Integer(62));
        htmlEscapes.put("OElig", new Integer(338));
        htmlEscapes.put("oelig", new Integer(339));
        htmlEscapes.put("Scaron", new Integer(352));
        htmlEscapes.put("scaron", new Integer(353));
        htmlEscapes.put("Yuml", new Integer(376));
        htmlEscapes.put("circ", new Integer(710));
        htmlEscapes.put("tilde", new Integer(732));
        htmlEscapes.put("ensp", new Integer(8194));
        htmlEscapes.put("emsp", new Integer(8195));
        htmlEscapes.put("thinsp", new Integer(8201));
        htmlEscapes.put("zwnj", new Integer(8204));
        htmlEscapes.put("zwj", new Integer(8205));
        htmlEscapes.put("lrm", new Integer(8206));
        htmlEscapes.put("rlm", new Integer(8207));
        htmlEscapes.put("ndash", new Integer(8211));
        htmlEscapes.put("mdash", new Integer(8212));
        htmlEscapes.put("lsquo", new Integer(8216));
        htmlEscapes.put("rsquo", new Integer(8217));
        htmlEscapes.put("sbquo", new Integer(8218));
        htmlEscapes.put("ldquo", new Integer(8220));
        htmlEscapes.put("rdquo", new Integer(8221));
        htmlEscapes.put("bdquo", new Integer(8222));
        htmlEscapes.put("dagger", new Integer(8224));
        htmlEscapes.put("Dagger", new Integer(8225));
        htmlEscapes.put("permil", new Integer(8240));
        htmlEscapes.put("lsaquo", new Integer(8249));
        htmlEscapes.put("rsaquo", new Integer(8250));
        htmlEscapes.put("euro", new Integer(8364));
    }

    /**
     * This is the complete legal non-escaped GSM character set as laid out in
     * the GSM 03.38 standard. They are laid out in order so
     * <code>SMS_CHARS.indexOf(int char)</code> will return the int value
     * matching the underlying character value in SMS. i.e. to get the SMS
     * character value for 'a' just get <code>SMS_CHARS.indexOf('a')</code>.
     * <p/> To perform the back conversion just take the SMS character you want
     * to convert back into Unicode and do
     * <code>SMS_CHARS.charAt(int smsChar)</code>. <p/> The pipe character at
     * position 27 is not a true conversion... it is a placeholder for the SMS
     * escape character 27. Special handling will have to be put in place for
     * this particular character.
     * 
     * @see SMS_ESCAPE_CHARS
     */
    public static final String SMS_CHARS = "@£$•ËÈ˘ÏÚ«\nÿ¯\r≈Â\u0394_\u03A6\u0393\u039B\u03A9\u03A0\u03A8\u03A3\u0398\u039E|∆Êﬂ… !\"#§%&'()*+,-./0123456789:;<=>?°ABCDEFGHIJKLMNOPQRSTUVWXYZƒ÷—‹ßøabcdefghijklmnopqrstuvwxyz‰ˆÒ¸‡";

    /**
     * This is the escape character set from the GSM 03.38 standard. They are
     * escaped by character 27. Any spaces marked by *s are non-legal escaped
     * characters. The conversion can be done in the same way as for SMS_CHARS.
     * 
     * @see SMS_CHARS
     */
    public static final String SMS_ESCAPE_CHARS = "**********\f*********^*******************{}*****\\************[~]*|************************************Ä**************************";

    /**
     * Utility method to convert unicode to SMS (GSM) characters, will handle
     * the escaped characters as defined in GSM 03.38.
     * 
     * @param toBeFormatted
     *            the unicode characters to be converted.
     * @return the converted GSM/SMS characters.
     */
    public static char[] getUnicodeToSMSFormattedChars(char[] toBeFormatted) {
        StringBuffer returnBuffer = new StringBuffer();
        for (int i = 0; i < toBeFormatted.length; i++) {
            char character = toBeFormatted[i];
            int newCharacter = SMS_CHARS.indexOf(character);
            if (newCharacter >= 0) // recognised character
            {
                returnBuffer.append(character);
                if (newCharacter == 27) // a pipe, needs special handling as an
                // escape character...
                {
                    returnBuffer.append((char) 27); // the escape character.
                    returnBuffer.append((char) SMS_ESCAPE_CHARS.indexOf(character));
                }
            } else // possibly another escape character.
            {
                int escapedCharacter = SMS_ESCAPE_CHARS.indexOf(character);
                if (escapedCharacter >= 0) {
                    returnBuffer.append((char) 27); // the escape character.
                    returnBuffer.append((char) escapedCharacter); // the
                    // escaped
                    // character.
                }
            }
        }
        return returnBuffer.toString().toCharArray();
    }

    /**
     * This method validates that a String is made up of valid SMS characters.
     * Now no longer accepts escaped characters as valid.
     * 
     * @param toBeValidated
     *            The String to be validated.
     * @return -1 if the String is valid, or the position of the first invalid
     *         character in the String.
     */
    public static int validUnicodeForSMS(String toBeValidated) {
        char[] characters = toBeValidated.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            char character = characters[i];
            int newCharacter = SMS_CHARS.indexOf(character);
            if (newCharacter < 0 || newCharacter == 27) // unrecognised
            // character
            // No longer supporting escaped chars
            // as valid.
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method takes an array of SMS characters and converts them into
     * Unicode.
     * 
     * @param toBeFormatted
     *            The characters to be formatted.
     * @return the formatted characters
     */
    public static char[] getSMStoUnicodeFormattedChars(char[] toBeFormatted) {
        StringBuffer returnBuffer = new StringBuffer();
        for (int i = 0; i < toBeFormatted.length; i++) {
            char character = toBeFormatted[i];
            char newCharacter = SMS_CHARS.charAt(character);
            if (newCharacter == '|') // the escape character.
            {
                i++; // get the next character.
                char escapedCharacter = toBeFormatted[i];
                returnBuffer.append(SMS_ESCAPE_CHARS.charAt(escapedCharacter));
            } else // Standard character.
            {
                returnBuffer.append(newCharacter);
            }
        }
        return returnBuffer.toString().toCharArray();
    }

    /**
     * This method trivially HTML escapes <i>every</i> character in the passed
     * in character array.
     * 
     * @param toBeEscaped
     *            The characters to be escaped.
     * @return The string of escaped characters.
     */
    public static String htmlEscapeCharacters(char[] toBeEscaped) {
        StringBuffer returnBuffer = new StringBuffer();
        for (int i = 0; i < toBeEscaped.length; i++) {
            char character = toBeEscaped[i];
            returnBuffer.append("&#");
            returnBuffer.append((int) character);
            returnBuffer.append(";");
        }
        return returnBuffer.toString();
    }

    /**
     * This method HTML 'unescapes' the passed in String. Currently only deals
     * with HTML 4.0 encoding, may need to add more in the future.
     * 
     * @param toBeUnEscaped
     *            The String to be 'unescaped'.
     * @return A char array of unEscaped characters.
     */
    public static char[] htmlUnEscapeCharacters(String toBeUnEscaped) {
        StringBuffer returnBuffer = new StringBuffer(toBeUnEscaped.length());

        // Record where we are.
        int position = 0;
        // Record where the ampersand we are interested in is.
        int ampPosition = toBeUnEscaped.indexOf("&");

        // Start working through String.
        while (ampPosition >= 0) {
            // Get position of ampersand and semicolon..
            int nextSemi = toBeUnEscaped.indexOf(";", ampPosition);
            int nextAmp = toBeUnEscaped.indexOf("&", ampPosition + 1);

            // There is a semi colon before either the end or an ampersand
            if (nextSemi != -1 && (nextAmp == -1 || nextSemi < nextAmp)) {
                // append all characters up to the current escape sequence.
                returnBuffer.append(toBeUnEscaped.substring(position, ampPosition));

                // get the escape text.
                String escapeText = toBeUnEscaped.substring(ampPosition + 1, nextSemi);

                // Initialise the value to an easily detectable value.
                int value = -1;
                if (escapeText.startsWith("#")) // numeric encoding
                {
                    try {
                        // try to parse to in.
                        value = Integer.parseInt(escapeText.substring(1));
                    } catch (NumberFormatException ex) {
                        // value remains -1
                        log.info(ex, ex);
                    }
                } else // is it one of the escape characters.
                {
                    if (htmlEscapes.containsKey(escapeText)) {
                        value = ((Integer) (htmlEscapes.get(escapeText))).intValue();
                    }
                }

                // Is it a valid number... also catches the
                // number format exception mode.
                if (value >= 0 && value < 65536) {
                    returnBuffer.append((char) value);
                    System.err.println("Character value: " + value);
                } else // just paste in the unchanged escape sequence.
                {
                    returnBuffer.append("&").append(escapeText).append(";");
                }
                // update position to be just after the current escape sequence.
                position = nextSemi + 1;
            }
            // go to the next ampersand...
            ampPosition = nextAmp;
        }
        returnBuffer.append(toBeUnEscaped.substring(position));
        return returnBuffer.toString().toCharArray();
    }

}
