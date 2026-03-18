<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:import href="/config/xsl/results/reports/common/CommonFormattingUtils.xsl"/>
<xsl:template name="RAGE">	
	<xsl:for-each select="./rage-values">
		<xsl:variable name="formattedCaseTitle">
			<xsl:call-template name="format-text-for-wrapping">
				<xsl:with-param name="str" select="./casetitle"/>
			</xsl:call-template>
		</xsl:variable>
	
		<fo:block font-size="12pt" font-family="sans-serif" font-weight="bold" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>&#xD;&#xA;</xsl:text>
			<xsl:value-of select="./casenumber"/>
			<xsl:text>&#x00A0;&#x00A0;</xsl:text>
			<xsl:value-of select="$formattedCaseTitle"/>
			<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;Class: </xsl:text>
			<xsl:value-of select="./classcode"/>
			<xsl:if test="./monitoringcategory != ''">
				<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;Monitor: </xsl:text>
				<xsl:value-of select="./monitoringcategory"/>
			</xsl:if>
			<xsl:if test="./juvenile != ''">
				<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;Juvenile: </xsl:text>
				<xsl:value-of select="./juvenile"/>
			</xsl:if>
			<xsl:if test="./benchwarrantdate != ''">
				<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;Bench Warrant Execution Date: </xsl:text>
				<xsl:value-of select="./benchwarrantdate"/>
			</xsl:if>
			<xsl:if test="./timeestimate != ''">
				<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;Est: </xsl:text>
				<xsl:value-of select="./timeestimate"/>
			</xsl:if>
		</fo:block>
		<fo:block font-size="11pt" font-family="sans-serif" font-weight="normal" linefeed-treatment="preserve" white-space-collapse="false" white-space-treatment="preserve">
			<xsl:text>Date sent for trial: </xsl:text>
			<xsl:value-of select="./commitalsent"/>
			<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;&#x00A0;Committed to: </xsl:text>
			<xsl:value-of select="./sitecommited"/>
			<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;&#x00A0;BC Status: </xsl:text>
			<xsl:value-of select="./bcstatus"/>
			<xsl:text>&#x00A0;&#x00A0;&#x00A0;&#x00A0;&#x00A0;Weeks old: </xsl:text>
			<xsl:value-of select="./weekdiff"/>
		</fo:block>
		
		<xsl:choose>
			<xsl:when test="./offences != ''">
				<fo:block font-size="11pt" font-family="sans-serif" font-weight="normal">
					<xsl:text>Offences</xsl:text>      
				</fo:block>
				<xsl:call-template name="tokenizeString">
					<xsl:with-param name="list" select="./offences"/>
					<xsl:with-param name="delimiter" select="'|'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
		
		<xsl:choose>
			<xsl:when test="./listhistorylist != ''">
				<fo:block font-size="11pt" font-family="sans-serif" font-weight="normal">
					<xsl:text>Listing History</xsl:text>      
				</fo:block>
				<xsl:call-template name="tokenizeString">
					<xsl:with-param name="list" select="./listhistorylist"/>
					<xsl:with-param name="delimiter" select="'|'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
		
		<xsl:choose>
			<xsl:when test="./noteslist != ''">
				 <fo:block font-size="11pt" font-family="sans-serif" font-weight="normal">
					<xsl:text>Case Notes</xsl:text>      
				 </fo:block>
				<xsl:call-template name="tokenizeString">
					<xsl:with-param name="list" select="./noteslist"/>
					<xsl:with-param name="delimiter" select="'|'"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
		
		<xsl:choose>
			<xsl:when test="./linkedcaseslist != ''">
				<fo:block font-size="11pt" font-family="sans-serif" font-weight="normal">
					<xsl:text>Linked Cases</xsl:text>      
				</fo:block>
				<xsl:call-template name="tokenizeStringLimited">
					<xsl:with-param name="list" select="./linkedcaseslist"/>
					<xsl:with-param name="delimiter" select="'|'"/>
					<xsl:with-param name="recursions" select="15"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
		
	</xsl:for-each>
</xsl:template>

<xsl:template name="tokenizeStringLimited">
	<xsl:param name = "list"/>
	<xsl:param name = "delimiter"/>
	<xsl:param name = "recursions"/>
	
	<xsl:if test="$recursions > 0">
	<xsl:choose>
		<xsl:when test="contains($list,$delimiter)">
			<fo:block font-size="11pt" font-family="sans-serif" start-indent="5pt" font-weight="normal">
				<xsl:value-of select="substring-before($list,$delimiter)"/>
			</fo:block>
			<xsl:call-template name="tokenizeStringLimited">
				<xsl:with-param name="list" select="substring-after($list,$delimiter)"/>
				<xsl:with-param name="delimiter" select="$delimiter"/>
				<xsl:with-param name="recursions" select="$recursions - 1"/>		
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose>
				<xsl:when test="$list = ''">
					<xsl:text/>
				</xsl:when>
				<xsl:otherwise>
					<fo:block font-size="11pt" font-family="sans-serif" start-indent="5pt" font-weight="normal">
						<xsl:value-of select="$list"/>
					</fo:block>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
	</xsl:if>
	<xsl:if test="$recursions = 0">
		<fo:block font-size="11pt" font-family="sans-serif" start-indent="5pt" font-weight="normal">
			<xsl:text>And Others</xsl:text>
		</fo:block>
	</xsl:if>
 </xsl:template>

<xsl:template name="tokenizeString">
	<xsl:param name = "list"/>
	<xsl:param name = "delimiter"/>
	<xsl:choose>
		<xsl:when test="contains($list,$delimiter)">
			<fo:block font-size="11pt" font-family="sans-serif" start-indent="5pt" font-weight="normal">
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="str" select="substring-before($list,$delimiter)"/>
				</xsl:call-template>
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
					<fo:block font-size="11pt" font-family="sans-serif" start-indent="5pt" font-weight="normal">
						<xsl:call-template name="format-text-for-wrapping">
							<xsl:with-param name="str" select="$list"/>
						</xsl:call-template>
					</fo:block>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
 </xsl:template>	
</xsl:stylesheet>