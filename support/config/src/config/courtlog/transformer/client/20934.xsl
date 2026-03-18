<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>

	<xsl:variable name="Event_Header_Text">
	<xsl:text>Special Measures Application:</xsl:text>
	</xsl:variable>


	<xsl:template name="SpecialMeasures" match="event">
	<event_header>
		<xsl:value-of select="$Event_Header_Text"/>
	</event_header>
	<event_text>
<!-- The formatting here is a little strange because newline characters are signigicant -->
<xsl:if test="E20934_Special_Measures_Application_Options/E20934_Order_For_Screens_Granted='yes'">
Order for Screens Granted</xsl:if><xsl:if test="E20934_Special_Measures_Application_Options/E20934_Evidence_By_Live_Link='yes'">
Evidence by Live Link</xsl:if><xsl:if test="E20934_Special_Measures_Application_Options/E20934_Evidence_To_Be_Given_In_Private='yes'">
Evidence to be Given in Private</xsl:if><xsl:if test="E20934_Special_Measures_Application_Options/E20934_Removal_Of_Wigs_And_Gowns='yes'">
Removal of Wigs and Gowns</xsl:if><xsl:if test="E20934_Special_Measures_Application_Options/E20934_Video_Recorded_Evidence_In_Chief='yes'">
Video Recorded Evidence in Chief</xsl:if><xsl:if test="E20934_Special_Measures_Application_Options/E20934_Aids_To_Communication='yes'">
Aids to Communication</xsl:if>
<xsl:if test="free_text!=''">
---
<xsl:apply-templates select="free_text"/>
</xsl:if >
	</event_text>
	</xsl:template>

	<xsl:template match="event">
		<xsl:call-template name="SpecialMeasures"/>
	</xsl:template>

</xsl:stylesheet>
