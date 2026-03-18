<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/drsr/printDRSRReportValue.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>
	<xsl:template match="DRSRReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			 <fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">				
					<fo:region-body margin-top="2.5cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="2.5cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="Header"/>
					<xsl:call-template name="CourtTitle"/>
					<fo:block space-before="5pt" space-after="5pt" font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center">
					    <xsl:text>Missing Cracked or Effective Codes </xsl:text>
					</fo:block>	
				</fo:static-content>

				<xsl:call-template name="Footer"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="DRSRReportValues"/>						
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>