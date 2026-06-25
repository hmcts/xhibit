package uk.gov.courtservice.xhibit.business.admin.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogsControllerBeanBusinessDelegate;

public class FileValidatorHelper {
	
	private static final Pattern FILE_NAME_PATTERN = 
			Pattern.compile("^CM_\\d{3}_(migration|unmigration)_\\d{8}\\.csv$");
	
	private static final Pattern CASE_NUMBER_PATTERN =
			Pattern.compile("^[A-Za-z]\\d{8}$");
	
	private static final Pattern COURT_ID_PATTERN =
			Pattern.compile("^\\d{3}$");
	
	private static final Pattern MIGRATED_TO_PATTERN = 
			Pattern.compile("^(ARM & CP|ARM)$");
	
	private static final String HEADER_CASE_NUMBER = "CASE NUMBER";
	private static final String HEADER_COURT_ID = "COURT ID";
	private static final String HEADER_MIGRATED_TO_LOCATION = "MIGRATED TO LOCATION";
	private static final String HEADER_COMMON_PLATFORM_URN = "COMMON PLATFORM URN";

	private static final String MESSAGE_CHECKSUM = "SHA-256";
	
	private static final Logger log = CSServices.getLogger(FileValidatorHelper.class);
	
	private CmLogsControllerBeanBusinessDelegate cmLogsControllerBeanBusinessDelegate;
	
	public FileValidatorHelper(CmLogsControllerBeanBusinessDelegate cmLogsControllerBeanBusinessDelegate) {
		this.cmLogsControllerBeanBusinessDelegate = cmLogsControllerBeanBusinessDelegate;
	}
	
	public FileProcessingResult validateFile(FileItem file) {
		List<String> errors = new ArrayList<String>();		
		
		// 1) Check if this is unmigration
		String fileName = file.getName();
		boolean isUnmigration = false;
		
		if (fileName.contains("unmigration")) {
			isUnmigration = true;
		}
		
		// 2) Validate file metadata to ensure file size doesn't exceed max size and file is a CSV.
		validateFileMetadata(file, errors);		
		
		// 3) Read file stream and validate rows + calculate checksum
		FileProcessingResult result = validateFileContents(file, errors, isUnmigration);
		
		return result;
	}
	
	public String validateIsCsv(FileItem file) {
		if (!file.getName().contains(".csv")) {
			return "File type must be text/csv or application/csv";
		}
		return null;
	}
	
	private void validateFileMetadata(FileItem file, List<String> errors) {
		String fileName = file.getName();
		long fileSizeBytes = file.getSize();
		long fileSizeMb = cmLogsControllerBeanBusinessDelegate.getMaxFileSizeMb();
		long maxFileSizeBytes = fileSizeMb * 1024L * 1024L;
		
		// validate file name
		if (!FILE_NAME_PATTERN.matcher(fileName).matches()) {
			errors.add("Filename must match CM_xxx_migration_yyyyMMdd.csv or CM_xxx_unmigration_yyyyMMdd.csv");
		} else  {
			validateFileDate(fileName, errors);
		}
		
		if (fileSizeBytes > maxFileSizeBytes) {
			errors.add("File size exceeds maximum allowed size of " + maxFileSizeBytes + " bytes");
		}
	}
	
	private void validateFileDate(String fileName, List<String> errors) {
		String datePart = fileName.substring(fileName.length() -12, fileName.length() -4);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sdf.setLenient(false);
		
		try {
			sdf.parse(datePart);
		}
		catch (Exception e) {
			errors.add("Filename date must be a valid yyyyMMdd date");
		}
	}
	
	private FileProcessingResult validateFileContents(FileItem file, List<String> errors, boolean isUnmigration) {
		// streams used to read the uploaded file and calculate checksum
		InputStream input = null;
		DigestInputStream digestInput = null;
		BufferedReader reader = null;
		
		// for storing full file content in the DB as per spec
		StringBuilder content = new StringBuilder();
		
		try {
			// DigestInputStream will automatically update this digest as bytes are read from the uploaded file stream
			MessageDigest md = MessageDigest.getInstance(MESSAGE_CHECKSUM);
			
			// open stream and wrap so checksum is calculated while reading. digestInput will hash the original uploaded file bytes
			input = file.getInputStream();
			digestInput = new DigestInputStream(input, md);
			reader = new BufferedReader(new InputStreamReader(digestInput, "UTF-8"));
			
			String line;
			int lineNumber = 0;
			int dataRowCount = 0;
			boolean firstLineProcessed = false;
			int maxRows = cmLogsControllerBeanBusinessDelegate.getMaxFileRows();
			
			// read csv line by line
			while ((line = reader.readLine()) != null) {
				lineNumber ++;
				content.append(line).append("\n");
				
				// reject blank lines
				if (line == null || line.trim().length() == 0) {
					errors.add("Blank line found at line " + lineNumber);
					continue;
				}
				
				// parse csv column and skip validation if parsing failed
				List<String> columns = parseCsvLine(line, lineNumber, errors);
				if (!errors.isEmpty() || columns == null) {
					continue;
				}
				
				// first non-empty line is header row
				if (!firstLineProcessed) {
					validateHeaders(columns, errors);
					firstLineProcessed = true;
					continue;
				}
				
				dataRowCount++;
				
				// enforce maximum allowed row count
				if (dataRowCount > maxRows) {
					errors.add("File exceeds maximum allowed row count of " + maxRows);
					break;
				}
				
				// validate row contents
				validateRow(columns, lineNumber, errors, isUnmigration);
			}
			
			if (lineNumber == 0) {
				errors.add("CSV file is empty");
			} else if (lineNumber == 1) {
				errors.add("No migration data included");
			}
			
			// only generate checksum if validation has succeeded
			// digest contains checksum of original uploaded file bytes
			String checkSum = null;
			if (errors.isEmpty()) {
				byte[] hash = md.digest();
				checkSum = bytesToHex(hash);
			}
			
			return new FileProcessingResult(errors, dataRowCount, content.toString(), checkSum);
			
		} catch (Exception e) {
			log.error("FileValidatorHelper: Unable to read file.", e);
			errors.add("Unable to validate file contents");
			return new FileProcessingResult(errors, 0, null, null);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				else if (digestInput != null) {
					digestInput.close();
				}
				else if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				log.error("FileValidatorHelper: Unable to close CSV reader.", e);
			}
		}
	}
	
	private List<String> parseCsvLine(String line, int lineNumber, List<String> errors) {
		List<String> values = new ArrayList<String>();
		StringBuilder current = new StringBuilder();
		
		boolean inQuotes = false;
		boolean quotedField = false;
		boolean justClosedQuote = false;
		
		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			
			if (inQuotes) {
				if (ch == '"') {
					if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
						current.append('"');
						i++;
					} else {
						inQuotes = false;
						justClosedQuote = true;
					}
				} else {
					current.append(ch);
				}
				continue;
			}
			
			if (ch == '"') {
				if (current.length() == 0) {
					inQuotes = true;
					quotedField = true;
					justClosedQuote = false;
				} else {
					errors.add("Line " + lineNumber + " invalid CSV quoting. Check that quoted fields are closed correctly");
					return values;
				}
			} else if (ch == ',') {
				values.add(current.toString());
				current.setLength(0);
				quotedField = false;
				justClosedQuote = false;
			} else {
				if (justClosedQuote) {
					errors.add("Line " + lineNumber + " invalid CSV quoting. Check that quoted fields are closed correctly");
					return null;
				}
				current.append(ch);
			}
		}
		
		if (inQuotes) {
			errors.add("Line " + lineNumber + " unterminated quote field");
			return null;
		}
		
		values.add(current.toString());
		return values;
	}
	
	private void validateHeaders(List<String> columns, List<String> errors) {
		if (columns.size() != 4) {
			errors.add("Header row must contain exactly 4 columns");
			return;
		}
		
		boolean headersValid = HEADER_CASE_NUMBER.equals(columns.get(0)) 
						 &&	HEADER_COURT_ID.equals(columns.get(1))
						 && HEADER_MIGRATED_TO_LOCATION.equals(columns.get(2))
						 && HEADER_COMMON_PLATFORM_URN.equals(columns.get(3));
		
		if (!headersValid) {
			errors.add("Incorrect header values. It must match the format: " + HEADER_CASE_NUMBER + "," + HEADER_COURT_ID + "," + HEADER_MIGRATED_TO_LOCATION + "," + HEADER_COMMON_PLATFORM_URN);
		}	
	}
	
	private void validateRow(List<String> columns, int lineNumber, List<String> errors, boolean isUnmigration) {
		if (columns.size() != 4) {
			errors.add("Line " + lineNumber + " must contain exactly 4 columns");
			return;
		}
		
		String caseNumber = columns.get(0);
		String courtId = columns.get(1);
		String migratedToLocation = columns.get(2);
		
		if (caseNumber.length() == 0) {
			errors.add("Line " + lineNumber + " " + HEADER_CASE_NUMBER + " is required");
		} else if (!CASE_NUMBER_PATTERN.matcher(caseNumber).matches()) {
			errors.add("Line " + lineNumber + " " + HEADER_CASE_NUMBER + " must be one letter followed by 8 digits");
		}
		
		if (courtId.length() == 0) {
			errors.add("Line " + lineNumber + " " + HEADER_COURT_ID + " is required");
		} else if (!COURT_ID_PATTERN.matcher(courtId).matches()) {
			errors.add("Line " + lineNumber + " " + HEADER_COURT_ID + " must be a 3 digit CREST court id");
		}
		
		if (!isUnmigration) {
			if (migratedToLocation.length() == 0) {
				errors.add("Line " + lineNumber + " " + HEADER_MIGRATED_TO_LOCATION + " is required");
			} else if (!MIGRATED_TO_PATTERN.matcher(migratedToLocation).matches()) {
				errors.add("Line " + lineNumber + " " + HEADER_MIGRATED_TO_LOCATION + " must be 'ARM' or 'ARM & CP'");
			}
		}
	}
	
	private String bytesToHex(byte[] hash) {
		StringBuilder hex = new StringBuilder();
		
		for (int i = 0; i < hash.length; i++) {
			String part = Integer.toHexString(hash[i] & 0xff);
			if (part.length() == 1) {
				hex.append(0);
			}
			hex.append(part);
		}
		
		return hex.toString();
	}
}
