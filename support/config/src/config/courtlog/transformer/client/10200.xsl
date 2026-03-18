<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:include href="basic_elements.xsl"/>
    <!-- Declare Global Variables -->
    <xsl:variable name="Option" select="event/E10200_Defendant_Attendance_Options/E10200_DEO_Type"/>
    <xsl:variable name="String1" select="substring-before($Option,'Defendant')"/>
    <xsl:variable name="String2" select="substring-after($Option,'Defendant')"/>
    <xsl:variable name="Event_Header_Text"><xsl:value-of select="concat($String1,'Defendant ',event/defendant_name,$String2)"/></xsl:variable>
    <xsl:template match="event">
        <xsl:call-template name="BasicElements"/>
    </xsl:template>
</xsl:stylesheet>
