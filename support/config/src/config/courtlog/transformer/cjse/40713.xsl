<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Directions For Case:
<xsl:if test="event/Directions_By_Case_Options/E40713_List!=''">List: <xsl:value-of select="event/Directions_By_Case_Options/E40713_List"/>

</xsl:if>
<xsl:if test="event/Directions_By_Case_Options/E40713_Placed_In/E40713_Placed_In_List_Options!=''">
Listed In: <xsl:value-of select="event/Directions_By_Case_Options/E40713_Placed_In/E40713_Placed_In_List_Options"/></xsl:if><xsl:if test="event/Directions_By_Case_Options/E40713_Placed_In/E40713_List_Date!=''">, For: <xsl:value-of select="event/Directions_By_Case_Options/E40713_Placed_In/E40713_List_Date"/>

</xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
