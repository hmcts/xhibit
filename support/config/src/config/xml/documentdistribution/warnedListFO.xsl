<?xml version="1.0" encoding="UTF-8"?>
<!--
	 +       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
    xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:fox="http://xml.apache.org/fop/extensions"
    version="1.0">
    
    <!-- Import common Warned List transform -->
    <xsl:include href="warnedListFO_common.xsl"/>
    
    <!-- Version Information -->
    <xsl:variable name="majorVersion" select="'2'" />
    <xsl:variable name="minorVersion" select="'0e'" />
    <xsl:variable name="stylesheet" select="'warnedListFO_cy.xsl'" />
    <xsl:variable name="last-modified-date" select="'2005-11-03'" />
    <!-- End Version Information -->
    
<!--    <xsl:comment>
        <xsl:text>Produced by : </xsl:text>
        <xsl:value-of select="$stylesheet" />
        <xsl:text> Version : </xsl:text>
        <xsl:value-of select="$majorVersion"/><xsl:text>.</xsl:text><xsl:value-of select="$minorVersion"/>
        <xsl:text>  last modified : </xsl:text>
        <xsl:value-of select="$last-modified-date"/>
    </xsl:comment>
    <xsl:comment>&#x00A9; Crown copyright 2003. All rights reserved.</xsl:comment>
    <xsl:comment>
    	<xsl:text>Document Unique Id : </xsl:text>
    	<xsl:value-of select="//cs:DocumentID/cs:UniqueID" />
    	<xsl:text> Document version : </xsl:text>
    	<xsl:value-of select="//cs:DocumentID/cs:Version" />
    	<xsl:text> Document timestamp : </xsl:text>
    	<xsl:value-of select="//cs:DocumentID/cs:TimeStamp" />
    	<xsl:text> Document stylesheet URL : </xsl:text>
    	<xsl:value-of select="//cs:DocumentID/cs:XSLstylesheetURL" />
    </xsl:comment>	-->
    
    <!-- top level match -->
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <!-- Call template to set up the page -->
            <xsl:call-template name="PageSetUp"/>
            <!-- Call template to display Warned List -->
            <xsl:call-template name="DisplayWarnedList">
                <xsl:with-param name="language">en</xsl:with-param>
            </xsl:call-template>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>