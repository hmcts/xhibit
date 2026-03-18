<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Quash Indictment
<xsl:value-of select="concat('Indictment ', event/E40210_Quash_Options/E40210_Quash_Indictment_Number, ' on ', event/E40210_Quash_Options/E40210_Quash_Case_Number, ' has been ordered to quash')"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
