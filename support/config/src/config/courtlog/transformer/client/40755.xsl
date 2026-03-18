<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->
<xsl:variable name="MagistrateGeneralDisposal">
	<xsl:choose>
		<xsl:when test="event/E40755_Magistrate_General_Disposal='true'">
			<xsl:text>Magistrates Court General </xsl:text>
		</xsl:when>
		<xsl:when test="event/E40755_Variation_For_Magistrate_General_Disposal='true'">
			<xsl:text>Variation of Magistrates Court General </xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="Event_Header_Text">
	<xsl:choose>
		<xsl:when test="/event/E40755_Case_Type='A'">
			<xsl:value-of select=" concat( $MagistrateGeneralDisposal, 'Disposal deleted for appellant ', event/E40755_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select=" concat( 'Disposal deleted for defendant ', event/E40755_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>
