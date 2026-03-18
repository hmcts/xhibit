package uk.gov.courtservice.xhibit.dartsdisposalresend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GenerateOutputFile {
	
	/**
	 * Check whether the location is valid and that the filename can be created (i.e. doesnt already exist) 
	 */
	public boolean validateOutputLocation() {
		return false;
	}
	
	public void generateOutputFile(List<String> dbRecordInsertText, String filename) {
		
		BufferedWriter bw = null;
		try {
			File f = new File(filename);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter fw;

			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);

			int i = 0;
			while (i<dbRecordInsertText.size()) {
				String thisLine = dbRecordInsertText.get(i)+"\n";
				System.out.print(thisLine);
				bw.write(thisLine);
				i++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
