package uk.gov.courtservice.xhibit.client.util.text;

import java.awt.Font;

/**
 * A utility class to help with fonts.
 * @author westalll
 *
 */
public class FontHelper {
	
	/**
	 * Method to get an italic version of a font.
	 * @param The input font you want an italic version of.
	 * @returnThe italic font.
	 */
	public static Font getItalicFontFromFont(final Font font) {
		return new Font(font.getFontName(),Font.ITALIC, font.getSize());
	}


}
