<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
	<xsl:choose>
		<xsl:when test="/event/E40747_Charge_Type='I'">
			<xsl:value-of select=" concat( 'Disposal deleted for Count ', event/E40747_Crest_Offence_Seq_No, ', Indictment ', event/E40747_Crest_Charge_Seq_No, ' on case ', event/E40747_Case_Type, event/E40747_Case_No, ' for defendant ',event/E40747_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40747_Charge_Type='O'">
			<xsl:value-of select=" concat( 'Disposal deleted for summary offence ', event/E40747_Crest_Offence_Seq_No, ' on case ', event/E40747_Case_Type, event/E40747_Case_No,  ' for defendant ',event/E40747_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40747_Charge_Type='B'">
			<xsl:value-of select=" concat( 'Disposal deleted for offence ', event/E40747_Crest_Offence_Seq_No, ', on breach ', event/E40747_Crest_Charge_Seq_No, ' on case ', event/E40747_Case_Type, event/E40747_Case_No,  ' for defendant ',event/E40747_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40747_Charge_Type='S'">
			<xsl:value-of select="  concat( 'Disposal deleted for offence ', event/E40747_Crest_Offence_Seq_No, ' on case ', event/E40747_Case_Type, event/E40747_Case_No,  ' for defendant ',event/E40747_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:when>
		<xsl:when test="/event/E40747_Charge_Type='C'">
			<xsl:value-of select="  concat( 'Disposal deleted for offence ', event/E40747_Crest_Offence_Seq_No, ' on case ', event/E40747_Case_Type, event/E40747_Case_No,  ' for appellant ',event/E40747_Defendant_Name)" disable-output-escaping="yes"/>
		</xsl:when>
	</xsl:choose>
</xsl:variable>
<xsl:template match="event">
    <xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>
