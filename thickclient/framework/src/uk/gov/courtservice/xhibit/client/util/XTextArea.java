package uk.gov.courtservice.xhibit.client.util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;

public class XTextArea extends JTextArea{

	private static final long serialVersionUID = 1L;
	
	private static final String EMPTY_STRING = "";

	/**
	 * Regular expression to check on losing focus
	 */
	private String regex = EMPTY_STRING;
	
	/**
	 * Error warning label
	 */
	private JLabel warningLabel = null;
	
	/**
	 * Boolean for mandatory fields
	 */
	private boolean isMandatory;
	
	/**
	 * Text Area Character Limit
	 */
	private int limit;
	
	private boolean gridBagLayout = false;
	
	/**
	 * Standard implementation of super constructor
	 */
	public XTextArea(){
		super();
		init();
	}
	
	/**
	 * Constructor needed for XLC
	 */
	public XTextArea(JLabel label, boolean mandatory, int limit){
		super();
		init(this);
		this.warningLabel = label;
		this.isMandatory = mandatory;
		this.limit = limit;
	}
	
	/**
	 * implementation with text, character limit and regex for input.
	 * @param text
	 * @param limit
	 * @param regex
	 */
	public XTextArea(String text, int limit, String regex){
		super(text);
		init();
		this.regex = regex;
	}
	
	public XTextArea(String text, int limit, String regex, JLabel label, boolean mandatory){
		super(text);
		this.regex = regex;
		this.limit = limit;
		this.warningLabel = label;
		isMandatory = mandatory;
		init(this);
	}
	
	public XTextArea(String text, int rows, int columns, int limit, JLabel label) {
		super(text, rows, columns);
		setLimit(limit);
		this.warningLabel = label;
		init(this);
	}
	
	private void init(){
		addFocusListener(new FocusAdapter(){
			public void focusLost(FocusEvent e){
				String text = ((JTextArea) (e.getComponent())).getText();
				if(!(text.length() > 0 && isRegexMatch(text))){
					if(warningLabel != null){
						warningLabel.setVisible(true);
					}
				} else {
					if(warningLabel != null) {
						warningLabel.setVisible(false);
					}
				}
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_TAB){
					if(e.getModifiers() > 0){
						e.getComponent().transferFocusBackward();
					} else {
						e.getComponent().transferFocus();
					}
					e.consume();
				}
			}
		});
	}
	
	private void init(final XTextArea xta){
		if(warningLabel != null) {
			// Needed due to changes made by GridBag requiring .setVisible never to be used as causes resize issues
			if(gridBagLayout) {
				warningLabel.setVisible(true);
				warningLabel.setText(" ");
			}
		}
		
		addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e){
			}
			public void focusLost(FocusEvent e){
				String text = ((JTextArea) (e.getComponent())).getText();
				if(isMandatory && text.length() == 0){
					if(warningLabel != null){
						warningLabel.setText("Mandatory Field");
					}
				} else if(!isRegexMatch(text)) {
					if(warningLabel != null){
						warningLabel.setText("Invalid Entry");
						if(text.length() == 0){
							warningLabel.setText(" ");
							warningLabel.setVisible(gridBagLayout);
						}
					}
				} else	{
					if(checkLimit(limit, text.length())) {
						warningLabel.setText(" ");
						warningLabel.setVisible(gridBagLayout);
					} else {
						warningLabel.setText("Invalid Entry");
					}
				}
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_TAB){
					if(e.getModifiers() > 0){
						xta.transferFocusBackward();
					} else {
						xta.transferFocus();
					}
					e.consume();
				}
			}
		});
	}
	
	private boolean isRegexMatch(String text) {
		return EMPTY_STRING.equals(regex) || text.matches(regex);
	}
	
	private boolean checkLimit(int limit, int length){
		boolean limitChecker = true;
		if(length > limit){
			limitChecker = false;
		}
		return limitChecker;
	}
	
	public void setGridBagLayout(boolean gridBagLayout) {
		this.gridBagLayout = gridBagLayout;
	}
	
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
		String currentString = this.getText();
		ArrayList<Capability> capabilities = new ArrayList<Capability>();
		if ( limit > 0 ) {
			capabilities.add(Capability.limitedText(limit));
		}
		final Document doc = DocumentFactory.newDocument(capabilities.toArray(new Capability[capabilities.size()]));
		this.setDocument(doc);
		this.setText(currentString);
	}
	
	/**
	 * Sets the border and font style of the text area to mimic the XTextField
	 */
	public void setStyleXTextField()
	{
		XTextField xtf = new XTextField();
		this.setBorder(xtf.getBorder());
		this.setFont(xtf.getFont());
	}
}
