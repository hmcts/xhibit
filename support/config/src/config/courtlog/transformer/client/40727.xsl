<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="verdictDescription">
	<xsl:choose>
		<xsl:when test="event/E40727_Ref_Verdict_Code='O'">
			<xsl:value-of select="concat('Other: ', event/Other_Verdict_Text)"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="event/Verdict_Desc"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="ratioText">
	<xsl:choose>
		<xsl:when test="event/Unanimous='true'">
			<xsl:text> Unanimous</xsl:text>
		</xsl:when>
		<xsl:when test="event/Unanimous='false'">
			<xsl:value-of select="concat(' majority ratio ', event/Ratio)"></xsl:value-of>
		</xsl:when>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="Event_Header_Text">
    <xsl:value-of select="event/Defendant_Name" disable-output-escaping="yes"/>
    <xsl:value-of select="concat(' indictment/count number ', event/Crest_Charge_Seq_No)" disable-output-escaping="yes"/>
    <xsl:value-of select="concat('/', event/Crest_Offence_Seq_No)" disable-output-escaping="yes"/>
    <xsl:value-of select="concat(' Verdict ', $verdictDescription, $ratioText)" disable-output-escaping="yes"/>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>