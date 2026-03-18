<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/authorise/printCaseAuthorisationFailures.xsl"/>
	<xsl:import href="/config/xsl/results/authorise/printDefendantAuthorisationFailures.xsl"/>
	<xsl:import href="/config/xsl/results/authorise/printDefendantOnCaseAuthorisationFailures.xsl"/>

	<xsl:template match="authorisation-failure-print-value">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="1cm" margin-top="1cm" page-width="29.7cm" page-height="21.0cm" master-name="simple">
					<fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block text-align="end" font-size="9pt" font-family="serif" line-height="14pt">
						<xsl:text>XHIBIT 2 - Page </xsl:text>
						<fo:page-number/>
						<xsl:text> of </xsl:text>
						<fo:page-number-citation ref-id="theEnd"/>
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="left" space-after.optimum="15pt" break-before="page">
						<xsl:text>Case Number: </xsl:text>
						<xsl:value-of select="./case-type-and-number"/>
					</fo:block>

			                <xsl:call-template name="CaseAuthorisationFailures"/>
			                <xsl:call-template name="DefendantAuthorisationFailures"/>
			                <xsl:call-template name="DefOnCaseAuthorisationFailures"/>

					<!--<xsl:apply-templates select="."/>-->
					<fo:block id="theEnd"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
