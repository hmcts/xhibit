<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
 <xsl:variable name="Event_Header_Text">
 <xsl:for-each select="event/E20921_Defendant_List/E20921_Defendant_Details"><xsl:value-of select="concat(./E20921_Defendant_Name,'; ',./E20921_Defendant_TIC,' TIC(s) put and admitted')"/>.
</xsl:for-each></xsl:variable>
<xsl:template match="event">
    <xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>
