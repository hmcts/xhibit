<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Deportation Event</xsl:variable>

	<xsl:variable name="reason">
		<xsl:choose>
			<xsl:when test="event/defendant_deportationReason = 'custodial'">
				<xsl:value-of select="'This defendant(or appellant) meets the requirements for Automatic Deportation because  the defendant(or appellant) is a foreign national and has received a custodial sentence of 12 months or more for any offence.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_deportationReason = 'suspended'">
				<xsl:value-of select="'This defendant(or appellant) meets the requirements for Automatic Deportation because the defendant(or appellant) is a foreign national and has Breached a suspended of 12 months or more for any offence.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_deportationReason ='recommendedDeportation'">
				<xsl:value-of select="'This defendant(or appellant) meets the requirements for Automatic Deportation because this court has recommended deportation.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_deportationReason ='seriousDrugOffence'">
				<xsl:value-of select="'This defendant(or appellant) meets the requirements for Automatic Deportation because the defendant(or appellant) is a foreign national and has been sentenced to a period of imprisonment of any length for a serious drug offence.'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'This defendant(or appellant) does not have any reason for deportation assigned to them.'"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<!-- Declare Global Variables END-->
	
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
		<xsl:call-template name="nationality"/>
		<xsl:call-template name="deportationReason"/>
	</xsl:template>
	
	<xsl:template name="deportationReason">
		<table><tr><td><b>Deportation Reason: </b></td><td><xsl:value-of select="$reason"/><br/></td></tr></table>
	</xsl:template>
	
	<xsl:template name="nationality">
		<xsl:if test="defendant_nationality!=''">
		<table><tr><td><b>Nationality: </b></td><td><xsl:value-of select="defendant_nationality"/></td></tr></table>		
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
