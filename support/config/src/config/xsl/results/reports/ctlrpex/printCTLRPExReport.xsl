<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/ctlrpex/printCTLRPEx.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>
	<xsl:template match="CTLRPExReport">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21cm" page-height="29.7cm" master-name="portrait">	
					<fo:region-body margin-top="3.5cm" margin-bottom="1cm"/>
					<fo:region-before extent="6.0cm"/>
					<fo:region-after extent="1.0cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="portrait">
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="Header"/>

					<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>&#xD;&#xA;</xsl:text>
						<xsl:text>The Crown Court at </xsl:text><xsl:value-of select="./court-name"/>
					</fo:block>
					<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>&#xD;&#xA;</xsl:text>
						<xsl:text>Custody Time Limits Exceptions Report</xsl:text>
					</fo:block>
				</fo:static-content>

				<xsl:call-template name="Footer"/>

				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>CTL Applies Indicator Is Y But No Custody Time Limit Date</xsl:text>
						<xsl:text>&#xD;&#xA;</xsl:text>
						<xsl:text>&#xD;&#xA;</xsl:text>
					</fo:block>		

					<xsl:call-template name="CTLRPEx">
						<xsl:with-param name="tableHeader" select="'type1'"/>
					</xsl:call-template>

					<fo:block break-after="page"/>

					<fo:block font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
						<xsl:text>Defendants In Custody Or In Care But No Custody Time Limit Date</xsl:text>
						<xsl:text>&#xD;&#xA;</xsl:text>
						<xsl:text>&#xD;&#xA;</xsl:text>
					</fo:block>		

					<xsl:call-template name="CTLRPEx">
						<xsl:with-param name="tableHeader" select="'type2'"/>
					</xsl:call-template>

					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>