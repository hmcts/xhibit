<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/CommonPubRepFunctions.xsl"/>
	<xsl:param name="basedir"/>
	<xsl:template match="OARRPrintInformation">
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
					<xsl:call-template name="OARRBody">
						<xsl:with-param name="basedir" select="$basedir" />
					</xsl:call-template>
				</fo:flow>
			</fo:page-sequence>		
		</fo:root>
	</xsl:template>
	
	<xsl:template name="OARRBody">
		<xsl:param name="basedir"/>
		<xsl:for-each select="./oarr-body-object">
				<!-- Copy for Respondent's previous Solicitor if present -->
				<xsl:if test="normalize-space(./previous-solicitor-name) != ''  and ./amendment-type='SOLICITOR'">
					<xsl:call-template name="PubRepLetterHeader"/>
					<fo:table table-layout="fixed" height="4cm">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body><fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:value-of select="./previous-solicitor-name"/><xsl:text>&#xA;</xsl:text>
								<xsl:choose>
									<xsl:when test="normalize-space(./previous-solicitor-dx) != ''">
										<xsl:value-of select="./previous-solicitor-dx"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="./previous-solicitor-address"/>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell></fo:table-row></fo:table-body>
					</fo:table>
					<xsl:call-template name="OARRCommonBody"/>
					<fo:block break-after="page"/>
				</xsl:if>
				
				
				<!-- Copy for Respondent's Solicitor if present -->
				<xsl:if test="normalize-space(./new-solicitor-name) != ''">
					<xsl:call-template name="PubRepLetterHeader"/>
					<fo:table table-layout="fixed" height="4cm">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body><fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:value-of select="./new-solicitor-name"/><xsl:text>&#xA;</xsl:text>
								<xsl:choose>
									<xsl:when test="normalize-space(./new-solicitor-dx) != ''">
										<xsl:value-of select="./new-solicitor-dx"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="./new-solicitor-address"/>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell></fo:table-row></fo:table-body>
					</fo:table>
					<xsl:call-template name="OARRCommonBody"/>
					<fo:block break-after="page"/>
				</xsl:if>
				
				<!-- Copy for Defendant/Appellant -->
				<xsl:call-template name="PubRepLetterHeader"/>
				<fo:table table-layout="fixed" height="4cm">
					<fo:table-column column-number="1" column-width="1.0cm"/>
					<fo:table-column column-number="2" column-width="10.0cm"/>
					<fo:table-body><fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="./prosecutor-name"/><xsl:text>&#xA;</xsl:text>
							<xsl:value-of select="./prosecutor-address"/>
						</fo:block>
					</fo:table-cell></fo:table-row></fo:table-body>
				</fo:table>
				<xsl:call-template name="OARRCommonBody"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="OARRCommonBody">		
		<xsl:variable name="secondParaText">		
			<xsl:call-template name="CounselBlock"/>
		</xsl:variable>
		
		<fo:block space-before="1px">
			<fo:leader leader-pattern='rule' leader-length='100%'/>
		</fo:block>	
		<fo:block font-size="11pt" font-family="sans-serif" text-align="center" space-before="3px" >
			<xsl:text>Order amending a representation order</xsl:text>
		</fo:block>
		
		<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="10px" >
			<fo:block >
				<xsl:text>On the </xsl:text><xsl:value-of select="./amendment-date"/><xsl:text> the order granting representation to </xsl:text>
			</fo:block>
			<fo:block>
				<xsl:value-of select="./prosecutor-name"/><xsl:text> of </xsl:text>
			</fo:block>
			<fo:block space-before="5px" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
				<xsl:value-of select="./prosecutor-address"/>
			</fo:block>
			<fo:block linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
				<xsl:text>was amended by </xsl:text><xsl:value-of select="$secondParaText"/><xsl:text>.</xsl:text>
			</fo:block>
		</fo:block>
	</xsl:template>
	
</xsl:stylesheet>