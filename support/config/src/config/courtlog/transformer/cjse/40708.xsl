<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Directions for Defendant <xsl:value-of select="event/Direction_By_Defendant_Options/Defendant_Name"/>
<xsl:choose>
<xsl:when test="event/Direction_By_Defendant_Options/E40708_Form_B/E40708_Form_B_Options ='Filed'">
Form B: <xsl:value-of select="event/Direction_By_Defendant_Options/E40708_Form_B/E40708_Form_B_Options"/>
</xsl:when>
<xsl:when test="event/Direction_By_Defendant_Options/E40708_Form_B/E40708_Form_B_Options ='To be filed by'">
Form B: To be filed by: <xsl:value-of select="event/Direction_By_Defendant_Options/E40708_Form_B/E40708_To_Be_Filed_Date"/>
</xsl:when>
</xsl:choose>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
