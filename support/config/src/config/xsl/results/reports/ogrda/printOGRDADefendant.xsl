<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/CommonPubRepFunctions.xsl"/>
	<xsl:template name="OGRDADefendant">
		<xsl:param name="basedir"/>
		<xsl:for-each select="./OGRDADefendantValues">
				
				<!-- Copy for Defendant/Appellant's Solicitor if present -->
				<xsl:if test="normalize-space(./solicitor-name) != ''">
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
					<xsl:call-template name="OGRDACommonBody"/>
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
				<xsl:call-template name="OGRDACommonBody"/>
				
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="OGRDACommonBody">
		<xsl:param name="path" select="."/>
		<xsl:variable name="partyDesc">
			<xsl:choose>
				<xsl:when test="$path/case-type = 'A'">
					<xsl:text>appellant</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>defendant</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="secondParaText">
			<xsl:choose>
				<xsl:when test="$path/case-type = 'T'">
					<xsl:text>Representation was granted for proceedings before the Crown Court in connection with a committal for trial, including in the event of being convicted or sentenced in those proceedings, advice and assistance in connection with the making of an appeal to the Criminal Division of the Court of Appeal.</xsl:text>
				</xsl:when>
				<xsl:when test="$path/case-type = 'S'">
					<xsl:text>Representation was granted for proceedings before the Crown Court in connection with a committal for sentence including advice and assistance in connection with the making of an appeal to the Criminal Division of the Court of Appeal.</xsl:text>
				</xsl:when>
				<xsl:when test="$path/case-type = 'A'">
					<xsl:text>Representation was granted for an appeal to the Crown Court against a decision of </xsl:text><xsl:value-of select="$path/court-full-name"/><xsl:text> on </xsl:text><xsl:value-of select="$path/mag-court-conviction-date"/><xsl:text>.</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="representationTeam">
			<xsl:choose>
				<xsl:when test="$path/junior-counsel = 'Y' and $path/queens-counsel = 'Y' and normalize-space($path/solicitor-name) != ''">
					<xsl:text>a solicitor, junior counsel and Queens counsel</xsl:text>
				</xsl:when>
				<xsl:when test="$path/junior-counsel = 'Y' and $path/queens-counsel = 'N' and normalize-space($path/solicitor-name) != ''">
					<xsl:text>a solicitor and junior counsel</xsl:text>
				</xsl:when>
				<xsl:when test="$path/junior-counsel = 'Y' and $path/queens-counsel = 'N' and normalize-space($path/solicitor-name) = ''">
					<xsl:text>junior counsel only</xsl:text>
				</xsl:when>
				<xsl:when test="$path/junior-counsel = 'N' and $path/queens-counsel = 'N' and normalize-space($path/solicitor-name) != ''">
					<xsl:text>a solicitor only</xsl:text>
				</xsl:when>
				<xsl:when test="$path/junior-counsel = 'N' and $path/queens-counsel = 'Y' and normalize-space($path/solicitor-name) != ''">
					<xsl:text>a solicitor and Queens counsel</xsl:text>
				</xsl:when>
				<xsl:when test="$path/junior-counsel = 'Y' and $path/queens-counsel = 'Y' and normalize-space($path/solicitor-name) = ''">
					<xsl:text>junior counsel and Queens counsel</xsl:text>
				</xsl:when>
				<xsl:when test="$path/junior-counsel = 'N' and $path/queens-counsel = 'Y' and normalize-space($path/solicitor-name) = ''">
					<xsl:text>Queens counsel only</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="finalParaText">
			<xsl:choose>
				<xsl:when test="$path/in-custody = 'Y'">
					<xsl:text>The </xsl:text><xsl:value-of select="$partyDesc"/><xsl:text> has been committed to </xsl:text><xsl:value-of select="$path/prison-name"/><xsl:text>.</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>The </xsl:text><xsl:value-of select="$partyDesc"/><xsl:text> has been released on bail and can be contacted at</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<fo:block space-before="1px">
			<fo:leader leader-pattern='rule' leader-length='100%'/>
		</fo:block>	
		<fo:block font-size="11pt" font-family="sans-serif" text-align="center" space-before="3px" >
			<xsl:text>Order granting Representation</xsl:text>
		</fo:block>
		
		<fo:block font-size="11pt" font-family="sans-serif" text-align="left" space-before="10px" >
			<fo:list-block>
				<fo:list-item>
					<fo:list-item-label>
						<fo:block space-before="10px">
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>On </xsl:text><xsl:value-of select="$path/order-date"/><xsl:text> the Crown Court granted representation&#xA;to the </xsl:text><xsl:value-of select="$partyDesc"/>
						</fo:block>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm" space-before="10px">
							<xsl:value-of select="$path/defendant-name"/><xsl:text>.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				
				<fo:list-item>
					<fo:list-item-label>
						<fo:block space-before="10px">
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:value-of select="$secondParaText"/>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				
				<fo:list-item>
					<fo:list-item-label>
						<fo:block space-before="10px">
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:text>The Representation granted shall consist of representation by </xsl:text><xsl:value-of select="$representationTeam"/><xsl:text> including advice on the preparation of the case for the proceedings.</xsl:text>
						</fo:block>
					</fo:list-item-body>
				</fo:list-item>
				
				<xsl:if test="normalize-space(./solicitor-name) != ''">
					<fo:list-item>
						<fo:list-item-label>
							<fo:block space-before="10px">
								<xsl:text>&#8226; </xsl:text>
							</fo:block>
						</fo:list-item-label>
						<fo:list-item-body>
							<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
								<xsl:text>The solicitor assigned is </xsl:text><xsl:value-of select="$path/solicitor-name"/><xsl:text> of</xsl:text>
							</fo:block>
							<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:value-of select="$path/solicitor-address"/><xsl:text>.</xsl:text>
							</fo:block>
						</fo:list-item-body>
					</fo:list-item>
				</xsl:if>
				
				<fo:list-item>
					<fo:list-item-label>
						<fo:block space-before="10px">
							<xsl:text>&#8226; </xsl:text>
						</fo:block>
					</fo:list-item-label>
					<fo:list-item-body>
						<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm">
							<xsl:value-of select="$finalParaText"/>
						</fo:block>
						<xsl:if test="$path/in-custody != 'Y'">
							<fo:block font-size="11pt" font-family="sans-serif" start-indent="1cm" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
								<xsl:value-of select="$path/defendant-address"/>
							</fo:block>
						</xsl:if>
					</fo:list-item-body>
				</fo:list-item>
			</fo:list-block>
		</fo:block>
		
	</xsl:template>

</xsl:stylesheet>