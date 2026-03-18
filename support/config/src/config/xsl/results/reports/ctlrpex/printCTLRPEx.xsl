<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
	<xsl:template name="CTLRPEx">	
		<xsl:param name = "tableHeader"/>
		<fo:block>
		<fo:table border="none" table-layout="fixed">
			<fo:table-column column-number="1" column-width="68px"/>
			<fo:table-column column-number="2" column-width="107px"/>
			<fo:table-column column-number="3" column-width="73px"/>
			<fo:table-column column-number="4" column-width="70px"/>
			<fo:table-column column-number="5" column-width="77px"/>
			<fo:table-column column-number="6" column-width="50px"/>
			<fo:table-column column-number="7" column-width="100px"/>
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">Case Deft-No</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">Defendant name</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">Comm/Sent Date</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">CTL Applies</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">CTL Expiry Date</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">Listed?</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold" font-size="11pt">Prosecutor</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<xsl:if test="($tableHeader != 'type2')">
				<fo:table-body>
					<xsl:for-each select="./ctlrpex-values[./ctlapplies = 'Y']">
						<fo:table-row border="none" space-before.optimum="12pt">
							<xsl:call-template name="CTLRPExTableRow">
							</xsl:call-template>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</xsl:if>

			<xsl:if test="($tableHeader != 'type1')">
				<fo:table-body>
					<xsl:for-each select="./ctlrpex-values[./ctlapplies = ' ' and (./currentbcstatus = 'C' or ./currentbcstatus = 'J')]">
						<fo:table-row border="none" space-before.optimum="12pt">
							<xsl:call-template name="CTLRPExTableRow">
							</xsl:call-template>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</xsl:if>
			
		</fo:table>
		</fo:block>
		<xsl:if test="$tableHeader = 'type2' and count(./ctlrpex-values) = 0">
			<fo:block font-weight="bold" font-size="11pt" text-align="center" space-before="2cm">No outstanding cases</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template name="CTLRPExTableRow">
	
		<xsl:variable name="formattedDefendant">
			<xsl:call-template name="format-text-for-wrapping">
				<xsl:with-param name="str" select="./defendantname"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="formattedProsecutor">
			<xsl:call-template name="format-text-for-wrapping">
				<xsl:with-param name="str" select="./prosecutorname"/>
			</xsl:call-template>
		</xsl:variable>
	
		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./casedefendantnumber"/>
			</fo:block>
		</fo:table-cell>

		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="$formattedDefendant"/>
			</fo:block>
		</fo:table-cell>

		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./committaldate"/>
			</fo:block>
		</fo:table-cell>

		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./ctlapplies"/>
			</fo:block>
		</fo:table-cell>

		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./custodytimelimit"/>
			</fo:block>
		</fo:table-cell>

		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./listed"/>
			</fo:block>
		</fo:table-cell>

		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="$formattedProsecutor"/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>	
</xsl:stylesheet>