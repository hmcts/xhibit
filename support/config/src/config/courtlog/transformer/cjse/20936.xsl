<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<!--
<xsl:variable name="Event_Header_Text">
    <xsl:value-of select="concat(event/E20936_Witness_Read_Options/E20936_WR_Type,' -  ',event/E20936_Witness_Read_Options/E20936_WR_Name)"/>
</xsl:variable>
-->
<xsl:variable name="Event_Header_Text">
    <xsl:choose>
        <xsl:when test="event/E20936_Witness_Read_Options/E20936_WR_Type[.='E20936_Appellant_Read']">
            <xsl:text>Appellant Read</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>Witness Read</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:variable>

<xsl:template match="event">

	<xsl:call-template name="BasicElements"/>

</xsl:template>

</xsl:stylesheet>
