<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/docar/printDOCARDefendant.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>	
	<xsl:template match="DOCARPrintValue">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">
					<fo:region-body margin-top="3cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="3cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="Header"/>
					<xsl:call-template name="CourtTitle"/>
					<fo:block font-size="11pt" font-weight="bold" font-family="sans-serif" text-align="center" >
						<xsl:text>Defendants with Outstanding Court of Appeal Results</xsl:text>
					</fo:block>		
					<fo:block font-size="11pt" font-weight="bold" font-family="sans-serif" text-align="center" space-after.optimum="20pt">
						<xsl:text>Prior to </xsl:text><xsl:value-of select="./prior-to-date"/>
					</fo:block>
				</fo:static-content>

				<xsl:call-template name="Footer"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="DOCARDefendants"/>
					<fo:block font-size="11pt" font-family="sans-serif" text-align="center" space-before.optimum="20pt">
						<xsl:text>End of Report</xsl:text>
					</fo:block>	
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>