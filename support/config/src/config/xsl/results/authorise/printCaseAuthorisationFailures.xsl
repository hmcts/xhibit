<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

	<xsl:template name="CaseAuthorisationFailures">
	       	<xsl:if test="@case-failure='true'">
			<fo:block>
			<fo:table table-layout="fixed">
				<fo:table-column column-number="1"/>
				<fo:table-header>
					<fo:table-row background-color="#EEEEEE">
						<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
							<fo:block font-weight="bold" font-size="9pt">
								<xsl:text>Reason for Failure</xsl:text>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<xsl:apply-templates select="./case-failure-reasons"/>
				</fo:table-body>
			</fo:table>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="case-failure-reasons">
		<!-- keep-together.within-column="always" keeps row together for page breaks -->
		<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
			<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
				<!-- failure reason column -->
  				<fo:block font-size="9pt" font-weight="normal">
        				<xsl:value-of select="."/>
        			</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

</xsl:stylesheet>
