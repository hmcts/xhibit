package uk.gov.courtservice.xhibit.pdda.publicdisplay;

import org.apache.commons.lang.SerializationUtils;
import org.castor.util.Base64Decoder;
import org.castor.util.Base64Encoder;

import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
//import uk.gov.hmcts.pdda.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
//import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;

public class TestPublicDisplayEvent {
	
	public static void main(String[] args) {
		System.out.println("Starting");
		TestPublicDisplayEvent pdet = new TestPublicDisplayEvent();
		
		pdet.testEncodeAndSerialize();
		pdet.testDecodeAndDeserializeConfigurationChangeEvent();
	}
	
	
	/**
	 * 
	 */
	private void testEncodeAndSerialize() {
		
		// Create a HearingStatusEvent
		CourtRoomIdentifier cri =
				new CourtRoomIdentifier(new Integer(81), new Integer(1), "Test Court", new Integer(1), new DisplayablePublicNoticeValue[0]);
		CaseChangeInformation cci = new CaseChangeInformation(true);
		
		HearingStatusEvent hse = new HearingStatusEvent(cri, cci);
		
		// Test to see if this is a PublicDisplayEvent
		if (hse instanceof uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent) {
			System.out.println("This is a PublicDisplayEvent");
		} else {
			System.out.println("This is NOT a PublicDisplayEvent");
		}
		
		// Convert to an object that's exactly the same but in a different package
		uk.gov.hmcts.pdda.common.publicdisplay.events.HearingStatusEvent newHSE = new  
				uk.gov.hmcts.pdda.common.publicdisplay.events.HearingStatusEvent(hse.getCourtRoomIdentifier(), hse.getCaseChangeInformation());
		
		System.out.println(newHSE.getCourtId());
		System.out.println("here"+newHSE.getCourtRoomIdentifier().getCourtId()+"1 here");
		System.out.println("here"+newHSE.getCourtRoomIdentifier().getCourtRoomId()+"2 here");
		System.out.println("here"+newHSE.getCaseChangeInformation().isCaseActive()+"3 here");
		
		// Serialize the object
		byte[] serializedObject = SerializationUtils.serialize(newHSE);

		// Base64 Encode
		String encodedInput = new String(Base64Encoder.encode(serializedObject));
		
		System.out.println("Encoded new event: "+encodedInput);
		
		// Base64 Decode
		byte[] newSerializedObject = Base64Decoder.decode(encodedInput);
		
		// Deserialize
		Object deserializedObj = SerializationUtils.deserialize(newSerializedObject);
		
		// What type of object is this
		System.out.println(deserializedObj.getClass().getCanonicalName());
		
		uk.gov.hmcts.pdda.common.publicdisplay.events.HearingStatusEvent newDeserHSE =
				(uk.gov.hmcts.pdda.common.publicdisplay.events.HearingStatusEvent) deserializedObj;
		
		// Display the contents of the message now!
		System.out.println(newDeserHSE.getCourtId());
		System.out.println("here"+newDeserHSE.getCourtRoomIdentifier().getCourtId()+"1 here");
		System.out.println("here"+newDeserHSE.getCourtRoomIdentifier().getCourtRoomId()+"2 here");
		System.out.println("here"+newDeserHSE.getCaseChangeInformation().isCaseActive()+"3 here");
		
	}
	
	
	/**
	 * Take an existing encoded and serialized ConfigurationChangeEvent message.
	 * Decode it, deserialize it and check the contents
	 */
	private void testDecodeAndDeserializeConfigurationChangeEvent() {
		//String encodedMessage="rO0ABXNyAE91ay5nb3YuY291cnRzZXJ2aWNlLnhoaWJpdC5jb21tb24ucHVibGljZGlzcGxheS5ldmVudHMuQ29uZmlndXJhdGlvbkNoYW5nZUV2ZW50cztXqJM92kMCAAFMAAZjaGFuZ2V0AF5MdWsvZ292L2NvdXJ0c2VydmljZS94aGliaXQvY29tbW9uL3B1YmxpY2Rpc3BsYXkvdHlwZXMvY29uZmlndXJhdGlvbi9Db3VydENvbmZpZ3VyYXRpb25DaGFuZ2U7eHBzcgBcdWsuZ292LmNvdXJ0c2VydmljZS54aGliaXQuY29tbW9uLnB1YmxpY2Rpc3BsYXkudHlwZXMuY29uZmlndXJhdGlvbi5Db3VydENvbmZpZ3VyYXRpb25DaGFuZ2XQOBGrUhY5MQIAAloADl9mb3JjZVJlY3JlYXRlTAAIX2NvdXJ0SWR0ABNMamF2YS9sYW5nL0ludGVnZXI7eHABc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAF8=";
		String encodedMessage="rO0ABXNyAE91ay5nb3YuY291cnRzZXJ2aWNlLnhoaWJpdC5jb21tb24ucHVibGljZGlzcGxheS5ldmVudHMuQ29uZmlndXJhdGlvbkNoYW5nZUV2ZW50cztXqJM92kMCAAFMAAZjaGFuZ2V0AF5MdWsvZ292L2NvdXJ0c2VydmljZS94aGliaXQvY29tbW9uL3B1YmxpY2Rpc3BsYXkvdHlwZXMvY29uZmlndXJhdGlvbi9Db3VydENvbmZpZ3VyYXRpb25DaGFuZ2U7eHBzcgBjdWsuZ292LmNvdXJ0c2VydmljZS54aGliaXQuY29tbW9uLnB1YmxpY2Rpc3BsYXkudHlwZXMuY29uZmlndXJhdGlvbi5Db3VydERpc3BsYXlDb25maWd1cmF0aW9uQ2hhbmdlR6vBGsiulQUCAAFJAApfZGlzcGxheUlkeHIAXHVrLmdvdi5jb3VydHNlcnZpY2UueGhpYml0LmNvbW1vbi5wdWJsaWNkaXNwbGF5LnR5cGVzLmNvbmZpZ3VyYXRpb24uQ291cnRDb25maWd1cmF0aW9uQ2hhbmdl0DgRq1IWOTECAAJaAA5fZm9yY2VSZWNyZWF0ZUwACF9jb3VydElkdAATTGphdmEvbGFuZy9JbnRlZ2VyO3hwAXNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAABfAAAE8w==";
		
		// Base64 Decode
		byte[] newSerializedObject = Base64Decoder.decode(encodedMessage);
		
		// Deserialize
		PublicDisplayEvent deserializedObj = (PublicDisplayEvent) SerializationUtils.deserialize(newSerializedObject);
		
		// What type of object is this
		System.out.println(deserializedObj.getClass().getCanonicalName());
		
	}

}
