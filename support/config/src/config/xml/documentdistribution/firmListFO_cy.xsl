<?xml version="1.0" encoding="utf-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:dt="http://xsltsl.org/date-time" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xml.apache.org/fop/extensions" version="1.0">
    <xsl:include href="firmListFO_common.xsl"/>
    <!-- Version Information -->
    <xsl:variable name="majorVersion" select="'2'" />
    <xsl:variable name="minorVersion" select="'0c'" />
    <xsl:variable name="stylesheet" select="'firmListFO_cy.xsl'" />
    <xsl:variable name="last-modified-date" select="'2005-09-12'" />
    <!-- End Version Information -->
    <!-- Top level  Match -->
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <!-- Call template to set up the page -->
            <xsl:call-template name="PageSetUp"/>
            <!-- Call template to display running List -->
            <xsl:call-template name="DisplayFirmList">
                <xsl:with-param name="language">cy</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="DisplayFirmList">
                <xsl:with-param name="language">en</xsl:with-param>
            </xsl:call-template>            
        </fo:root>
    </xsl:template>
</xsl:stylesheet>