<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Application to sever defendant on count
<xsl:value-of select="concat('Defendant ', event/E20922_Charges_Put_Options/E20922_Defendant_Name, ', on count ', event/E20922_Charges_Put_Options/E20922_Count_Number, ', on indictment ', event/E20922_Charges_Put_Options/E20922_Indictment_Number, '; Application to sever')"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>