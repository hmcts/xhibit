package uk.gov.courtservice.xhibit.client.order.gui.helpers;

public class radioButtonHelper {

    public String crown = "";

    private String magistrate = "";

    private String youth = "";

    private String crownRef = "";

    private String magistrateRef = "";

    private String youthRef = "";

    public int checkAttributes(String crown, String magistrate, String youth) {

        if (crown.equals("true") && (magistrate.equals("true") && (youth.equals("true")))) {
            return 1;
        }
        if (crown.equals("true") && (magistrate.equals("true") && (youth.equals("false")))) {
            return 2;
        }

        if (crown.equals("false") && (magistrate.equals("true") && (youth.equals("false")))) {
            return 3;
        }

        if (crown.equals("false") && (magistrate.equals("false") && (youth.equals("true")))) {
            return 4;
        }
        return 0;
    }

}
