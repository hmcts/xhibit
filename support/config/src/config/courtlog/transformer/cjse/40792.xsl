<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="basic_elements.xsl"/>
	<!-- Declare Global Variables -->
	<xsl:variable name="Event_Header_Text">Hate Crime Event</xsl:variable>

	<xsl:variable name="reason">
		<xsl:choose>
			<xsl:when test="event/defendant_hateCrimeReason = 'general_disability'">
				<xsl:value-of select="'The court finds that the offence was aggravated because of disability in general.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason = 'victim_disability'">
				<xsl:value-of select="'The court finds that the offence was aggravated because of disability of the victim.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='racially_aggravated'">
				<xsl:value-of select="'The court finds that the offence was racially aggravated.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='religiously_aggravated'">
				<xsl:value-of select="'The court finds that the offence to have been both racially and religiously aggravated.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='racially_and_religiously_aggravated'">
				<xsl:value-of select="'The court finds that the offence was religiously aggravated.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='general_sex'">
				<xsl:value-of select="'The court finds that the offence was aggravated due to sexual orientation in general.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='victim_sex'">
				<xsl:value-of select="'The court finds that the offence was aggravated due to the sexual orientation of the victim.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='general_transgender'">
				<xsl:value-of select="'The court finds that the offence was aggravated due to transgender issues in general.'"/>
			</xsl:when>
			<xsl:when test="event/defendant_hateCrimeReason ='victim_transgender'">
				<xsl:value-of select="'The court finds that the offence was aggravated due to the transgender, or presumed transgender of the victim.'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'This defendant(or appellant) does not have any reason for hate crime assigned to them.'"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>	
	<!-- Declare Global Variables END-->
	
	<xsl:template match="event">
		<xsl:call-template name="BasicElements"/>
		<xsl:call-template name="hateCrimeReason"/>
	</xsl:template>
	
	<xsl:template name="hateCrimeReason">
		<table><tr><td><b>Hate Crime Reason: </b></td><td><xsl:value-of select="$reason"/><br/></td></tr></table>
	</xsl:template>
	
</xsl:stylesheet>
