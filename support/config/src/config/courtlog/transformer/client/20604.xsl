<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Appeal Witness <xsl:if test="event/E20604_Witness_Name!=''"><xsl:value-of select="event/E20604_Witness_Name"/></xsl:if><xsl:if test="event/E20604_Witness_No!=''"><xsl:value-of select="concat(' number ',event/E20604_Witness_No)"/></xsl:if> Released</xsl:variable>

<xsl:template match="event">

	<xsl:call-template name="BasicElements"/>

</xsl:template>

</xsl:stylesheet>

