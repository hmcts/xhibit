<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/rumo/printRUMOReport.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>
	<xsl:template match="RUMOReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="0.5cm" margin-top="0.5cm" page-width="21cm" page-height="29.7cm" master-name="portrait">
					<fo:region-body margin-top="2.5cm" margin-bottom="1cm"/>
					<fo:region-before extent="2.5cm"/>
					<fo:region-after extent="0.8cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<xsl:for-each select="./collect-courts-values">
				<fo:page-sequence master-reference="portrait" initial-page-number="1" force-page-count="no-force">
					<xsl:variable name="collectCourtName" select="./collect-court-name"/>
					
					<fo:static-content flow-name="xsl-region-before">
						<fo:block text-align="left" font-size="9pt" font-family="serif" line-height="14pt">
							<xsl:value-of select="//date-of-report"/>
						</fo:block>
						<fo:block text-align="end" font-size="9pt" font-family="serif" line-height="14pt">
							<xsl:text>Page </xsl:text>
							<fo:page-number/>
							<xsl:text> of </xsl:text>
							<fo:page-number-citation ref-id="{$collectCourtName}"/>
						</fo:block>
						<xsl:call-template name="CourtTitle"/>
					</fo:static-content>
					
					<xsl:call-template name="Footer"/>
					
					<fo:flow flow-name="xsl-region-body">
						<fo:block space-before="10px">
							<fo:leader leader-pattern='rule' leader-length='100%'/>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left">
							<xsl:text>Clerk to the Justices</xsl:text>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left" space-before="5px">
							<xsl:value-of select="./collect-court-name"/>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="./collect-court-address"/>
						</fo:block>
						<fo:block space-before="10px">
							<fo:leader leader-pattern='rule' leader-length='100%'/>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" space-before ="5px">
							<xsl:text>LIST OF UNACKNOWLEDGED MONETARY ORDERS</xsl:text>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" space-before ="8px" space-after ="8px">
							<xsl:text>The following is a list of Monetary Orders sent to your court for which no acknowledgement has been received.</xsl:text>
						</fo:block>		
						<xsl:call-template name="RUMOReports"/>	
						<fo:block id="{$collectCourtName}"/>
					</fo:flow>
				</fo:page-sequence>
			</xsl:for-each>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>