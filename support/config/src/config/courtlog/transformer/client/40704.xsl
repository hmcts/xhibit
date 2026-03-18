<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
    <xsl:variable name="Option" select="event/Direction_By_Defendant_Options/E40704_Identification"/>
    <xsl:variable name="Event_Header_Text">
        <xsl:value-of select="concat('Defendant ',event/Direction_By_Defendant_Options/Defendant_Name,' ')"/>
        <xsl:choose>
            <xsl:when test="starts-with($Option,'Defendant ')">
                <xsl:value-of select="substring-after($Option,'Defendant ')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$Option"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <xsl:template match="event">
        <xsl:call-template name="BasicElements"/>
    </xsl:template>
</xsl:stylesheet>