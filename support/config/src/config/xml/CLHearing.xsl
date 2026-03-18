<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="CLHearingValue">
<html>
<body>
	<table border="1" width='100%'>
			<tr align="center">
				<td>
					<b>Current Case</b>
				</td>
				<td>
					<b>Defendant</b>
				</td>
				<td>
					<b>Judge</b>
				</td>
				<td>
					<b>Prosecutor Advocate</b>
				</td>
				<td>
					<b>Defence Advocate</b>
				</td>
				<td>
					<b>Hearing Type</b>
				</td>
				<td>
					<b>Time Listed</b>
				</td>
				<td>
					<b>Court Reporter</b>
				</td>
			</tr>
			<tr align="center">
				<td>
					<xsl:value-of select="current-case" />
				</td>
				<td>
					<xsl:for-each select="defendants">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>

				</td>
				<td>
					<xsl:value-of select="judge" />
				</td>
				<td>
					<xsl:for-each select="prosecution-advocates">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>
				</td>
				<td>
					<xsl:for-each select="defence-advocates">
						<xsl:value-of select="." /><br></br>
					</xsl:for-each>
				</td>
				<td>
					<xsl:value-of select="hearing-type" />
				</td>
				<td>
					<xsl:value-of select="time-listed" />
				</td>
				<td>
					<xsl:value-of select="shorthand-writer" />
				</td>

			</tr>
	</table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>