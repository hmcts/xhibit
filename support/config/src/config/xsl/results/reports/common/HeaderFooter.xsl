<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:template name="Header">
		<fo:table table-layout="fixed" font-size="9pt" font-family="serif">
			<fo:table-column column-width="267px"/>
			<fo:table-column column-width="267px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block text-align="left" line-height="14pt" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="//date-of-report"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block text-align="right" line-height="14pt" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>Page </xsl:text>
							<fo:page-number/>
							<xsl:text> of </xsl:text>
							<fo:page-number-citation ref-id="theEnd"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<xsl:template name="CourtTitle">
		<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center">
			<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="//court-name"/>
		</fo:block>
	</xsl:template>
	<xsl:template name="Footer">
		<fo:static-content flow-name="xsl-region-after">
			<fo:table table-layout="fixed">
				<fo:table-column column-width="222px"/>
				<fo:table-column column-width="222px"/>
				<fo:table-column column-width="90px"/>
				<fo:table-body>
					<fo:table-row font-size="7pt">
						<fo:table-cell>
							<fo:block font-size="9pt" font-family="serif" line-height="14pt" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>Report: </xsl:text><xsl:value-of select="//short-report-code"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="9pt" font-family="serif" line-height="14pt" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>Printed at: </xsl:text><xsl:value-of select="//time-of-report"/>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block font-size="9pt" font-family="serif" line-height="14pt" linefeed-treatment="preserve" text-align="right" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:text>Ref: </xsl:text><xsl:value-of select="//user-name"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>				
				</fo:table-body>
			</fo:table>
		</fo:static-content>
	</xsl:template>				
</xsl:stylesheet>	