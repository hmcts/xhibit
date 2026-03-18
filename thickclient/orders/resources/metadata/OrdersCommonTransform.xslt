<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
	<!-- ************************* TEMPLATE START ************************* -->
	<!-- S.Bachra 17/4/03 Handling of no Title, Suffix (Tracker 52676) -->
	<xsl:template name="CallableDefendantFullName">
		<fo:inline>
			<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Name">
				<xsl:if test="count(apd:CitizenNameTitle) !=0">
					<xsl:value-of select="apd:CitizenNameTitle"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="apd:CitizenNameForename[1]">
					<xsl:value-of select="apd:CitizenNameForename[1]"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:if test="apd:CitizenNameForename[2] and apd:CitizenNameForename[2] !=' '">
					<xsl:value-of select="apd:CitizenNameForename[2]"/>
					<xsl:text> </xsl:text>
				</xsl:if>
				<xsl:value-of select="apd:CitizenNameSurname"/>
				<xsl:text> </xsl:text>
				<xsl:if test="count(apd:CitizenNameSuffix) !=0">
					<xsl:value-of select="apd:CitizenNameSuffix"/>
					<xsl:text> </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<!-- Common Template used to display appropriate text depending on the case type -->
	<xsl:template name="AppellantDefendant">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="substring($baseAll/ord:OrderHeader/ord:CaseNumber,1,1)='A'">
                    appellant
                </xsl:when>
				<xsl:otherwise>
                    defendant
                </xsl:otherwise>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<!-- Template to display a bullet point with text -->
	<xsl:template name="bullet">
		<xsl:param name="text"/>
		<fo:list-item space-after="1em">
			<fo:list-item-label>
				<fo:block font-weight="bold">
					<xsl:text>&#x2219;</xsl:text>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()" end-indent="5mm">
				<fo:block>
					<xsl:value-of select="$text"/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<!-- Template to display text as per bullet without the bullet point [indented correctly]-->
	<xsl:template name="no_bullet">
		<xsl:param name="text"/>
		<fo:list-item space-after="1em">
			<fo:list-item-label>
				<fo:block font-weight="bold"/>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()" end-indent="5mm">
				<fo:block>
					<xsl:value-of select="$text"/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<!-- Common Template to display Breach Text -->
	<xsl:template name="BreachText">
        These hours of work to be 
        <xsl:choose>
			<xsl:when test="$baseAll/ord:CommunityPunishment/ord:Hours/@TermType='Concurrent'">
                concurrent with
            </xsl:when>
			<xsl:when test="$baseAll/ord:CommunityPunishment/ord:Hours/@TermType='Consecutive'">
                consecutive to
            </xsl:when>
		</xsl:choose>
        those specified in a Community Punishment Order made by <xsl:value-of select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseName"/>
		<xsl:for-each select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:CourtHouse">
			<xsl:call-template name="CourtHouseType"/>
		</xsl:for-each>
        on 
        <xsl:call-template name="FormatDate">
			<xsl:with-param name="date" select="$baseAll/ord:Breach/ord:OriginatingCourt/ord:Date"/>
		</xsl:call-template>
	</xsl:template>
	<!-- Template used to display Defendant Address -->
	<xsl:template name="CallableDefendantAddress">
		<xsl:for-each select="$DefAddress">
			<xsl:call-template name="CallableAddress"/>
		</xsl:for-each>
	</xsl:template>
	<!-- Template used to check and display address -->
	<xsl:template name="CallableAddress">
		<fo:inline>
			<!-- AW Daley 01/09/03 Added Check for default address line -->
			<xsl:if test="apd:Line[1] != '  ' and apd:Line[1] != 'Address Line1'">
				<xsl:value-of select="apd:Line[1]"/>
			</xsl:if>
			<xsl:if test="apd:Line[2] != '  ' and apd:Line[2] != 'Address Line2'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[2]"/>
			</xsl:if>
			<xsl:if test="apd:Line[3] and apd:Line[3] != '  ' and apd:Line[3] != '' and apd:Line[3] != ' '">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[3]"/>
			</xsl:if>
			<xsl:if test="apd:Line[4] and apd:Line[4] != '  ' and apd:Line[4] != '' and apd:Line[4] != ' '">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[4]"/>
			</xsl:if>
			<xsl:if test="apd:Line[5] and apd:Line[5] != '  ' and apd:Line[5] != '' and apd:Line[5] != ' '">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[5]"/>
			</xsl:if>
			<!-- AW Daley 01/09/03 Check for default post code added -->
			<xsl:if test="apd:PostCode and apd:PostCode!='AA1 1AA'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:PostCode"/>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Template used to check and display address -->
	<xsl:template name="CallableAddress_Comm_Order">
		<fo:inline>
			<xsl:if test="apd:Line[1] != '-'">
				<xsl:value-of select="apd:Line[1]"/>
			</xsl:if>
			<xsl:if test="apd:Line[2] != '-'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[2]"/>
			</xsl:if>
			<xsl:if test="apd:Line[3] != '-'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[3]"/>
			</xsl:if>
			<xsl:if test="apd:Line[4] != '-'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[4]"/>
			</xsl:if>
			<xsl:if test="apd:Line[5] != '-'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:Line[5]"/>
			</xsl:if>
			<xsl:if test="apd:PostCode != 'A1 1AA'">
				<xsl:text>, </xsl:text>
				<xsl:value-of select="apd:PostCode"/>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Common Template to display Breach Court Type -->
	<xsl:template name="CallableCommunityCourtHouseType">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:Breach/ord:OriginatingCourt/ord:CourtHouse/ord:CourtHouseType=$CrownCourt"> the Crown Court at </xsl:when>
			<xsl:otherwise> unspecified court</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="callableP">
		<!--xsl:copy-->
		<fo:block space-after="12pt">
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
		<!--/xsl:copy-->
	</xsl:template>
	<!-- Common template to display court type -->
	<xsl:template name="CourtHouseType">
		<xsl:choose>
			<xsl:when test="./ord:CourtHouseType=$CrownCourt"> Crown Court</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- template made callable as apply-templates does not pull in data - Neil Entwistle 26/09/03 -->
	<xsl:template name="CallableNextAppearanceCourtType">
		<fo:inline>
			<xsl:for-each select="$baseAll/ord:NextAppearance/ord:AppearanceCourt">
				<xsl:call-template name="PrecedingCourtHouseType"/>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>
	<!-- Template used to display next appearance court -->
	<xsl:template name="CallableNextAppearanceCourtHouseName">
		<xsl:value-of select="$baseAll/ord:NextAppearance/ord:AppearanceCourt/ord:CourtHouseName"/>
	</xsl:template>
	<!-- Common Template to display Punishment Hours -->
	<xsl:template name="CommunityPunishmentHours">
		<xsl:value-of select="$baseAll/ord:CommunityPunishment/ord:Hours"/>
	</xsl:template>
	<!-- Template to set up the page -->
	<xsl:template name="PageSetUp">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="print" page-height="297mm" page-width="210mm" margin-top="20mm" margin-bottom="15mm" margin-left="1.0cm" margin-right="1.0cm">
				<xsl:choose>
					<xsl:when test="/ord:Order/ord:OrderData/ord:D20">
						<fo:region-body margin-top="80mm" margin-bottom="15mm"/>
					</xsl:when>
					<xsl:otherwise>
						<fo:region-body margin-top="55mm" margin-bottom="15mm"/>
					</xsl:otherwise>
				</xsl:choose>
				<fo:region-before region-name="header-first" extent="45.5cm"/>
				<fo:region-after region-name="footer-first" extent="15mm"/>
			</fo:simple-page-master>
			<!-- S.Bachra 22/04/03 page-height adjusted to make display page longer (Tracker 52683) -->
			<fo:simple-page-master master-name="display" page-height="1000mm" page-width="210mm" margin-top="20mm" margin-bottom="15mm" margin-left="0.5cm" margin-right="0.5cm">
				<xsl:choose>
					<xsl:when test="/ord:Order/ord:OrderData/ord:D20">
						<fo:region-body margin-top="80mm" margin-bottom="15mm"/>
					</xsl:when>
					<xsl:otherwise>
						<fo:region-body margin-top="55mm" margin-bottom="15mm"/>
					</xsl:otherwise>
				</xsl:choose>
				<fo:region-before region-name="header-first" extent="45.5cm"/>
				<fo:region-after region-name="footer-first" extent="1.5cm"/>
			</fo:simple-page-master>
			<fo:simple-page-master master-name="rest" page-height="297mm" page-width="210mm" margin-top="20mm" margin-bottom="15mm" margin-left="0.5cm" margin-right="0.5cm">
				<fo:region-body margin-top="15mm" margin-bottom="15mm"/>
				<fo:region-before region-name="header-rest" extent="1.5cm"/>
				<fo:region-after region-name="footer-rest" extent="7mm"/>
			</fo:simple-page-master>
			<fo:page-sequence-master master-name="A4">
				<fo:repeatable-page-master-alternatives>
					<fo:conditional-page-master-reference page-position="first">
						<xsl:attribute name="master-reference"><xsl:value-of select="$mode"/></xsl:attribute>
					</fo:conditional-page-master-reference>
					<fo:conditional-page-master-reference page-position="rest">
						<xsl:attribute name="master-reference"><xsl:value-of select="$mode"/></xsl:attribute>
					</fo:conditional-page-master-reference>
				</fo:repeatable-page-master-alternatives>
			</fo:page-sequence-master>
		</fo:layout-master-set>
	</xsl:template>
	<!-- Date time notified -->
	<xsl:template name="DateTimeNotified">
		<fo:inline>
			<xsl:text> on a date and at a time to be notified </xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Common Template used to display Defendant Date Of Birth -->
	<xsl:template name="CallableDefendantDOB">
		<fo:inline>
			<xsl:if test="$DefDOB">
				<xsl:call-template name="FormatDate">
					<xsl:with-param name="date" select="$DefDOB"/>
				</xsl:call-template>
			</xsl:if>
		</fo:inline>
	</xsl:template>
	<!-- Format monetary amounts depending on the currency  -->
	<xsl:template name="FormatAmount">
		<xsl:param name="amount"/>
		<xsl:param name="currency"/>
		<xsl:choose>
			<xsl:when test="ord:Currency='GBP'">
				<xsl:value-of select="format-number($amount, '#,##0.00', 'en_gb')"/>
			</xsl:when>
			<xsl:when test="ord:Currency='EURO'">
				<xsl:value-of select="format-number($amount, '#,##0.00', 'en_euro')"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:decimal-format name="en_gb" decimal-separator="." grouping-separator=","/>
	<xsl:decimal-format name="en_euro" decimal-separator="," grouping-separator="."/>
	<!-- Template used for date formatting DD-MMM-YYYY -->
	<xsl:template name="FormatDate">
		<xsl:param name="date"/>
		<xsl:param name="year" select="substring($date,1,4)"/>
		<xsl:param name="month" select="substring($date,6,2)"/>
		<xsl:param name="day" select="substring($date,9,2)"/>
		<xsl:value-of select="$day"/>
		<xsl:text>-</xsl:text>
		<xsl:choose>
			<xsl:when test="$month='01'">
				<xsl:text>Jan</xsl:text>
			</xsl:when>
			<xsl:when test="$month='02'">
				<xsl:text>Feb</xsl:text>
			</xsl:when>
			<xsl:when test="$month='03'">
				<xsl:text>Mar</xsl:text>
			</xsl:when>
			<xsl:when test="$month='04'">
				<xsl:text>Apr</xsl:text>
			</xsl:when>
			<xsl:when test="$month='05'">
				<xsl:text>May</xsl:text>
			</xsl:when>
			<xsl:when test="$month='06'">
				<xsl:text>Jun</xsl:text>
			</xsl:when>
			<xsl:when test="$month='07'">
				<xsl:text>Jul</xsl:text>
			</xsl:when>
			<xsl:when test="$month='08'">
				<xsl:text>Aug</xsl:text>
			</xsl:when>
			<xsl:when test="$month='09'">
				<xsl:text>Sep</xsl:text>
			</xsl:when>
			<xsl:when test="$month='10'">
				<xsl:text>Oct</xsl:text>
			</xsl:when>
			<xsl:when test="$month='11'">
				<xsl:text>Nov</xsl:text>
			</xsl:when>
			<xsl:when test="$month='12'">
				<xsl:text>Dec</xsl:text>
			</xsl:when>
		</xsl:choose>
		<xsl:text>-</xsl:text>
		<xsl:value-of select="$year"/>
	</xsl:template>
	<!-- Format the date in dd-mm-yyyy -->
	<xsl:template name="FormatDate2">
		<xsl:param name="date"/>
		<xsl:param name="year" select="substring($date,1,4)"/>
		<xsl:param name="month" select="substring($date,6,2)"/>
		<xsl:param name="day" select="substring($date,9,2)"/>
		<xsl:if test="$year != '0000'">
			<xsl:value-of select="$day"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="$month"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="$year"/>
		</xsl:if>
	</xsl:template>
	<!-- Template used to display his or her depending on gender -->
	<xsl:template name="HisHer">
		<xsl:choose>
			<xsl:when test="./ord:Sex='female'">
				<xsl:text>her</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>his</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template used to display his or her depending on gender -->
	<xsl:template name="HimHer">
		<xsl:choose>
			<xsl:when test="./ord:Sex='female'">
				<xsl:text>her</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>him</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template used to format TextArea widgets to force a newline where appropriate -->
	<xsl:template name="FormatTextArea">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="contains($string,'&#10;')">
				<xsl:value-of select="substring-before($string,'&#10;')"/>
				<fo:block hyphenate="true" language="en_GB"/>
				<xsl:call-template name="FormatTextArea">
					<xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<fo:block hyphenate="true" language="en_GB">
					<xsl:value-of select="$string"/>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template used to format TextArea widgets to force a newline where appropriate -->
	<xsl:template name="FormatBulletTextArea">
		<xsl:param name="string"/>
		<xsl:choose>
			<xsl:when test="contains($string,'&#10;')">
				<fo:block hyphenate="true" language="en_GB">
					<xsl:value-of select="substring-before($string,'&#10;')"/>
				</fo:block>
				<xsl:call-template name="FormatBulletTextArea">
					<xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<fo:block hyphenate="true" language="en_GB">
					<xsl:value-of select="$string"/>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template to format time -->
	<!-- S.Bachra 18/6/03 Format the time to remove the seconds element -->
	<xsl:template name="FormatTime">
		<xsl:param name="time"/>
		<xsl:choose>
			<xsl:when test="string-length($time)=8">
				<xsl:value-of select="substring($time,1,5)"/>
			</xsl:when>
			<xsl:when test="string-length($time)=7">
				<xsl:value-of select="substring($time,1,4)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$time"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template used to display himself or herself depending on gender -->
	<xsl:template name="HimselfHerself">
		<xsl:choose>
			<!-- <xsl:when test="./ord:Sex='male'">himself<xsl:text> </xsl:text>
            </xsl:when> -->
			<xsl:when test="./ord:Sex='female'">herself<xsl:text> </xsl:text>
			</xsl:when>
			<xsl:otherwise>himself<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template to display letter a or an depending on text to display -->
	<xsl:template name="LetterDisplay">
		<xsl:param name="text"/>
		<xsl:variable name="lowerText">
			<xsl:call-template name="toLower">
				<xsl:with-param name="content" select="$text"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="starts-with($lowerText,'a') or
							starts-with($lowerText,'e') or
							starts-with($lowerText,'i') or
							starts-with($lowerText,'o') or
							starts-with($lowerText,'u') or
							starts-with($lowerText,' ') or
							not(string($lowerText)) ">
				<xsl:text> an </xsl:text>
				<xsl:value-of select="$text"/>
				<xsl:text> </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text> a </xsl:text>
				<xsl:value-of select="$text"/>
				<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Common Template used to display him or her depending on gender -->
	<xsl:template name="MaleFemale">
		<xsl:choose>
			<!-- <xsl:when test="./ord:Sex='male'">him<xsl:text> </xsl:text>
            </xsl:when> -->
			<xsl:when test="./ord:Sex='female'">her<xsl:text> </xsl:text>
			</xsl:when>
			<xsl:otherwise>him<xsl:text> </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- Template to display signed date -->
	<!-- SB 17/04/03 Check to ensure that the value is not the default                       -->
	<!-- set-up in the blank schema 0001-01-01, if not select value (Tracker 52695) -->
	<xsl:template name="SignedDate">
		<xsl:if test="$SignedDate != '0001-01-01'">
			<xsl:call-template name="FormatDate">
				<xsl:with-param name="date" select="$SignedDate"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<!-- Common Template to display signed information -->
	<xsl:template name="SignedInfo">
		<fo:block space-after="12pt">
			<!-- S.Bachra 22/4/03 Signed Details: Display Judge of the Crown Court if Bench Warrant otherwise Officer of the Crown Court (Tracker 52696)-->
			<xsl:choose>
				<xsl:when test="$base/ord:BenchWarrant">
                    Judge of the Crown Court
                </xsl:when>
				<xsl:otherwise>
                    An Officer of the Crown Court 	
                </xsl:otherwise>
			</xsl:choose>
		</fo:block>
		<!-- S.Bachra 22/4/03 Display Signed Date (Tracker 52695) -->
		<!-- S.Bachra 6/5/03 Display name of Signing person (Tracker 52875) -->
		<fo:table table-layout="fixed">
			<fo:table-column column-width="80mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block>
                            Signed: <xsl:text> </xsl:text>
							<xsl:call-template name="SignedName"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="1mm">
                            Date:<xsl:text> </xsl:text>
							<fo:block>
								<xsl:call-template name="SignedDate"/>
							</fo:block>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!-- Common Signed Information for Community Orders -->
	<xsl:template name="SignedInfoCommunity">
		<fo:block space-after="12pt" font-size="10pt">
            (For the use of the probation service only) This order has been explained to me and I have received a copy of it
        </fo:block>
		<fo:table table-layout="fixed">
			<fo:table-column column-width="80mm"/>
			<fo:table-column column-width="30mm"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell text-align="left">
						<fo:block>
                            Signed:
                        </fo:block>
					</fo:table-cell>
					<fo:table-cell text-align="left">
						<fo:block start-indent="1mm">
                            Date:
                        </fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	<!-- Template to display signed details name -->
	<!-- S.Bachra 6/5/03 Display the name of the Signing person if present (Tracker 52875)-->
	<xsl:template name="SignedName">
		<xsl:for-each select="$baseAll/ord:OrderHeader/ord:SignedBy">
			<xsl:choose>
				<!-- Display Court Officer info if present -->
				<xsl:when test="./ord:CourtOfficer/@selected='true'">
					<xsl:value-of select="./ord:CourtOfficer/apd:CitizenNameTitle"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="./ord:CourtOfficer/apd:CitizenNameForename"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="./ord:CourtOfficer/apd:CitizenNameSurname"/>
					<xsl:text> </xsl:text>
				</xsl:when>
				<!-- Display Judiciary info if present -->
				<xsl:when test="./ord:Judiciary/@selected='true'">
					<xsl:value-of select="./ord:Judiciary/ord:Judge/apd:CitizenNameTitle"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="./ord:Judiciary/ord:Judge/apd:CitizenNameForename"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="./ord:Judiciary/ord:Judge/apd:CitizenNameSurname"/>
					<xsl:text> </xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<!-- Common Template to display Term Details for Months and days-->
	<xsl:template name="TermMonthsDays">
		<xsl:value-of select="./ord:Months"/>
		<xsl:text> month(s) </xsl:text>
		<xsl:value-of select="./ord:Days"/>
		<xsl:text> day(s) </xsl:text>
	</xsl:template>
	<!-- Common Template to display  term details -->
	<xsl:template name="TermType">
		<xsl:if test=".='Concurrent'">
            concurrent to
        </xsl:if>
		<xsl:if test=".='Consecutive'">
            consecutive to
        </xsl:if>
	</xsl:template>
	<!-- Template to display Term details including the days -->
	<xsl:template name="TermIncDays">
		<xsl:value-of select="./ord:Years"/>
		<xsl:text> year(s) </xsl:text>
		<xsl:value-of select="./ord:Months"/>
		<xsl:text> month(s) </xsl:text>
		<xsl:value-of select="./ord:Days"/>
		<xsl:text> day(s)</xsl:text>
	</xsl:template>
	<xsl:template name="TermIncWeeksDays">
		<xsl:value-of select="./ord:Years"/>
		<xsl:text> year(s) </xsl:text>
		<xsl:value-of select="./ord:Months"/>
		<xsl:text> month(s) </xsl:text>
		<xsl:value-of select="./ord:Weeks"/>
		<xsl:text> week(s) </xsl:text>
		<xsl:value-of select="./ord:Days"/>
		<xsl:text> day(s)</xsl:text>
	</xsl:template>
	<!-- Template to display only days for the Term  -->
	<xsl:template name="TermOnlyDays">
		<xsl:value-of select="./ord:Days"/>
		<xsl:text> day(s)</xsl:text>
	</xsl:template>
	<!-- Template to display Term details only where prodvided-->
	<xsl:template name="TermDuration">
		<xsl:if test="./ord:Years and ./ord:Years != '' and  ./ord:Years != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="./ord:Years"/>
			<xsl:text> year(s)</xsl:text>
		</xsl:if>
		<xsl:if test="./ord:Months and ./ord:Months != '' and  ./ord:Months != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="./ord:Months"/>
			<xsl:text> month(s)</xsl:text>
		</xsl:if>
		<xsl:if test="./ord:Weeks and ./ord:Weeks != '' and ./ord:Weeks != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="./ord:Weeks"/>
			<xsl:text> week(s)</xsl:text>
		</xsl:if>
		<xsl:if test="./ord:Days and ./ord:Days != '' and  ./ord:Days != '0' ">
			<xsl:text> </xsl:text>
			<xsl:value-of select="./ord:Days"/>
			<xsl:text> day(s)</xsl:text>
		</xsl:if>
	</xsl:template>
	<!-- template used to convert a string to Lower Case -->
	<xsl:template name="toLower">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
	</xsl:template>
	<!-- template used to convert a string to Upper Case -->
	<xsl:template name="toUpper">
		<xsl:param name="content"/>
		<xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	</xsl:template>
	<!-- Template to display Sentence Details -->
	<xsl:template name="YOICallableSentenceOption">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='period'">
				<xsl:call-template name="YOICallableSentenceOptionText"/>
			</xsl:when>
			<xsl:when test="$baseAll/ord:CustodialSentence/ord:ImprisonmentType ='section91' and $baseAll/ord:CustodialSentence/ord:Section91TermType/@selected='true' and $baseAll/ord:CustodialSentence/ord:Section91TermType='section91term'">
				<xsl:call-template name="YOICallableSentenceOptionText"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- Get order associated with Bench Warrant after fail -->
	<xsl:template name="Callable_BW_AF_getAssociatedOrder">
		<xsl:choose>
			<xsl:when test="$baseAll/ord:AssociatedOrder = 'communityOrder' ">
				<xsl:text>Community Order</xsl:text>
			</xsl:when>
			<xsl:when test="$baseAll/ord:AssociatedOrder = 'suspendedSentenceOrder' ">
				<xsl:text>Suspended Sentence Order</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- ************************* TEMPLATE END ************************* -->
	<!-- ************************* ORDER START ************************* -->
	<!-- Common Template used to display Currency and Value -->
	<!-- S.Bachra 19/5/03 Namespace changed to ord: to match calls to this template -->
	<xsl:template match="ord:MonetaryValue">
		<xsl:choose>
			<xsl:when test="ord:Currency='GBP'">&#163;</xsl:when>
			<xsl:when test="ord:Currency='EURO'">&#8364;</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="ord:Currency"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="FormatAmount">
			<xsl:with-param name="currency" select="ord:Currency"/>
			<xsl:with-param name="amount" select="ord:Amount"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ************************* ORDER END ************************* -->
	<xsl:template name="format-text-for-wrapping">
		<xsl:param name="string"/>
		<xsl:param name="maxlen" select="25"/>
		<xsl:choose>
			<!-- Tab -->
			<xsl:when test="contains($string,'&#09;')">
				<xsl:value-of select="substring-before($string,'&#09;')"/>
				<xsl:text> </xsl:text>
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="string" select="substring-after($string,'&#09;')"/>
					<xsl:with-param name="maxlen" select="$maxlen"/>
				</xsl:call-template>
			</xsl:when>
			<!-- Linefeed -->
			<xsl:when test="contains($string,'&#10;')">
				<xsl:value-of select="substring-before($string,'&#10;')"/>
				<xsl:text>&#x200B;</xsl:text>
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
					<xsl:with-param name="maxlen" select="$maxlen"/>
				</xsl:call-template>
			</xsl:when>
			<!-- Carriage Return -->
			<xsl:when test="contains($string,'&#13;')">
				<xsl:value-of select="substring-before($string,'&#13;')"/>
				<xsl:text>&#x200B;</xsl:text>
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="string" select="substring-after($string,'&#13;')"/>
					<xsl:with-param name="maxlen" select="$maxlen"/>
				</xsl:call-template>
			</xsl:when>
			<!-- Long word -->
			<xsl:when test="(string-length($string) &gt; $maxlen)">
				<xsl:value-of select="substring($string,0,$maxlen)"/>
				<xsl:text>&#x200B;</xsl:text>
				<xsl:call-template name="format-text-for-wrapping">
					<xsl:with-param name="string" select="substring($string, $maxlen)"/>
					<xsl:with-param name="maxlen" select="$maxlen"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<fo:block hyphenate="true" language="en_GB">
					<xsl:value-of select="$string"/>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
