<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/ctlrp/printCTLRP.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>
	<xsl:template match="CTLRPReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21cm" page-height="29.7cm" master-name="portrait">	
					<fo:region-body margin-top="3cm" margin-bottom="1cm"/>
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
						<xsl:text>Trials Approaching Custody Time Limits Due to Expire on or before </xsl:text>
						<xsl:value-of select="./input-date"/>
					</fo:block>
				</fo:static-content>
				
				<xsl:call-template name="Footer"/>
				
				<fo:flow flow-name="xsl-region-body">
		
					<xsl:call-template name="CTLRP_Type1">
						<xsl:with-param name="expired" select="'Y'"/>
					</xsl:call-template>
		
					<fo:block break-after="page"/>
			
					<xsl:call-template name="CTLRP_Type1">
						<xsl:with-param name="expired" select="'N'"/>
					</xsl:call-template>
			
					<fo:block break-after="page"/>
	
					<xsl:call-template name="CTLRP_Type2">
						<xsl:with-param name="expired" select="'Y'"/>
					</xsl:call-template>
						
					<fo:block break-after="page"/>

					<xsl:call-template name="CTLRP_Type2">
						<xsl:with-param name="expired" select="'N'"/>
					</xsl:call-template>

					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>