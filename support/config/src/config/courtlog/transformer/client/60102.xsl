<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>
<!-- Declare Global Variables -->
<!-- GAO - Alternate Offence, GLO - Lesser Offence -->
<xsl:variable name="offenceType">
<xsl:if test="event/Ref_Plea_Code='GLO'">
            <xsl:text>lesser</xsl:text>
</xsl:if>
<xsl:if test="event/Ref_Plea_Code='GAO'">
            <xsl:text>alternate</xsl:text>
</xsl:if>
</xsl:variable>
<xsl:variable name="Event_Header_Text">
<xsl:value-of select=" concat( 'Guilty plea to ', $offenceType, ' offence namely ', event/Alt_Offence, ' by ', event/defendant_name, ' on count ', event/Crest_Offence_Seq_No, ' on indictment ', event/Crest_Charge_Seq_No, '. Arraigned on ', event/Arraingment_Date )" disable-output-escaping="yes"/>
</xsl:variable>
<xsl:template match="event">
	<xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>