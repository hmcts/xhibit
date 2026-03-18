<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Lie On File Defendant On Indictment
<xsl:value-of select="concat('Defendant ', event/E40213_Lie_Options/E40213_Lie_Defendant_Name, ' Indictment ', event/E40213_Lie_Options/E40213_Lie_Indictment_Number, ' on ', event/E40213_Lie_Options/E40213_Lie_Case_Number, ' has been ordered to lie on file')"/>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>