<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/ogrda/printOGRDADefendant.xsl"/>
	<xsl:param name="basedir"/>
	<xsl:template match="OGRDAOrder">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="0cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">
					<fo:region-body margin-bottom="0cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="3.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<xsl:call-template name="PubRepFooter"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="OGRDADefendant">
						<xsl:with-param name="basedir" select="$basedir" />
					</xsl:call-template>
				</fo:flow>
			</fo:page-sequence>
			
		</fo:root>
	</xsl:template>
</xsl:stylesheet>