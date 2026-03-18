<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text"><xsl:if test="event/Directions_By_Case_Options/E40712_Directions!=''">Directions: <xsl:value-of select="event/Directions_By_Case_Options/E40712_Directions"/>

</xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
