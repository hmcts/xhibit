<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:include href="basic_elements.xsl"/>
  <!-- Declare Global Variables -->
<xsl:variable name="SelectedOption"><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Type"/></xsl:variable>
<xsl:variable name="identifier">reserved</xsl:variable>
<xsl:variable name="OptionLabel"><xsl:choose><xsl:when test="contains($SelectedOption, $identifier)"><xsl:text>Judge: </xsl:text></xsl:when><xsl:otherwise><xsl:text>Defendant: </xsl:text></xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="Event_Header_Text">

<xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Type"/>
<xsl:text>
</xsl:text>

<xsl:if test="event/E30200_Long_Adjourn_Options/E30200_LAO_Date!=''"><xsl:text>Date: </xsl:text><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Date"/>
<xsl:text>
</xsl:text>
</xsl:if>
<xsl:if test="event/E30200_Long_Adjourn_Options/E30200_LAO_Name!=''"><xsl:value-of select="$OptionLabel"/><xsl:value-of select="event/E30200_Long_Adjourn_Options/E30200_LAO_Name"/>
</xsl:if>

</xsl:variable>

  <xsl:template match="event">
    <xsl:call-template name="BasicElements"/>
  </xsl:template>

</xsl:stylesheet>