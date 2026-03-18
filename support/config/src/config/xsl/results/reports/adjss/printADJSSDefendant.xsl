<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="ADJSSDefendants">	
	<fo:block>
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="75px"/>
		<fo:table-column column-number="2" column-width="75px"/>
		<fo:table-column column-number="3" column-width="240px"/>
		<fo:table-column column-number="4" column-width="160px"/>
		<fo:table-header>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="11pt">
						<xsl:text>List Date</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="11pt">
						<xsl:text>Case No</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="11pt">
						<xsl:text>Defendant</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="11pt">
						<xsl:text>Judge</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./ADJSSDefendantValues">
			
				<xsl:variable name="formattedDefendant">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./defendant-name"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:variable name="formattedJudge">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./judge-name"/>
					</xsl:call-template>
				</xsl:variable>
			
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="11pt">
						<fo:block font-size="11pt" font-weight="normal">
							<xsl:value-of select="./list-date"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="11pt">
						<fo:block font-size="11pt" font-weight="normal">
							<xsl:value-of select="./case-number"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="11pt">
						<fo:block wrap-option="wrap" space-before="1pt" space-after="1pt" font-weight="normal">							
							<xsl:value-of select="$formattedDefendant"/>
						</fo:block>
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">	
							<xsl:value-of select="concat(./hearing-date, ': for ', ./reason, ', to ', ./hearing-adjourned-date,'.')"/>
						</fo:block>
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">	
							<xsl:value-of select="./pti-urn"/>   
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="11pt">
						<fo:block wrap-option="wrap" space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:value-of select="$formattedJudge"/>
						</fo:block>
					</fo:table-cell>													
				</fo:table-row>                    
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
	</fo:block>
</xsl:template>
</xsl:stylesheet>