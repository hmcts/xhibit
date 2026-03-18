<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->
<xsl:variable name="admitted">
<xsl:if test="event/Admitted='true'">
            <xsl:text> admitted </xsl:text>
</xsl:if>
<xsl:if test="event/Admitted='false'">
            <xsl:text> not admitted </xsl:text>
</xsl:if>
</xsl:variable>
<xsl:variable name="Event_Header_Text">
<xsl:value-of select=" concat( 'Failure to Appear offence ', event/Crest_Charge_Seq_No, $admitted, ' by ', event/defendant_name, '. Date put ', event/Date_Put )" disable-output-escaping="yes"/>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>