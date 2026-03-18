<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="CLDayValue">
<html>
<body>
	<table border="1" width='100%'>
			<tr align="center">				
				<td>
					<b>Date</b>
				</td>
				<td>
					<b>Shorhand Writer</b>
				</td>
				<td>
					<b>Court Clerk</b>
				</td>
				<td>
					<b>Usher</b>
				</td>
				<td>
					<b>Counsel</b>
				</td>
				
			</tr>
			<tr align="center">				
				<td>
					<xsl:value-of select="date" />
				</td>
				<td>
					<xsl:for-each select="shorthand-writers">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>
					
				</td>
				<td>
					<xsl:for-each select="court-clerks">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>
				</td>
				<td>
					<xsl:for-each select="ushers">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>
				</td>
				<td>
					<xsl:for-each select="counsels">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>
				</td>
			</tr>
	</table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>