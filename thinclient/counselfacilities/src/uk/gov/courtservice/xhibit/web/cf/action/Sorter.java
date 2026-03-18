package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class Sorter {

    private int indexes[];

    private Vector sortingColumns = new Vector();

    private boolean ascending = true;

    // private int compares;
    private Vector data = new Vector();

    private static final Logger log = CSServices.getLogger(Sorter.class);

    public Sorter(Vector idata) {
        data = idata;
        reallocateIndexes();
    }

    public String getData(int row, String column) {
        String o = null;
        try {
            PartyOnCaseValue r = (PartyOnCaseValue) (data).elementAt(row);
            if (r != null) {
                // if
                // (DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Court.equals(column))
                // o = "" +r.getCourtRoomId();
                if (DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Court.equals(column)) {
                    if (r.getIsFloating().equals("1")) { // this is what
                        // the jstl does
                        // to check
                        // unassigned,
                        // so we should
                        // return
                        // unassigend in
                        // this case for
                        // sorting
                        // purposes . .
                        // .
                        o = "Unassigned"; // should probably look this up
                        // from bundle
                    } else {
                        o = "" + r.getCrestCourtRoomNumber().toString();
                    }
                }
                if (DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Defendant.equals(column)) {
                    Collection c = r.getDefendants();
                    Iterator i = c.iterator();
                    if (i.hasNext()) {
                        o = ((DefendantValue) i.next()).getSurName();
                    }
                }
            } else {
                log.error("no party on case found on row " + row);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return o;
    }

    private int compareRowsByColumn(int row1, int row2, String column) {
        Class type = String.class;

        // Check for nulls.

        Object o1 = getData(row1, column);
        Object o2 = getData(row2, column);

        log.debug("Comparing : " + o1 + " | " + o2);
        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        /*
         * We copy all returned values from the getValue call in case an
         * optimised model is reusing one object to return many values. The
         * Number subclasses in the JDK are immutable and so will not be used in
         * this way but other subclasses of Number might want to do this to save
         * space and avoid unnecessary heap allocation.
         */

        if (type.getSuperclass() == java.lang.Number.class) {
            Number n1 = (Number) o1;
            double d1 = n1.doubleValue();
            Number n2 = (Number) o2;
            double d2 = n2.doubleValue();

            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == java.util.Date.class) {
            Date d1 = (Date) o1;
            long n1 = d1.getTime();
            Date d2 = (Date) o2;
            long n2 = d2.getTime();

            if (n1 < n2) {
                return -1;
            } else if (n1 > n2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == String.class) {
            // right, here we need more than a standard string compare, we
            // need to take into account court 1, court 2 . . . court 10,
            // court 11 etc. properly sorted, this relates to bug X53968
            // String s1 = (String)o1;
            // String s2 = (String)o2;
            int result = new CourtNameComparator().compare(o1, o2);
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == Boolean.class) {
            Boolean bool1 = (Boolean) o1;
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) o2;
            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) { // Define false < true
                return 1;
            } else {
                return -1;
            }
        } else {
            Object v1 = o1;
            String s1 = v1.toString();
            Object v2 = o2;
            String s2 = v2.toString();
            int result = s1.compareTo(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private int compare(int row1, int row2) {
        // compares++;
        for (int sortingColumnsIndex = 0; sortingColumnsIndex < sortingColumns.size(); sortingColumnsIndex++) {
            String column = (String) sortingColumns.elementAt(sortingColumnsIndex);
            int result = compareRowsByColumn(row1, row2, column);
            if (result != 0)
                return ascending ? result : -result;
        }
        return 0;
    }

    public void sort(Object sender) {
        // compares = 0;
        // n2sort();
        // qsort(0, indexes.length-1);
        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
    }

    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    private void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        /*
         * This is an optional short-cut; at each recursive call, check to see
         * if the elements in this subset are already ordered. If so, no further
         * comparisons are needed; the sub-array can just be copied. The array
         * must be copied rather than assigned otherwise sister calls in the
         * recursion might get out of sinc. When the number of elements is three
         * they are partitioned so that the first set, [low, mid), has one
         * element and and the second, [mid, high), has two. We skip the
         * optimisation when the number of elements is three or less as the
         * first compare in the normal merge will produce the same sequence of
         * steps. This optimisation seems to be worthwhile for partially ordered
         * lists but some analysis is needed to find out how the performance
         * drops to Nlog(N) as the initial order diminishes - it may drop very
         * quickly.
         */

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge.

        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }

    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "indexes".

    public Vector sortByColumn(String column) {
        return sortByColumn(column, true);
    }

    public Vector sortByColumn(String column, boolean ascending) {
        this.ascending = ascending;
        reallocateIndexes();
        sortingColumns.removeAllElements();
        sortingColumns.addElement(column);
        sort(this);

        log.debug("indexes.length = " + indexes.length);
        Vector v = new Vector();
        for (int i = 0; i < this.indexes.length; i++) {
            v.add("" + this.indexes[i]);
        }
        log.debug("returing index vector with " + v.size() + " elements");
        return v;
    }

    private void reallocateIndexes() {
        int rowCount = data.size();
        indexes = new int[rowCount];
        for (int row = 0; row < rowCount; row++)
            indexes[row] = row;
    }

    public CourtNameComparator getCourtNameCompator() {
        return new CourtNameComparator();
    }

    // this class was previously in SendInstantMessageAction, as it's name
    // suggests it sorts court names.
    // The code was moved to another part of the system (I can't see it in
    // InstantMessageServices), so this should probably be factored out at
    // some point.

    class CourtNameComparator implements Comparator {

        public CourtNameComparator() {
        }

        public int compare(Object o1, Object o2) throws ClassCastException {
            if (!(o1 instanceof String || o2 instanceof String)) { // must
                // be
                // strings!
                throw new ClassCastException("Objects to compare in CourtNameComparator must be strings!");
            }

            String s1 = (String) o1;
            String s2 = (String) o2;
            int s1NumberIndex = s1.length();
            int s2NumberIndex = s2.length();
            while (s1NumberIndex > 0 && Character.isDigit(s1.charAt(s1NumberIndex - 1))) { // check
                // the
                // character
                // before
                // is a
                // digit,
                // if
                // so
                // take
                // on
                // off
                // the
                s1NumberIndex--;
            }
            while (s2NumberIndex > 0 && Character.isDigit(s2.charAt(s2NumberIndex - 1))) {
                s2NumberIndex--;
            }

            String s1StringPart = s1.substring(0, s1NumberIndex);
            String s2StringPart = s2.substring(0, s2NumberIndex);

            if (s1StringPart.equals(s2StringPart)) { // then we check the
                // numbers
                if (s1.substring(s1NumberIndex).length() == 0 && s2.substring(s2NumberIndex).length() == 0) {
                    return 0;
                }
                if (s1.substring(s1NumberIndex).length() == 0) {
                    return 1;
                }
                if (s2.substring(s2NumberIndex).length() == 0) {
                    return -1;
                }
                int s1NumberPart = Integer.parseInt(s1.substring(s1NumberIndex));
                int s2NumberPart = Integer.parseInt(s2.substring(s2NumberIndex));
                return s1NumberPart - s2NumberPart;
            } else {

                return s1StringPart.compareTo(s2StringPart);
            }

        }
    }
}