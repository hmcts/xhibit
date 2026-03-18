<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
<xsl:variable name="hours"><xsl:choose><xsl:when test="event/E20913_Jury_Out_Time_Hours!=''"><xsl:value-of select="event/E20913_Jury_Out_Time_Hours"/></xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose><xsl:text> hr(s) </xsl:text></xsl:variable>
<xsl:variable name="minutes"><xsl:choose>	<xsl:when test="event/E20913_Jury_Out_Time_Minutes!=''"><xsl:value-of select="event/E20913_Jury_Out_Time_Minutes"/></xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose><xsl:text> min(s) </xsl:text></xsl:variable>
<xsl:variable name="Event_Header_Text">Jury Returns<xsl:if test="event/E20913_Jury_Returns_Options!=''"><xsl:value-of select="concat(' - ',event/E20913_Jury_Returns_Options/E20913_JTO_Type)"/></xsl:if>.
<xsl:text>Total time jury has been out </xsl:text><xsl:value-of select="$hours"/><xsl:value-of select="$minutes"></xsl:value-of>.</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
