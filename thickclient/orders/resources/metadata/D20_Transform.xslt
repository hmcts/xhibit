<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ********************************* -->
	<!-- D20 ORDER START -->
	<!-- ********************************* -->
	<xsl:template match="nar:D20_Seperator">
		<!-- This fo:block is the section seperator -->
		<fo:block space-after="14pt" text-align="center">
			<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20HeaderSection">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="proportional-column-width(1)"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-page="always">
					<fo:table-cell start-indent="1mm" end-indent="3mm" border="1mm">
						<fo:block>
							<xsl:apply-templates select="nar:Body"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- This fo:block is the section seperator -->
		<fo:block space-after="14pt" text-align="center">
			<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20AppealSection">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="proportional-column-width(1)"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-page="always">
					<fo:table-cell start-indent="1mm" end-indent="3mm" border="1mm">
						<fo:block>
							<xsl:apply-templates select="nar:Body"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- This fo:block is the section seperator -->
		<fo:block space-after="14pt" text-align="center">
			<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20FooterSection">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="proportional-column-width(1)"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-page="always">
					<fo:table-cell start-indent="1mm" end-indent="3mm" border="1mm">
						<fo:block>
							<xsl:apply-templates select="nar:Body"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- This fo:block is the section seperator -->
		<fo:block space-after="14pt" text-align="center">
			<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20Section">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="proportional-column-width(1)"/>
			<fo:table-body>
				<fo:table-row keep-with-next.within-page="always">
					<fo:table-cell start-indent="1mm" end-indent="3mm" border="1mm">
						<fo:block>
							<xsl:apply-templates select="nar:Body"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- This fo:block is the section seperator -->
		<fo:block space-after="14pt" text-align="center">
			<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20HeaderSection/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20FooterSection/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20Section/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20AppealSection/nar:Body">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="nar:D20Table">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="60mm"/>
			<fo:table-column column-width="80mm"/>
			<fo:table-column column-width="60mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r1_col_one"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r1_col_two"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r1_col_three"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r2_col_one"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r2_col_two"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r2_col_three"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r3_col_one"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r3_col_two"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_r3_col_three"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- Call template to display rest of the text in the first section -->
		<xsl:call-template name="RestOfText"/>
	</xsl:template>
	<xsl:template name="d20_r1_col_one">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[1]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r1_col_two">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[1]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r1_col_three">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[1]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r2_col_one">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[2]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r2_col_two">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[2]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r2_col_three">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[2]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r3_col_one">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[3]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r3_col_two">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[3]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_r3_col_three">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20HeaderSection/nar:Body/nar:D20Table/nar:TR[3]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
			<xsl:for-each select="./nar:Line[2]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:D20FooterTable">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="130mm"/>
			<fo:table-column column-width="70mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r1c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r1c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r2c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r2c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r3c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r3c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r4c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r4c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r5c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r5c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r6c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r6c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r7c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r7c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r8c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r8c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r9c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r9c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r10c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_footer_r10c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- Call template to display rest of the text in the first section -->
		<xsl:call-template name="RestOfText"/>
	</xsl:template>
	<xsl:template name="d20_footer_r1c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[1]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r1c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[1]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r2c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[2]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r2c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[2]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r3c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[3]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r3c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[3]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r4c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[4]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r4c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[4]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r5c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[5]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r5c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[5]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r6c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[6]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r6c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[6]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r7c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[7]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r7c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[7]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r8c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[8]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r8c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[8]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r9c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[9]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r9c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[9]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r10c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[10]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_footer_r10c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20FooterSection/nar:Body/nar:D20FooterTable/nar:TR[10]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:D20AppealTable">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="130mm"/>
			<fo:table-column column-width="70mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r1c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r1c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r2c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r2c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r3c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r3c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r4c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r4c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r5c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r5c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r6c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_appeal_r6c2"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		<!-- Call template to display rest of the text in the first section -->
		<xsl:call-template name="RestOfText"/>
	</xsl:template>
	<xsl:template name="d20_appeal_r1c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[1]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r1c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[1]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r2c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[2]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r2c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[2]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r3c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[3]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r3c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[3]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r4c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[4]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r4c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[4]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r5c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[5]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r5c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[5]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r6c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[6]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_appeal_r6c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20AppealSection/nar:Body/nar:D20AppealTable/nar:TR[6]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="nar:D20OffenceTable">
		<fo:table table-layout="fixed">
			<fo:table-column column-width="65mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-column column-width="35mm"/>
			<fo:table-body>
				<!-- Row 1 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r1c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r1c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r1c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r1c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r1c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 2 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r2c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r2c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r2c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r2c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r2c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 3 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r3c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r3c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r3c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r3c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r3c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 4 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r4c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r4c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r4c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r4c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r4c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 5 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r5c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r5c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r5c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r5c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r5c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 6 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r6c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r6c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r6c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r6c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r6c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 7 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r7c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r7c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r7c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r7c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r7c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 8 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r8c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r8c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r8c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r8c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r8c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 9 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r9c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r9c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r9c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r9c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r9c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 10 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r10c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r10c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r10c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r10c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r10c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 11 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r11c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r11c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r11c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r11c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r11c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 12 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r12c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r12c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r12c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r12c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r12c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 13 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r13c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r13c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r13c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r13c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r13c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 14 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r14c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r14c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r14c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r14c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r14c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 15 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r15c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r15c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r15c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r15c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r15c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 16 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r16c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r16c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r16c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r16c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r16c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 17 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r17c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r17c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r17c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r17c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r17c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<!-- Row 18 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r18c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r18c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r18c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r18c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r18c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 19 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r19c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r19c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r19c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r19c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r19c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 20 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r20c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r20c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r20c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r20c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r20c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 21 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r21c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r21c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r21c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r21c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r21c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 22 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r22c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r22c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r22c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r22c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r22c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 23 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r23c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r23c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r23c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r23c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r23c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 24 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r24c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r24c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r24c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r24c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r24c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 25 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r25c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r25c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r25c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r25c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r25c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 26 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r26c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r26c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r26c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r26c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r26c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				<!-- Row 27 -->
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r27c1"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r27c2"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r27c3"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r27c4"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="5mm" hyphenate="true" language="en_GB">
							<xsl:call-template name="d20_offence_r27c5"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
			</fo:table-body>
		</fo:table>
		<!-- Call template to display rest of the text in the first section -->
		<xsl:call-template name="RestOfText"/>
	</xsl:template>
	<xsl:template name="d20_offence_r1c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[1]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r1c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[1]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r1c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[1]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r1c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[1]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r1c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[1]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r2c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[2]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r2c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[2]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r2c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[2]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r2c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[2]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r2c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[2]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 3 -->
	<xsl:template name="d20_offence_r3c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[3]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r3c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[3]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r3c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[3]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r3c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[3]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r3c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[3]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 4 -->
	<xsl:template name="d20_offence_r4c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[4]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r4c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[4]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r4c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[4]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r4c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[4]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r4c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[4]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 5 -->
	<xsl:template name="d20_offence_r5c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[5]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r5c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[5]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r5c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[5]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r5c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[5]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r5c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[5]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 6 -->
	<xsl:template name="d20_offence_r6c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[6]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r6c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[6]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r6c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[6]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r6c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[6]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r6c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[6]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 7 -->
	<xsl:template name="d20_offence_r7c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[7]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r7c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[7]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r7c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[7]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r7c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[7]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r7c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[7]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 8 -->
	<xsl:template name="d20_offence_r8c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[8]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r8c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[8]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r8c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[8]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r8c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[8]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r8c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[8]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 9 -->
	<xsl:template name="d20_offence_r9c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[9]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r9c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[9]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r9c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[9]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r9c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[9]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r9c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[9]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 10 -->
	<xsl:template name="d20_offence_r10c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[10]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r10c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[10]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r10c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[10]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r10c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[10]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r10c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[10]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 11 -->
	<xsl:template name="d20_offence_r11c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[11]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r11c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[11]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r11c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[11]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r11c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[11]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r11c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[11]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 12 -->
	<xsl:template name="d20_offence_r12c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[12]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r12c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[12]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r12c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[12]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r12c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[12]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r12c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[12]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 13 -->
	<xsl:template name="d20_offence_r13c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[13]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r13c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[13]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r13c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[13]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r13c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[13]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r13c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[13]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 14 -->
	<xsl:template name="d20_offence_r14c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[14]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r14c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[14]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r14c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[14]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r14c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[14]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r14c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[14]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 15 -->
	<xsl:template name="d20_offence_r15c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[15]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r15c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[15]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r15c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[15]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r15c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[15]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r15c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[15]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 16 -->
	<xsl:template name="d20_offence_r16c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[16]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r16c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[16]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r16c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[16]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r16c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[16]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r16c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[16]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 17 -->
	<xsl:template name="d20_offence_r17c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[17]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r17c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[17]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r17c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[17]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r17c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[17]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r17c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[17]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	
	<!-- Row 18 -->
	<xsl:template name="d20_offence_r18c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[18]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r18c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[18]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r18c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[18]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r18c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[18]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r18c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[18]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 19 -->
	<xsl:template name="d20_offence_r19c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[19]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r19c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[19]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r19c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[19]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r19c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[19]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r19c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[19]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 20 -->
	<xsl:template name="d20_offence_r20c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[20]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r20c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[20]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r20c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[20]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r20c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[20]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r20c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[20]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 21 -->
	<xsl:template name="d20_offence_r21c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[21]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r21c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[21]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r21c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[21]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r21c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[21]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r21c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[21]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 22 -->
	<xsl:template name="d20_offence_r22c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[22]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r22c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[22]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r22c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[22]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r22c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[22]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r22c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[22]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 23 -->
	<xsl:template name="d20_offence_r23c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[23]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r23c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[23]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r23c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[23]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r23c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[23]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r23c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[23]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 24 -->
	<xsl:template name="d20_offence_r24c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[24]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r24c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[24]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r24c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[24]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r24c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[24]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r24c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[24]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 25 -->
	<xsl:template name="d20_offence_r25c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[25]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r25c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[25]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r25c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[25]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r25c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[25]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r25c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[25]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 26 -->
	<xsl:template name="d20_offence_r26c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[26]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r26c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[26]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r26c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[26]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r26c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[26]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r26c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[26]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<!-- Row 27 -->
	<xsl:template name="d20_offence_r27c1">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[27]/nar:TD[1]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r27c2">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[27]/nar:TD[2]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r27c3">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[27]/nar:TD[3]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r27c4">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[27]/nar:TD[4]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="d20_offence_r27c5">
		<xsl:for-each select="/ord:Order/nar:Narrative/nar:Body/nar:D20Section/nar:Body/nar:D20OffenceTable/nar:TR[27]/nar:TD[5]">
			<xsl:for-each select="./nar:Line[1]">
				<xsl:apply-templates select="*|text()"/>
			</xsl:for-each>
			<fo:block/>
		</xsl:for-each>
	</xsl:template>
	
	
	<!-- Notice of D20 -->
	<xsl:template match="nar:D20_Notice">
		<fo:inline>
			<xsl:text>Notice of Court of Order for Endorsement</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_Notice_pt2">
		<fo:inline>
			<xsl:text> D20</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_StdText">
		<fo:inline>
			<xsl:text>Please refer to the Guidelines to Courts issued by DVLA for advice on completing this form.</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DefendantNameLabel">
		<fo:inline>
			<xsl:text>Defendant Name</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DefendantAddressLabel">
		<fo:inline>
			<xsl:text>Defendant Address</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- In the case of -->
	<xsl:template match="nar:D20_CaseOf">
		<fo:inline>
			<xsl:text>In the case of</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- The -->
	<xsl:template match="nar:D20_The">
		<fo:inline>
			<xsl:text>The</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Defendant -->
	<xsl:template match="nar:D20_Defendant">
		<fo:inline>
			<xsl:text>Defendant</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- was convicted on -->
	<xsl:template match="nar:D20_DateOfConvictionLabel">
		<fo:inline>
			<xsl:text>Date of Conviction: </xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_ConvictionDate">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:D20CommittingCourtSection/@selected = 'true'">
					<xsl:call-template name="FormatDate">
						<xsl:with-param name="date" select="$baseAll/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:Date"/>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_Salutation">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:D20PersonalDetails/ord:Title = 'Other'">
					<xsl:value-of select="//ord:D20/ord:D20PersonalDetails/ord:OtherTitle"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="//ord:D20/ord:D20PersonalDetails/ord:Title"/>
				</xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_Gender">
		<fo:inline>
			 (<xsl:value-of select="//ord:D20/ord:D20PersonalDetails/ord:Gender"/>)
		</fo:inline>
	</xsl:template>
	<!-- DVLA use only box 1  -->
	<xsl:template match="nar:D20_DVLAUseOnlyBox1">
		<fo:inline>
			<xsl:text>DVLA Use Only</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_ConvictionCourtLabel">
		<fo:inline>
			<xsl:text>Convicting Court: </xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_ConvictionCourt">
		<xsl:choose>
			<xsl:when test="//ord:D20/ord:D20CommittingCourtSection/@selected = 'true'">
				<xsl:choose>
					<xsl:when test="$baseAll/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseType=$CrownCourt">
						<xsl:text>0</xsl:text>
						<xsl:call-template name="ConvictionCourtHouseNumber"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="ConvictionCourtHouseNumber"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="nar:D20_DriverNumberLabel">
		<fo:inline>
			<xsl:text>Driver Number</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DriverNumber">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="not(starts-with($baseAll/ord:LicenceType, '0'))">
					<xsl:call-template name="toUpper">
						<xsl:with-param name="content" select="$baseAll/ord:DriverNumber"/>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_LicenceIssueNoLabel">
		<fo:inline>
			<xsl:text>Licence Issue No</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_LicenceIssueNo">
		<fo:inline>
			<xsl:call-template name="toUpper">
				<xsl:with-param name="content" select="$baseAll/ord:LicenceIssueNo"/>
			</xsl:call-template>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_FormNumber">
		<!-- This fo:block is the section seperator -->
		<fo:block space-after="14pt" text-align="center">
			<fo:leader leader-length="90%" leader-pattern="rule" rule-style="groove" rule-thickness="2pt" color="rgb(155, 195, 185)"/>
		</fo:block>
		<fo:block>
			<fo:inline font-weight="bold">
				<xsl:text>Form: </xsl:text>
			</fo:inline>
			<fo:inline>
				<xsl:value-of select="$baseAll/ord:Forms/ord:FormNumber"/>
				<xsl:text> of </xsl:text>
				<xsl:value-of select="$baseAll/ord:Forms/ord:TotalForms"/>
			</fo:inline>
		</fo:block>
	</xsl:template>
	
	<xsl:template name="SentencingCourtType1">
		<fo:inline>
			<xsl:for-each select="//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceSentencingCourt1">
				<xsl:call-template name="PrecedingCourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<xsl:template name="SentencingCourtType2">
		<fo:inline>
			<xsl:for-each select="//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceSentencingCourt2">
				<xsl:call-template name="PrecedingCourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<xsl:template name="SentencingCourtType3">
		<fo:inline>
			<xsl:for-each select="//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceSentencingCourt3">
				<xsl:call-template name="PrecedingCourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<xsl:template name="SentencingCourtType4">
		<fo:inline>
			<xsl:for-each select="//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceSentencingCourt4">
				<xsl:call-template name="PrecedingCourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	
	<xsl:template name="SentencingCourtHouseName1">
		<xsl:value-of select="//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceSentencingCourt1/ord:CourtHouseName"/>
	</xsl:template>
	<xsl:template name="SentencingCourtHouseName2">
		<xsl:value-of select="//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceSentencingCourt2/ord:CourtHouseName"/>
	</xsl:template>
	<xsl:template name="SentencingCourtHouseName3">
		<xsl:value-of select="//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceSentencingCourt3/ord:CourtHouseName"/>
	</xsl:template>
	<xsl:template name="SentencingCourtHouseName4">
		<xsl:value-of select="//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceSentencingCourt4/ord:CourtHouseName"/>
	</xsl:template>
	
	<xsl:template name="substring-before-last">
		<xsl:param name="input" select="''" />
		<xsl:param name="substr" select="''" />
		<xsl:if test="$input != '' and $substr != ''">
			<xsl:variable name="head" select="substring-before($input, $substr)" />
			<xsl:variable name="tail" select="substring-after($input, $substr)" />
			<xsl:value-of select="$head" />
			<xsl:if test="contains($tail, $substr)">
				<xsl:value-of select="$substr" />
				<xsl:call-template name="substring-before-last">
					<xsl:with-param name="input" select="$tail" />
					<xsl:with-param name="substr" select="$substr" />
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="substring-after-last">
		<xsl:param name="input" select="''" />
		<xsl:param name="substr" select="''" />
		<xsl:message><xsl:value-of select="$input" />"</xsl:message>
		<xsl:message><xsl:value-of select="$substr" />"</xsl:message>
		<xsl:variable name="temp" select="substring-after($input, $substr)" />
		<xsl:message><xsl:value-of select="$temp" />"</xsl:message>
		<xsl:choose>
			<xsl:when test="$substr and contains($temp, $substr)">
				<xsl:call-template name="substring-after-last">
					<xsl:with-param name="input" select="$temp" />
					<xsl:with-param name="substr" select="$substr" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$temp" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!--<xsl:template name="ConvictionCourtHouseNumber">
		<xsl:message>Calling ConvictionCourtHouseNumber</xsl:message>
		<xsl:variable name="firstSubstr">
			<xsl:message>Calling ConvictionCourtHouseNumber 1.1</xsl:message>
			<xsl:call-template name="substring-after-last">
				<xsl:with-param name="input" select="//ord:D20/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseName" />
				<xsl:with-param name="substr" select="'('" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:value-of select="substring-before($firstSubstr, ')')"/>
	</xsl:template>-->
	
	<xsl:template name="ConvictionCourtHouseNumber">
		<xsl:variable name="apos">'</xsl:variable>
		<xsl:value-of select='translate(//ord:D20/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>

	<!--<xsl:template name="ConvictionCourtHouseNumber">
		<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseName, '('), ')')"/>
	</xsl:template>-->

	
	<xsl:template name="SentencingCourtHouseNumber1">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceSentencingCourt1/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceSentencingCourt1/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	<xsl:template name="SentencingCourtHouseNumber2">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceSentencingCourt2/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceSentencingCourt2/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	<xsl:template name="SentencingCourtHouseNumber3">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceSentencingCourt3/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceSentencingCourt3/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	<xsl:template name="SentencingCourtHouseNumber4">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceSentencingCourt4/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceSentencingCourt4/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	
	<xsl:template name="AppealCourtHouseNumber1">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence1/ord:OffenceAppealCourt1/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/ord:OffenceAppealCourt1/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	<xsl:template name="AppealCourtHouseNumber2">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence2/ord:OffenceAppealCourt2/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/ord:OffenceAppealCourt2/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	<xsl:template name="AppealCourtHouseNumber3">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence3/ord:OffenceAppealCourt3/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/ord:OffenceAppealCourt3/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	<xsl:template name="AppealCourtHouseNumber4">
		<!--<xsl:value-of select="substring-before(substring-after(//ord:D20/ord:Offence4/ord:OffenceAppealCourt4/ord:CourtHouseName, '('), ')')"/>-->
		<xsl:value-of select='translate(//ord:D20//ord:Offence4/ord:OffenceAppeal4Section/ord:OffenceAppealCourt4/ord:CourtHouseName, "ABCDEFGHIJKLMNOPQRSTUVWXYZ ().*-,&apos;&amp;", "")'/>
	</xsl:template>
	
	<xsl:template name="ConvictionCourtType">
		<fo:inline>
			<xsl:for-each select="$baseAll/ord:D20CommittingCourt/ord:CourtHouse">
				<xsl:call-template name="PrecedingCourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<xsl:template name="ConvictionCourtHouseName">
		<xsl:value-of select="$baseAll/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseName"/>
	</xsl:template>
	<xsl:template match="nar:D20_LicenceTypeLabel">
		<fo:inline>
			<xsl:text>Licence Produced in Court</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_LicenceType">
		<fo:inline>
			<xsl:value-of select="$baseAll/ord:LicenceType"/>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_NotificationPreviouslySentLabel">
		<fo:inline>
			<xsl:text>Notification Previously Sent</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NotificationPreviouslySent">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:NotificationPreviouslySent = 'true'">Yes</xsl:when>
				<xsl:otherwise>No</xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DifferentAddressLabel">
		<fo:inline>
			<xsl:text>Name and/or address on licence if different from above:</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DifferentAddress">
		<xsl:value-of select="//ord:D20/ord:DifferentAddress/ord:DifferentName"/>
		<xsl:for-each select="$baseAll/ord:DifferentAddress/ord:Address">
			<xsl:call-template name="D20DifferentAddress"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="D20DifferentAddress">
		<fo:inline>
		
			<xsl:if test="normalize-space(apd:Line[1]) != '' and apd:Line[1] != 'Address Line1'">
				<xsl:if test="normalize-space(//ord:D20/ord:DifferentAddress/ord:DifferentName) != ''">
					<xsl:text>, </xsl:text>
				</xsl:if>
				<xsl:value-of select="normalize-space(apd:Line[1])"/>
			</xsl:if>
			
			<xsl:if test="normalize-space(apd:Line[2]) != ' ' and apd:Line[2] != 'Address Line2' and string-length(normalize-space(apd:Line[2])) &gt; 0">
				<xsl:choose>
					<xsl:when test="normalize-space(apd:Line[1]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[1]) = '' and normalize-space(//ord:D20/ord:DifferentAddress/ord:DifferentName) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
				</xsl:choose>
				<xsl:value-of select="normalize-space(apd:Line[2])"/>
			</xsl:if>			
			
			<xsl:if test="normalize-space(apd:Line[3]) != ''">
				<xsl:choose>
					<xsl:when test="normalize-space(apd:Line[2]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) = '' and normalize-space(//ord:D20/ord:DifferentAddress/ord:DifferentName) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
				</xsl:choose>
				<xsl:value-of select="normalize-space(apd:Line[3])"/>
			</xsl:if>
						
			<xsl:if test="normalize-space(apd:Line[4]) != ''">
				<xsl:choose>
					<xsl:when test="normalize-space(apd:Line[3]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) = '' and normalize-space(//ord:D20/ord:DifferentAddress/ord:DifferentName) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
				</xsl:choose>
				<xsl:value-of select="normalize-space(apd:Line[4])"/>
			</xsl:if>
			
			<xsl:if test="normalize-space(apd:Line[5]) != ''">
				<xsl:choose>
					<xsl:when test="normalize-space(apd:Line[4]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) = '' and normalize-space(//ord:D20/ord:DifferentAddress/ord:DifferentName) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
				</xsl:choose>
				<xsl:value-of select="normalize-space(apd:Line[5])"/>
			</xsl:if>
			
			<xsl:if test="apd:PostCode and normalize-space(apd:PostCode) != '' and apd:PostCode!='AA1 1AA'">
				<!-- Work out whether to display a comma -->
				<xsl:choose>
					<xsl:when test="normalize-space(apd:Line[5]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[5]) = '' and normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) = '' and normalize-space(//ord:D20/ord:DifferentAddress/ord:DifferentName) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[5]) = '' and normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) = '' and normalize-space(apd:Line[1]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[5]) = '' and normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) = '' and normalize-space(apd:Line[2]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[5]) = '' and normalize-space(apd:Line[4]) = '' and normalize-space(apd:Line[3]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:when test="normalize-space(apd:Line[5]) = '' and normalize-space(apd:Line[4]) != ''">
						<xsl:text>, </xsl:text>
					</xsl:when>
				</xsl:choose>
				<xsl:value-of select="normalize-space(apd:PostCode)"/>
			</xsl:if>
			
		</fo:inline>
	</xsl:template>
	
	
	<!-- "Offences" -->
	<xsl:template match="nar:D20_OffenceCodeLabel">
		<fo:inline>
			<xsl:text>Offence Code</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceCode1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceCode1"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceCode2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceCode2"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceCode3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceCode3"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceCode4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceCode4"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_OffenceDateLabel">
		<fo:inline>
			<xsl:text>Date of Offence</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceDate1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceDate1Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceDate1Section/ord:OffenceDate1"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceDate2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceDate2Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceDate2Section/ord:OffenceDate2"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceDate3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceDate3Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceDate3Section/ord:OffenceDate3"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceDate4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceDate4Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceDate4Section/ord:OffenceDate4"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_OffenceFineLabel">
		<fo:inline>
			<xsl:text>Fine (££££pp)</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceFine1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence1/ord:OffenceFine1/ord:MonetaryValue/ord:Amount) != '0.00' and normalize-space($baseAll/ord:Offence1/ord:OffenceFine1/ord:MonetaryValue/ord:Amount) !=''">
							<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceFine1/ord:MonetaryValue/ord:Amount"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceFine2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence2/ord:OffenceFine2/ord:MonetaryValue/ord:Amount) != '0.00' and normalize-space($baseAll/ord:Offence2/ord:OffenceFine2/ord:MonetaryValue/ord:Amount) !=''">
							<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceFine2/ord:MonetaryValue/ord:Amount"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceFine3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence3/ord:OffenceFine3/ord:MonetaryValue/ord:Amount) != '0.00' and normalize-space($baseAll/ord:Offence3/ord:OffenceFine3/ord:MonetaryValue/ord:Amount) !=''">
							<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceFine3/ord:MonetaryValue/ord:Amount"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffenceFine4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence4/ord:OffenceFine4/ord:MonetaryValue/ord:Amount) != '0.00' and normalize-space($baseAll/ord:Offence4/ord:OffenceFine4/ord:MonetaryValue/ord:Amount) !=''">
							<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceFine4/ord:MonetaryValue/ord:Amount"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_OffencePenaltyPointsLabel">
		<fo:inline>
			<xsl:text>Penalty Points</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffencePenaltyPoints1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence1/ord:OffencePenaltyPoints1) !='' and normalize-space($baseAll/ord:Offence1/ord:OffencePenaltyPoints1) !='0'">
							<xsl:value-of select="$baseAll/ord:Offence1/ord:OffencePenaltyPoints1"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffencePenaltyPoints2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence2/ord:OffencePenaltyPoints2) !='' and normalize-space($baseAll/ord:Offence2/ord:OffencePenaltyPoints2) !='0'">
							<xsl:value-of select="$baseAll/ord:Offence2/ord:OffencePenaltyPoints2"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffencePenaltyPoints3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence3/ord:OffencePenaltyPoints3) !='' and normalize-space($baseAll/ord:Offence3/ord:OffencePenaltyPoints3) !='0'">
							<xsl:value-of select="$baseAll/ord:Offence3/ord:OffencePenaltyPoints3"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OffencePenaltyPoints4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence4/ord:OffencePenaltyPoints4) !='' and normalize-space($baseAll/ord:Offence4/ord:OffencePenaltyPoints4) !='0'">
							<xsl:value-of select="$baseAll/ord:Offence4/ord:OffencePenaltyPoints4"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AlcoholLevel1Label">
		<fo:inline>
			<xsl:text>Alcohol Level / Drug Level</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AlcoholLevel1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue) !=''">
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Blood'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>A</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Breath'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>B</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Urine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>U</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Amphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>C</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Benzoylecgonine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>D</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Clonazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>E</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Cocaine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>F</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Diazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>G</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Flunitrazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>H</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Ketamine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>J</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Lorazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>K</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Lysergic Acid Diethylamide'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>L</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Methadone'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>M</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Methamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>N</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Methylenedioxymethamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>P</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = '6-Monoacetylmorphine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>R</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Morphine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>S</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Oxazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>T</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Temazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>V</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Delta-9-Tetrahydrocannabinol'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>W</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AlcoholLevel2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue) !=''">
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Blood'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>A</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Breath'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>B</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Urine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>U</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Amphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>C</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Benzoylecgonine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>D</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Clonazepam'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>E</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Cocaine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>F</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Diazepam'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>G</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Flunitrazepam'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>H</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Ketamine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>J</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Lorazepam'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>K</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Lysergic Acid Diethylamide'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>L</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Methadone'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>M</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Methamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>N</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Methylenedioxymethamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>P</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = '6-Monoacetylmorphine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>R</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Morphine'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>S</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Oxazepam'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>T</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Temazepam'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>V</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelDropdown = 'Delta-9-Tetrahydrocannabinol'">
								<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceAlcoholLevel2/ord:D20AlcoholLevelValue"/>
								<xsl:text>W</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AlcoholLevel3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue) !=''">
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Blood'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>A</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Breath'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>B</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Urine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>U</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Amphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>C</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Benzoylecgonine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>D</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Clonazepam'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>E</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Cocaine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>F</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Diazepam'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>G</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Flunitrazepam'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>H</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Ketamine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>J</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Lorazepam'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>K</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Lysergic Acid Diethylamide'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>L</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelDropdown = 'Methadone'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>M</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Methamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>N</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Methylenedioxymethamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>P</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = '6-Monoacetylmorphine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>R</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Morphine'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>S</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Oxazepam'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>T</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Temazepam'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>V</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelDropdown = 'Delta-9-Tetrahydrocannabinol'">
								<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceAlcoholLevel3/ord:D20AlcoholLevelValue"/>
								<xsl:text>W</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AlcoholLevel4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="normalize-space($baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue) !=''">
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Blood'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>A</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Breath'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>B</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Urine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>U</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Amphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>C</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Benzoylecgonine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>D</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Clonazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>E</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Cocaine'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>F</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Diazepam'">
								<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceAlcoholLevel1/ord:D20AlcoholLevelValue"/>
								<xsl:text>G</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Flunitrazepam'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>H</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Ketamine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>J</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Lorazepam'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>K</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Lysergic Acid Diethylamide'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>L</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Methadone'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>M</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Methamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>N</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Methylenedioxymethamphetamine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>P</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = '6-Monoacetylmorphine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>R</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Morphine'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>S</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Oxazepam'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>T</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Temazepam'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>V</xsl:text>
							</xsl:if>
							<xsl:if test="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelDropdown = 'Delta-9-Tetrahydrocannabinol'">
								<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceAlcoholLevel4/ord:D20AlcoholLevelValue"/>
								<xsl:text>W</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DisqualifiedPeriodLabel">
		<fo:inline>
			<xsl:text>Disqualified Period (YYMMDD)</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DisqualifiedPeriod1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:if test="string-length($baseAll/ord:Offence1/ord:OffenceDisqualifiedPeriod1/ord:Years) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceDisqualifiedPeriod1/ord:Years"/>
					<xsl:if test="string-length($baseAll/ord:Offence1/ord:OffenceDisqualifiedPeriod1/ord:Months) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceDisqualifiedPeriod1/ord:Months"/>
					<xsl:if test="string-length($baseAll/ord:Offence1/ord:OffenceDisqualifiedPeriod1/ord:Days) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceDisqualifiedPeriod1/ord:Days"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DisqualifiedPeriod2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:if test="string-length($baseAll/ord:Offence2/ord:OffenceDisqualifiedPeriod2/ord:Years) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceDisqualifiedPeriod2/ord:Years"/>
					<xsl:if test="string-length($baseAll/ord:Offence2/ord:OffenceDisqualifiedPeriod2/ord:Months) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceDisqualifiedPeriod2/ord:Months"/>
					<xsl:if test="string-length($baseAll/ord:Offence2/ord:OffenceDisqualifiedPeriod2/ord:Days) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceDisqualifiedPeriod2/ord:Days"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DisqualifiedPeriod3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:if test="string-length($baseAll/ord:Offence3/ord:OffenceDisqualifiedPeriod3/ord:Years) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceDisqualifiedPeriod3/ord:Years"/>
					<xsl:if test="string-length($baseAll/ord:Offence3/ord:OffenceDisqualifiedPeriod3/ord:Months) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceDisqualifiedPeriod3/ord:Months"/>
					<xsl:if test="string-length($baseAll/ord:Offence3/ord:OffenceDisqualifiedPeriod3/ord:Days) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceDisqualifiedPeriod3/ord:Days"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DisqualifiedPeriod4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:if test="string-length($baseAll/ord:Offence4/ord:OffenceDisqualifiedPeriod4/ord:Years) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceDisqualifiedPeriod4/ord:Years"/>
					<xsl:if test="string-length($baseAll/ord:Offence4/ord:OffenceDisqualifiedPeriod4/ord:Months) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceDisqualifiedPeriod4/ord:Months"/>
					<xsl:if test="string-length($baseAll/ord:Offence4/ord:OffenceDisqualifiedPeriod4/ord:Days) &lt; 2">
						<xsl:text>0</xsl:text>
					</xsl:if>
					<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceDisqualifiedPeriod4/ord:Days"/>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_OtherSentenceLabel">
		<fo:inline>
			<xsl:text>Other Sentence</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:variable name="otherSentenceType1" select="'ACEFP?+'"/>
	<xsl:variable name="and"><![CDATA[&]]></xsl:variable>
	<xsl:variable name="otherSentenceType2" select="'IJRST@'"/>
	<xsl:template match="nar:D20_OtherSentence1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/@selected = 'true'">
							<xsl:value-of select="substring(//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceDropdown,1,1)"/>
							<xsl:choose>
								<xsl:when test="contains('JM', substring(//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:when test="contains('IRST@&amp;', substring(//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="string-length(//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceValue) = 2">
										<xsl:text>0</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceValue) = 1">
										<xsl:text>00</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceValue) = 0">
										<xsl:text>000</xsl:text>
									</xsl:if>
									<xsl:value-of select="$baseAll/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceValue"/>
									<xsl:if test="$baseAll/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceDurationType != 'Not Applicable'">
										<xsl:value-of select="translate(substring($baseAll/ord:Offence1/ord:OffenceOtherSentence1Section/ord:OffenceOtherSentence1/ord:D20OffenceOtherSentenceDurationType,1,1),'dhmwy','DHMWY')"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OtherSentence2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/@selected = 'true'">
							<xsl:value-of select="substring(//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceDropdown,1,1)"/>
							<xsl:choose>
								<xsl:when test="contains('JM', substring(//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:when test="contains('IRST@&amp;', substring(//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="string-length(//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceValue) = 2">
										<xsl:text>0</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceValue) = 1">
										<xsl:text>00</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceValue) = 0">
										<xsl:text>000</xsl:text>
									</xsl:if>
									<xsl:value-of select="$baseAll/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceValue"/>
									<xsl:if test="$baseAll/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceDurationType != 'Not Applicable'">
										<xsl:value-of select="translate(substring($baseAll/ord:Offence2/ord:OffenceOtherSentence2Section/ord:OffenceOtherSentence2/ord:D20OffenceOtherSentenceDurationType,1,1),'dhmwy','DHMWY')"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OtherSentence3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/@selected = 'true'">
							<xsl:value-of select="substring(//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceDropdown,1,1)"/>
							<xsl:choose>
								<xsl:when test="contains('JM', substring(//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:when test="contains('IRST@&amp;', substring(//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="string-length(//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceValue) = 2">
										<xsl:text>0</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceValue) = 1">
										<xsl:text>00</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceValue) = 0">
										<xsl:text>000</xsl:text>
									</xsl:if>
									<xsl:value-of select="$baseAll/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceValue"/>
									<xsl:if test="$baseAll/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceDurationType != 'Not Applicable'">
										<xsl:value-of select="translate(substring($baseAll/ord:Offence3/ord:OffenceOtherSentence3Section/ord:OffenceOtherSentence3/ord:D20OffenceOtherSentenceDurationType,1,1),'dhmwy','DHMWY')"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_OtherSentence4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/@selected = 'true'">
							<xsl:value-of select="substring(//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceDropdown,1,1)"/>
							<xsl:choose>
								<xsl:when test="contains('JM', substring(//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:when test="contains('IRST@&amp;', substring(//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceDropdown,1,1))">
									<xsl:text>000</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="string-length(//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceValue) = 2">
										<xsl:text>0</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceValue) = 1">
										<xsl:text>00</xsl:text>
									</xsl:if>
									<xsl:if test="string-length(//ord:D20/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceValue) = 0">
										<xsl:text>000</xsl:text>
									</xsl:if>
									<xsl:value-of select="$baseAll/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceValue"/>
									<xsl:if test="$baseAll/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceDurationType != 'Not Applicable'">
										<xsl:value-of select="translate(substring($baseAll/ord:Offence4/ord:OffenceOtherSentence4Section/ord:OffenceOtherSentence4/ord:D20OffenceOtherSentenceDurationType,1,1),'dhmwy','DHMWY')"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_PPSCOLabel">
		<fo:inline>
			<xsl:text>PSS / CO</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_PPSCO1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffencePPSCO1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODropdown = 'Prison Sentence Suspended'">
									<xsl:text>PSS</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>CO</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text> </xsl:text>
							<xsl:if test="string-length($baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODurationType/ord:Years) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODurationType/ord:Years"/>
							
							<xsl:if test="string-length($baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODurationType/ord:Months) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODurationType/ord:Months"/>
							<xsl:if test="string-length($baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODurationType/ord:Days) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence1/ord:OffencePPSCO1Section/ord:OffencePPSCO1/ord:D20OffencePPSCODurationType/ord:Days"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_PPSCO2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffencePPSCO2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODropdown = 'Prison Sentence Suspended'">
									<xsl:text>PSS</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>CO</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text> </xsl:text>
							<xsl:if test="string-length($baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODurationType/ord:Years) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODurationType/ord:Years"/>
							
							<xsl:if test="string-length($baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODurationType/ord:Months) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODurationType/ord:Months"/>
							<xsl:if test="string-length($baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODurationType/ord:Days) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence2/ord:OffencePPSCO2Section/ord:OffencePPSCO2/ord:D20OffencePPSCODurationType/ord:Days"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_PPSCO3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffencePPSCO3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODropdown = 'Prison Sentence Suspended'">
									<xsl:text>PSS</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>CO</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text> </xsl:text>
							<xsl:if test="string-length($baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODurationType/ord:Years) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODurationType/ord:Years"/>
							
							<xsl:if test="string-length($baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODurationType/ord:Months) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODurationType/ord:Months"/>
							<xsl:if test="string-length($baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODurationType/ord:Days) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence3/ord:OffencePPSCO3Section/ord:OffencePPSCO3/ord:D20OffencePPSCODurationType/ord:Days"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_PPSCO4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffencePPSCO4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODropdown = 'Prison Sentence Suspended'">
									<xsl:text>PSS</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>CO</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text> </xsl:text>
							<xsl:if test="string-length($baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODurationType/ord:Years) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODurationType/ord:Years"/>
							
							<xsl:if test="string-length($baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODurationType/ord:Months) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODurationType/ord:Months"/>
							<xsl:if test="string-length($baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODurationType/ord:Days) &lt; 2">
								<xsl:text>0</xsl:text>
							</xsl:if>
							<xsl:value-of select="$baseAll/ord:Offence4/ord:OffencePPSCO4Section/ord:OffencePPSCO4/ord:D20OffencePPSCODurationType/ord:Days"/>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DTTPLabel">
		<fo:inline>
			<xsl:text>DTTP/DTETP</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DTTP1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence1/ord:OffenceDisqTestPassed1 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence1/ord:OffenceDisqTestPassed1 = 'DTTP - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence1/ord:OffenceDisqTestPassed1 = 'DTETP - 4'">
							<xsl:text>4</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DTTP2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence2/ord:OffenceDisqTestPassed2 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence2/ord:OffenceDisqTestPassed2 = 'DTTP - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence2/ord:OffenceDisqTestPassed2 = 'DTETP - 4'">
							<xsl:text>4</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DTTP3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence3/ord:OffenceDisqTestPassed3 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence3/ord:OffenceDisqTestPassed3 = 'DTTP - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence3/ord:OffenceDisqTestPassed3 = 'DTETP - 4'">
							<xsl:text>4</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DTTP4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence4/ord:OffenceDisqTestPassed4 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence4/ord:OffenceDisqTestPassed4 = 'DTTP - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence4/ord:OffenceDisqTestPassed4 = 'DTETP - 4'">
							<xsl:text>4</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DPSLabel">
		<fo:inline>
			<xsl:text>DPS/Sentenced</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DPS1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence1/ord:OffenceDPSSentence1 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence1/ord:OffenceDPSSentence1 = 'DPS - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence1/ord:OffenceDPSSentence1 = 'Sentenced - 2'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DPS2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence2/ord:OffenceDPSSentence2 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence2/ord:OffenceDPSSentence2 = 'DPS - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence2/ord:OffenceDPSSentence2 = 'Sentenced - 2'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DPS3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence3/ord:OffenceDPSSentence3 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence3/ord:OffenceDPSSentence3 = 'DPS - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence3/ord:OffenceDPSSentence3 = 'Sentenced - 2'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DPS4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence4/ord:OffenceDPSSentence4 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence4/ord:OffenceDPSSentence4 = 'DPS - 1'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence4/ord:OffenceDPSSentence4 = 'Sentenced - 2'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_InterimFinalLabel">
		<fo:inline>
			<xsl:text>Interim/Final</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_InterimFinal1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence1/ord:OffenceInterimFinal1 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence1/ord:OffenceInterimFinal1 = '1 - Interim Imposed'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence1/ord:OffenceInterimFinal1 = '2 - Final Sentence'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_InterimFinal2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence2/ord:OffenceInterimFinal2 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence2/ord:OffenceInterimFinal2 = '1 - Interim Imposed'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence2/ord:OffenceInterimFinal2 = '2 - Final Sentence'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_InterimFinal3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence3/ord:OffenceInterimFinal3 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence3/ord:OffenceInterimFinal3 = '1 - Interim Imposed'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence3/ord:OffenceInterimFinal3 = '2 - Final Sentence'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_InterimFinal4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:if test="$baseAll/ord:Offence4/ord:OffenceInterimFinal4 != 'Not Applicable'">
						<xsl:if test="$baseAll/ord:Offence4/ord:OffenceInterimFinal4 = '1 - Interim Imposed'">
							<xsl:text>1</xsl:text>
						</xsl:if>
						<xsl:if test="$baseAll/ord:Offence4/ord:OffenceInterimFinal4 = '2 - Final Sentence'">
							<xsl:text>2</xsl:text>
						</xsl:if>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_SentencingCourtLabel">
		<fo:inline>
			<xsl:text>Sentencing Court</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentencingCourt1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceSentencingCourt1/ord:CourtHouseType=$CrownCourt">
									<xsl:call-template name="SentencingCourtHouseNumber1"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="SentencingCourtHouseNumber1"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentencingCourt2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceSentencingCourt2/ord:CourtHouseType=$CrownCourt">
									<xsl:call-template name="SentencingCourtHouseNumber2"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="SentencingCourtHouseNumber2"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentencingCourt3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceSentencingCourt3/ord:CourtHouseType=$CrownCourt">
									<xsl:call-template name="SentencingCourtHouseNumber3"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="SentencingCourtHouseNumber3"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentencingCourt4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceSentencingCourt4/ord:CourtHouseType=$CrownCourt">
									<xsl:call-template name="SentencingCourtHouseNumber4"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="SentencingCourtHouseNumber4"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_SentDateLabel">
		<fo:inline>
			<xsl:text>Date of Sentence (if different)</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentDate1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/@selected = 'true'
							and string-length(//ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceDateOfSentence1) > 0
							and //ord:D20/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceDateOfSentence1 != '0000-00-00'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceSentencingCourt1Section/ord:OffenceDateOfSentence1"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentDate2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/@selected = 'true'
							and string-length(//ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceDateOfSentence2) > 0
							and //ord:D20/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceDateOfSentence2 != '0000-00-00'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceSentencingCourt2Section/ord:OffenceDateOfSentence2"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentDate3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/@selected = 'true'
							and string-length(//ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceDateOfSentence3) > 0
							and //ord:D20/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceDateOfSentence3 != '0000-00-00'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceSentencingCourt3Section/ord:OffenceDateOfSentence3"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentDate4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/@selected = 'true'
							and string-length(//ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceDateOfSentence4) > 0
							and //ord:D20/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceDateOfSentence4 != '0000-00-00'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceSentencingCourt4Section/ord:OffenceDateOfSentence4"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DateDisqRemovedLabel">
		<fo:inline>
			<xsl:text>Date from which disq. removed</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqRemoved1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceDisqualificationRemoved1Section/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceDisqualificationRemoved1Section/ord:OffenceDisqualificationRemoved1"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqRemoved2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceDisqualificationRemoved2Section/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceDisqualificationRemoved2Section/ord:OffenceDisqualificationRemoved2"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqRemoved3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceDisqualificationRemoved3Section/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceDisqualificationRemoved3Section/ord:OffenceDisqualificationRemoved3"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqRemoved4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceDisqualificationRemoved4Section/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceDisqualificationRemoved4Section/ord:OffenceDisqualificationRemoved4"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DateDisqSuspLabel">
		<fo:inline>
			<xsl:text>Date disq suspended pending appeal / Further sentence</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqSusp1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceDisqualificationSuspended1Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceDisqualificationSuspended1Section/ord:OffenceDisqualificationSuspended1"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqSusp2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceDisqualificationSuspended2Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceDisqualificationSuspended2Section/ord:OffenceDisqualificationSuspended2"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqSusp3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceDisqualificationSuspended3Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceDisqualificationSuspended3Section/ord:OffenceDisqualificationSuspended3"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqSusp4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceDisqualificationSuspended4Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceDisqualificationSuspended4Section/ord:OffenceDisqualificationSuspended4"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_DateDisqReimposedLabel">
		<fo:inline>
			<xsl:text>Date disq reimposed pending appeal / Further sentence</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqReimposed1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceDisqualificationReimposed1Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceDisqualificationReimposed1Section/ord:OffenceDisqualificationReimposed1"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqReimposed2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceDisqualificationReimposed2Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceDisqualificationReimposed2Section/ord:OffenceDisqualificationReimposed2"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqReimposed3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceDisqualificationReimposed3Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceDisqualificationReimposed3Section/ord:OffenceDisqualificationReimposed3"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_DateDisqReimposed4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceDisqualificationReimposed4Section/@selected = 'true'">
							<xsl:call-template name="FormatDate2">
								<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceDisqualificationReimposed4Section/ord:OffenceDisqualificationReimposed4"/>
							</xsl:call-template>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_NoDisqualificationMitigatingCircumstancesLabel">
		<fo:inline>
			<xsl:text>Mitigating Circumstances</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationMitigatingCircumstances1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence1/ord:NoDisqualificationMitigatingCircumstances1 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationMitigatingCircumstances2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence2/ord:NoDisqualificationMitigatingCircumstances2 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationMitigatingCircumstances3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence3/ord:NoDisqualificationMitigatingCircumstances3 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationMitigatingCircumstances4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence4/ord:NoDisqualificationMitigatingCircumstances4 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_NoDisqualificationSpecialReasonsLabel">
		<fo:inline>
			<xsl:text>Special Reasons</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationSpecialReasons1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence1/ord:NoDisqualificationSpecialReasons1 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationSpecialReasons2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence2/ord:NoDisqualificationSpecialReasons2 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationSpecialReasons3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence3/ord:NoDisqualificationSpecialReasons3 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NoDisqualificationSpecialReasons4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence4/ord:NoDisqualificationSpecialReasons4 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_NotificationOfDisabilityLabel">
		<fo:inline>
			<xsl:text>Notification of Disability</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NotificationOfDisability1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence1/ord:NotificationOfDisability1 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NotificationOfDisability2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence2/ord:NotificationOfDisability2 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NotificationOfDisability3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence3/ord:NotificationOfDisability3 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_NotificationOfDisability4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="$baseAll/ord:Offence4/ord:NotificationOfDisability4 = 'true'">Yes</xsl:when>
						<xsl:otherwise>No</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AppealCourtLabel">
		<fo:inline>
			<xsl:text>Appeal Court</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealCourt1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:OffenceAppealCourt1/ord:CourtHouseType=$CrownCourt">
									<xsl:text>0</xsl:text>
									<xsl:call-template name="AppealCourtHouseNumber1"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="AppealCourtHouseNumber1"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealCourt2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:OffenceAppealCourt2/ord:CourtHouseType=$CrownCourt">
									<xsl:text>0</xsl:text>
									<xsl:call-template name="AppealCourtHouseNumber2"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="AppealCourtHouseNumber2"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealCourt3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:OffenceAppealCourt3/ord:CourtHouseType=$CrownCourt">
									<xsl:text>0</xsl:text>
									<xsl:call-template name="AppealCourtHouseNumber3"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="AppealCourtHouseNumber3"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealCourt4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:OffenceAppealCourt4/ord:CourtHouseType=$CrownCourt">
									<xsl:text>0</xsl:text>
									<xsl:call-template name="AppealCourtHouseNumber4"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="AppealCourtHouseNumber4"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>

	<xsl:template match="nar:D20_AppealDateLabel">
		<fo:inline>
			<xsl:text>Appeal Date</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDate1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/ord:OffenceDateOfAppeal1Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:OffenceDateOfAppeal1Section/ord:OffenceDateOfAppeal1"/>
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDate2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/ord:OffenceDateOfAppeal2Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:OffenceDateOfAppeal2Section/ord:OffenceDateOfAppeal2"/>
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDate3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/ord:OffenceDateOfAppeal3Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:OffenceDateOfAppeal3Section/ord:OffenceDateOfAppeal3"/>
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>	
	<xsl:template match="nar:D20_AppealDate4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/ord:OffenceDateOfAppeal4Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:OffenceDateOfAppeal4Section/ord:OffenceDateOfAppeal4"/>
									</xsl:call-template>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AppealWasAgainstConvictionLabel">
		<fo:inline>
			<xsl:text>Appeal Was Against Conviction</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstConviction1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasAgainstConviction1 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstConviction2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasAgainstConviction2 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstConviction3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasAgainstConviction3 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstConviction4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasAgainstConviction4 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AppealWasAgainstSentenceOnlyLabel">
		<fo:inline>
			<xsl:text>Appeal Was Against Sentence Only</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstSentenceOnly1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasAgainstSentenceOnly1 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstSentenceOnly2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasAgainstSentenceOnly2 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstSentenceOnly3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasAgainstSentenceOnly3 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAgainstSentenceOnly4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasAgainstSentenceOnly4 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AppealWasAllowedLabel">
		<fo:inline>
			<xsl:text>Appeal Was Allowed</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAllowed1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasAllowed1 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAllowed2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasAllowed2 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAllowed3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasAllowed3 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAllowed4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasAllowed4 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>

	<xsl:template match="nar:D20_AppealDismissedLabel">
		<fo:inline>
			<xsl:text>Appeal Dismissed</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDismissed1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealDismissed1Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealDismissed1Section/ord:AppealDateDismissed1"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDismissed2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealDismissed2Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealDismissed2Section/ord:AppealDateDismissed2"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDismissed3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealDismissed3Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealDismissed3Section/ord:AppealDateDismissed3"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealDismissed4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealDismissed4Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealDismissed4Section/ord:AppealDateDismissed4"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AppealWasAbandonedLabel">
		<fo:inline>
			<xsl:text>Appeal Was Abandoned</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAbandoned1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasAbandoned1Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasAbandoned1Section/ord:AppealDateAbandoned1"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAbandoned2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasAbandoned2Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasAbandoned2Section/ord:AppealDateAbandoned2"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAbandoned3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasAbandoned3Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasAbandoned3Section/ord:AppealDateAbandoned3"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasAbandoned4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasAbandoned4Section/@selected = 'true'">
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasAbandoned4Section/ord:AppealDateAbandoned4"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_SentenceWasVariedLabel">
		<fo:inline>
			<xsl:text>Sentence Was Varied</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentenceWasVaried1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:SentenceWasVaried1 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentenceWasVaried2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:SentenceWasVaried2 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentenceWasVaried3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:SentenceWasVaried3 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_SentenceWasVaried4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:SentenceWasVaried4 = 'true'">Yes</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="nar:D20_AppealWasRemittedLabel">
		<fo:inline>
			<xsl:text>Appeal Was Remitted</xsl:text>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasRemitted1">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence1/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasRemitted1Section/@selected = 'true'">Yes, 
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence1/ord:OffenceAppeal1Section/ord:AppealWasRemitted1Section/ord:AppealDateRemitted1"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasRemitted2">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence2/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasRemitted2Section/@selected = 'true'">Yes, 
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence2/ord:OffenceAppeal2Section/ord:AppealWasRemitted2Section/ord:AppealDateRemitted1"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasRemitted3">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence3/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasRemitted3Section/@selected = 'true'">Yes, 
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence3/ord:OffenceAppeal3Section/ord:AppealWasRemitted3Section/ord:AppealDateRemitted1"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:D20_AppealWasRemitted4">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="//ord:D20/ord:Offence4/@selected = 'true'">
					<xsl:choose>
						<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/@selected = 'true'">
							<xsl:choose>
								<xsl:when test="//ord:D20/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasRemitted4Section/@selected = 'true'">Yes, 
									<xsl:call-template name="FormatDate2">
										<xsl:with-param name="date" select="$baseAll/ord:Offence4/ord:OffenceAppeal4Section/ord:AppealWasRemitted4Section/ord:AppealDateRemitted1"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>No</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	
	
	<!-- ****************************** -->
	<!-- D20 ORDER END -->
	<!-- ****************************** -->
</xsl:stylesheet>
