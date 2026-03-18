package uk.gov.courtservice.framework.jdbc.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import uk.gov.courtservice.framework.jdbc.exception.ExceptionTranslator;

/**
 * <p>
 * Title: Row
 * </p>
 * <p>
 * Description: This class hides the result set from the clients. The fetch
 * direction is forward only.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: Row.java,v 1.19 2014/06/20 18:01:08 atwells Exp $
 */
public class Row {
    /** Backing result set */
    private final ResultSet rs;

    /** Exception translator */
    private final ExceptionTranslator translator = ExceptionTranslator.getInstance();

    /**
     * Creates a row with the backing resultset
     * 
     * @param Result
     *            set
     */
    protected Row(final ResultSet rs) {
        this.rs = rs;
    }

    /**
     * Convenience method to convert a BigDecimal into an Integer
     */
    public Integer getInteger(final String col) {
        try {
            final BigDecimal r = rs.getBigDecimal(col);
            return ((r == null) ? null : new Integer(r.intValue()));
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * Convenience method to convert a BigDecimal into a Long
     */
    public Long getWrappedLong(final String col) {
        try {
            final BigDecimal r = rs.getBigDecimal(col);
            return ((r == null) ? null : new Long(r.longValue()));
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * Return an <code>OutputStream</code> pointing to the <code>Blob</code>
     * value of the designated column in the current row of this
     * <code>Row</code> object. If the <code>Blob</code> found is <i>null</i>,
     * then this method will return <i>null</i>.
     * 
     * Note: This method uses Oracle specific functionality, however, several
     * classes in this JDBC framework also use Oracle specific functionality,
     * and it would require significant work to extract in a proper manner.
     * 
     * @param col
     *            the name of the column from which to retrieve the value.
     * @return an <code>OutputStream</code> object pointing to the
     *         <code>BLOB</code> value in the specified column.
     * 
     * @see #getBlob(java.lang.String)
     * @see oracle.sql.BLOB.getBinaryOutputStream()
     */
    public OutputStream getBinaryOutputStream(final String col) {
        try {
            final Blob blob = rs.getBlob(col);
            if (blob != null) {
                return blob.setBinaryStream(0);
            }

            // return null if the original blob was null...
            return null;
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * Return an <code>InputStream</code> pointing to the <code>Blob</code>
     * value of the designated column in the current row of this
     * <code>Row</code> object. If the <code>Blob</code> found is <i>null</i>,
     * then this method will return <i>null</i>.
     * 
     * Note: This method uses Oracle specific functionality, however, several
     * classes in this JDBC framework also use Oracle specific functionality,
     * and it would require significant work to extract in a proper manner.
     * 
     * @param col
     *            the name of the column from which to retrieve the value.
     * @return an <code>InputStream</code> object pointing to the
     *         <code>BLOB</code> value in the specified column.
     * 
     * @see #getBlob(java.lang.String)
     * @see oracle.sql.BLOB.getBinaryStream()
     */
    public InputStream getBinaryStream(final String col) {
        try {
            final Blob blob = rs.getBlob(col);
            if (blob != null) {
                return blob.getBinaryStream();
            }

            // return null if the original blob was null...
            return null;
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public boolean getBoolean(final String col) {
        try {
            return rs.getBoolean(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public byte getByte(final String col) {
        try {
            return rs.getByte(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public Reader getCharacterStream(final String col) {
        try {
            return rs.getCharacterStream(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public Date getDate(final String col) {
        try {
            return rs.getDate(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public double getDouble(final String col) {
        try {
            return rs.getDouble(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public float getFloat(final String col) {
        try {
            return rs.getFloat(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public int getInt(final String col) {
        try {
            return rs.getInt(col); 
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public long getLong(final String col) {
        try {
            return rs.getLong(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public Object getObject(final String col) {
        try {
            return rs.getObject(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public short getShort(final String col) {
        try {
            return rs.getShort(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public String getString(final String col) {
        try {
            return rs.getString(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * Gets the given clob as a string.
     * 
     * @param clob
     *            the clob to convert to a string.
     * @return the clob as a string.
     */
    public String getClobAsString(String col) {
        try {
            Clob clob = rs.getClob(col);
            if (clob == null) {
                return null;
            }

            // set the default size to a reasonable amount to reduce the
            // number
            // of dynamic extensions to the buffer...
            StringBuffer sb = new StringBuffer(1024);
            char[] buffer = new char[4096];

            Reader reader = clob.getCharacterStream();

            for (int charsRead = reader.read(buffer); charsRead != -1; charsRead = reader.read(buffer)) {
                sb.append(buffer, 0, charsRead);
            }
            reader.close();

            return sb.toString();
        } catch (IOException ex) {
            throw translator.translate(ex, col);
        } catch (SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * Gets the given clob as a string.
     * 
     * @param clob
     *            the clob to convert to a string.
     * @return the clob as a string.
     */
    public String getBlobAsString(String col) {
        try {
            Blob blob = rs.getBlob(col);
            if (blob == null) {
                return null;
            }

            // set the default size to a reasonable amount to reduce the
            // number
            // of dynamic extensions to the buffer...
            ByteArrayOutputStream writeBuffer = new ByteArrayOutputStream(4096);
            byte[] readBuffer = new byte[4096];

            InputStream in = blob.getBinaryStream();
            for (int bytesRead = in.read(readBuffer); bytesRead != -1; bytesRead = in.read(readBuffer)) {
                writeBuffer.write(readBuffer, 0, bytesRead);
            }
            in.close();

            return new String(writeBuffer.toByteArray()); // Assumes
        } catch (IOException ex) {
            throw translator.translate(ex, col);
        } catch (SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public Time getTime(final String col) {
        try {
            return rs.getTime(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }

    /**
     * @see Javadoc for <code>java.sql.ResultSet</a>
     */
    public Timestamp getTimestamp(final String col) {
        try {
            return rs.getTimestamp(col);
        } catch (final SQLException ex) {
            throw translator.translate(ex, col);
        }
    }
}
