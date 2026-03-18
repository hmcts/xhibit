<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Lie On File Count
<xsl:value-of select="concat('Count ', event/E40214_Lie_Options/E40214_Lie_Count_Number, ', Indictment ', event/E40214_Lie_Options/E40214_Lie_Indictment_Number, ' on case ', event/E40214_Lie_Options/E40214_Lie_Case_Number, ' has been ordered to lie on file')"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>