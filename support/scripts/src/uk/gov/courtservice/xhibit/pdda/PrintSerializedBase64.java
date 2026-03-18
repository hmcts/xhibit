package uk.gov.courtservice.xhibit.pdda;

/* PrintSerializedBase64.java
*
* Java 1.6 compatible.
*
* Usage:
*   javac PrintSerializedBase64.java
*   java PrintSerializedBase64 <file-or-base64-string>
*
* Examples:
*   java PrintSerializedBase64 /tmp/myObject.ser
*   java PrintSerializedBase64 SGVsbG8s...  (pass base64 on command line)
*   echo "SGVsbG8s..." | java PrintSerializedBase64 -   (read base64 from stdin)
*
* Behavior:
*  - If the argument is '-' -> read all bytes/text from stdin.
*  - If the argument is an existing filename -> read file bytes as-is.
*  - Otherwise: if the argument looks like Base64 (only A-Z a-z 0-9 + / = and length>16),
*    attempt to decode it as Base64 and use those bytes.
*  - If decoding fails, the program will try to treat the string as a filename and fail clearly.
*
* The rest of the program attempts to deserialize and reflectively dump the object graph
* (including checking for CourtRoomIdentifier fields). If deserialization cannot happen,
* it falls back to a printable-string + FQCN heuristic scan.
*/

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Character;
import java.lang.reflect.Array;

//DatatypeConverter is available in Java 6 (JAXB). We'll try it first, otherwise fall back.
import javax.xml.bind.DatatypeConverter;

public class PrintSerializedBase64 {

   public static void main(String[] args) {
       if (args.length != 1) {
           System.err.println("Usage: java PrintSerializedBase64 <file-or-base64-string-or->");
           System.exit(1);
       }

       String arg = args[0];
       byte[] data = null;

       try {
           if ("-".equals(arg)) {
               // read stdin completely (could be raw bytes or base64 ASCII)
               data = readAllBytesFromStdin();
           } else {
               File f = new File(arg);
               if (f.exists() && f.isFile()) {
                   data = readAllBytes(f);
               } else {
                   // heuristically decide if arg looks like base64
                   if (looksLikeBase64(arg)) {
                       try {
                           data = decodeBase64(arg);
                       } catch (Exception e) {
                           System.err.println("Attempt to decode argument as Base64 failed: " + e);
                           // try to interpret as filename (to give clearer error message)
                           System.err.println("Also attempted to treat argument as filename and file not found: " + arg);
                           System.exit(2);
                       }
                   } else {
                       // attempt decode anyway (maybe the user omitted padding/newlines)
                       try {
                           data = decodeBase64(arg);
                       } catch (Exception e) {
                           System.err.println("Argument does not look like a file and Base64 decode failed: " + e);
                           System.err.println("If you intended to pass a filename, ensure the file exists; if you passed Base64, ensure it's valid ASCII Base64.");
                           System.exit(3);
                       }
                   }
               }
           }
       } catch (IOException ioe) {
           System.err.println("I/O error while reading input: " + ioe);
           System.exit(4);
       }

       if (data == null) {
           System.err.println("No input data available.");
           System.exit(5);
       }

       // Now attempt deserialization first (works if classes are available on classpath)
       try {
           ByteArrayInputStream bais = new ByteArrayInputStream(data);
           ObjectInputStream ois = new ObjectInputStream(bais);
           Object root = ois.readObject();
           ois.close();
           System.out.println("=== Deserialization SUCCESS: root class = " + (root == null ? "null" : root.getClass().getName()) + " ===");
           dumpObject(root, 0, new IdentityHashMap());
           System.out.println();
           findAndPrintCourtRoomIdentifier(root);
           return;
       } catch (Throwable t) {
           System.out.println("Deserialization failed or not possible in this JVM/classpath.");
           System.out.println("Reason: " + t.getClass().getName() + " - " + t.getMessage());
           System.out.println("Falling back to byte-level scanning for class names and nearby text...");
           System.out.println();
       }

       // Fallback: byte-level scan for readable strings and FQCNs
       try {
           List<String> strings = extractPrintableStrings(data, 4);
           printLikelyClassNames(strings);
           heuristicSearchNearby(data, "CourtRoomIdentifier", 200);
           heuristicSearchNearby(data, "DisplayablePublicNoticeValue", 200);
       } catch (Exception e) {
           System.err.println("Error in fallback scanning: " + e);
           e.printStackTrace();
       }
   }

   /* -------------------- Input helpers -------------------- */

   private static byte[] readAllBytes(File f) throws IOException {
       FileInputStream fis = new FileInputStream(f);
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] buf = new byte[8192];
       int r;
       while ((r = fis.read(buf)) != -1) baos.write(buf, 0, r);
       fis.close();
       return baos.toByteArray();
   }

   private static byte[] readAllBytesFromStdin() throws IOException {
       InputStream in = System.in;
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] buf = new byte[8192];
       int r;
       while ((r = in.read(buf)) != -1) baos.write(buf, 0, r);
       in.close();
       byte[] raw = baos.toByteArray();

       // If stdin looks like ASCII-base64 (printable), attempt to decode; otherwise return bytes as-is.
       String asText = new String(raw, "UTF-8").trim();
       if (looksLikeBase64(asText)) {
           try {
               return decodeBase64(asText);
           } catch (Exception e) {
               // fallback: return raw bytes
               return raw;
           }
       } else {
           return raw;
       }
   }

   private static boolean looksLikeBase64(String s) {
       if (s == null) return false;
       String t = s.trim();
       // remove whitespace/newlines
       String noWs = t.replaceAll("\\s+", "");
       if (noWs.length() < 16) return false; // too short to be a serialized base64 blob
       // Base64 only contains A-Z a-z 0-9 + / and possibly = padding
       return noWs.matches("^[A-Za-z0-9+/=]+$");
   }

   private static byte[] decodeBase64(String s) throws Exception {
       String noWs = s.replaceAll("\\s+", "");
       // Try DatatypeConverter (Java 6)
       try {
           return DatatypeConverter.parseBase64Binary(noWs);
       } catch (Throwable t) {
           // Fallback: small internal decoder (robust)
           return base64DecodeFallback(noWs);
       }
   }

   // Simple fallback base64 decoder (works for standard base64, no URL-safe)
   private static byte[] base64DecodeFallback(String s) throws Exception {
       final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
       // build reverse map
       int[] rev = new int[256];
       for (int i = 0; i < rev.length; i++) rev[i] = -1;
       for (int i = 0; i < chars.length(); i++) rev[chars.charAt(i)] = i;
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       int val = 0, valb = -8;
       for (int i = 0; i < s.length(); i++) {
           char c = s.charAt(i);
           if (c == '=') break;
           int idx = (c < 256) ? rev[c] : -1;
           if (idx == -1) {
               throw new IllegalArgumentException("Invalid base64 character: " + c);
           }
           val = (val << 6) | idx;
           valb += 6;
           if (valb >= 0) {
               baos.write((val >> valb) & 0xFF);
               valb -= 8;
           }
       }
       return baos.toByteArray();
   }

   /* ------------------ Part A: Reflection-based object dumper ------------------ */

   private static void dumpObject(Object o, int depth, IdentityHashMap seen) {
       try {
           if (o == null) {
               indent(depth); System.out.println("null");
               return;
           }
           if (seen.containsKey(o)) {
               indent(depth); System.out.println("<already-seen " + o.getClass().getName() + ">");
               return;
           }
           seen.put(o, null);

           Class c = o.getClass();
           indent(depth); System.out.println(c.getName() + " {");

           if (c.isArray()) {
               int len = Array.getLength(o);
               indent(depth+1); System.out.println("array length=" + len);
               for (int i=0;i<len;i++) dumpObject(Array.get(o,i), depth+2, seen);
               indent(depth); System.out.println("}");
               return;
           }

           if (o instanceof Collection) {
               Collection coll = (Collection) o;
               indent(depth+1); System.out.println("Collection size=" + coll.size());
               for (Iterator it = coll.iterator(); it.hasNext();) dumpObject(it.next(), depth+2, seen);
               indent(depth); System.out.println("}");
               return;
           }

           if (o instanceof Map) {
               Map m = (Map) o;
               indent(depth+1); System.out.println("Map size=" + m.size());
               for (Iterator it = m.entrySet().iterator(); it.hasNext();) {
                   Map.Entry e = (Map.Entry) it.next();
                   indent(depth+2); System.out.println("Key:");
                   dumpObject(e.getKey(), depth+3, seen);
                   indent(depth+2); System.out.println("Value:");
                   dumpObject(e.getValue(), depth+3, seen);
               }
               indent(depth); System.out.println("}");
               return;
           }

           if (o instanceof String || o instanceof Number || o instanceof Boolean || o instanceof Character) {
               indent(depth+1); System.out.println("value = " + o.toString());
               indent(depth); System.out.println("}");
               return;
           }

           Class cc = c;
           while (cc != null) {
               Field[] fields = cc.getDeclaredFields();
               AccessibleObject.setAccessible(fields, true);
               for (int i=0;i<fields.length;i++) {
                   Field f = fields[i];
                   if (Modifier.isStatic(f.getModifiers())) continue;
                   Object val;
                   try { val = f.get(o); } catch (Throwable tt) { val = "<<unreadable: " + tt + ">>"; }
                   indent(depth+1);
                   System.out.println(f.getName() + " (" + f.getType().getSimpleName() + ") = ");
                   dumpObject(val, depth+2, seen);
               }
               cc = cc.getSuperclass();
           }
           indent(depth); System.out.println("}");
       } catch (Throwable t) {
           indent(depth); System.out.println("<<dump failed: " + t + ">>");
       }
   }

   private static void indent(int n) {
       for (int i=0;i<n;i++) System.out.print("  ");
   }

   private static void findAndPrintCourtRoomIdentifier(Object root) {
       try {
           List found = new ArrayList();
           collectIf(root, found, new IdentityHashMap());
           if (found.isEmpty()) {
               System.out.println("No objects with simple class name 'CourtRoomIdentifier' were found during reflective traversal.");
               return;
           }
           System.out.println("=== Found " + found.size() + " object(s) named 'CourtRoomIdentifier' ===");
           for (int i=0;i<found.size();i++) {
               Object cri = found.get(i);
               System.out.println("Instance " + (i+1) + ": " + safeToString(cri));
               Field[] fields = cri.getClass().getDeclaredFields();
               AccessibleObject.setAccessible(fields, true);
               for (int j=0;j<fields.length;j++) {
                   Field f = fields[j];
                   Object val;
                   try { val = f.get(cri); } catch (Throwable tt) { val = "<<unreadable>>"; }
                   if (val == null) {
                       System.out.println("  Field " + f.getName() + " -> null");
                   } else if (val.getClass().isArray()) {
                       int len = Array.getLength(val);
                       System.out.println("  Field " + f.getName() + " is array, length=" + len);
                       for (int k=0;k<len;k++) {
                           Object el = Array.get(val,k);
                           System.out.println("    [" + k + "] -> " + safeToString(el));
                       }
                   } else if (val instanceof Collection) {
                       Collection col = (Collection) val;
                       System.out.println("  Field " + f.getName() + " is Collection, size=" + col.size());
                       int idx=0;
                       for (Iterator it = col.iterator(); it.hasNext(); ) {
                           System.out.println("    [" + (idx++) + "] -> " + safeToString(it.next()));
                       }
                   } else {
                       System.out.println("  Field " + f.getName() + " -> " + safeToString(val));
                   }
               }
           }
       } catch (Throwable t) {
           System.out.println("Error while searching for CourtRoomIdentifier reflectively: " + t);
       }
   }

   // collect objects whose simple class name == "CourtRoomIdentifier"
   private static void collectIf(Object root, List out, IdentityHashMap seen) {
       if (root == null) return;
       if (seen.containsKey(root)) return;
       seen.put(root, null);
       try {
           if (root.getClass().getSimpleName().equals("CourtRoomIdentifier")) out.add(root);
       } catch (Throwable t) { /* ignore */ }

       Class c = root.getClass();
       if (c.isArray()) {
           int len = Array.getLength(root);
           for (int i=0;i<len;i++) collectIf(Array.get(root,i), out, seen);
           return;
       }
       if (root instanceof Collection) {
           for (Iterator it = ((Collection)root).iterator(); it.hasNext();) collectIf(it.next(), out, seen);
           return;
       }
       if (root instanceof Map) {
           for (Iterator it = ((Map)root).entrySet().iterator(); it.hasNext();) {
               Map.Entry e = (Map.Entry) it.next();
               collectIf(e.getKey(), out, seen);
               collectIf(e.getValue(), out, seen);
           }
           return;
       }

       Class cc = c;
       while (cc != null) {
           Field[] fields = cc.getDeclaredFields();
           AccessibleObject.setAccessible(fields, true);
           for (int i=0;i<fields.length;i++) {
               Object val = null;
               try { val = fields[i].get(root); } catch (Throwable tt) { continue; }
               collectIf(val, out, seen);
           }
           cc = cc.getSuperclass();
       }
   }

   private static String safeToString(Object o) {
       if (o == null) return "null";
       try { return o.toString(); } catch (Throwable t) { return "<toString() failed: " + t + ">"; }
   }

   /* ------------------ Part B: Byte-level scanner fallback ------------------ */

   private static List<String> extractPrintableStrings(byte[] data, int minLen) {
       List out = new ArrayList();
       StringBuffer sb = new StringBuffer();
       for (int i=0;i<data.length;i++) {
           int v = data[i] & 0xFF;
           if (v >= 32 && v <= 126) {
               sb.append((char)v);
           } else {
               if (sb.length() >= minLen) out.add(sb.toString());
               sb.setLength(0);
           }
       }
       if (sb.length() >= minLen) out.add(sb.toString());
       return out;
   }

   private static void printLikelyClassNames(List strings) {
       Pattern fqcn = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_\\$]*\\.)+[A-Za-z_][A-Za-z0-9_\\$]*");
       LinkedHashSet found = new LinkedHashSet();
       for (Iterator it = strings.iterator(); it.hasNext();) {
           String s = (String) it.next();
           Matcher m = fqcn.matcher(s);
           while (m.find()) {
               found.add(m.group());
           }
       }
       System.out.println("Likely fully-qualified class names found in stream (heuristic):");
       if (found.isEmpty()) {
           System.out.println("  (none found)");
       } else {
           int i=0;
           for (Iterator it = found.iterator(); it.hasNext();) {
               System.out.println("  " + (++i) + ": " + it.next());
               if (i >= 200) break;
           }
       }
       System.out.println();
   }

   private static void heuristicSearchNearby(byte[] data, String needle, int window) {
       String hay = toAsciiString(data);
       int idx = hay.indexOf(needle);
       if (idx < 0) {
           System.out.println("No literal '" + needle + "' text found in the stream (byte-level scan).");
           return;
       }
       System.out.println("Found literal '" + needle + "' at byte offsets (first 10 shown):");
       int count = 0;
       int pos = 0;
       while ((pos = hay.indexOf(needle, pos)) >= 0 && count < 10) {
           int start = Math.max(0, pos - window);
           int end = Math.min(hay.length(), pos + needle.length() + window);
           String context = hay.substring(start, end);
           System.out.println("  occurrence at offset approx " + pos + " -> context snippet:");
           System.out.println("  --------------------------------------------------");
           System.out.println(context.replaceAll("[\\x00-\\x1F]+", " "));
           System.out.println("  --------------------------------------------------");
           pos += needle.length();
           count++;
       }
       System.out.println();
   }

   // Convert bytes to a best-effort ASCII string for searching (non-printable -> 0x00)
   private static String toAsciiString(byte[] data) {
       char[] carr = new char[data.length];
       for (int i=0;i<data.length;i++) {
           int v = data[i] & 0xFF;
           if (v >= 32 && v <= 126) carr[i] = (char)v;
           else carr[i] = '\0';
       }
       return new String(carr);
   }
}

