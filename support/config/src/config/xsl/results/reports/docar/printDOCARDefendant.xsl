<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="DOCARDefendants">	
	<fo:table table-layout="fixed">
		<fo:table-column column-number="1" column-width="180px"/>
		<fo:table-column column-number="2" column-width="250px"/>
		<fo:table-column column-number="3" column-width="100px"/>
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
						<xsl:text>Date Papers Sent to CCA</xsl:text>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>
			<xsl:for-each select="./DOCARDefendantValues">
			
				<xsl:variable name="formattedDefName">
					<xsl:call-template name="format-text-for-wrapping">
						<xsl:with-param name="str" select="concat(./surname, ' ', ./first-name, ' ', ./middle-name)"/>
					</xsl:call-template>
				</xsl:variable>
			
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always">
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="normal">
							<xsl:value-of select="./case-type"/><xsl:value-of select="./case-number"/>-<xsl:value-of select="./defendant-number"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block wrap-option="wrap" space-before="1pt" space-after="1pt" font-weight="normal">							
							<xsl:value-of select="$formattedDefName"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-style="solid" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block space-before="1pt" space-after="1pt" font-weight="normal">
							<xsl:value-of select="./form-nGDate"/>
						</fo:block>
					</fo:table-cell>													
				</fo:table-row>                    
			</xsl:for-each>
		</fo:table-body>
	</fo:table>
</xsl:template>
</xsl:stylesheet>
