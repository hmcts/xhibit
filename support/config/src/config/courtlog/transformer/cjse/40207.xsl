<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Stay Count
<xsl:value-of select="concat('Count ', event/E40207_Stay_Options/E40207_Stay_Count_Number, ', Indictment ', event/E40207_Stay_Options/E40207_Stay_Indictment_Number, ' on case ', event/E40207_Stay_Options/E40207_Stay_Case_Number, ' is stayed')"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>