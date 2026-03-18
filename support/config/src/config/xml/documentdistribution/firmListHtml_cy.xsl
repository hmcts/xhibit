<?xml version="1.0" encoding="UTF-8"?>
<!--
	+       &#x00A9; Crown copyright 2003. All rights reserved.                                         +
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" version="1.1">
	<xsl:output method="html" indent="yes"/>
       <xsl:include href="firmListHtml_common.xsl"/>
	<!-- set up global variables -->
	<!-- Version and name Informaiton -->
	<xsl:variable name="majorVersion" select="'2'"/>
	<xsl:variable name="minorVersion" select="'0'"/>
	<xsl:variable name="stylesheet" select="'firmlistHtml_cy.xsl'"/>
	<xsl:variable name="last-modified-date" select="'2005-11-14'"/>
	<!-- End Version and name Informaiton -->
	<xsl:variable name="reportdate" select="//cs:ListHeader/cs:StartDate"/>
	<xsl:variable name="endDate" select="//cs:ListHeader/cs:EndDate"/>
	<xsl:variable name="version" select="//cs:ListHeader/cs:Version"/>
	<!-- set up keys -->
	<xsl:key name="hearingByDate" match="cs:Hearing" use="//cs:Sittings/cs:Sitting/cs:Hearings/cs:Hearing/cs:HearingDetails/cs:HearingDate"/>
	<xsl:key name="courtByName" match="//cs:CourtHouse/cs:CourtHouseName" use="//cs:CourtHouse/cs:CourtHouseName"/>
	<!-- Top level  Match -->
	<xsl:template match="/">
		<html>
			<meta content="text/html;  charset=UTF-8" http-equiv="Content-Type"/>
			<!-- Add Comments to HTML to help future maintenance -->
			<xsl:comment>
				<xsl:text>Produced by : </xsl:text>
				<xsl:value-of select="$stylesheet"/>
				<xsl:text> Version : </xsl:text>
				<xsl:value-of select="$majorVersion"/>
				<xsl:text>.</xsl:text>
				<xsl:value-of select="$minorVersion"/>
				<xsl:text>  last modified : </xsl:text>
				<xsl:value-of select="$last-modified-date"/>
			</xsl:comment>
			<xsl:comment>&#x00A9; Crown copyright 2003. All rights reserved.</xsl:comment>
			<xsl:comment>
				<xsl:text>Document Unique Id : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:UniqueID"/>
				<xsl:text> Document version : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:Version"/>
				<xsl:text> Document timestamp : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:TimeStamp"/>
				<xsl:text> Document stylesheet URL : </xsl:text>
				<xsl:value-of select="//cs:DocumentID/cs:XSLstylesheetURL"/>
			</xsl:comment>
			<!-- End of Add Comments to HTML to help future maintenance -->
			<body>
                        <xsl:call-template name="DisplayFirmList">
                            <xsl:with-param name="language">cy</xsl:with-param>
                        </xsl:call-template>
                        <div style="height=200px"/>
                        <xsl:call-template name="DisplayFirmList">
                            <xsl:with-param name="language">en</xsl:with-param>
                        </xsl:call-template>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
