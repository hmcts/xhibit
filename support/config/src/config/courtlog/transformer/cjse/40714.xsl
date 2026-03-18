<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Directions For Case:
<xsl:if test="event/Directions_By_Case_Options/E40714_Defendants_To_Attend!=''">Defendants To Attend:
<xsl:for-each select="event/Directions_By_Case_Options/E40714_Defendants_To_Attend/E40714_Defendant_List">
<xsl:if test="./E40714_Defendant_Details/E40714_Defendant_Name!=''"> - <xsl:value-of select="./E40714_Defendant_Details/E40714_Defendant_Name"/>.
</xsl:if>
</xsl:for-each></xsl:if><xsl:if test="event/Directions_By_Case_Options/E40714_Defendants_Not_To_Attend!=''">
Defendants NOT To Attend:
<xsl:for-each select="event/Directions_By_Case_Options/E40714_Defendants_Not_To_Attend/E40714_Defendant_List">
<xsl:if test="./E40714_Defendant_Details/E40714_Defendant_Name!=''"> - <xsl:value-of select="./E40714_Defendant_Details/E40714_Defendant_Name"/>.
</xsl:if></xsl:for-each></xsl:if></xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
