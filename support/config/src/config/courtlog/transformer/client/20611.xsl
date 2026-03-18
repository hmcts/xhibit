<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">
	<xsl:value-of select="concat(event/E20611_Legal_Argument_Options/E20611_Opt1_Public_Not_Permitted_To_Enter,' ',event/E20611_Legal_Argument_Options/E20611_Opt2_Judges_Ruling,' ',event/E20611_Legal_Argument_Options/E20611_Opt3_Appellant_Addresses_Judge,' ',event/E20611_Legal_Argument_Options/E20611_Opt4_Respondent_Addresses_Judge)"/>
</xsl:variable>

<xsl:template match="event">

	<xsl:call-template name="BasicElements"/>

</xsl:template>

</xsl:stylesheet>
