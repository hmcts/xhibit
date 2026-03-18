package uk.gov.courtservice.xhibit.client.util;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment (2005)
 * @version $id:$
 */
public class XTextField extends JTextField {

	private final Logger log = CSServices.getLogger(getClass());
	
	//If this is true then set to upper case (by calling setUpperCase method)
	private Boolean toUpperCase = false;
	
	//Iff this is true then only allow text
	private Boolean isText = false;
	
	//if this is set to >0 then it will stop you typing more than that limit in the field (by calling setMaxLength)
	private int maxLimit = 0;
	
	//if this is set then field is numeric only
	private Boolean isNumeric = false;
	
	private Boolean isAlphaNumeric = false;
    
	/**
	 * The color used to render the foreground if the date is invalid
	 */
	public static final Color ERROR_COLOR = new Color(244, 0, 0);

	/**
	 * Set to true if the text is invalid
	 */
	private boolean error;

	/**
	 * Regular expression to check on losing focus
	 */
	private String regex = "";

	/**
	 * Error warning label
	 */
	private JLabel warningLabel = null;

	/**
	 * Boolean for mandatory fields
	 */
	private Boolean isMandatory;
	
	/**
	 * Will be used so that if a text field is linked to a dropdown 
	 * it won't lose the 'invalid entry' error message above the field.
	 */
	private Boolean matchesDropDownValue = true;

	private String mandatoryText = "Mandatory Field";
	private String invalidText = "Invalid Entry";
	
	private Boolean gridBagLayout = false;

	/**
	 * Implementation of super constructor
	 */
	public XTextField() {
		super();
		init();
	}

	/**
	 * Implementation of super constructor
	 */
	public XTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		init();
	}

	/**
	 * Implementation of super constructor
	 */
	public XTextField(int columns) {
		super(columns);
		init();
	}

	/**
	 * Implementation of super constructor
	 */
	public XTextField(String text, int columns) {
		super(text, columns);
		init();
	}

	/**
	 * Implementation of super constructor
	 */
	public XTextField(String text) {
		super(text);
		init();
	}

	/**
	 * Custom constructor for validation
	 */
	public XTextField(int limit, String regex, JLabel warningLabel, boolean mandatory) {
		super();
		this.regex = regex;
		this.warningLabel = warningLabel;
		isMandatory = mandatory;
		init(warningLabel);
	}

	/**
	 * Custom constructor for validation
	 */
	public XTextField(String text, int limit, String regex, JLabel warningLabel, boolean mandatory) {
		super(text);
		this.regex = regex;
		this.warningLabel = warningLabel;
		isMandatory = mandatory;
		init(warningLabel);
	}

	/**
	 * Custom constructor for validation
	 */
	public XTextField(int limit, String regex) {
		super(limit);
		init(regex);
		this.regex = regex;
	}

	/**
	 * Custom constructor for validation
	 */
	public XTextField(int limit, String regex, boolean mandatory) {
		super();
		init(regex);
		this.regex = regex;
		isMandatory = mandatory;
	}

	/**
	 * Custom constructor for validation
	 */
	public XTextField(int limit, String postcode, JLabel warningLabel, boolean mandatory, boolean pcode) {
		super();
		init(warningLabel, pcode);
		this.warningLabel = warningLabel;
		isMandatory = mandatory;
	}

    /**
     * Stop the control losing focus if it currently has an error.
     */
    private void init() {
        addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (hasError()) {
                    requestFocus();
                }
            }
        });
    }
    
	/**
	 * Show warning label if the text does not match the regex.
	 */
	private void init(String exp) {
		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				String text = ((JTextField) (e.getComponent())).getText();
				boolean rc = text.matches(regex);
				if ((text.length() > 0) && text.matches(regex)) {
					setError(false);
					if (warningLabel != null) {
						if(gridBagLayout) 
							warningLabel.setText(" ");
						warningLabel.setVisible(gridBagLayout);
					}
				} else {
					setError(true);
					if (warningLabel != null) {
						warningLabel.setText(invalidText);
						warningLabel.setVisible(true);
					}
				}
			}
		});
	}

	/**
	 * Stop the control losing focus if it currently has an error, accepts a
	 * JLabel for the warning message for text change.
	 */
	private void init(JLabel label) {
		// Needed due to changes made by GridBag requiring .setVisible never to be used as causes resize issues
		if(gridBagLayout) {
			label.setVisible(true);
			label.setText(" ");		
		}
		
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				String text = ((JTextField) (e.getComponent())).getText();
				if (matchesDropDownValue && (warningLabel != null)) {
					if (isMandatory && (text.length() == 0)) {
						setError(true);
						warningLabel.setText(mandatoryText);
						warningLabel.setVisible(true);
					} else if (!(text.matches(regex))) {
						setError(true);
						warningLabel.setText(invalidText);
						warningLabel.setVisible(true);
						if(text.length()==0){
							if(gridBagLayout)
								warningLabel.setText(" ");
							warningLabel.setVisible(gridBagLayout);
							setError(false);
						}
					} else {
						setError(false);
						if(gridBagLayout)
							warningLabel.setText(" ");
						warningLabel.setVisible(gridBagLayout);
					}
				}
			}
		});
	}

	private void init(JLabel label, boolean pcode) {
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				String text = ((JTextField) (e.getComponent())).getText();
				
				String expression = "(GIR 0AA)|^((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
			            + "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$";
				boolean matched = false;
				
				try {
					RE regexp = new RE(expression);
					matched = regexp.match(text);
				} catch (RESyntaxException e1) {
					matched = false;
				}	

				if (!text.isEmpty() && !matched) {
					setError(true);
					if (warningLabel != null) {
						warningLabel.setText(invalidText);
						warningLabel.setVisible(true);
					}
				} else {
					setError(false);
					if (warningLabel != null) {
						if(gridBagLayout)
							warningLabel.setText(" ");
						warningLabel.setVisible(gridBagLayout);
					}
				}
				if (isMandatory && text.isEmpty()) {
					setError(true);
					if (warningLabel != null) {
						warningLabel.setText("Mandatory Field");
						warningLabel.setVisible(true);
					}
				}

			}
		});
	}

	/**
	 * Set the error flag
	 * 
	 * @param error
	 *            The error to set.
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return true if the error flag has been set
	 */
	public boolean hasError() {
		return error;
	}
	
	/**
	 * Get the foreground colour, return the error colour if an error has
	 * occured else return the foreground colour.
	 */
	public Color getForeground() {
		return hasError() ? ERROR_COLOR : super.getForeground();
	}

	/**
	 * Used to test the XDateField
	 * 
	 * @param args
	 *            the command line arguments, these are not used
	 */
	public static void main(String[] args) throws Exception {
		// Set look and feel to system look and feel.
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		// Create the test frame
		JFrame frame = createTestFrame();

		// Pack. Center. Show!
		frame.pack();
		Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
		frame.setLocation(bounds.x + ((bounds.width - frame.getWidth()) / 2),
				bounds.y + ((bounds.height - frame.getHeight()) / 2));
		frame.setVisible(true);
	}


	private static JFrame createTestFrame() {
		final JFrame frame = new JFrame("Test: " + XTextField.class.getName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final XTextField field = new XTextField();
		field.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				update(e);
			}

			public void removeUpdate(DocumentEvent e) {
				update(e);
			}

			public void changedUpdate(DocumentEvent e) {
				update(e);
			}

			private void update(DocumentEvent e) {
				String text = field.getText();
				field.setError(!isEmpty(text) && !isInteger(text));
			}

			private boolean isEmpty(String value) {
				return "".equals(value.trim());
			}

			private boolean isInteger(String value) {
				try {
					Integer.parseInt(value);
					return true;
				} catch (NumberFormatException nfe) {
					return false;
				}
			}

		});
		frame.getContentPane().add(field);
		return frame;
	}


	public Boolean isRegexMatch() {
		Boolean valid = true;
		if (!regex.isEmpty()) {
			valid = getText().matches(regex);
		}
		return valid;
	}

	
	public void setError() {
		if(isNullOrEmpty() && isMandatory) {
			warningLabel.setText(mandatoryText);
		} else {
			warningLabel.setText(invalidText);
		}
		
		warningLabel.setVisible(true);
		setError(true);
		matchesDropDownValue = false;
	}
	
	public void setError(String errorToSet) {
		warningLabel.setText(errorToSet);
		
		warningLabel.setVisible(true);
		setError(true);
	}

	
	public void clearError() {
		setError(false);
		if (warningLabel != null) {
			if(gridBagLayout)
				warningLabel.setText(" ");
			warningLabel.setVisible(gridBagLayout);
		}
		matchesDropDownValue = true;
	}
	

	public void setMandatory(boolean mandatory) {
		this.isMandatory = mandatory;
	}

	//--- Getter/Setter for Mandatory Field text ---
	public void setMandatoryText(String mandatoryText) {
		this.mandatoryText = (mandatoryText == null ? "" : mandatoryText);
	}
	public String getMandatoryText() {
		return mandatoryText;
	}

	//--- Getter/Setter for Invalid Field text ---
	public void setInvalidText(String invalidText) {
		this.invalidText = (invalidText == null ? "" : invalidText);
	}
	public String getInvalidText() {
		return invalidText;
	}

	//--- Setter for both warning text fields ---
	public void setWarningText(String invalidText, String mandatoryText) {
		setInvalidText(invalidText);
		setMandatoryText(mandatoryText);
	}
	
	public boolean isNullOrEmpty() {
		return ((getText() == null) || (getText().isEmpty()));
	}

	public void setEnabledAndFocusable(Boolean enabled) {
		if(enabled) {
			this.setEnabled(true);
			this.setFocusable(true);
		}
		else {
			this.setEnabled(false);
			this.setFocusable(false);
		}
	}
    /**
     * Sets the textfield to upper case (or not depending on what's passed through)
	 */
	public void setUpperCase(boolean toUpperCase) {
       this.toUpperCase = toUpperCase;
	   setCapabilities();
    }
	
	/**
     * Sets the textfield to upper case (or not depending on what's passed through)
	 */
	public void setMaxLength(int maxLimit) {
       this.maxLimit = maxLimit;
	   setCapabilities();
    }
	
	/**
     * Sets the textfield to numeric (or not depending on what's passed through)
	 */
	public void setNumeric(boolean isNumeric) {
       this.isNumeric = isNumeric;
	   setCapabilities();
    }
	
	public void setAlphaNumeric(boolean alphaNumeric) {
		this.isAlphaNumeric = alphaNumeric;
		setCapabilities();
	}
	
	public void setText(boolean isText) {
		this.isText = isText;
		setCapabilities();
	}
	
   /**
     * Sets the capabilities of the field
     * if toUpperCase = true then set text field to be upper case
     * if maxLimit >0 then stop users from typing more than allowed length.
	 *
     */
	 private void setCapabilities() {
	 
		String currentString = this.getText();
		ArrayList<Capability> capabilities = new ArrayList<Capability>();
		if(toUpperCase) {
			capabilities.add(Capability.upperCase());
		}
		if(maxLimit >0) {
			capabilities.add(Capability.limitedText(maxLimit));
		}
		if(isNumeric) {
			capabilities.add(Capability.numeric());
		}
		if(isAlphaNumeric) {
			capabilities.add(Capability.alphaNumeric());
		}
		if(isText) {
			capabilities.add(Capability.plainText());
		}
		 final Document doc = DocumentFactory.newDocument(capabilities.toArray(new Capability[capabilities.size()]));
         this.setDocument(doc);
         this.setText(currentString);

	 }
	 
	 public void setGridBagLayout(boolean gridBagLayout) {
		 this.gridBagLayout = gridBagLayout;
		 warningLabel.setVisible(gridBagLayout);
	 }
	 
	 /**
	  * Needed for Case - specifically Defendant Tab to be able to call 
	  * validation on the field when there's more than 1 defendant 
	  */
	 public void validate(String text) {
		 if(text!=null) {
			boolean rc = text.matches(regex);
			if ((text.length() > 0) && text.matches(regex)) {
				setError(false);
				if(gridBagLayout) 
					warningLabel.setText(" ");
				warningLabel.setVisible(gridBagLayout);
				
			} else if(text.length()>0 && !rc) {
				setError(true);
				warningLabel.setText(invalidText);
				warningLabel.setVisible(true);
			}
		 }
			
	 }
}

