
import java.io.IOException;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

/**
 * Check that all the characters in the file are ascii 
 */ 
public class TranslationCheck {

    /**
     * Executed from the command line
     * @param args the command line arguments
     */
    private static void main(String[] args) throws IOException {
        if(args.length == 1) {
            main(args[0], null);
        } else if(args.length == 2) {
            main(args[0], args[1]);
        } else {
            System.out.println("Usage: java TranslationCheck <filename> [<encoding>]");            
        }
    }

    public static void main(String file, String enc) throws IOException {
        Reader reader = (enc == null) ? new InputStreamReader(new FileInputStream(file)) 
                                      : new InputStreamReader(new FileInputStream(file),enc);
        try {
            main(reader);
        } finally {
            try {
                reader.close();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void main(Reader reader) throws IOException {
        int l = 1;
        int p = 0;

        for(int c = reader.read(); c != -1; c = reader.read()) {            

            // Check Char
            if(c > 128) {
                System.out.println("Char " + l + "/" + p + " '" + ((char)c) + "' (" + c + ") is out of range.");
            }

            // Update line and column
            if(c == '\n') {
                l += 1;
                p = 0;
            } else {
                p += 1;
            }
        }
    }
}
