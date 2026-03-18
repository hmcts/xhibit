<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ************************ -->
    <!-- BAIL ORDER START -->
    <!-- ************************ -->
    <!-- Bail Granted Text -->
    <xsl:template match="nar:BCBCase_BailGranted">
        <fo:inline>
            <xsl:text>Bail granted </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Type of Bail -->
    <xsl:template match="nar:BCBCase_BailGrantedType">
        <fo:inline>
            <xsl:if test="$base/ord:BailOrderBCase/ord:BailDecision='Conditional'">
                <xsl:text>conditionally</xsl:text>
            </xsl:if>
            <xsl:if test="$base/ord:BailOrderBCase/ord:BailDecision='Unconditional'">
                <xsl:text>unconditionally</xsl:text>
            </xsl:if>
        </fo:inline>
    </xsl:template>
    <!-- Record of Decision -->
    <xsl:template match="nar:BCBCase_RecordOfDecision">
        <fo:inline>
            <xsl:text> (record of decision)</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 1 -->
    <xsl:template match="nar:BCBCase_Part1">
        <fo:inline>
            <xsl:text>Part 1</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Personal Details -->
    <xsl:template match="nar:BCBCase_Part1_Title1">
        <fo:inline>
            <xsl:text>Personal details</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template used to display appropriate text depending on the case type -->
    <xsl:template match="nar:BCBCase_AppellantDefendant">
        <fo:inline>The </fo:inline>
        <xsl:call-template name="AppellantDefendant"/>
    </xsl:template>
    <!-- Apply to Court -->
    <xsl:template match="nar:BCBCase_ApplyToCourt">
        <fo:inline>
            <xsl:text>applied to this Court for bail.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 2 -->
    <xsl:template match="nar:BCBCase_Part2">
        <fo:inline>
            <xsl:text>Part 2</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 2 Title Line 1 -->
    <xsl:template match="nar:BCBCase_Part2_Title1">
        <fo:inline>
            <xsl:text>The decision and</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 2 Title Line 2 -->
    <xsl:template match="nar:BCBCase_Part2_Title2">
        <fo:inline>
            <xsl:text>any conditions</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Bail Act 1976 -->
    <xsl:template match="nar:BCBCase_BailAct1976">
        <fo:inline>
            <xsl:text>The Court considered the application under the Bail Act 1976 on: </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Was Order Amended Varied? - RFS4417 -->
    <xsl:template match="nar:BCBCase_AmendedVaried">
		<xsl:for-each select="$baseAll/ord:Amended[@selected='true']">
			<fo:inline>
				<xsl:text>&#xa;On </xsl:text>
				<xsl:call-template name="FormatDate">
					<xsl:with-param name="date" select="$baseAll/ord:Amended/ord:AmendedVariedDate"/>
				</xsl:call-template>
				<xsl:text> this Order was </xsl:text>
				<xsl:if test="$baseAll/ord:Amended/ord:AmendedVaried='Amended'">
					<xsl:text> amended to the following conditions: </xsl:text>
				</xsl:if>
				<xsl:if test="$baseAll/ord:Amended/ord:AmendedVaried='Varied'">
					<xsl:text> varied to the following conditions: </xsl:text>
				</xsl:if>
			</fo:inline>
		</xsl:for-each>
    </xsl:template>
    <!-- Bail Was Granted -->
    <xsl:template match="nar:BCBCase_BailWasGranted">
        <fo:inline>
            <xsl:text>Bail was granted: </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Bail Order Pre Condition Text -->
    <xsl:template match="nar:BCBCase_PreConditions">
        <xsl:if test="$base/ord:BailOrderBCase">
            <!-- Surety -->
            <xsl:for-each select="$baseAll/ord:PreConditions[@selected='true']">
                <fo:block space-after="12pt">
                    (A) To be complied with <fo:inline font-weight="bold">before</fo:inline> release from custody
                </fo:block>
                <!-- call template to display surety details -->
                <xsl:call-template name="Surety"/>
                <!-- call template to display passport details -->
                <xsl:call-template name="Passport"/>
                <!-- call template to display Security details -->
                <xsl:call-template name="Security"/>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <!-- Bail Order Post Conditions -->
    <xsl:template match="nar:BCBCase_PostConditions">
        <xsl:if test="$base/ord:BailOrderBCase">
            <xsl:for-each select="$baseAll/ord:PostConditions[@selected='true']">
                <fo:block space-before="12pt" space-after="12pt">
                    (B) To be complied with <fo:inline font-weight="bold">after</fo:inline> release from custody
                </fo:block>
                <!-- call template to display live sleep details -->
                <xsl:call-template name="LiveSleep"/>
                <!-- call template to display Notify Police details -->
                <xsl:call-template name="NotifyPolice"/>
                <!-- call template to display curfew details -->
                <xsl:call-template name="Curfew"/>
                <!-- call template to display police details -->
                <xsl:call-template name="PoliceReport"/>
                <!-- call template to display available details -->
                <xsl:call-template name="Available"/>
                <!-- call template to display contact details -->
                <xsl:call-template name="Contact"/>
                <!-- call template to display distance details -->
                <xsl:call-template name="Distance"/>
                <!-- call template to display International Travel Warrants -->
                <xsl:call-template name="TravelWarrant"/>
                <!-- call template to display other details -->
                <xsl:call-template name="Other"/>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <!-- Template to display Bail Order Surety Details -->
    <xsl:template name="Surety">
        <xsl:for-each select="ord:Surety[@selected='true']">
            <fo:block>
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                To provide 
                                <xsl:value-of select="ord:Plural"/>
                                in the sum of 
                                <xsl:apply-templates select="ord:MonetaryValue"/>
                            </fo:block>
                            <fo:block>
                                    <!-- RFC 1356 Amend spelling for recognizance -->
                                    to secure the surrender of the <xsl:call-template name="AppellantDefendant"/>
                                    to custody at the time and place directed (recognizance(s) of 
                                    <xsl:value-of select="ord:Plural"/>
                                    endorsed on Form 5102D: 'Bail: recognizance of a surety').
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display Bail Order Passport Details -->
    <xsl:template name="Passport">
        <xsl:for-each select="ord:Passport[@selected='true' and (ord:Surrendered='true' or ord:Retained='true')]">
            <fo:block>
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <xsl:text>Passport to be</xsl:text>
                                <xsl:if test="ord:Surrendered='true'"> surrendered to </xsl:if>
                                <xsl:if test="ord:Retained = 'true'">
                                    <xsl:if test="ord:Surrendered='true'"> and</xsl:if>
                                    <xsl:text> retained by </xsl:text>
                                </xsl:if>
                                <xsl:text>the police.</xsl:text>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display Bail Order Security Details -->
    <xsl:template name="Security">
        <xsl:for-each select="ord:Security[@selected='true']">
            <fo:block>
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                To provide a security in the sum of
                                <xsl:apply-templates select="ord:MonetaryValue"/>
                                to be deposited with the court.
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display live sleep details -->
    <xsl:template name="LiveSleep">
        <xsl:for-each select="ord:LiveSleep[@selected='true']">
            <fo:block>
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                To live and sleep each night
                                <xsl:choose>
                                    <xsl:when test="ord:AtFollowingAddress='false'">
										<xsl:choose>
											<xsl:when test="$DefAddress != null">
												at: <xsl:call-template name="CallableDefendantAddress"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
													at: <xsl:call-template name="CallableDefendantAddress"/>
												</xsl:for-each>
											</xsl:otherwise>
										</xsl:choose>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        at: 
                                        <xsl:for-each select="./ord:Address">
                                            <xsl:call-template name="CallableBailAddress"/>
                                        </xsl:for-each>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display Notify Police Details -->
    <xsl:template name="NotifyPolice">
        <xsl:if test="ord:NotifyPolice/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>-</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            To notify police of any change of address.	
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
    </xsl:template>
    <!-- Template to display curfew details -->
    <xsl:template name="Curfew">
        <xsl:for-each select="ord:Curfew[@selected='true']">
            <fo:block>
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                To observe a curfew between the hours of:<xsl:text> </xsl:text>
                                <xsl:call-template name="FormatTime">
                                    <xsl:with-param name="time" select="ord:From"/>
                                </xsl:call-template>
                                and 
                                <xsl:call-template name="FormatTime">
                                    <xsl:with-param name="time" select="ord:To"/>
                                </xsl:call-template>
                                <xsl:if test="ord:ElectronicMonitoring='true'">
									<fo:block>
										<xsl:text>This will be Electronically Monitored.</xsl:text>
									</fo:block>
								</xsl:if>
								  <xsl:if test="ord:DoorstepCondition='true'">
									<fo:block>
										<xsl:text>DoorstepConditions will apply.</xsl:text>
									</fo:block>
								</xsl:if>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display Police Report Details -->
    <xsl:template name="PoliceReport">
        <xsl:for-each select="ord:PoliceReport[@selected='true']">
            <fo:block>
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block hyphenate="true" language="en_GB">
                                To report to: 
                                <xsl:value-of select="ord:Station"/>
                                Police Station each: 
                                <xsl:value-of select="ord:Period"/>
                                between: 
                                <xsl:call-template name="FormatTime">
                                    <xsl:with-param name="time" select="ord:From"/>
                                </xsl:call-template>
                                and 
                                <xsl:call-template name="FormatTime">
                                    <xsl:with-param name="time" select="ord:To"/>
                                </xsl:call-template>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display available details -->
    <xsl:template name="Available">
        <fo:block>
            <xsl:if test="ord:Available/@selected='true'">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                To be available as and when required to enable enquiries or reports to be made.
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </xsl:if>
        </fo:block>
    </xsl:template>
    <!-- Template to display Contact Details -->
    <xsl:template name="Contact">
        <fo:block hyphenate="true" language="en_GB">
            <xsl:if test="ord:Contact/@selected='true'">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                Not to contact directly or indirectly: <xsl:value-of select="ord:Contact"/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </xsl:if>
        </fo:block>
    </xsl:template>
    <!-- Template to display Distance Details -->
    <xsl:template name="Distance">
        <xsl:for-each select="ord:Distance[@selected='true']">
            <fo:block hyphenate="true" language="en_GB">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                Not to come within <xsl:value-of select="ord:TheDistance"/> of <xsl:value-of select="ord:Location"/>
                                <fo:block>
                                    except to see a solicitor by prior written appointment.
                                </fo:block>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </fo:block>
        </xsl:for-each>
    </xsl:template>
    <!-- Template to display International Travel Warrant -->
    <xsl:template name="TravelWarrant">
        <fo:block>
            <xsl:if test="ord:TravelWarrants/@selected='true'">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                Not to apply for international travel warrants.
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </xsl:if>
        </fo:block>
    </xsl:template>
    <!-- Template to display Other Details -->
    <xsl:template name="Other">
        <fo:block hyphenate="true" language="en_GB">
            <xsl:if test="ord:Other[@selected='true']">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item>
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>-</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                Other <fo:inline font-style="italic">(please say here): </fo:inline>
                                <xsl:value-of select="ord:Other"/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </xsl:if>
        </fo:block>
    </xsl:template>
    <!-- Part 3 -->
    <xsl:template match="nar:BCBCase_Part3">
        <fo:inline>
            <xsl:text>Part 3</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 3 Title Line 1 -->
    <xsl:template match="nar:BCBCase_Part3_Title1">
        <fo:inline>
            <xsl:text>Reason(s) for imposing</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 3 Title Line 2 -->
    <xsl:template match="nar:BCBCase_Part3_Title2">
        <fo:inline>
            <xsl:text>the above conditions</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display Reasons Text -->
    <xsl:template match="nar:BCBCase_Reasons">
        <xsl:call-template name="FormatTextArea">
            <xsl:with-param name="string" select="$baseAll/ord:Reasons"/>
        </xsl:call-template>
    </xsl:template>
    <!-- Template used to display Bail Address -->
    <!-- S.Bachra 19/5/03 Check for single spaces, text within blank schema for Bail Order Live/Sleep Conditions -->
    <xsl:template name="CallableBailAddress">
        <fo:inline>
            <!-- AW Daley 01/09/03 Added Check for default address line -->
            <xsl:if test="normalize-space(apd:Line[1]) != '' and apd:Line[1] != 'Address Line1'">
                <xsl:value-of select="normalize-space(apd:Line[1])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[2]) != ' ' and apd:Line[2] != 'Address Line2'">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[2])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[3]) != ''">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[3])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[4]) != ''">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[4])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[5]) != ''">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[5])"/>
            </xsl:if>
            <!-- AW Daley 01/09/03 Check for default post code added -->
            <xsl:if test="apd:PostCode and normalize-space(apd:PostCode) != '' and apd:PostCode!='AA1 1AA'">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:PostCode)"/>
            </xsl:if>
        </fo:inline>
    </xsl:template>
    <xsl:template name="BCDefendantAddress">
        <fo:inline>
            <xsl:if test="normalize-space(apd:Line[1]) != '' and apd:Line[1] != 'Address Line1'">
                <xsl:value-of select="normalize-space(apd:Line[1])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[2]) != ' ' and apd:Line[2] != 'Address Line2'">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[2])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[3]) != ''">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[3])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[4]) != ''">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[4])"/>
            </xsl:if>
            <xsl:if test="normalize-space(apd:Line[5]) != ''">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:Line[5])"/>
            </xsl:if>
            <!-- AW Daley 01/09/03 Check for default post code added -->
            <xsl:if test="apd:PostCode and normalize-space(apd:PostCode) != '' and apd:PostCode!='AA1 1AA'">
                <xsl:text>, </xsl:text>
                <xsl:value-of select="normalize-space(apd:PostCode)"/>
            </xsl:if>
        </fo:inline>
    </xsl:template>
    <!-- Part 4 -->
    <xsl:template match="nar:BCBCase_Part4">
        <fo:inline>
            <xsl:text>Part 4</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Part 4 Title Line 1 -->
    <xsl:template match="nar:BCBCase_Part4_Title1">
        <fo:inline>
            <xsl:text>The next appearance</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Shall appear -->
    <xsl:template match="nar:BCBCase_ShallAppear">
        <fo:inline>
            <xsl:text> shall appear </xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Bail Order specific court text -->
    <xsl:template match="nar:BCBCase_CourtText">
        <xsl:choose>
            <xsl:when test="$baseAll/ord:NextAppearance/ord:AppearanceCourt/ord:CourtHouseType=$CrownCourt">
                before the <xsl:call-template name="CallableNextAppearanceCourtType"/> sitting at: <xsl:call-template name="CallableNextAppearanceCourtHouseName"/>
            </xsl:when>
            <xsl:otherwise> before: <xsl:call-template name="CallableNextAppearanceCourtHouseName"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Place Notified -->
    <xsl:template match="nar:BCBCase_PlaceNotified">
        <fo:inline>
            <xsl:text> or any other place that may be notified, there to surrender to custody.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Warning to -->
    <xsl:template match="nar:BCBCase_Warning">
        <fo:inline>
            <xsl:text>Warning to the </xsl:text>
            <xsl:call-template name="AppellantDefendant"/>
        </fo:inline>
    </xsl:template>
    <!-- List item1 -->
    <xsl:template match="nar:BCBCase_ListItem1">
        <fo:inline>
            <xsl:text>If you do not surrender to custody you will commit an offence.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- List item2 -->
    <xsl:template match="nar:BCBCase_ListItem2">
        <fo:inline>
            <xsl:text>You must comply with the conditions in Part 2 while you are on bail.</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- In Custody -->
    <xsl:template match="nar:BCBCase_InCustody">
        <fo:inline>
            <xsl:text>If you do not, or if it seems likely that you will not, you may  be remanded in custody.</xsl:text>
        </fo:inline>
    </xsl:template>
    
    <!-- Replacement for order header personal details as these wont be available on B cases -->
	<xsl:template match="nar:BCDefendantFullName">
		<fo:inline>
			<xsl:choose>
				<xsl:when test="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Name/apd:CitizenNameSurname">
					<xsl:value-of select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Name/apd:CitizenNameSurname"/>
					<xsl:text> </xsl:text>
				</xsl:when>
			</xsl:choose>
		</fo:inline>
	</xsl:template>
	<xsl:template match="nar:BCDefendantAddress">
		<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:Address">
			<xsl:call-template name="CallableDefendantAddress"/>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="nar:BCDefendantDOB">
		<fo:inline>
			<xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:DateOfBirth/apd:BirthDate">
				<xsl:call-template name="FormatDate">
					<xsl:with-param name="date" select="$baseAll/ord:OrderHeader/ord:Defendant/ord:PersonalDetails/ord:DateOfBirth/apd:BirthDate"/>
				</xsl:call-template>
			</xsl:for-each>
		</fo:inline>
	</xsl:template>

    <!-- ************************ -->
    <!-- BAIL ORDER END     -->
    <!-- ************************ -->
</xsl:stylesheet>
