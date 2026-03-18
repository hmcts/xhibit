package uk.gov.courtservice.xhibit.business.services.courtel.exceptions;

/**
 * ExceptionMessage contains the details of the exception.
 * 
 * @author d120520
 *
 */
public class ExceptionMessage
{

    /**
     * Exception status.
     */
    private String status;

    /**
     * Exception detail.
     */
    private String detail;

    /**
     * Exception reason.
     */
    private String reason;

    /**
     * @return the status
     */
    public String getStatus ()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus (final String status)
    {
        this.status = status;
    }


    /**
     * @return the detail
     */
    public String getDetail ()
    {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail (final String detail)
    {
        this.detail = detail;
    }

    /**
     * @return the reason
     */
    public String getReason ()
    {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason (final String reason)
    {
        this.reason = reason;
    }
}