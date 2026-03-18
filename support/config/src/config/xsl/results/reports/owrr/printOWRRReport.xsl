<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/CommonPubRepFunctions.xsl"/>
	<xsl:param name="basedir"/>
	<xsl:template match="OWRRPrintInformation">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="0cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">
					<fo:region-body margin-bottom="0cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="7cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<xsl:call-template name="OWRRFooter"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="OWRRBody">
						<xsl:with-param name="basedir" select="$basedir" />
					</xsl:call-template>
				</fo:flow>
			</fo:page-sequence>		
		</fo:root>
	</xsl:template>
	
	<xsl:template name="OWRRBody">
		<xsl:param name="basedir"/>
		<xsl:for-each select="./owrr-body">
				
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
							<xsl:choose>
								<xsl:when test="normalize-space(./prosecutor-dx) != ''">
									<xsl:value-of select="./prosecutor-dx"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="./prosecutor-address"/>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell></fo:table-row></fo:table-body>
				</fo:table>
				<xsl:call-template name="OWRRCommonBody"/>
				
				<!-- Copy for Defendant/Appellant's Solicitor if present -->
				<xsl:if test="normalize-space(./solicitor-name) != ''">
					<fo:block break-before="page"/>
					<xsl:call-template name="PubRepLetterHeader"/>
					<fo:table table-layout="fixed" height="4cm">
						<fo:table-column column-number="1" column-width="1.0cm"/>
						<fo:table-column column-number="2" column-width="10.0cm"/>
						<fo:table-body><fo:table-row>
						<fo:table-cell/>
						<fo:table-cell>
							<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:value-of select="./solicitor-name"/><xsl:text>&#xA;</xsl:text>
								<xsl:choose>
									<xsl:when test="normalize-space(./solicitor-dx) != ''">
										<xsl:value-of select="./solicitor-dx"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="./solicitor-address"/>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell></fo:table-row></fo:table-body>
					</fo:table>
					<xsl:call-template name="OWRRCommonBody"/>
				</xsl:if>
				
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="OWRRCommonBody">
		<xsl:param name="path" select="."/>
		
		<xsl:variable name="secondParaText">		
			<xsl:call-template name="CounselBlock"/>
		</xsl:variable>
		
		<fo:block space-before="1px">
			<fo:leader leader-pattern='rule' leader-length='100%'/>
		</fo:block>	
		<fo:block font-size="11pt" font-family="sans-serif" text-align="center" space-before="3px" >
			<xsl:text>Order withdrawing a representation order</xsl:text>
		</fo:block>
		
		<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="10px" space-after="20px" >
			<fo:block >
				<xsl:text>On </xsl:text><xsl:value-of select="$path/date-of-revocation"/><xsl:text> the Crown Court withdrew the order granting representation to the respondent </xsl:text>
				<xsl:value-of select="./prosecutor-name"/><xsl:text> of </xsl:text>
			</fo:block>
			<fo:block space-before="5px" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
				<xsl:value-of select="./prosecutor-address"/>
			</fo:block>
			<fo:block >
				<xsl:text>The order which was withdrawn granted representation to resist an appeal to the court against a decision of </xsl:text>
				<xsl:value-of select="$path/court-full-name"/> on 
				<xsl:value-of select="$path/mag-conviction-date"/>.
			</fo:block>
			<fo:block>
			<xsl:text>The order was withdrawn because </xsl:text>
				<xsl:choose>
					<xsl:when test="./revocation-reason = 1">
						<xsl:text>the respondent did not pay the representation contributions which had been ordered.</xsl:text>
					</xsl:when>
					<xsl:when test="./revocation-reason = 2">
						<xsl:text>the respondent applied for the representation to be withdrawn.</xsl:text>
					</xsl:when>
					<xsl:when test="./revocation-reason = 3">
						<xsl:text>the legal representatives withdrew and it was not in the interests of justice to assign new representatives.</xsl:text>
					</xsl:when>
				</xsl:choose>
			</fo:block>
		</fo:block>
		
	</xsl:template>
	
	<xsl:template name="OWRRFooter">
		<fo:static-content flow-name="xsl-region-after">
			<xsl:call-template name="SignatureBlock"/>
			<fo:block font-size="11pt" font-family="sans-serif" space-before="10px">To the respondent</fo:block>
			<fo:list-block>
				<fo:list-item>
					<fo:list-item-label>
						<fo:block>
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:text>You do not get a representation order now.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				<fo:list-item>
					<fo:list-item-label>
						<fo:block>
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:text>There may be costs to be paid if some work has already been done.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				<fo:list-item>
					<fo:list-item-label>
						<fo:block>
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:text>You may have to pay towards these costs.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				<fo:list-item>
					<fo:list-item-label>
						<fo:block>
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:text>Your solicitor or barrister, or both, cannot work for you under a representation order now.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				<fo:list-item>
					<fo:list-item-label>
						<fo:block>
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:text>You may have to pay their costs if they work for you after the date of this order.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
			</fo:list-block>
			<xsl:call-template name="FooterContents"/>
			<fo:block font-size="9pt" font-family="sans-serif">
				<xsl:text>This order was made under the Access to Justice Act 1999</xsl:text>
			</fo:block>
		</fo:static-content>
	</xsl:template>
	
</xsl:stylesheet>