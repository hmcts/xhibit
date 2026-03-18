<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Defendant <xsl:value-of select="event/Direction_By_Defendant_Options/Defendant_Name"/><xsl:if test="event/Direction_By_Defendant_Options/E40705_Arraignment!=''"><xsl:if test="event/Direction_By_Defendant_Options/E40705_Arraignment='true'"> arraigned</xsl:if><xsl:if test="event/Direction_By_Defendant_Options/E40705_Arraignment!='true'"> not arraigned</xsl:if>
</xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
