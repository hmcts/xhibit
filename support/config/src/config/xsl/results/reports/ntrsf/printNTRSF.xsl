<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:template name="NTRSF">

	<xsl:param name="index" select="0"/>
	<xsl:param name="section" select="'noloop'"/>
		
		<!-- Prosecutor copy -->
		<xsl:if test="$section!='loop'">
			<fo:table border="none" table-layout="fixed">
				<fo:table-column column-number="1" column-width="11.0cm"/>
				<fo:table-body>
					<fo:table-row border="none">
						<fo:table-cell height="3.5cm">
							<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
								<xsl:value-of select="./ntrsf-values[1]/prosecutorname"/>
							</fo:block>
							<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
								<xsl:call-template name="tokenizeString">
									<xsl:with-param name="list" select="./ntrsf-values[1]/prosecutoraddress"/>
									<xsl:with-param name="delimiter" select="'|'"/>
								</xsl:call-template>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<xsl:call-template name="printSecondPart">
				<xsl:with-param name="lastpage" select="'false'"/>
			</xsl:call-template>
		</xsl:if>
		
		<!-- Governor copy -->
		<xsl:if test="$section!='noloop'">
			<xsl:choose>
				<xsl:when test="./ntrsf-values[$index]/incustody='Y'">
					<fo:table border="none" table-layout="fixed">
						<fo:table-column column-number="1" column-width="11.0cm"/>
						<fo:table-body>
							<fo:table-row border="none">
								<fo:table-cell height="3.5cm">
									<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
										<xsl:text>Care of the Governor</xsl:text>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
					<xsl:call-template name="printSecondPart">
						<xsl:with-param name="lastpage" select="'false'"/>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</xsl:if>
		
		<!-- Defendant/Solicitor copy -->
		<xsl:if test="$section!='noloop'">
			<fo:table border="none" table-layout="fixed">
				<fo:table-column column-number="1" column-width="11.0cm"/>
				<fo:table-body>
					<fo:table-row border="none">
						<fo:table-cell height="3.5cm">
							<xsl:choose>
								<xsl:when test="./ntrsf-values[$index]/representative">
									<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
										<xsl:value-of select="./ntrsf-values[$index]/representative"/>
									</fo:block>
									<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left"  start-indent="10mm">
										<xsl:call-template name="tokenizeString">
											<xsl:with-param name="list" select="./ntrsf-values[$index]/soladdress"/>
											<xsl:with-param name="delimiter" select="'|'"/>
										</xsl:call-template>
									</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
										<xsl:value-of select="./ntrsf-values[$index]/defendantname"/>
									</fo:block>
									<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
										<xsl:call-template name="tokenizeString">
											<xsl:with-param name="list" select="./ntrsf-values[$index]/defaddress"/>
											<xsl:with-param name="delimiter" select="'|'"/>
										</xsl:call-template>
									</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<xsl:choose>
				<xsl:when test="./ntrsf-values[$index+1]">
					<xsl:call-template name="printSecondPart">
						<xsl:with-param name="lastpage" select="'false'"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="printSecondPart">
						<xsl:with-param name="lastpage" select="'true'"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>	
		</xsl:if>
		
		<!-- Court To copy -->
		<xsl:if test="$section!='loop'">
			<fo:table border="none" table-layout="fixed">
				<fo:table-column column-number="1" column-width="11.0cm"/>
				<fo:table-body>
					<fo:table-row border="none">
						<fo:table-cell height="3.5cm">
							<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
								<xsl:value-of select="./ntrsf-values[1]/courtto"/>
							</fo:block>
							<fo:block font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left" start-indent="10mm">
								<xsl:choose>
									<xsl:when test="./ntrsf-values[1]/court-to-dx-ref">
										<xsl:value-of select="./ntrsf-values[1]/court-to-dx-ref"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="tokenizeString">
											<xsl:with-param name="list" select="./ntrsf-values[1]/courttoaddress"/>
											<xsl:with-param name="delimiter" select="'|'"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			<xsl:call-template name="printSecondPart">
				<xsl:with-param name="lastpage" select="'false'"/>
			</xsl:call-template>
		</xsl:if>
	
	<xsl:if test="./ntrsf-values[$index+1]">
		<xsl:call-template name="NTRSF">
			<xsl:with-param name="index" select="$index + 1" />
			<xsl:with-param name="section" select="'loop'"/>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<xsl:template name="tokenizeString">
	<xsl:param name="list"/>
	<xsl:param name="delimiter"/>
	<xsl:choose>
		<xsl:when test="contains($list, $delimiter)">
			<fo:block font-size="10pt" font-weight="normal">
				<xsl:value-of select="substring-before($list,$delimiter)"/>
			</fo:block>
			<xsl:call-template name="tokenizeString">
				<xsl:with-param name="list" select="substring-after($list,$delimiter)"/>
				<xsl:with-param name="delimiter" select="$delimiter"/>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose>
				<xsl:when test="$list = ''">
					<xsl:text/>
				</xsl:when>
				<xsl:otherwise>
					<fo:block font-size="10pt" font-weight="normal">
						<xsl:value-of select="$list"/>
					</fo:block>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="printSecondPart">
		<xsl:param name="lastpage"/>
		
		<fo:block font-size="12pt" font-weight="normal" text-align="center" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>NOTIFICATION OF TRANSFER</xsl:text>
		</fo:block>
		<fo:block font-size="11pt" font-family="sans-serif" font-weight="normal" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>The following case has been transferred from The Crown Court at </xsl:text><xsl:value-of select="./ntrsf-values[1]/courtfrom"/>
			<xsl:text>.</xsl:text>
		</fo:block>
		<fo:block font-size="11pt" font-family="sans-serif" font-weight="normal" text-align="left">
			<xsl:text>It will now be managed by The Crown Court at </xsl:text><xsl:value-of select="./ntrsf-values[1]/courttobody"/>
			<xsl:text>.</xsl:text>
		</fo:block>
		<fo:block font-size="11pt" font-weight="normal" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>Please contact the Crown Court at </xsl:text><xsl:value-of select="./ntrsf-values[1]/courttobody"/><xsl:text> for any questions about this transfer.</xsl:text>
		</fo:block>
		
		<fo:block font-size="11pt" font-weight="normal" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>Date of transfer: </xsl:text><xsl:value-of select="./ntrsf-values[1]/datetrans"/>
		</fo:block>
		<fo:block font-size="11pt" font-weight="normal" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:variable name="courtaddress" select="./ntrsf-values[1]/courttoaddress"/>
			<xsl:text>At: </xsl:text><xsl:value-of select="translate($courtaddress,'|',' ')"/>
		</fo:block>
		<fo:block font-size="11pt" font-weight="normal" text-align="left" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:value-of select="./ntrsf-values[1]/casenumber"/>
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:for-each select="./ntrsf-values">
				<xsl:choose>
					<xsl:when test="./rownumber &gt; 1">
						<fo:block start-indent="61pt" font-family="sans-serif" font-weight="normal" text-align="left">
							<xsl:value-of select="./defendantname2"/>
						</fo:block>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./defendantname2"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</fo:block>
		
		<fo:block font-size="11pt" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:text>An Officer of the Crown Court</xsl:text>
		</fo:block>
		
		<xsl:choose>
			<xsl:when test="$lastpage = 'false'">
				<fo:block break-after="page" font-size="11pt" font-weight="normal">
					<xsl:value-of select="./date-of-report"/>
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block font-size="11pt" font-weight="normal">
					<xsl:value-of select="./date-of-report"/>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
</xsl:template>

</xsl:stylesheet>