<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->

<xsl:variable name="MagistrateGeneralDisposal">
	<xsl:choose>
		<xsl:when test="event/E40751_Magistrate_General_Disposal='true'">
			<xsl:text>Magistrates Court General Disposal </xsl:text>
		</xsl:when>
		<xsl:when test="event/E40751_Variation_For_Magistrate_General_Disposal='true'">
			<xsl:text>Variation of Magistrates Court General Disposal </xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>


<xsl:variable name="Event_Header_Text">
	<xsl:choose>
		<xsl:when test="/event/E40751_Case_Type='A'">
			<xsl:value-of select=" concat( $MagistrateGeneralDisposal, event/E40751_Ref_Disposal_Title,' ', event/E40751_Disposal_Detail, ' for appellant ', event/E40751_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select=" concat( event/E40751_Ref_Disposal_Title,' ', event/E40751_Disposal_Detail, ' for defendant ', event/E40751_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>
