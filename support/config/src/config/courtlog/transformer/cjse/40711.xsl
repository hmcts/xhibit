<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Directions For Case:
<xsl:if test="event/Directions_By_Case_Options/E40711_Time_Estimate/E40711_Time!=''">Trial Time Estimate: <xsl:value-of select="event/Directions_By_Case_Options/E40711_Time_Estimate/E40711_Time"/><xsl:text> </xsl:text><xsl:value-of select="event/Directions_By_Case_Options/E40711_Time_Estimate/E40711_Time_Estimate_Options"/>

</xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
