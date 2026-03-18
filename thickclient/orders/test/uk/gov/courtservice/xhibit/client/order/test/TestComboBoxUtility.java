///**
// * Created by IntelliJ IDEA.
// * User: hzf3bb
// * Date: Jan 8, 2003
// * Time: 2:21:14 PM
// * To change this template use Options | File Templates.
// */
//package uk.gov.courtservice.xhibit.client.order.test;
//
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.client.order.gui.components.CustomComboBox;
//import uk.gov.courtservice.xhibit.client.order.gui.entry.ComboBoxUtility;
//import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtList;
//import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListFactory;
//
//public class TestComboBoxUtility extends TestCase {
//    private ComboBoxUtility util;
//
//    private CustomComboBox box;
//
//    private JTextField text;
//
//    private JComboBox cb;
//
//    public TestComboBoxUtility(String s) {
//        super(s);
//
//    }
//
//    protected void setUp() throws Exception {
//        CourtList list = CourtListFactory.createCourtList();
//        cb = new JComboBox(list.getCourtValues());
//
//        JFrame frame = new JFrame();
//        JPanel panel = new JPanel();
//
//        panel.add(cb);
//        frame.getContentPane().add(panel);
//        frame.setTitle("Test Address Fields");
//        frame.setSize(200, 200);
//        frame.setVisible(true);
//
//        box = new CustomComboBox(CourtListFactory.createCourtList());
//        text = (JTextField) cb.getEditor().getEditorComponent();
//        util = new ComboBoxUtility();
//    }
//
//    public void testPerformSearch() {
//
//    }
//
//    /*
//     * public void testCheckMatch() { assertTrue(util.checkMatch("London",
//     * "L")); assertTrue(util.checkMatch("London", "Lo"));
//     * assertTrue(util.checkMatch("London", "l"));
//     * assertTrue(util.checkMatch("London", "lo"));
//     * assertTrue(util.checkMatch("Milton Keynes", "mIl"));
//     * assertFalse(util.checkMatch("Luton","Bedford")); }
//     */
//
//    public void searchExact(String search) {
//
//    }
//
//    public void search(String search, int expected) {
//
//    }
//}
//