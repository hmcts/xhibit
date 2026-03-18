<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="CTLRP_Type1">
	<xsl:param name = "expired"/>
	<xsl:call-template name="CTLRP">
		<xsl:with-param name="param1" select="'ST'"/>
		<xsl:with-param name="param2" select="'IO'"/>
		<xsl:with-param name="param3" select="'EW'"/>
		<xsl:with-param name="tableHeader" select="'type1'"/>
		<xsl:with-param name="expired" select="$expired"/>
	</xsl:call-template>
</xsl:template>
<xsl:template name="CTLRP_Type2">
	<xsl:param name = "expired"/>
	<xsl:call-template name="CTLRP">
		<xsl:with-param name="param1" select="'CT'"/>
		<xsl:with-param name="param2" select="'VB'"/>
		<xsl:with-param name="param3" select="'TC'"/>
		<xsl:with-param name="tableHeader" select="'type2'"/>
		<xsl:with-param name="expired" select="$expired"/>
	</xsl:call-template>
</xsl:template>
<xsl:template name="CTLRP">	
	<xsl:param name = "param1"/>
	<xsl:param name = "param2"/>
	<xsl:param name = "param3"/>
	<xsl:param name = "tableHeader"/>
	<xsl:param name = "expired"/>
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
				<fo:table-cell number-columns-spanned="7">
					<fo:block font-weight="bold" font-size="11pt" font-family="sans-serif" text-align="center" margin-bottom="10mm">
						<xsl:choose>
							<xsl:when test="($tableHeader = 'type1')">
								<xsl:text> Committals for Trial, Voluntary Bill, Transfer Certificate</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text> Sent for Trial, Indictment Only, Either Way</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</fo:block>	
				</fo:table-cell>
			</fo:table-row>
			<xsl:choose>
				<xsl:when test="($expired = 'Y')">
					<fo:table-row>
						<fo:table-cell number-columns-spanned="7">
							<fo:block font-weight="bold" font-size="11pt" margin-bottom="2.5mm">Defendants whose CTL Expired date has expired</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:when>
			</xsl:choose>
			<fo:table-row>
			<fo:table-cell>
				<fo:block font-weight="bold" font-size="11pt">Case Deft-No</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block font-weight="bold" font-size="11pt">Defendant name</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<xsl:choose>
					<xsl:when test="($tableHeader = 'type1')">
						<fo:block font-weight="bold" font-size="11pt">Committal Date</fo:block>
					</xsl:when>
					<xsl:otherwise>
						<fo:block font-weight="bold" font-size="11pt">1st Magistrates Hearing Date</fo:block>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block font-weight="bold" font-size="11pt">CTL Expiry Date</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block font-weight="bold" font-size="11pt">Reminder last Printed</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block font-weight="bold" font-size="11pt">Listed?</fo:block>
			</fo:table-cell>
			<fo:table-cell>
				<fo:block font-weight="bold" font-size="11pt">Prosecutor</fo:block>
			</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		
		<fo:table-body>
			<xsl:for-each select="./ctlrp-values[./expired = $expired]">
				<xsl:if test="(./receipttype != $param1 and ./receipttype != $param2 and ./receipttype != $param3)">
					<xsl:choose>
						<xsl:when test="./highlightedrow !='N'">
							<xsl:call-template name="CTRLPTableRow">
								<xsl:with-param name="param11" select="$param1"/>
								<xsl:with-param name="param21" select="$param2"/>
								<xsl:with-param name="param31" select="$param3"/>
								<xsl:with-param name="highlightColour" select="'#D0D0D0'"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="CTRLPTableRow">
								<xsl:with-param name="param11" select="$param1"/>
								<xsl:with-param name="param21" select="$param2"/>
								<xsl:with-param name="param31" select="$param3"/>
								<xsl:with-param name="highlightColour" select="'transparent'"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
			
		</fo:table-body>
	</fo:table>
	</fo:block>
	<xsl:if test="$tableHeader = 'type2' and $expired = 'N' and count(./ctlrp-values) = 0">
		<fo:block font-weight="bold" font-size="11pt" text-align="center" space-before="2cm">No outstanding cases</fo:block>
	</xsl:if>
</xsl:template>

<xsl:template name="CTRLPTableRow">
	<xsl:param name = "param11"/>
	<xsl:param name = "param21"/>
	<xsl:param name = "param31"/>
	<xsl:param name = "highlightColour"/>
	
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
	
	<fo:table-row border="none" space-before.optimum="12pt">
		<xsl:attribute name="background-color">
			<xsl:value-of select="$highlightColour"/>
		</xsl:attribute>
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
				<xsl:choose>
					<xsl:when test="(./receipttype != 'ST' and ./receipttype != 'IO' and ./receipttype != 'EW')">
						<xsl:value-of select="./committaldate"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./convictiondate"/>
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
		</fo:table-cell>
	
		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./custodytimelimit"/>
			</fo:block>
		</fo:table-cell>
						
		<fo:table-cell border-style="none" border-width="1px" border-collapse="collapse" padding="1mm" font-size="9pt">
			<fo:block wrap-option="wrap" font-weight="normal">
				<xsl:value-of select="./reminderprinted"/>
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
	</fo:table-row>
 </xsl:template>	
</xsl:stylesheet>