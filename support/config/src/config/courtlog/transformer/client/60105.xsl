<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="chargeText">
		<xsl:choose>
			<xsl:when test="/event/Charge_Type='I'">
				<xsl:value-of select=" concat( 'count ', /event/Crest_Offence_Seq_No, ' on indictment ', event/Crest_Charge_Seq_No)"/>
			</xsl:when>
			<xsl:when test="/event/Charge_Type='O'">
				<xsl:value-of select=" concat( 'summary offence ', /event/Crest_Offence_Seq_No )"/>
			</xsl:when>
			<xsl:when test="/event/Charge_Type='B'">
				<xsl:value-of select=" concat( 'breach ', /event/Crest_Offence_Seq_No )"/>
			</xsl:when>
			<xsl:when test="/event/Charge_Type='F'">
				<xsl:value-of select=" concat( 'failure to appear offence ', /event/Crest_Charge_Seq_No )"/>
			</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="Event_Header_Text">
		<xsl:value-of select=" concat( 'Plea deleted for ', event/defendant_name, ' on ', $chargeText )" disable-output-escaping="yes"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
