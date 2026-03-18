<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 	xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" 
 	xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
 	version="1.0">

	<!-- 
		COMMENTS 
		
		This Daily List Convertor takes the Mercator daily list and converts certain elements / removes 
		the name spaces  to allow for thx xml to be displayed as expected
			>> Xml converted for xslt : xhbDailyListFO_en_GB.xsl

		XSLT Tranformer to convert an XML to another XML file without namespaces
		Comments: local-name() - name excluding name space 
	-->

	<!-- MATCH ANY ELEMENTS -->
	<xsl:template match="*">
		<xsl:element name="{local-name()}">
			<xsl:apply-templates select="@* | node()"/>
		</xsl:element>
	</xsl:template>

	<!-- MATCH ANY ATTRIBUTES -->
	<xsl:template match="@*">
		<!-- set up an attribute-->
		<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
		<xsl:apply-templates/>
	</xsl:template>
	
	<!--	********************************
		Over ride default mappings 
		********************************
	-->
	
	<!-- move the attribute for the CommittingCourt to the element content -->
	<xsl:template match="cs:CommittingCourt/cs:CourtHouseCode">
		<xsl:element name="CourtHouseCode"><xsl:value-of select="@CourtHouseShortName"/></xsl:element>
	</xsl:template>

	<!-- prefix the court room number with text -->
	<xsl:template match="cs:CourtRoomNumber">
		<xsl:element name="CourtRoomNumber">Court Room <xsl:value-of select="."/></xsl:element>
	</xsl:template>
	
	<!-- change the element name to lower case 'Crest' -->
	<xsl:template match="cs:CRESTprintRef">
		<xsl:element name="CrestprintRef"><xsl:value-of select="."/></xsl:element>
	</xsl:template>
	
	<!-- insert the document name -->
	<xsl:template match="cs:DocumentName">
		<xsl:element name="DocumentName">Daily List</xsl:element>
	</xsl:template>
	
	<!-- translate the isMasked option -->
	<xsl:template match="cs:IsMasked">
		<xsl:element name="IsMasked">
			<xsl:choose>
				<xsl:when test=".='no'">N</xsl:when>
				<xsl:when test=".='yes'">Y</xsl:when>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	
	<!-- pick up the Judge's name and place it in the CitizenNameFullTitle element -->
	<xsl:template match="cs:Judiciary/cs:Judge">
		<xsl:element name="Judge">
			<xsl:element name="CitizenNameFullTitle">
				<xsl:if test="apd:CitizenNameRequestedName != 'N/A'">
					<xsl:value-of select="apd:CitizenNameRequestedName "/>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	
	<!-- pick up the justice names and place in the CitizenSurname element -->
	<xsl:template match="cs:Judiciary/cs:Justice">
		<xsl:element name="Justice">
			<xsl:element name="CitizenNameSurname">
                        <xsl:value-of select="apd:CitizenNameRequestedName"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!-- format the time corretly, removing the seconds element -->
	<xsl:template match="cs:SittingAt">
		<xsl:element name="SittingAt">
			<xsl:choose>
				<xsl:when test="string-length(.)=8">
					<xsl:value-of select="substring(.,1,5)"/>
				</xsl:when>
				<xsl:when test="string-length(.)=7">
					<xsl:value-of select="substring(.,1,4)"/>
				</xsl:when>
			</xsl:choose>
		</xsl:element>
	</xsl:template>

	<!--  translate the sitting priority -->
	<!-- 'Sitting priority' is now used to capture the isFloating logic
		T = Top Priority
		F = Floating
		R = Reserved
	-->

	<xsl:template match="cs:SittingPriority">
		<xsl:element name="SittingPriority">
			<xsl:choose>
				<xsl:when test=".='T'">false</xsl:when>
				<xsl:when test=".='F'">true</xsl:when>
				<xsl:when test=".='R'">false</xsl:when>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	
</xsl:stylesheet>
