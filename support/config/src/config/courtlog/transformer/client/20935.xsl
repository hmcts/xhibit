<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
    <!-- only put the witness name & number on the end if it actually has them -->
    <xsl:value-of select="event/E20935_Witness_Read_Options/E20935_WR_Type"></xsl:value-of>
    <xsl:if test="string(event/E20935_Witness_Read_Options/E20935_WR_Name)"><xsl:text> </xsl:text><xsl:value-of select="event/E20935_Witness_Read_Options/E20935_WR_Name" /></xsl:if>
    <xsl:text> Read</xsl:text>
</xsl:variable>

<xsl:template match="event">

	<xsl:call-template name="BasicElements"/>

</xsl:template>

</xsl:stylesheet>
