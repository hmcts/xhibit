<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text"><xsl:if test="event/Directions_By_Case_Options/E40714_Defendants_To_Attend!=''">
<xsl:for-each select="event/Directions_By_Case_Options/E40714_Defendants_To_Attend/E40714_Defendant_List">
<xsl:if test="./E40714_Defendant_Details/E40714_Defendant_Name!=''">Defendant <xsl:value-of select="./E40714_Defendant_Details/E40714_Defendant_Name"/> To Attend.
</xsl:if>
</xsl:for-each></xsl:if><xsl:if test="event/Directions_By_Case_Options/E40714_Defendants_Not_To_Attend!=''">

<xsl:for-each select="event/Directions_By_Case_Options/E40714_Defendants_Not_To_Attend/E40714_Defendant_List">
<xsl:if test="./E40714_Defendant_Details/E40714_Defendant_Name!=''">Defendant <xsl:value-of select="./E40714_Defendant_Details/E40714_Defendant_Name"/> NOT To Attend.
</xsl:if></xsl:for-each></xsl:if></xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
