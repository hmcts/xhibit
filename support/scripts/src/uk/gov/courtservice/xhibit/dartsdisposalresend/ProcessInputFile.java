package uk.gov.courtservice.xhibit.dartsdisposalresend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessInputFile {
	
	private String infilename;
	private String outfilename;
	
	public ProcessInputFile(String infilename, String outfilename) {
		this.setInFilename(infilename);
		this.setOutFilename(outfilename);
	}
	
	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length == 2) { // ok
			ProcessInputFile pif = new ProcessInputFile(args[0], args[1]);
			pif.doProcessing();
		} else {
			System.out.println("Usage: java ProcessInputFile <in-filename> <out-filename>");
		}
	}
	
	/**
	 * Begin the task of doing the processing
	 */
	public void doProcessing() {
		if (!validateInputFile()) {
			return;
		}
		
		List<List<String>> records = parseInputFileAndProcess();
		
		if (records.size() > 0) {
			RecordHandler rh = new RecordHandler();
			List<String> insertRecords = rh.generateRecords(records);
			
			GenerateOutputFile gof = new GenerateOutputFile();
			gof.generateOutputFile(insertRecords, getOutFilename());
		}
	}
	
	/**
	 * Validate that the input csv file exists
	 * Validate that the first line of csv file is a header line and that there is more than one line 
	 * 
	 * @param filename
	 */
	public boolean validateInputFile() {
		if (!fileExists(getInFilename())) {
			System.out.println("The file "+getInFilename()+" does not exist.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Assumes file has been validated
	 * Parse the file
	 * 
	 */
	public List<List<String>> parseInputFileAndProcess() {
		
		List<List<String>> records = new ArrayList<List<String>>();
		BufferedReader br = null;
		try {
			String line = "";
			int lineNo = 0;
			boolean headerValid = true;
			
			br = new BufferedReader(new FileReader(getInFilename()));
			line = br.readLine();
			if (line == null) {
				System.out.println("There is no header line in the file "+getInFilename()+". Please check the header is valid.");
				headerValid = false;
			} else if (!headerExists(line)) {
				headerValid = false;
			}
			if (headerValid) {
				lineNo++;
				
				// The first line should be a header line, so start parsing from line 2
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");
					records.add(Arrays.asList(values));
					lineNo++;
				}
				if (lineNo<2) {
					System.out.println("The file "+getInFilename()+" does not have any data to parse.");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return records;
	}
	
	/**
	 * Check if the file exists
	 * 
	 * @param filename
	 * @return
	 */
	public boolean fileExists(String filename) {
		File f = new File(filename);
		if (f.isFile()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check that the header line exists
	 * Header:: CASE,COURT,DEFENDANTNAME,CODE
	 * 
	 * @param filename
	 * @return
	 */
	public boolean headerExists(String headerLine) {
		if (headerLine.equals("CASE,COURT,DEFENDANTNAME,CODE")) {
			return true;
		}
		System.out.println("The header in the file is invalid");
		return false;
	}
	
	/********************/

	public String getInFilename() {
		return infilename;
	}

	public void setInFilename(String infilename) {
		this.infilename = infilename;
	}
	
	public String getOutFilename() {
		return outfilename;
	}

	public void setOutFilename(String outfilename) {
		this.outfilename = outfilename;
	}

}
