<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
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
    <xsl:value-of select="concat(' indictment/count number ', event/Crest_Charge_Seq_No, '/', event/Crest_Offence_Seq_No)" disable-output-escaping="yes"/>
    <xsl:if test="event/E40725_Ref_Verdict_Code='GA'">
		<xsl:text> Jury return with guilty verdict for an alternate offence namely </xsl:text>
	</xsl:if>
	<xsl:if test="event/E40725_Ref_Verdict_Code='GAJ'">
		<xsl:text> Not guilty but guilty of alternative offence on Judge's direction namely </xsl:text>
	</xsl:if>
	<xsl:if test="event/E40725_Ref_Verdict_Code='GAOJ'">
		<xsl:text> Not Guilty but Guilty of Alternate Offence not charged namely (By Judge alone under DVC&amp;VA 2004) </xsl:text>
    </xsl:if>    
    <xsl:value-of select="concat(' ', event/Offence_Desc, $ratioText)" disable-output-escaping="yes"/>

</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>