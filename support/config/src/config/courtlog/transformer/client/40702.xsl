<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Directions For Case
<xsl:if test="event/E40702_Directions_By_Case_Options/E40702_P_And_D!=''"><xsl:text>P and D: </xsl:text><xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_P_And_D"/>

</xsl:if>
<xsl:if test="event/E40702_Directions_By_Case_Options/E40702_Time_Estimate/E40702_Time!=''">
Trial Time Estimate: <xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_Time_Estimate/E40702_Time"/><xsl:text> </xsl:text><xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_Time_Estimate/E40702_Time_Estimate_Options"/> 

</xsl:if>
<xsl:if test="event/E40702_Directions_By_Case_Options/E40702_Directions!=''">
Directions: <xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_Directions"/>

</xsl:if>
<xsl:if test="event/E40702_Directions_By_Case_Options/E40702_List!=''">
List: <xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_List"/>

</xsl:if>
<xsl:if test="event/E40702_Directions_By_Case_Options/E40702_Placed_In/E40702_Placed_In_List_Options!=''">
Listed In: <xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_Placed_In/E40702_Placed_In_List_Options"/></xsl:if><xsl:if test="event/E40702_Directions_By_Case_Options/E40702_Placed_In/E40702_List_Date!=''">, For: <xsl:value-of select="event/E40702_Directions_By_Case_Options/E40702_Placed_In/E40702_List_Date"/>

</xsl:if>
<xsl:if test="event/E40702_Directions_By_Case_Options/E40702_Defendants_To_Attend!=''">
Defendants To Attend:
<xsl:for-each select="event/E40702_Directions_By_Case_Options/E40702_Defendants_To_Attend/E40702_Defendant_List">
<xsl:if test="./E40702_Defendant_Details/E40702_Defendant_Name!=''">
 - <xsl:value-of select="./E40702_Defendant_Details/E40702_Defendant_Name"/>

</xsl:if>

</xsl:for-each></xsl:if></xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
