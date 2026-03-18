

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * ImportKey.java
 * 
 * <p>
 * This class imports a key and a certificate chain into a keystore. If the
 * keystore is already present it is ammended. Both the key and the certificate
 * files must be in <code>DER</code>-format. The key must be encoded with
 * <code>PKCS#8</code>-format. The certificates must be encoded in
 * <code>X.509</code>-format.
 * </p>
 */
public class ImportKey {

	public static void main(String args[]) throws Exception {
		// Declare Arguments
		boolean verbose = false;
		String keystore = null;
		char[] storepass = "changeit".toCharArray();
		String storetype = "JKS";
		String alias = null;
		char[] keypass = "changeit".toCharArray();
		String file = null;
		String[] certfiles = null;

		// Parse Arguments
		for (int i = 0; i < args.length; i++) {
			if ("-keystore".equals(args[i])) {
				if (++i < args.length) {
					keystore = args[i];
				} else {
					usage("Error: -keystore argument requires keystore.");
				}
			} else if ("-storepass".equals(args[i])) {
				if (++i < args.length) {
					storepass = args[i].toCharArray();
				} else {
					usage("Error: -storepass argument requires storepass.");
				}
			} else if ("-alias".equals(args[i])) {
				if (++i < args.length) {
					alias = args[i];
				} else {
					usage("Error: -alias argument requires alias.");
				}
			} else if ("-keypass".equals(args[i])) {
				if (++i < args.length) {
					keypass = args[i].toCharArray();
				} else {
					usage("Error: -keypass argument requires keypass.");
				}
			} else if ("-file".equals(args[i])) {
				if (++i < args.length) {
					file = args[i];
				} else {
					usage("Error: -file argument requires file.");
				}
			} else if ("-certfiles".equals(args[i])) {
				if (++i < args.length) {
					certfiles = args[i].split(",");
				} else {
					usage("Error: -certfiles argument requires certfiles.");
				}
			} else if ("-v".equals(args[i])) {
				verbose = true;
			} else if ("-?".equals(args[i])) {
				usage(null);
			} else {
				usage("Error: " + args[i] + " not recognised.");
			}
		}

		// Check Arguments
		StringBuffer missingArguments = new StringBuffer();
		if (keystore == null) {
			missingArguments.append(" -keystore");
		}
		if (alias == null) {
			missingArguments.append(" -alias");
		}
		if (file == null) {
			missingArguments.append(" -file");
		}
		if (certfiles == null) {
			missingArguments.append(" -certfiles");
		}
		if (missingArguments.length() > 0) {
			usage("Error: the following arguments are required" + missingArguments);
		}
		importKey(verbose, keystore, storepass, storetype, alias, keypass, file, certfiles);
	}

	public static final void importKey(final boolean verbose, final String keyStoreFileName, final char[] storePassword,
			final String storeType, final String keyAlias, final char[] keyPassword, final String keyFileName, final String[] certificateFileNames)
			throws Exception {

		// Load keystore if it exists
		File keyStoreFile = new File(keyStoreFileName);
		KeyStore keyStore = KeyStore.getInstance(storeType);
		if (keyStoreFile.exists()) {
			InputStream keyStoreIn = new BufferedInputStream(new FileInputStream(keyStoreFile));
			try {
				keyStore.load(keyStoreIn, storePassword);
				if (verbose) {
					System.out.println("Info: Loaded keystore \"" + keyStoreFile.getAbsolutePath() + "\".");
				}
			} finally {
				closeQuietly(keyStoreIn);
			}
		} else {
			keyStore.load(null, storePassword);
			if (verbose) {
				System.out.println("Info: Initalised keystore \"" + keyStoreFile.getAbsolutePath() + "\".");
			}
		}
				
		// Load key
		File keyFile = new File(keyFileName);
		byte[] keyData = new byte[(int)keyFile.length()];
		InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
		try {
			readFully(keyIn, keyData);
		} finally {
			closeQuietly(keyIn);
		}
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		if(verbose) {
			System.out.println("Info: Loaded key \"" + keyFile.getAbsolutePath() + "\".");
		}
		
		// Load Certificates
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		Certificate[] certificates = new Certificate[certificateFileNames.length];
		for(int i = 0; i < certificateFileNames.length; i++) {
			File certificateFile = new File(certificateFileNames[i]);
			InputStream certificateIn = new BufferedInputStream(new FileInputStream(certificateFile));
			try {
				certificates[i] = certificateFactory.generateCertificate(certificateIn);
			} finally {
				closeQuietly(certificateIn);
			}
			if(verbose) {
				System.out.println("Info: Loaded certifcate " + i + " \"" + keyFile.getAbsolutePath() + "\".");
			}
		}

		// Add entry		
		keyStore.setKeyEntry(keyAlias, key, keyPassword, certificates);
		if(verbose) {
			System.out.println("Info: Added key entry \"" + keyAlias + "\".");
		}

		// Save keystore
		OutputStream out = new BufferedOutputStream(new FileOutputStream(keyStoreFile));
		try {
			keyStore.store(out, storePassword);
			if (verbose) {
				System.out.println("Info: Saved keystore \"" + keyStoreFile.getAbsolutePath() + "\".");
			}
		} finally {
			closeQuietly(out);
		}

	}

	public static final void usage(final String message) {
		if (message != null) {
			System.out.println(message);
		}
		System.out.println("Usage: java " + ImportKey.class.getName());
		System.out.println("   [-v] [-alias <alias>] [-keypass <keypass>]");
		System.out.println("   [-file <pkcs8key>] -certfiles [<certfile0,certfile1 ... certfileN>]");
		System.out.println("   [-keystore <keystore>] [-storepass <storepass>] [-storetype <storetype>]");
		System.exit(1);
	}

	protected static void closeQuietly(final InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (final IOException ioe) {
			// Do Nothing
		}
	}

	protected static void closeQuietly(final OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (final IOException ioe) {
			// Do Nothing
		}
	}
	
	protected static void readFully(final InputStream in, final byte[] b) throws IOException {
		for(int read = in.read(b); read != b.length; read += in.read(b, read, b.length - read));
	}

}
