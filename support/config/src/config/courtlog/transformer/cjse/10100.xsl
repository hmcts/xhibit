<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- edited with XMLSPY v5 rel. 4 U (http://www.xmlspy.com) by Fred (None) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="event">
		<be:EventParameters>
			<be:EventTypeID>
				<xsl:apply-templates select="type"/>
			</be:EventTypeID>
			<be:EventTime>
				<xsl:apply-templates select="date"/><xsl:text> </xsl:text><xsl:apply-templates select="time"/>
			</be:EventTime>
			<be:EventLocation>
				<xsl:apply-templates select="crestCourtId"/>
			</be:EventLocation>
			<be:MessageText>
				<xsl:apply-templates select="free_text"/>
			</be:MessageText>
			<be:CaseFileID>
				<xsl:apply-templates select="caseNumber"/>
			</be:CaseFileID>
		</be:EventParameters>
	</xsl:template>
</xsl:stylesheet>
