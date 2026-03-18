<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="OBWDefendants">	
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="100px"/>
		<fo:table-column column-number="2" column-width="150px"/>
		<fo:table-column column-number="3" column-width="40px"/>
		<fo:table-column column-number="4" column-width="80px"/>
		<fo:table-column column-number="5" column-width="150px"/>
		<fo:table-header>
			<fo:table-row background-color="#EEEEEE">
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Case/Deft Number</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Defendant</xsl:text>						
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Class</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Date BW Issued</xsl:text>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell  border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm">
					<fo:block font-weight="bold" font-size="9pt">
						<xsl:text>Solicitor</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./OBWDefendantValues">
				<xsl:variable name="formattedDefName">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="./defendant-name"/>
					</xsl:call-template>
				</xsl:variable>
			
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./case-number"/>-<xsl:value-of select="./defendant-number"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">							
							<xsl:value-of select="$formattedDefName"/><xsl:text>&#xa;</xsl:text>
						</fo:block>
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:value-of select="./ptiurn"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:value-of select="./class-code"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:value-of select="./bw-issue-date"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<xsl:choose>
							<xsl:when test="(./solicitor-firm-name= '') or not(./solicitor-firm-name)">
								<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
									<xsl:text>No representation recorded</xsl:text>
								</fo:block>	
							</xsl:when>
							<xsl:otherwise>
								<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
									<xsl:call-template name="format-text-for-wrapping">
										<xsl:with-param name="str" select="./solicitor-firm-name"/>
									</xsl:call-template>
								</fo:block>
							</xsl:otherwise>
						</xsl:choose>
					</fo:table-cell>
				</fo:table-row>                    
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
</xsl:template>
</xsl:stylesheet>