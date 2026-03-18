<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:template name="CTLRL">	
	<xsl:param name = "currentdate"/>
	
	<xsl:for-each select="./cases">
	
		<fo:block font-size="13pt" font-family="sans-serif" font-weight="bold">
			<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
		</fo:block>
	
		<fo:table border="none" table-layout="fixed">
			<fo:table-column column-number="1" column-width="1.0cm"/>
			<fo:table-column column-number="2" column-width="10.0cm"/>
			<fo:table-body>
				<fo:table-row border="none">
					<fo:table-cell>
						<fo:block>
							<xsl:text> </xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell height="3.5cm">
						<fo:block font-size="10pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="./prosecutorname"/><xsl:text>&#xA;</xsl:text>
							<xsl:value-of select="./prosecutor-address"/>
						</fo:block>	
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	
		<fo:block font-size="11pt" font-weight="normal" text-align="right">
			<xsl:value-of select="$currentdate"/>
		</fo:block>
		
		<fo:block font-size="13pt" font-family="sans-serif" font-weight="bold">
			<fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="2pt"/>
		</fo:block>
		
		<fo:block font-size="11pt" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>CUSTODY TIME LIMITS</xsl:text>
		</fo:block>
	
		<fo:block font-size="11pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>The custody time limits (pursuant to The Prosecution of Offences (Custody Time Limits) (Amendment) Regulations 2000),  in respect of the defendants named below are due to expire on the date(s) shown.</xsl:text>
		</fo:block>
	
		<fo:block font-size="11pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:value-of select="./casenumber"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="./casetitle"/>
		</fo:block>
		<fo:block font-size="11pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>(pursuant to The Prosecution of Offences (Custody Time Limits) (Amendment) Regulations 2000</xsl:text>
		</fo:block>
		
		<xsl:for-each select="./expiryinfos">
			<fo:block font-size="11pt" font-weight="bold" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
				<xsl:text>&#xD;&#xA;</xsl:text>
				<xsl:text>Expiry Date: </xsl:text>
				<xsl:value-of select="./custodytimelimit"/>
				<xsl:text>&#xD;&#xA;</xsl:text>
			</fo:block>
			<xsl:for-each select="./expiries">
				<fo:block font-size="11pt" start-indent="2pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
					<xsl:text>&#xD;&#xA;</xsl:text>
					<xsl:value-of select="./defendantname"/>
				</fo:block>
				<fo:block font-size="11pt" start-indent="2pt" font-weight="normal">
					<xsl:value-of select="./urn"/>
				</fo:block>
			</xsl:for-each>
		</xsl:for-each>

		<xsl:choose>
			<xsl:when test="position()&lt;last()">
				<fo:block break-after="page" font-size="11pt" font-weight="normal" text-align="right" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
					<xsl:text>&#xD;&#xA;</xsl:text>
					<xsl:text>An Officer of the Crown Court</xsl:text>
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block font-size="11pt" font-weight="normal" text-align="right" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
					<xsl:text>&#xD;&#xA;</xsl:text>
					<xsl:text>An Officer of the Crown Court</xsl:text>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>