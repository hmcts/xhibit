<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	<xsl:template name="RJSRow">
		<xsl:for-each select="./rjs-judge-type-values">
			<fo:table table-layout="fixed" hyphenate="true" language="en">
			<xsl:call-template name="SetColumnWidths"/>
				<fo:table-header>
					<xsl:call-template name="RJSTableHeader"/>
				</fo:table-header>
				<fo:table-body>
					<xsl:for-each select="./judges">
						<fo:table-row space-before.optimum="12pt" keep-together.within-column="always" border-style="none">
							<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
								<fo:block font-size="9pt" font-weight="normal">
									<xsl:call-template name="format-text-for-wrapping">
										<xsl:with-param name="str" select="./judge-name"/>
									</xsl:call-template>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
								<fo:block font-size="9pt" font-weight="normal">
									<xsl:value-of select="./sat-in-court"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
								<fo:block font-size="9pt" font-weight="normal">
									<xsl:value-of select="./sat-in-chambers"/>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
								<fo:block font-size="9pt" font-weight="normal">
									<xsl:value-of select="./total"/>
								</fo:block>
							</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>
		</fo:table>
			<xsl:call-template name="RJSTotal"/>

			<xsl:if test="position()&lt;last()">
				<fo:block break-after="page"/>
			</xsl:if>
		</xsl:for-each>			
	</xsl:template>
	<xsl:template name="RJSTotal">
		<fo:table border="none" table-layout="fixed">
			<fo:table-column column-number="1" column-width="150px"/>
			<fo:table-column column-number="2" column-width="130px"/>
			<fo:table-column column-number="3" column-width="130px"/>
			<fo:table-column column-number="4" column-width="130px"/>
		
			<fo:table-body>
				<fo:table-row space-before.optimum="12pt" keep-together.within-column="always" border-style="none">
					<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="bold">
							<xsl:text>Total</xsl:text>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="bold">
							<xsl:value-of select="./total-sat-in-court"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="bold">
							<xsl:value-of select="./total-sat-in-chambers"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
						<fo:block font-size="9pt" font-weight="bold">
							<xsl:value-of select="./total-all-sittings"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>                    			
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<xsl:template name="SetColumnWidths">
		<fo:table-column column-number="1" column-width="150px"/>
		<fo:table-column column-number="2" column-width="130px"/>
		<fo:table-column column-number="3" column-width="130px"/>
		<fo:table-column column-number="4" column-width="130px"/>
	</xsl:template>
	<xsl:template name="RJSTableHeader">
		<fo:table-row>
			<fo:table-cell number-columns-spanned="4" border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
				<fo:block font-size="13pt" font-family="sans-serif" font-weight="bold" text-align="center" space-after="6px">
					<xsl:value-of select="./judge-type"/><xsl:text> Sittings for month ending </xsl:text><xsl:value-of select="//sitting-date"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row background-color="#EEEEEE">
			<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
				<fo:block wrap-option="wrap" font-weight="bold" font-size="9pt">
					<xsl:text>Surname/Initials</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
				<fo:block font-weight="bold" font-size="9pt">
					<xsl:text>Sittings</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
				<fo:block font-weight="bold" font-size="9pt">
					<xsl:text>Chambers</xsl:text>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell  border-style="none" border-width="1px" border-collapse="collapse" padding="1mm">
				<fo:block font-weight="bold" font-size="9pt">
					<xsl:text>Total</xsl:text>						
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>