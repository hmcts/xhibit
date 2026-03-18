<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Directions By Defendant
<xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Name"/>
<xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Identification!=''">
Identification: <xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Identification"/>
</xsl:if>
<xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Arraignment!=''">
Arraignment: <xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Arraignment='true'">Arraigned</xsl:if><xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Arraignment!='true'">Not Arraigned</xsl:if>
</xsl:if>
<xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Certificate_Of_Attendance!=''">
Certificate of Attendance: <xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Certificate_Of_Attendance"/>
</xsl:if>
<xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Bail_Or_Custody/E40701_Bail_Or_Custody_Options!=''">
Bail and Custody: <xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Bail_Or_Custody/E40701_Bail_Or_Custody_Options"/></xsl:if><xsl:if test="event/E40701_Direction_By_Defendant_Options/E40701_Bail_Or_Custody/E40701_Bail_Or_Custody_Conditions != ''">. New Conditions: <xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Bail_Or_Custody/E40701_Bail_Or_Custody_Conditions"/>
</xsl:if>
<xsl:choose>
<xsl:when test="event/E40701_Direction_By_Defendant_Options/E40701_Form_B/E40701_Form_B_Options ='Filed'">
Form B: <xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Form_B/E40701_Form_B_Options"/>
</xsl:when>
<xsl:when test="event/E40701_Direction_By_Defendant_Options/E40701_Form_B/E40701_Form_B_Options ='To be filed by'">
Form B: To be filed by: <xsl:value-of select="event/E40701_Direction_By_Defendant_Options/E40701_Form_B/E40701_To_Be_Filed_Date"/>
</xsl:when>
</xsl:choose>
</xsl:variable><xsl:template match="event"><xsl:call-template name="BasicElements"/></xsl:template></xsl:stylesheet>
