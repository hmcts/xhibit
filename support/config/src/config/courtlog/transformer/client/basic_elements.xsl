<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:template name="BasicElements" match="event">
    <event_header>
        <xsl:value-of select="$Event_Header_Text"/>
    </event_header>
    <event_text>
        <xsl:apply-templates select="free_text"/>
    </event_text>
	</xsl:template>
	<xsl:template match="free_text">
		<xsl:value-of select="."/>
	</xsl:template>
</xsl:stylesheet>
