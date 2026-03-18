<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/CommonPubRepFunctions.xsl"/>
	<xsl:param name="basedir"/>
	<xsl:template match="OWRDAPrintInformation">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1cm" margin-left="1cm" margin-bottom="0cm" margin-top="1cm" page-width="21.0cm" page-height="29.7cm" master-name="simple">
					<fo:region-body margin-bottom="0cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="7cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<xsl:call-template name="OWRDAFooter"/>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:call-template name="OWRDABody">
						<xsl:with-param name="basedir" select="$basedir" />
					</xsl:call-template>
				</fo:flow>
			</fo:page-sequence>		
		</fo:root>
	</xsl:template>
	
	<xsl:template name="OWRDABody">
		<xsl:param name="basedir"/>
		<xsl:for-each select="./owrda-body-object">
				
				<!-- Copy for Defendant/Appellant -->
				<xsl:call-template name="PubRepLetterHeader"/>
				<fo:table table-layout="fixed" height="4cm">
					<fo:table-column column-number="1" column-width="1.0cm"/>
					<fo:table-column column-number="2" column-width="10.0cm"/>
					<fo:table-body><fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block font-size="11pt" font-family="sans-serif" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:value-of select="./defendant-name"/><xsl:text>&#xA;</xsl:text>
							<xsl:choose>
								<xsl:when test="./in-custody = 'Y'">
									<xsl:value-of select="./prison-name"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="./defendant-address"/>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
					</fo:table-cell></fo:table-row></fo:table-body>
				</fo:table>
				<xsl:call-template name="OWRDACommonBody"/>
				
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
					<xsl:call-template name="OWRDACommonBody"/>
				</xsl:if>
				
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="OWRDACommonBody">
		<xsl:param name="path" select="."/>
		
		<xsl:variable name="partyDesc">
			<xsl:choose>
				<xsl:when test="starts-with($path/case-number, 'A')">
					<xsl:text>appellant</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>defendant</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
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
				<xsl:text>On </xsl:text><xsl:value-of select="$path/date-of-revocation"/><xsl:text> the Crown Court withdrew the order granting representation to the </xsl:text>
				<xsl:value-of select="$partyDesc"/>
			</fo:block>
			<fo:block>
				<xsl:value-of select="$path/defendant-name"/><xsl:text> of </xsl:text>
			</fo:block>
			<fo:block space-before="5px" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
				<xsl:choose>
					<xsl:when test="$path/in-custody = 'Y'">
						<xsl:value-of select="$path/prison-name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$path/defendant-address"/>
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
			<fo:block >
				<xsl:text>The order which was withdrawn granted representation for </xsl:text>
				
				<xsl:choose>
					<xsl:when test="starts-with($path/case-number, 'T')">
						<xsl:text>proceedings before the court in connection with Trial proceedings including, in 
						the event of being convicted or sentenced in those proceedings, advice and assistance in connection with the making of an appeal to the 
						Criminal Division of the Court of Appeal.</xsl:text>
					</xsl:when>
					<xsl:when test="starts-with($path/case-number, 'S')">
						<xsl:text>proceedings before the court in connection with a committal for sentence (including advice and assistance in connection
						with the making of an appeal to the Criminal Division of the Court of Appeal).</xsl:text>
					</xsl:when>
					<xsl:when test="starts-with($path/case-number, 'A')">
						<xsl:text>an appeal to the court against the decision at </xsl:text>
						<xsl:value-of select="$path/court-full-name"/> on 
						<xsl:value-of select="$path/mag-conviction-date"/>
					</xsl:when>
				</xsl:choose>
			</fo:block>
			<xsl:call-template name="RevocationReasonBlock">
				<xsl:with-param name="caseNumber" select="$path/case-number"/>
			</xsl:call-template>
		</fo:block>
		
	</xsl:template>
	
	<xsl:template name="OWRDAFooter">
		<xsl:variable name="partyDesc">
			<xsl:choose>
				<xsl:when test="starts-with(./owrda-body-object/case-number, 'A')">
					<xsl:text>appellant</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>defendant</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<fo:static-content flow-name="xsl-region-after">
			<xsl:call-template name="SignatureBlock"/>
			<fo:block font-size="11pt" font-family="sans-serif" space-before="10px">
				<xsl:text>To the </xsl:text><xsl:value-of select="$partyDesc"/>
			</fo:block>
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