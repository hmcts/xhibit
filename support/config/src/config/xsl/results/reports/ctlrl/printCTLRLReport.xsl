<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/ctlrl/printCTLRL.xsl"/>
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooterLetters.xsl"/>
	<xsl:param name="basedir"/>
	<xsl:template match="CTLRLReport">
	<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="0.5cm" page-width="21.0cm" page-height="29.7cm" master-name="portrait">	
					<fo:region-body margin-top="4cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="4cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="portrait">
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="Header">
						<xsl:with-param name="basedir" select="$basedir"/>
					</xsl:call-template>
				</fo:static-content>
				
				<xsl:call-template name="Footer"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="CTLRL">
						<xsl:with-param name="currentdate"> 
							<xsl:value-of select="./date-of-report"/>
						</xsl:with-param>
					</xsl:call-template>
					
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>