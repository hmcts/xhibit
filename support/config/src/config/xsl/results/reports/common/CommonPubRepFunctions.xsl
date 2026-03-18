<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="/config/xsl/results/reports/common/HeaderFooterLetters.xsl"/>
	<xsl:template name="PubRepLetterHeader">
		<xsl:param name="path" select="//."/>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="15.0cm"/>
			<fo:table-column column-width="50mm"/>
			<fo:table-body>
				<fo:table-row font-size="7pt">
					<fo:table-cell>
						<fo:block font-size="15pt" font-family="sans-serif" font-weight="bold" text-align="left" white-space-collapse="false" white-space-treatment="preserve">
							<xsl:text>The Crown Court</xsl:text>
							<xsl:text>&#xD;&#xA;</xsl:text>
							<xsl:text>at </xsl:text><xsl:value-of select="$path//court-name"/>
							<xsl:text>   </xsl:text>                     
						</fo:block>
						<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="15px" >
							<fo:inline font-weight="bold">Case No: </fo:inline>
							<xsl:value-of select="$path/case-number"/>
						</fo:block>	
						<fo:block font-size="10pt" font-family="sans-serif" text-align="left" space-before="3px" >
							<fo:inline font-weight="bold">Court Code: </fo:inline>
							<xsl:value-of select="$path/court-code"/>
						</fo:block>	
					</fo:table-cell>
					<fo:table-cell>
						<fo:block>
							<xsl:value-of select="./basedir"/>
							<fo:external-graphic>
								<xsl:attribute name="src"><xsl:value-of select="concat($basedir, 	'/images/Crown_Logo_Updated_2025.gif')"/></xsl:attribute>
								<xsl:attribute name="height">3.5cm</xsl:attribute>
								<xsl:attribute name="width">4.5cm</xsl:attribute>
								<xsl:attribute name="content-width">scale-to-fit</xsl:attribute>
							</fo:external-graphic>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>	
		<fo:block space-before="1px">
			<fo:leader leader-pattern='rule' leader-length='100%'/>
		</fo:block>		
	</xsl:template>
	
	<xsl:template name="SignatureBlock">
		<xsl:param name="path" select="."/>
		<fo:table table-layout="fixed">
		<fo:table-column column-width="13cm"/>
		<fo:table-column column-width="7cm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block>
							<xsl:text>An Officer of the Crown Court</xsl:text>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell/>
					<fo:table-cell>
						<fo:block space-before="3px">
							<xsl:text>Date: </xsl:text><xsl:value-of select="$path/date-of-report"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="PubRepFooter">
		<xsl:param name="path" select="."/>
		<fo:static-content flow-name="xsl-region-after">
			<xsl:call-template name="SignatureBlock"/>
			<xsl:call-template name="FooterContents"/>
			<fo:block font-size="9pt" font-family="sans-serif">
				<xsl:text>This order was made under the Access to Justice Act 1999</xsl:text>
			</fo:block>
		</fo:static-content>
	</xsl:template>
	
	<xsl:template name="SolicitorBlock">
		<xsl:if test="./amendment-type = 'SOLICITOR' and ./new-solicitor-name != ''">
			<xsl:choose>
				<xsl:when test="./previous-solicitor-name != ''">
					<xsl:text>substituting another solicitor, namely </xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>authorising the instruction of a solicitor, namely </xsl:text>
				</xsl:otherwise>
			</xsl:choose>	
			<xsl:value-of select="./new-solicitor-name"/><xsl:text> of </xsl:text><xsl:text>&#xA;</xsl:text>
			<fo:block linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
				<xsl:value-of select="./new-solicitor-address"/>
			</fo:block><xsl:text>&#xA;</xsl:text>
			<xsl:text>for the solicitor named in the order</xsl:text>
		</xsl:if>
	</xsl:template>

	<xsl:template name="CounselBlock">	
		<xsl:variable name="solicitorAmendmentBlock">
			<xsl:call-template name="SolicitorBlock"/>
		</xsl:variable>
		<xsl:value-of select="$solicitorAmendmentBlock"/>
		<xsl:choose>
			<xsl:when test="./junior-counsel + ./queens-counsel &gt; 0">
				<xsl:if test="$solicitorAmendmentBlock != ''">
					<xsl:text> and </xsl:text>
				</xsl:if>
				<xsl:text>authorising the instruction of </xsl:text>
				<xsl:if test="./junior-counsel &gt; 0">
					<xsl:if test="./junior-counsel &gt; 1">
						<xsl:value-of select="./junior-counsel"/>
					</xsl:if>
					<xsl:text> junior counsel</xsl:text>
					<xsl:if test="./queens-counsel &gt; 0">
						<xsl:text> and </xsl:text>
					</xsl:if>
				</xsl:if>
				<xsl:if test="./queens-counsel &gt; 0">
					<xsl:if test="./queens-counsel &gt; 1">
						<xsl:value-of select="./queens-counsel"/>
					</xsl:if>
					<xsl:text> Queens counsel</xsl:text>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$solicitorAmendmentBlock != ''">
					<xsl:text> and </xsl:text>
				</xsl:if>
				<xsl:text>revoking the authorisation for the instruction of counsel</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="RevocationReasonBlock">
	<xsl:param name="caseNumber" select="."/>
		<xsl:variable name="partyDesc">
			<xsl:choose>
				<xsl:when test="starts-with($caseNumber, 'A')">
					<xsl:text>appellant</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>defendant</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<fo:block>
			<xsl:text>The order was withdrawn because </xsl:text>
			<xsl:choose>
				<xsl:when test="./revocation-reason = 1">
					<xsl:text>the </xsl:text><xsl:value-of select="$partyDesc"/><xsl:text> did not pay the representation contributions which had been ordered.</xsl:text>
				</xsl:when>
				<xsl:when test="./revocation-reason = 2">
					<xsl:text>the </xsl:text><xsl:value-of select="$partyDesc"/><xsl:text> applied for the representation to be withdrawn.</xsl:text>
				</xsl:when>
				<xsl:when test="./revocation-reason = 3">
					<xsl:text>the legal representatives withdrew and it was not in the interests of justice to assign new representatives.</xsl:text>
				</xsl:when>
			</xsl:choose>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>	