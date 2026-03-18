<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text"><xsl:if test="event/Directions_By_Case_Options/E40713_List!=''">List <xsl:value-of select="event/Directions_By_Case_Options/E40713_List"/> hearing
</xsl:if>
<xsl:choose><xsl:when test="event/Directions_By_Case_Options/E40713_Placed_In/E40713_Placed_In_List_Options='Fixed list'"><xsl:text>Fixed for </xsl:text></xsl:when><xsl:when test="event/Directions_By_Case_Options/E40713_Placed_In/E40713_Placed_In_List_Options='Warned list'"><xsl:text>Place in warned list for week commencing on </xsl:text></xsl:when><xsl:otherwise>
Listed In: <xsl:value-of select="event/Directions_By_Case_Options/E40713_Placed_In/E40713_Placed_In_List_Options"/>, For: </xsl:otherwise></xsl:choose><xsl:if test="event/Directions_By_Case_Options/E40713_Placed_In/E40713_List_Date!=''"><xsl:value-of select="event/Directions_By_Case_Options/E40713_Placed_In/E40713_List_Date"/>

</xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
