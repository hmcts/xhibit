<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Stay Defendant On Count
<xsl:value-of select="concat('Defendant ', event/E40204_Stay_Options/E40204_Stay_Defendant_Name, ' on count ',  event/E40204_Stay_Options/E40204_Stay_Count_Number, ' on indictment ', event/E40204_Stay_Options/E40204_Stay_Indictment_Number, ' on ', event/E40204_Stay_Options/E40204_Stay_Case_Number, ' is stayed')"/></xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
