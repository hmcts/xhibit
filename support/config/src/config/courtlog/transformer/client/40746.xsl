<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
	<xsl:choose>
		<xsl:when test="/event/E40746_Charge_Type='I'">
            <xsl:value-of select=" concat( 'Count ', event/E40746_Crest_Offence_Seq_No, ' on indictment ', event/E40746_Crest_Charge_Seq_No, ' ',event/E40746_Ref_Disposal_Title,' for defendant ', event/E40746_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40746_Charge_Type='O'">
			<xsl:value-of select=" concat( 'Summary Offence ', event/E40746_Crest_Offence_Seq_No, ' ',event/E40746_Ref_Disposal_Title,' for defendant ', event/E40746_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40746_Charge_Type='S'">
			<xsl:value-of select=" concat( 'Offence ', event/E40746_Crest_Offence_Seq_No, ' ',event/E40746_Ref_Disposal_Title,' for defendant ', event/E40746_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40746_Charge_Type='B'">
			<xsl:value-of select=" concat( 'Offence ', event/E40746_Crest_Offence_Seq_No, ' on breach ', event/E40746_Crest_Charge_Seq_No, ' ',event/E40746_Ref_Disposal_Title,' for defendant ', event/E40746_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40746_Charge_Type='C'">
			<xsl:value-of select=" concat( 'Offence ', event/E40746_Crest_Offence_Seq_No, ' ',event/E40746_Ref_Disposal_Title,' for appellant ', event/E40746_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select=" concat(event/E40746_Ref_Disposal_Title,' for defendant ', event/E40746_Defendant_Name )" disable-output-escaping="yes"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>
