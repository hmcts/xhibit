package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query;

import java.io.Serializable;

/**
 * A value object used to represent a person. This object is immutable.
 * 
 * @author tz0d5m
 * @version $Id: Person.java,v 1.5 2006/06/05 12:28:44 bzjrnl Exp $
 */
public class Person implements Serializable {
    private Integer id;

    private String title;

    private String firstName;

    private String middleName;

    private String surname;
    private static final long serialVersionUID = -8044157087043398556L;

    /**
     * Constructor for use only by the serialization process. This is an
     * immutable class, and should only be constructed with the argument-taking
     * constructor.
     */
    public Person() {
        super();
    }

    /**
     * Only useable constructor, takes all parameters required to set up this
     * immutable value object.
     * 
     * @param id
     *            The id of the person.
     * @param title
     *            The persons title.
     * @param firstName
     *            The persons first name.
     * @param middleName
     *            The persons middle name.
     * @param surname
     *            The persons surname.
     */
    public Person(final Integer id, final String title, final String firstName, final String middleName,
            final String surname) {
        this.id = id;
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Below are all of the direct accessor methods for this classes
    // variables.
    // /////////////////////////////////////////////////////////////////////////

    public final Integer getId() {
        return this.id;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getFirstName() {
        return this.firstName;
    }

    public final String getMiddleName() {
        return this.middleName;
    }

    public final String getSurname() {
        return this.surname;
    }
}
