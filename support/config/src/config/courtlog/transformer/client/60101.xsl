<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="pleaDescription">
		<xsl:choose>
			<xsl:when test="event/Ref_Plea_Code='O'">
				<xsl:value-of select="concat('Other: ', event/Other_Plea_Text)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="event/Ref_Plea_Description"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="Event_Header_Text">
		<xsl:value-of select=" concat( 'Plea ', $pleaDescription, ' by ', event/defendant_name, ' on count ', event/Crest_Offence_Seq_No, ' on indictment ', event/Crest_Charge_Seq_No, '. Arraigned on ', event/Arraingment_Date )" disable-output-escaping="yes"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
