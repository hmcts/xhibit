<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<!--
<xsl:variable name="Event_Header_Text">
	<xsl:value-of select="concat(event/E20603_Witness_Sworn_Options/E20603_WS_List,' -  ',event/E20603_Witness_Sworn_Options/E20603_Witness_Name,' - ',event/E20603_Witness_Sworn_Options/E20603_Witness_No)"/>
</xsl:variable>
-->
<xsl:variable name="Event_Header_Text">
    <xsl:choose>
        <xsl:when test="event/E20603_Witness_Sworn_Options/E20603_WS_List[.='E20603_Appellant_Sworn']">
            <xsl:text>Appellant Sworn</xsl:text>
        </xsl:when>
        <xsl:when test="event/E20603_Witness_Sworn_Options/E20603_WS_List[.='E20603_Interpreter_Sworn']">
            <xsl:text>Interpreter Sworn</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>Witness Sworn</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:variable>

<xsl:template match="event">

	<xsl:call-template name="BasicElements"/>

</xsl:template>

</xsl:stylesheet>
