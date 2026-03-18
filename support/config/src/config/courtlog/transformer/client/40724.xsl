<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="basic_elements.xsl"/>

<!-- Declare Global Variables -->
<xsl:variable name="Event_Header_Text">Judgment: Miscellaneous Appeals
   <xsl:text>Judge and justices return with judgment for appeal</xsl:text>
   <xsl:value-of select="concat(' ', event/Appeal_Decs)" disable-output-escaping="yes"/>
</xsl:variable>
<xsl:template match="event">
    <xsl:call-template name="BasicElements"/>
</xsl:template>
</xsl:stylesheet>