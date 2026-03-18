<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Bail and Custody for Defendant <xsl:value-of select="event/Direction_By_Defendant_Options/Defendant_Name"/>.
<xsl:if test="event/Direction_By_Defendant_Options/E40706_Bail_Or_Custody/E40706_Bail_Or_Custody_Options!=''">
<xsl:value-of select="event/Direction_By_Defendant_Options/E40706_Bail_Or_Custody/E40706_Bail_Or_Custody_Options"/></xsl:if><xsl:if test="event/Direction_By_Defendant_Options/E40706_Bail_Or_Custody/E40706_Bail_Or_Custody_Conditions != ''">. New Conditions: <xsl:value-of select="event/Direction_By_Defendant_Options/E40706_Bail_Or_Custody/E40706_Bail_Or_Custody_Conditions"/>
</xsl:if>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
