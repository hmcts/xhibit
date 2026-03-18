<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
    <!-- only put the witness name & number on the end if it actually has them -->
    <xsl:value-of select="event/E20904_Witness_Sworn_Options/E20904_WSO_Type"></xsl:value-of>
    <xsl:if test="string(event/E20904_Witness_Sworn_Options/E20904_WSO_Name)"><xsl:text> </xsl:text><xsl:value-of select="event/E20904_Witness_Sworn_Options/E20904_WSO_Name" /></xsl:if>
    <xsl:if test="string(event/E20904_Witness_Sworn_Options/E20904_WSO_Number)"><xsl:text> number </xsl:text><xsl:value-of select="event/E20904_Witness_Sworn_Options/E20904_WSO_Number" /></xsl:if>
    <xsl:text> sworn</xsl:text>
</xsl:variable>

<!-- old way of setting up the variable
<xsl:variable name="Event_Header_Text">
    <xsl:value-of select="concat(event/E20904_Witness_Sworn_Options/E20904_WSO_Type,' -  ', event/E20904_Witness_Sworn_Options/E20904_WSO_Name,' - ', event/E20904_Witness_Sworn_Options/E20904_WSO_Number)"/>
</xsl:variable>
-->

<xsl:template match="event">

	<xsl:call-template name="BasicElements"/>

</xsl:template>

</xsl:stylesheet>
