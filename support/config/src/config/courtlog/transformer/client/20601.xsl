<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
    <xsl:variable name="selectedOption" select="event/E20601_Appellant_Attendance/E20601_AA_Type"/>
	<xsl:variable name="Event_Header_Text">
		<xsl:value-of select="concat('Appellant ', event/defendant_name, ' ')"/>
        <xsl:choose><xsl:when test="starts-with($selectedOption, 'Appellant')">
        <xsl:value-of select="substring-after($selectedOption,'Appellant')"/>
        </xsl:when>
        <xsl:otherwise><xsl:value-of select="$selectedOption"/></xsl:otherwise></xsl:choose>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
