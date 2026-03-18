<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:include href="basic_elements.xsl"/>
  <!-- Declare Global Variables -->
<xsl:variable name="SelectedOption"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Type"/></xsl:variable>
<xsl:variable name="LAODate"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Date"/></xsl:variable>
<xsl:variable name="SelectedDefendant"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Name"/></xsl:variable>
<xsl:variable name="PSRRequired"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_PSR_Required"/></xsl:variable>
<xsl:variable name="JudgeName"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Reserved_To_Judge_Name"/></xsl:variable>
<xsl:variable name="NotReserved"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Not_Reserved"/></xsl:variable>
<xsl:variable name="DefendantLabel">Defendant </xsl:variable>
<xsl:variable name="DefendantName"><xsl:value-of select="event/defendant_name"/></xsl:variable>
<xsl:variable name="JudgeLabel">Reserved to judge </xsl:variable>
<xsl:variable name="PSRLabel">PSR Required </xsl:variable>
<xsl:variable name="NotReservedLabel">Not reserved. </xsl:variable>
<xsl:variable name="Event_Header_Text">
<xsl:if test="$DefendantName!=''">
<xsl:value-of select="concat($DefendantLabel,$DefendantName,'; ')"/>
</xsl:if>
<xsl:if test="$SelectedOption!=''">
<xsl:value-of select="concat($SelectedOption,' ')"/>
</xsl:if>
<xsl:if test="$LAODate!=''">
<xsl:value-of select="concat($LAODate,'; ')"/>
</xsl:if>
<xsl:if test="$PSRRequired='true'">
<xsl:value-of select="concat($PSRLabel,'; ')"/>
</xsl:if>
<xsl:if test="$NotReserved='true'">
<xsl:value-of select="$NotReservedLabel"/>
</xsl:if>
<xsl:if test="$JudgeName!=''">
<xsl:value-of select="concat($JudgeLabel,$JudgeName,'. ')"/>
</xsl:if>
</xsl:variable>

  <xsl:template match="event">
    <xsl:call-template name="BasicElements"/>
  </xsl:template>

</xsl:stylesheet>
