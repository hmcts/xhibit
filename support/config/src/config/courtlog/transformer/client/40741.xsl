<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
	<xsl:choose>
		<xsl:when test="/event/E40741_Charge_Type='I'">
			<xsl:value-of select=" concat( 'Count ', event/E40741_Crest_Offence_Seq_No, ' on Indictment ', event/E40741_Crest_Charge_Seq_No, ' ',event/E40741_Ref_Disposal_Title,' ', event/E40741_Disposal_Detail, ' for defendant ', event/E40741_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40741_Charge_Type='O'">
			<xsl:value-of select=" concat( 'Summary Offence ', event/E40741_Crest_Offence_Seq_No, ' ',event/E40741_Ref_Disposal_Title,' ', event/E40741_Disposal_Detail, ' for defendant ', event/E40741_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40741_Charge_Type='S'">
			<xsl:value-of select=" concat( 'Offence ', event/E40741_Crest_Offence_Seq_No, ' ',event/E40741_Ref_Disposal_Title,' ', event/E40741_Disposal_Detail, ' for defendant ', event/E40741_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40741_Charge_Type='B'">
			<xsl:value-of select=" concat( 'Offence ', event/E40741_Crest_Offence_Seq_No, ' on breach ', event/E40741_Crest_Charge_Seq_No, ' ',event/E40741_Ref_Disposal_Title,' ', event/E40741_Disposal_Detail, ' for defendant ', event/E40741_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40741_Charge_Type='C'">
			<xsl:value-of select=" concat( 'Offence ', event/E40741_Crest_Offence_Seq_No, ' ',event/E40741_Ref_Disposal_Title,' ', event/E40741_Disposal_Detail, ' for appellant ', event/E40741_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select=" concat( event/E40741_Ref_Disposal_Title,' ', event/E40741_Disposal_Detail, ' for defendant ', event/E40741_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>
