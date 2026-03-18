<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="event">
<html>
<body>
	<table border='1' width='100%'>
			<tr>
				<td align='center' width='7%'>
					<b><xsl:value-of select="date" /></b>
				</td>
				<td align="center" width='7%'>
					<b><xsl:value-of select="time" /></b>
				</td>
				<td>
					<b><xsl:value-of select="description" /></b><br></br>
					<xsl:value-of select="free_text" />
				</td>
			</tr>
	</table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>