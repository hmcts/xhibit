package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentAdapter;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.util.XDateField;


/**
 * <p>
 * Title: V23DataComponent
 * </p>
 * <p>
 * Description: Display combo box to select between abused substance 
 * </p>
 * <p>
 * Company: CGI UK Ltd
 * </p>
 * 
 * @version $Id: V23DataComponent.java,v 1.0 2021/01/28 13:46:40  Exp mcarthurr$
 */
public class V23DataComponent extends ComboBoxDataComponent {

	private static final long serialVersionUID = 1L;
	private static final String V23_COMBO_VALUE0 = "v23DataComponent.value0";
	private static final String V23_COMBO_VALUE1 = "v23DataComponent.value1";
	private static final String V23_COMBO_VALUE2 = "v23DataComponent.value2";
	private static final String V23_COMBO_VALUE3 = "v23DataComponent.value3";
	private static final String V23_COMBO_VALUE4 = "v23DataComponent.value4";
	private static final String V23_COMBO_VALUE5 = "v23DataComponent.value5";
	private static final String V23_COMBO_VALUE6 = "v23DataComponent.value6";
	private static final String V23_COMBO_VALUE7 = "v23DataComponent.value7";
	private static final String V23_COMBO_VALUE8 = "v23DataComponent.value8";
	private static final String V23_COMBO_VALUE9 = "v23DataComponent.value9";
	private static final String V23_COMBO_VALUE10 = "v23DataComponent.value10";
	private static final String V23_COMBO_VALUE11 = "v23DataComponent.value11";
	private static final String V23_COMBO_VALUE12 = "v23DataComponent.value12";
	private static final String V23_COMBO_VALUE13 = "v23DataComponent.value13";
	private static final String V23_COMBO_VALUE14 = "v23DataComponent.value14";
	private static final String V23_COMBO_VALUE15 = "v23DataComponent.value15";
	private static final String V23_COMBO_VALUE16 = "v23DataComponent.value16";
	private static final String V23_COMBO_VALUE17 = "v23DataComponent.value17";
	private static final String V23_COMBO_VALUE18 = "v23DataComponent.value18";
	private static final String V23_COMBO_VALUE19 = "v23DataComponent.value19";
	private static final String V23_COMBO_VALUE20 = "v23DataComponent.value20";
	
	
	

    /**
     * The options
     */
    private static final String[] SINGLE_OPTIONS = new String[] {DisposalUtil.getResource(V23_COMBO_VALUE0), DisposalUtil.getResource(V23_COMBO_VALUE1), 
    															DisposalUtil.getResource(V23_COMBO_VALUE2), DisposalUtil.getResource(V23_COMBO_VALUE3), 
    															DisposalUtil.getResource(V23_COMBO_VALUE4), DisposalUtil.getResource(V23_COMBO_VALUE5), 
    															DisposalUtil.getResource(V23_COMBO_VALUE6), DisposalUtil.getResource(V23_COMBO_VALUE7), 
    															DisposalUtil.getResource(V23_COMBO_VALUE8), DisposalUtil.getResource(V23_COMBO_VALUE9), 
    															DisposalUtil.getResource(V23_COMBO_VALUE10), DisposalUtil.getResource(V23_COMBO_VALUE11), 
    															DisposalUtil.getResource(V23_COMBO_VALUE12), DisposalUtil.getResource(V23_COMBO_VALUE13),
    															DisposalUtil.getResource(V23_COMBO_VALUE14), DisposalUtil.getResource(V23_COMBO_VALUE15), 
    															DisposalUtil.getResource(V23_COMBO_VALUE16), DisposalUtil.getResource(V23_COMBO_VALUE17), 
    															DisposalUtil.getResource(V23_COMBO_VALUE18), DisposalUtil.getResource(V23_COMBO_VALUE19), 
    															DisposalUtil.getResource(V23_COMBO_VALUE20)};

    private static final String[] PLURAL_OPTIONS = new String[] {DisposalUtil.getResource(V23_COMBO_VALUE0), DisposalUtil.getResource(V23_COMBO_VALUE1), 
																DisposalUtil.getResource(V23_COMBO_VALUE2), DisposalUtil.getResource(V23_COMBO_VALUE3), 
																DisposalUtil.getResource(V23_COMBO_VALUE4), DisposalUtil.getResource(V23_COMBO_VALUE5), 
																DisposalUtil.getResource(V23_COMBO_VALUE6), DisposalUtil.getResource(V23_COMBO_VALUE7), 
																DisposalUtil.getResource(V23_COMBO_VALUE8), DisposalUtil.getResource(V23_COMBO_VALUE9), 
																DisposalUtil.getResource(V23_COMBO_VALUE10), DisposalUtil.getResource(V23_COMBO_VALUE11), 
																DisposalUtil.getResource(V23_COMBO_VALUE12), DisposalUtil.getResource(V23_COMBO_VALUE13),
																DisposalUtil.getResource(V23_COMBO_VALUE14), DisposalUtil.getResource(V23_COMBO_VALUE15), 
																DisposalUtil.getResource(V23_COMBO_VALUE16), DisposalUtil.getResource(V23_COMBO_VALUE17), 
																DisposalUtil.getResource(V23_COMBO_VALUE18), DisposalUtil.getResource(V23_COMBO_VALUE19), 
																DisposalUtil.getResource(V23_COMBO_VALUE20)};

    /**
     * Construct a new instance
     */
    public V23DataComponent() {
        super();

        // Determine the larger preferred size to stop component resizing.
        // Note leaves the component with the default plural model!

        // Get UI Sizes
        setModel(SINGLE_OPTIONS);
        Dimension singlePreferedSize = getPreferredSize();
        setModel(PLURAL_OPTIONS);
        Dimension pluralPreferedSize = getPreferredSize();

        // Fix preferred size to largest
        setPreferredSize(max(singlePreferedSize, pluralPreferedSize));
    }

    protected int getIndex(String data) {
        // If data null or unrecognised set selected to first (blank item)
        if (data != null) {
            for (int i = 0; i < SINGLE_OPTIONS.length; i++) {
                // Data can be capitilised first, all or none
                if (data.equalsIgnoreCase(SINGLE_OPTIONS[i])) {
                    return i;
                }
            }
            for (int i = 0; i < PLURAL_OPTIONS.length; i++) {
                // Data can be capitilised first, all or none
                if (data.equalsIgnoreCase(PLURAL_OPTIONS[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return DisposalUtil.getToolTipText("v20DataComponentToolTip");
    }

    /**
     * Set the previous data component.
     */
    public void setPreviousDataComponent(DataComponent previousDataComponent) {
        if (previousDataComponent != null) {
            setModel(delegate.isSingular(previousDataComponent) ? SINGLE_OPTIONS : PLURAL_OPTIONS);

            previousDataComponent.addDataComponentListener(new DataComponentAdapter() {
                public void dataChanged(DataComponentEvent e) {
                    setModel(delegate.isSingular(e.getDataComponent()) ? SINGLE_OPTIONS : PLURAL_OPTIONS);
                }
            });
        }
    }

    /**
     * Calculate a dimension based on the maximum height and width of the two
     * parameters.
     * 
     * @param d1
     *            the first dimension
     * @param d2
     *            the second dimension
     * @return the max dimension!
     */
    private static Dimension max(Dimension d1, Dimension d2) {
        return new Dimension(Math.max(d1.width, d2.width), Math.max(d1.height, d2.height));
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
        frame.setLocation(bounds.x + ((bounds.width - frame.getWidth()) / 2), bounds.y
                + ((bounds.height - frame.getHeight()) / 2));
        frame.setVisible(true);
    }

    private static JFrame createTestFrame() {
        JFrame frame = new JFrame("Test: " + XDateField.class.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComboBox combo = new JComboBox(SINGLE_OPTIONS);
        combo.setBackground(Color.red);
        frame.getContentPane().setLayout(new GridLayout(2, 1, 4, 4));
        frame.getContentPane().add(combo);
        JTextField field = new JTextField("Foo Bar Car");
        frame.getContentPane().add(field);
        field.requestFocus();
        return frame;
    }

}
