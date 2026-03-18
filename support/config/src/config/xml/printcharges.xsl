<?xml version="1.0"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format" 
	version="1.0">
	
	<!-- <xsl:include href="/config/xml/PrintCourtLogEvents.xsl" /> -->
	<xsl:import href="/config/xml/PrintCourtLogEvents.xsl"/>
	
	<xsl:template match="print-charges-value">
		<!-- <xsl:template match="/"> -->
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master margin-right="1.5cm" margin-left="1.5cm" 
				margin-bottom="2cm" margin-top="1cm" 
				page-width="21cm" page-height="29.7cm" master-name="first">
					<fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
					<fo:region-before extent="1cm"/>
					<fo:region-after extent="1.5cm"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="first">
				<fo:flow flow-name="xsl-region-body">
					<fo:block space-after.optimum="10pt" font-weight="bold" font-size="16pt" text-align="center">
						<xsl:value-of select="header-line1"/>
					</fo:block>
					<fo:block space-after.optimum="10pt" font-weight="bold" font-size="16pt" text-align="center">
						<xsl:value-of select="header-line2"/>
					</fo:block>
					<fo:block space-after.optimum="10pt" font-weight="bold" font-size="16pt" text-align="center">
						<xsl:value-of select="header-line3"/>
					</fo:block>
					<!--
					<fo:block space-after.optimum="20pt" font-weight="bold" font-size="12pt" text-align="left">
						<xsl:text>The Queen V's </xsl:text>
						<xsl:apply-templates select="./charge-data/all-defendants"/>
					</fo:block>
					-->
					<!-- Indictments -->
					<fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="12pt" font-weight="bold" text-align="left">
				       	<xsl:text>Indictments</xsl:text>
					</fo:block>
					<fo:block>
					<fo:table table-layout="fixed" inline-progression-dimension.minimum="100%" border-collapse="separate">
						<fo:table-column column-number="1" column-width="30mm"/>
						<fo:table-column column-number="2" column-width="60mm"/>
						<fo:table-column column-number="3" column-width="60mm"/>
						<fo:table-body>
							<xsl:apply-templates select="./charge-data/charges/charge-type[.='I']">
								<xsl:sort select="../crest-charge-seq-no" data-type="number"/>
							</xsl:apply-templates>
						</fo:table-body>
					</fo:table>
					</fo:block>
					<!-- Summary Offences (Section 41s) -->
					<fo:block>
					<fo:table table-layout="fixed" inline-progression-dimension.minimum="100%" border-collapse="separate">
						<fo:table-column column-number="1" column-width="30mm"/>
						<fo:table-column column-number="2" column-width="60mm"/>
						<fo:table-column column-number="3" column-width="60mm"/>
						<fo:table-body>
							<xsl:apply-templates select="./charge-data/charges/charge-type[.='O']">
								<xsl:sort select="../offence-values/crest-offence-seq-no" data-type="number"/>
							</xsl:apply-templates>
						</fo:table-body>
					</fo:table>
					</fo:block>
					<!-- Committals for Sentence -->
					<fo:block>
					<fo:table table-layout="fixed" inline-progression-dimension.minimum="100%" border-collapse="separate">
						<fo:table-column column-number="1" column-width="30mm"/>
						<fo:table-column column-number="2" column-width="60mm"/>
						<fo:table-column column-number="3" column-width="60mm"/>
						<fo:table-body>
							<xsl:apply-templates select="./charge-data/charges/charge-type[.='S']">
								<xsl:sort select="../offence-values/crest-offence-seq-no" data-type="number"/>
							</xsl:apply-templates>
						</fo:table-body>
					</fo:table>
					</fo:block>
					<!-- Breaches -->
					<fo:block font-size="14pt" font-family="sans-serif" break-before="page" space-after.optimum="12pt" font-weight="bold" text-align="left">
						<xsl:text>Breaches</xsl:text>
					</fo:block>
					<fo:block>
					<fo:table table-layout="fixed" inline-progression-dimension.minimum="100%" border-collapse="separate">
						<fo:table-column column-number="1" column-width="30mm"/>
						<fo:table-column column-number="2" column-width="60mm"/>
						<fo:table-column column-number="3" column-width="60mm"/>
						<fo:table-body>
							<xsl:apply-templates select="./charge-data/charges/charge-type[.='B']">
								<xsl:sort select="../offence-values/crest-offence-seq-no" data-type="number"/>
							</xsl:apply-templates>
						</fo:table-body>
					</fo:table>
					</fo:block>
					<!-- Failure to Appear Charges (Bail Act Offences-->
					<fo:block font-size="14pt" font-family="sans-serif" break-before="page" space-after.optimum="12pt" font-weight="bold" text-align="left">
						<xsl:text>Failure to Appear Offences (Corwn Court)</xsl:text>
					</fo:block>
					<fo:block>
					<fo:table table-layout="fixed" inline-progression-dimension.minimum="100%" border-collapse="separate">
						<fo:table-column column-number="1" column-width="30mm"/>
						<fo:table-column column-number="2" column-width="60mm"/>
						<fo:table-column column-number="3" column-width="60mm"/>
						<fo:table-body>
							<xsl:apply-templates select="./charge-data/charges/charge-type[.='F']">
								<xsl:sort select="../crest-charge-seq-no" data-type="number"/>
							</xsl:apply-templates>
						</fo:table-body>
					</fo:table>
					</fo:block>

					<!--log items -->
					<xsl:if test="./print-selection = 2">
						<fo:block font-size="14pt" font-family="sans-serif" break-before="page" space-after.optimum="15pt" font-weight="bold" text-align="left">
							<xsl:text>Charges Amendment Log</xsl:text>
						</fo:block>
						<xsl:apply-templates select="./charge-data"/>
					</xsl:if>
				  </fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template match="charge-data">
		<xsl:call-template name="CourtLogEvents" />
	</xsl:template>
	
	<xsl:template match="all-defendants">
		<xsl:value-of select="concat( first-name ,' ' ,initials,' ', sur-name, ' ')"/>
		<xsl:choose>
			<xsl:when test="position() &lt; last()">
				<xsl:text>, </xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="charges/charge-type">
		<xsl:choose>
			<!-- Indictments -->
			<xsl:when test="text()='I'">
				<fo:table-row>
				
					<xsl:variable name="defendant" />
					
					<fo:table-cell border-collapse="collapse" number-columns-spanned="3">
						<fo:block space-before="20pt" font-weight="bold" font-size="12pt" text-align="left">
							<xsl:text>The Queen V's </xsl:text>
							<xsl:for-each select="../offence-values/defendant-values" >
								<xsl:sort select="defendant-iD" />
								<xsl:variable name="lastDefendant" select="defendant-iD" />
								<xsl:if test="not(defendant-iD=$defendant)" >
									<xsl:value-of select="concat(first-name, ' ', initials, ' ', sur-name, '  ')" />
								</xsl:if>
								<xsl:variable name="defendant" select="defendant-iD" />
							</xsl:for-each>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>

				<fo:table-row>
					<fo:table-cell border-collapse="collapse" number-columns-spanned="3">
						<fo:block font-weight="bold" space-before="10pt">
							<xsl:text>Indictment </xsl:text>
							<!-- <xsl:value-of select="substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ', position(), 1)"/>  -->
							<xsl:value-of select="position()"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:apply-templates select="../offence-values">
					<xsl:sort select="./crest-offence-seq-no" data-type="number"/>
				</xsl:apply-templates>
			</xsl:when>
			<!-- Summary Offences (Section 41s) -->
			<xsl:when test="text()='O'">
				<xsl:choose>
					<xsl:when test="position()='1'">
						<fo:table-row>
							<fo:table-cell border-collapse="collapse" number-columns-spanned="3">
								<fo:block font-size="14pt" font-family="sans-serif" 
											break-before="page" space-after.optimum="15pt" font-weight="bold" text-align="left">
									<xsl:text>Summary Offences</xsl:text>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:apply-templates select="../offence-values">
							<xsl:sort select="./crest-offence-seq-no" data-type="number"/>
						</xsl:apply-templates>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<!-- Committals for Sentence -->
			<xsl:when test="text()='S'">
				<xsl:choose>
					<xsl:when test="position()='1'">
						<fo:table-row>
							<fo:table-cell border-collapse="collapse" number-columns-spanned="3">
								<fo:block font-size="14pt" font-family="sans-serif" 
										break-before="page" space-after.optimum="15pt" font-weight="bold" text-align="left">
									<xsl:text>Committals for Sentence</xsl:text>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<xsl:apply-templates select="../offence-values">
							<xsl:sort select="./crest-offence-seq-no" data-type="number"/>
						</xsl:apply-templates>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<!-- Breaches -->
			<xsl:when test="text()='B'">
				<fo:table-row>
					<fo:table-cell border-collapse="collapse" number-columns-spanned="3">
						<fo:block font-weight="bold" space-before="10pt">
							<xsl:text>Breach </xsl:text>
							<!--  <xsl:value-of select="substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ', position(), 1)"/>   -->
							<xsl:value-of select="position()"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<xsl:apply-templates select="../offence-values">
					<xsl:sort select="./crest-offence-seq-no" data-type="number"/>
				</xsl:apply-templates>
			</xsl:when>
			<!-- Failure to Appear Offences -->
			<xsl:when test="text()='F'">
				<xsl:apply-templates select="../offence-values">
						<xsl:sort select="../crest-charge-seq-no" data-type="number"/>
				</xsl:apply-templates>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	
	<xsl:template match="offence-values">
		<fo:table-row>
			<fo:table-cell padding="1mm">
				<fo:block>
					<xsl:choose>
						<xsl:when test="../charge-type/text()='I'">
							<xsl:text>Count </xsl:text>
							<xsl:value-of select="position()"/>
						</xsl:when>
						<xsl:when test="../charge-type/text()='B'">
							<xsl:text>Count </xsl:text>
							<xsl:value-of select="position()"/>
							<xsl:value-of select="substring( description, 1, 3 )"/>
						</xsl:when>
						<xsl:when test="../charge-type/text()='F'">
							<xsl:text>Offence </xsl:text>
							<xsl:value-of select="../crest-charge-seq-no"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Offence </xsl:text>
							<xsl:value-of select="position()"/>
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding="1mm">
				<fo:block>
					<xsl:text> - </xsl:text>
					<xsl:value-of select="offence-description"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell padding="1mm">
				<!-- <fo:block> -->
				<xsl:apply-templates select="defendant-values"/>
				<!-- </fo:block> -->
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	
	<xsl:template match="defendant-values">
		<!-- <xsl:param name="separator" select="'newLine'" />
		<xsl:if test="$separator='newLine'" >
			<xsl:value-of select="$block" />
		</xsl:if> -->
		<fo:block>
			<xsl:value-of select="concat(first-name, ' ', initials, ' ', sur-name)"/>
			<xsl:choose>
				<xsl:when test="position() &lt; last()">
					<xsl:text>, </xsl:text>
					<!--  <xsl:value-of select="$separator" />  -->
				</xsl:when>
			</xsl:choose>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>
