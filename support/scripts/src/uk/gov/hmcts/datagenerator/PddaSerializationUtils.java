package uk.gov.hmcts.datagenerator;

import org.apache.commons.lang.SerializationUtils;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * Title: PddaSerializationUtils.
 * </p>
 * <p>
 * Description: Holds the methods used to encode, decode, serialize and deserialize events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2024
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Luke Gittins
 * @version 1.0
 */
public final class PddaSerializationUtils {

    private PddaSerializationUtils() {
        // Private constructor
    }

    public static byte[] serializePublicEvent(PublicDisplayEvent event) {
        return SerializationUtils.serialize(event);
    }

    public static PublicDisplayEvent deserializePublicEvent(byte[] eventBytes) {
        if (eventBytes != null) {
            return (PublicDisplayEvent) SerializationUtils.deserialize(eventBytes);
        }
        return null;
    }

    public static String encodePublicEvent(byte[] event) {
        return new String(Base64.encodeBase64(event));
    }

    public static byte[] decodePublicEvent(String encodedEvent) {
        return Base64.decodeBase64(encodedEvent);
    }
}
