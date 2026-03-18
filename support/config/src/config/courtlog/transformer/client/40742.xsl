<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">
		<xsl:choose>
			<xsl:when test="/event/E40742_Charge_Type='I'">
				<xsl:value-of select="  concat( 'Disposal amended on count ', event/E40742_Crest_Offence_Seq_No, ' on indictment ', event/E40742_Crest_Charge_Seq_No, ' for defendant ',event/E40742_Defendant_Name, ' to ', event/E40742_Ref_Disposal_Title,' ')" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:when test="/event/E40742_Charge_Type='O'">
				<xsl:value-of select="  concat( 'Disposal amended on summary offence ', event/E40742_Crest_Offence_Seq_No, ' for defendant ',event/E40742_Defendant_Name, ' to ', event/E40742_Ref_Disposal_Title,' ')" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:when test="/event/E40742_Charge_Type='S'">
				<xsl:value-of select="  concat( 'Disposal amended on offence ', event/E40742_Crest_Offence_Seq_No, ' for defendant ',event/E40742_Defendant_Name, ' to ', event/E40742_Ref_Disposal_Title,' ')" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:when test="/event/E40742_Charge_Type='B'">
				<xsl:value-of select=" concat( 'Disposal amended on offence ', event/E40742_Crest_Offence_Seq_No, ' on breach  ', event/E40742_Crest_Charge_Seq_No, ' for defendant ',event/E40742_Defendant_Name, ' to ', event/E40742_Ref_Disposal_Title,' ')" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:when test="/event/E40742_Charge_Type='C'">
				<xsl:value-of select="  concat( 'Disposal amended on offence ', event/E40742_Crest_Offence_Seq_No, ' for appellant ',event/E40742_Defendant_Name, ' to ', event/E40742_Ref_Disposal_Title,' ')" disable-output-escaping="yes"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select=" concat('Disposal amended for defendant ',event/E40742_Defendant_Name, ' to ', event/E40742_Ref_Disposal_Title,' ')" disable-output-escaping="yes"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="event/E40742_Disposal_Detail!='None'"><xsl:value-of select="event/E40742_Disposal_Detail"/></xsl:if>
	</xsl:variable>
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
	</xsl:template>
</xsl:stylesheet>
