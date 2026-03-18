<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">
		<xsl:choose>
			<xsl:when test="event/E20101_Bench_Warrant_Options/E20101_ABSCOND_Type">
				<xsl:value-of select="concat(event/defendant_name, '; ',event/E20101_Bench_Warrant_Options/E20101_OPTION_Type,'; ',event/E20101_Bench_Warrant_Options/E20101_ABSCOND_Type)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat(event/defendant_name, '; ',event/E20101_Bench_Warrant_Options/E20101_OPTION_Type)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>