<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/unlc/printUNLCReportValue.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooter.xsl"/>
	<xsl:template match="UNLCReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			 <fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="21cm" page-height="29.7cm" master-name="simple">				
					<fo:region-body margin-top="4cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="4cm"/>
					<fo:region-after extent="1.0cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="Header"/>
					<xsl:call-template name="CourtTitle"/>
					<fo:block space-before="5pt" font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="center">
					    <xsl:text>UNLISTED CASES (ORDERED BY </xsl:text>
						<xsl:value-of select="./sort-by"/> 
						<xsl:text>) </xsl:text>
					</fo:block>	
					<fo:block space-before="5pt" font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="center">
					    <xsl:text> Case Type: </xsl:text>
						<xsl:value-of select="./case-type"/>
						<xsl:text> Case Class: </xsl:text>
						<xsl:value-of select="./case-class"/>
						<xsl:text> BC Status: </xsl:text>
						<xsl:value-of select="./bc-status"/> 
						<xsl:text> Default Hearing Type: </xsl:text>
						<xsl:value-of select="./hearing-type"/>
						<xsl:text> Required Judge Type: </xsl:text>
						<xsl:value-of select="./judge-description"/> 
					</fo:block>	
					<fo:block space-before="5pt" font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="center">
						<xsl:text> (Notes Requested: Priority : </xsl:text>
						<xsl:value-of select="./priority-notes"/>
						<xsl:text> Restricted: </xsl:text>
						<xsl:value-of select="./restricted-notes"/>
						<xsl:text> Standard: </xsl:text>
						<xsl:value-of select="./standard-notes"/>
						<xsl:text> ) </xsl:text>
					</fo:block>
				</fo:static-content>

				<xsl:call-template name="Footer"/>
							
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="UNLCReportValues"/>
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>