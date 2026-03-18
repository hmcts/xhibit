<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">
		<xsl:value-of select="concat(event/E20200_Bail_And_Custody_Options/E20200_BC_Defendant_Name,': ',event/E20200_Bail_And_Custody_Options/E20200_BC_Type,' ',event/E20200_Bail_And_Custody_Options/E20200_BC_Date)"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
