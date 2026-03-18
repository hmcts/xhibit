<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">
<xsl:value-of select="concat('Defendant ', event/E20924_Charges_Put_Options/E20924_Defendant_Name, ', on count ', event/E20924_Charges_Put_Options/E20924_Count_Number, ', on indictment ', event/E20924_Charges_Put_Options/E20924_Indictment_Number, '; Leave to prefer late bill of indictment')"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>