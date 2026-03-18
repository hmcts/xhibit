<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<!--
<xsl:variable name="Event_Header_Text">
    <xsl:value-of select="concat(event/E20904_Witness_Sworn_Options/E20904_WSO_Type,' -  ',event/E20904_Witness_Sworn_Options/E20904_WSO_Name,' - ',event/E20904_Witness_Sworn_Options/E20904_WSO_Number)"/>
</xsl:variable>
-->
<xsl:variable name="Event_Header_Text">
    <xsl:choose>
        <xsl:when test="event/E20904_Witness_Sworn_Options/E20904_WSO_Type[.='E20904_Defendant_sworn']">
            <xsl:text>Deft Sworn</xsl:text>
        </xsl:when>
        <xsl:when test="event/E20904_Witness_Sworn_Options/E20904_WSO_Type[.='E20904_Interpreter_sworn']">
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
