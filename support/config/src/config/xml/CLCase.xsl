<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="CLCaseValue">
<html>
<body>
	<table border="1" width='100%'>
			<tr align="center">				
				<td>
					<b><xsl:value-of select="case-details" /></b>
				</td>
				<td width="10%">
					<b><xsl:value-of select="user-id" /></b>
				</td>
			</tr>
		
	</table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>