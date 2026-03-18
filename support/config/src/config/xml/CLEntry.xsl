<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
<html>
<body>
	<table border='1' width='100%'>
		<xsl:for-each select="events/event">
			<tr>
				<!--<td align='center' width='7%'>
					<b><xsl:value-of select="date" /></b>
				</td>-->
				<td align="center" width='7%'>
					<b><xsl:value-of select="time" /></b>
				</td>
				<td>
					<b><xsl:value-of select="text" /></b><br></br>
					<xsl:apply-templates select="." />
				</td>
			</tr>
		</xsl:for-each>
	</table>
</body>
</html>

</xsl:template>
<xsl:template match="event">
	<xsl:if test="./type='200_094'">
		<xsl:apply-templates select="./subtext/Prosecution-addresses-judge" />
		<xsl:apply-templates select="./subtext/Defence-addresses-judge" />
		<xsl:apply-templates select="./subtext/Judges-ruling" />
	</xsl:if>
</xsl:template>

<xsl:template match="Prosecution-addresses-judge">
	Prosecution addresses judge: <xsl:value-of select="." /><br></br>
</xsl:template>

<xsl:template match="Defence-addresses-judge">
	Defence addresses judge: <xsl:value-of select="." /><br></br>
</xsl:template>

<xsl:template match="Judges-ruling">
	Judge Addresses Advocate: <xsl:value-of select="." /><br></br>
</xsl:template>

</xsl:stylesheet>