<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:preserve-space elements="free_text"/>
	<xsl:template name="BasicElements" match="event">
	<html><body><table><tr><td><b>
<xsl:value-of select="$Event_Header_Text"/>
</b>
    <br/><xsl:apply-templates select="free_text"/></td></tr></table></body></html>
	</xsl:template>

	<xsl:template match="free_text">
		<xsl:value-of select="."/>
	</xsl:template>
</xsl:stylesheet>
