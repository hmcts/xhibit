<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ord="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders" xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:set="http://xml.apache.org/xslt">
    <!-- ********************************** -->
    <!-- COMMUNITY ORDER START -->
    <!-- ********************************** -->
    <xsl:variable name="Officer1">
        <xsl:value-of select="$baseAll/ord:ResponsibleOfficer1"/>
    </xsl:variable>
    <xsl:variable name="Officer2">
        <xsl:value-of select="$baseAll/ord:ResponsibleOfficer2"/>
    </xsl:variable>
    <!-- Template to display CO Title -->
    <xsl:template match="nar:CO_Title">
        <!-- Check to see if this is a revised Community Order -->
        <xsl:if test="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:RevisedOrder='true'">
            <xsl:text>Change of </xsl:text>
        </xsl:if>
        <xsl:text>Community Order</xsl:text>
    </xsl:template>
    <!-- Template to display CO Revision Text -->
    <xsl:template match="nar:CO_Revision">
        <!-- Check to see if this is a revised Community Order -->
        <xsl:if test="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:RevisedOrder='true'">
            <xsl:text>This order has been revised on </xsl:text>
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$OrderDate"/>
            </xsl:call-template>
            <xsl:text> and replaces the previous order made on  </xsl:text>
            <xsl:call-template name="FormatDate">
                <xsl:with-param name="date" select="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:PreviousOrderDate"/>
            </xsl:call-template>
            <fo:block space-before="10pt">
                <xsl:text>Revision number: </xsl:text>
                <xsl:value-of select="$baseAll/ord:OrderHeader/ord:RevisionDetails/ord:RevisionNo"/>
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- Order title -->
    <xsl:template match="nar:CO_OrderTitle">
        <fo:inline>
            <xsl:text>Order</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display CO Order Text -->
    <xsl:template match="nar:CO_Order">
        <xsl:text>The court makes a community order containing the requirements shown below.  You must have complied with all the requirements by </xsl:text>
        <xsl:call-template name="FormatDate">
            <xsl:with-param name="date" select="$baseAll/ord:CompletionDate"/>
        </xsl:call-template>
        <xsl:text>. This is the &apos;end date&apos; for the order when the order will stop being in force. However if you must comply with an &apos;unpaid work requirement&apos; and that is the only requirement that remains for you to comply with, the order remains in force until you have complied with it.</xsl:text>
        <fo:block space-after="10pt"/>
            You must
            <fo:list-block provisional-distance-between-starts="5mm">
            <xsl:call-template name="bullet">
                <xsl:with-param name="text">
                    <xsl:text>keep in touch with the </xsl:text>
                    <xsl:value-of select="$Officer1"/>
                    <xsl:text> as your </xsl:text>
                    <xsl:value-of select="$Officer2"/>
                    <xsl:text> tells you</xsl:text>
                </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="bullet">
                <xsl:with-param name="text">
                    <xsl:text>tell your </xsl:text>
                    <xsl:value-of select="$Officer2"/>
                    <xsl:text> if you intend to change your address</xsl:text>
                </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="bullet">
                <xsl:with-param name="text">
                    comply with the following requirements
                </xsl:with-param>
            </xsl:call-template>
        </fo:list-block>
    </xsl:template>
    <!-- Requirements title -->
    <xsl:template match="nar:CO_RequirementTitle">
        <fo:inline>
            <xsl:text>Requirements</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display CO Requirements text -->
    <xsl:template match="nar:CO_Requirement">
        <!-- Section A -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>carry out unpaid work for </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Hours"/>
                            <xsl:text> hours </xsl:text>
                            <xsl:choose>
                                <xsl:when test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent='no'">
                                    <xsl:text> consecutive to </xsl:text>
                                </xsl:when>
                                <xsl:when test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent='yes'">
                                    <xsl:text> concurrent to </xsl:text>
                                </xsl:when>
                                <xsl:when test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent='none'">
                                    <!-- Display nothing -->
                                </xsl:when>
                            </xsl:choose>
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:Concurrent != 'none'">
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:WorkDetails"/>
                            </xsl:if>
                            <xsl:text> as you are told by </xsl:text>
                            <xsl:call-template name="FormatDate">
                                <xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:WorkEndDate"/>
                            </xsl:call-template>
                            <xsl:text>. Your </xsl:text>
                            <xsl:value-of select="$Officer2"/>
                            <xsl:text> will supervise this work. </xsl:text>
                        </fo:block>
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:AdditonalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:UnpaidWorkRequirement/ord:AdditonalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section B -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/@selected='true'">
            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true' or $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true' or $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:AdditonalRequirements/@selected='true'">
                <fo:list-block provisional-distance-between-starts="5mm">
                    <fo:list-item space-after="1em">
                        <fo:list-item-label>
                            <fo:block font-weight="bold">
                                <xsl:text>&#x2219;</xsl:text>
                            </fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                            <fo:block>
                                <!-- Option 1 -->
                                <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true'">
                                    <xsl:text>present yourself to </xsl:text>
                                    <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/ord:Person"/>
                                    <xsl:text> at </xsl:text>
                                    <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/ord:Place/ord:Site"/>
                                </xsl:if>
                                <!-- and -->
                                <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true' and $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true'">
                                    <xsl:text> and </xsl:text>
                                </xsl:if>
                                <!-- Option 2 -->
                                <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true'">
                                    <xsl:text>undertake </xsl:text>
                                    <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/ord:Activity"/>
                                    <xsl:text> for </xsl:text>
                                    <xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/ord:ActivityPeriod">
                                        <!-- Display duration details -->
                                        <xsl:call-template name="TermMonthsDays"/>
                                    </xsl:for-each>
                                    <xsl:text> in the way you are told by your </xsl:text>
                                    <xsl:value-of select="$Officer2"/>
                                    <xsl:text>.</xsl:text>
                                </xsl:if>
                            </fo:block>
                            <!-- Additional Req -->
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:AdditonalRequirements/@selected='true'">
                                <xsl:if test="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:PresentDetails/@selected='true' or $baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:ActivityDetails/@selected='true'">
                                    <xsl:text>.  </xsl:text>
                                </xsl:if>
                                <xsl:call-template name="FormatBulletTextArea">
                                    <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ActivityRequirement/ord:AdditonalRequirements/ord:AdditionalInformation"/>
                                </xsl:call-template>
                            </xsl:if>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
            </xsl:if>
        </xsl:if>
        <!-- Section C -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>participate in </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:Programme"/>
                            <xsl:text> at </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:Place/ord:Site"/>
                            <xsl:text> for </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:Days"/>
                            <xsl:text> days.  </xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ProgrammeRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section D -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>not take part in </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:Activity"/>
                            <xsl:text> for </xsl:text>
                            <xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:ActivityPeriod">
                                <!-- Display duration details -->
                                <xsl:call-template name="TermIncDays"/>
                            </xsl:for-each>
                            <xsl:text>.  </xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ProhibitedActivityRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section E -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewOption='yes'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <xsl:call-template name="bullet">
                    <xsl:with-param name="text">
                        <xsl:text>be under a curfew - remain in place or places so specified during periods specified by the court. This curfew lasts for </xsl:text>
                        <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewDuration/ord:Duration"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:CurfewRequirement/ord:CurfewDuration/ord:Period"/>
                        <xsl:text>. See separate sheet for details</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
            </fo:list-block>
        </xsl:if>
        <!-- Section F -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>not enter </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:Place/ord:Site"/>
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:BetweenPeriod/@selected='true'">
                                <xsl:text> between </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:BetweenPeriod/ord:ApplicablePeriod"/>
                            </xsl:if>
                            <xsl:text>.  </xsl:text>
                            <xsl:text>This exclusion requirement lasts for </xsl:text>
                            <xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:ExclusionPeriod">
                                <!-- Display duration details -->
                                <xsl:call-template name="TermIncDays"/>
                            </xsl:for-each>
                            <xsl:text>.  </xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ExclusionRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section G -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>live at </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:Hostel/ord:Site"/>
                            <xsl:text> and obey its rules for </xsl:text>
                            <xsl:for-each select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:ObeyRules">
                                <!-- Display duration details -->
                                <xsl:call-template name="TermIncDays"/>
                            </xsl:for-each>
                            <xsl:text>.  </xsl:text>
                            <!-- Another Place -->
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:PlaceOption/@selected='true'">
                                <xsl:text>You may live at </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:PlaceOption/ord:Place/ord:Site"/>
                                <xsl:text> with the prior approval of your </xsl:text>
                                <xsl:value-of select="$Officer2"/>
                                <xsl:text>.  </xsl:text>
                            </xsl:if>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:ResidenceRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section H -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>have mental health treatment by or under the direction of a </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentDirector"/>
                            <!-- Clinic -->
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentOption/@selected='true'">
                                <xsl:text> at </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentOption/ord:TreatmentLocation/ord:Site"/>
                                <xsl:text> as a </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentOption/ord:ResidenceStatus"/>
                                <xsl:text> patient </xsl:text>
                            </xsl:if>
                            <xsl:text> for </xsl:text>
                            <xsl:for-each select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:TreatmentPeriod">
                                <!-- Display duration details -->
                                <xsl:call-template name="TermIncDays"/>
                            </xsl:for-each>
                            <xsl:text>.</xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:MentalHealthTreatmentRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section I -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>have treatment for drug dependency by or under the direction of  </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentDirector"/>
                            <!-- Clinic -->
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentOption/@selected='true'">
                                <xsl:text> at </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentOption/ord:TreatmentLocation/ord:Site"/>
                                <xsl:text> as a </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentOption/ord:ResidenceStatus"/>
                            </xsl:if>
                            <xsl:text> for </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:TreatmentPeriod/ord:Months"/>
                            <xsl:text> month(s).  </xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:SampleOption/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <xsl:call-template name="no_bullet">
                    <xsl:with-param name="text">
                        <xsl:text>To be sure that you do not have any drug in your body, you must provide samples at such times or in such circumstances as your </xsl:text>
                        <xsl:value-of select="$Officer2"/>
                        <xsl:text> or the person responsible for your treatment will tell you.  The results of tests on the samples will be sent to your </xsl:text>
                        <xsl:value-of select="$Officer2"/>
                        <xsl:text> who will report the results to the court.  Your </xsl:text>
                        <xsl:value-of select="$Officer2"/>
                        <xsl:text> will also tell the court how your order is progressing and the views of your treatment provider.</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <xsl:call-template name="no_bullet">
                    <xsl:with-param name="text">
                        <xsl:text>The court will review this order </xsl:text>
                        <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewDetails"/>
                        <xsl:text>.  The first review will be on </xsl:text>
                        <xsl:call-template name="FormatDate">
                            <xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewDate"/>
                        </xsl:call-template>
                        <xsl:text> at </xsl:text>
                        <xsl:call-template name="FormatTime">
                            <xsl:with-param name="time" select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewTime"/>
                        </xsl:call-template>
                        <xsl:text> at </xsl:text>
                        <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseName"/>
                        <xsl:text>, </xsl:text>
                        <xsl:for-each select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseAddress">
                            <xsl:call-template name="CallableAddress_Comm_Order"/>
                        </xsl:for-each>
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:TelephoneOption/@selected='true'">
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseTelephone != '-'">
                                <xsl:text> (telephone </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseTelephone"/>
                                <xsl:text>).  </xsl:text>
                            </xsl:if>
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:ReviewCourt/ord:CourtHouseTelephone = '-'">
                                <xsl:text>).  </xsl:text>
                            </xsl:if>
                        </xsl:if>
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:TelephoneOption/@selected='false'">
                            <xsl:text>.  </xsl:text>
                        </xsl:if>
                        <xsl:text>You</xsl:text>
                        <xsl:choose>
                            <xsl:when test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:Attendance = 'yes'">
                                <xsl:text> must attend the hearing.</xsl:text>
                            </xsl:when>
                            <xsl:when test="$baseAll/ord:OrderRequirements/ord:DrugRehabilitationRequirement/ord:ReviewOption/ord:Attendance = 'no'">
                                <xsl:text> need not attend the hearing.</xsl:text>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </fo:list-block>
        </xsl:if>
        <!-- Section J -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>have treatment for alcohol dependency by or under the direction of </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentDirector"/>
                            <!-- Clinic -->
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentOption/@selected='true'">
                                <xsl:text> at </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentOption/ord:TreatmentLocation/ord:Site"/>
                                <xsl:text> as a </xsl:text>
                                <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentOption/ord:ResidenceStatus"/>
                            </xsl:if>
                            <xsl:text> for </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:TreatmentPeriod/ord:Months"/>
                            <xsl:text> month(s).  </xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:AlcoholTreatmentRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section K -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:SupervisionRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>attend appointments with your </xsl:text>
                            <xsl:value-of select="$Officer2"/>
                            <xsl:text> or another person at the times and places your </xsl:text>
                            <xsl:value-of select="$Officer2"/>
                            <xsl:text> says.  </xsl:text>
                        </fo:block>
                        <!-- Additional Req -->
                        <xsl:if test="$baseAll/ord:OrderRequirements/ord:SupervisionRequirement/@selected='true' and $baseAll/ord:OrderRequirements/ord:SupervisionRequirement/ord:AdditionalRequirements/@selected='true'">
                            <xsl:call-template name="FormatBulletTextArea">
                                <xsl:with-param name="string" select="$baseAll/ord:OrderRequirements/ord:SupervisionRequirement/ord:AdditionalRequirements/ord:AdditionalInformation"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section L -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:AttendanceCentreRequirement/ord:AttendanceOption='yes'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <xsl:call-template name="bullet">
                    <xsl:with-param name="text">
                        <xsl:text>attend an attendance centre - see separate sheet for details</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
            </fo:list-block>
        </xsl:if>
        <!-- Section M -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:Days!=''">
								<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:Days!=' '">
									<xsl:text> on </xsl:text>
									<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:Days"/>
									<xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/@selected='true'">
										<xsl:text> or</xsl:text>
									</xsl:if>
								</xsl:if>
                            </xsl:if>
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/@selected='true'">
								<xsl:text> from </xsl:text>
								<xsl:call-template name="FormatDate">
									<xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/ord:FromDate"/>
								</xsl:call-template>
								<xsl:text> to </xsl:text>
								<xsl:call-template name="FormatDate">
									<xsl:with-param name="date" select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:FromToOption/ord:ToDate"/>
								</xsl:call-template>
							</xsl:if>
                            <xsl:text> you are not to travel to </xsl:text>
                            <xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:ProhibitedFrom"/>
                            <xsl:if test="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:ExceptionOption/@selected='true'">
								<xsl:text> other than </xsl:text>
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:ForeignTravelProhibitionRequirement/ord:ExceptionOption/ord:Exception"/>
							</xsl:if>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section N -->
        <xsl:if test="$baseAll/ord:OrderRequirements/ord:RehabilitationActivityRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>participate in Rehabilitation Activity Requirement(s) as instructed for a maximum of </xsl:text>
							<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:RehabilitationActivityRequirement/ord:Days"/>
							<xsl:text> days</xsl:text>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
        <!-- Section O -->
		 <xsl:if test="$baseAll/ord:OrderRequirements/ord:AlcoholAbstinenceAndMonitoringRequirement/@selected='true'">
            <fo:list-block provisional-distance-between-starts="5mm">
                <fo:list-item space-after="1em">
                    <fo:list-item-label>
                        <fo:block font-weight="bold">
                            <xsl:text>&#x2219;</xsl:text>
                        </fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()" end-indent="5mm">
                        <fo:block>
                            <xsl:text>Abstain from alcohol and be monitored for </xsl:text>
							<xsl:variable name="alcDays">
								<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:AlcoholAbstinenceAndMonitoringRequirement/ord:Days"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$alcDays != ''">
									<xsl:value-of select="$alcDays"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>0</xsl:text>
								</xsl:otherwise>
							</xsl:choose>		
							<xsl:text> days.</xsl:text>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </xsl:if>
	<!-- Section P -->
        <xsl:if test="$baseAll/ord:ElectronicMonitoringRequirement/@selected='true'">
            <fo:block space-before="10pt" space-after="10pt">
                <xsl:text>You will be electronically monitored so that the court can be sure you are complying with the requirements of this order.</xsl:text>
            </fo:block>
        </xsl:if>
        <!-- Section Q -->
		<xsl:if test="$baseAll/ord:OrderRequirements/ord:TrailMonitoringRequirement/@selected='true'">
			<fo:block space-before="10pt" space-after="10pt">
				<xsl:text>You will be electronically Trail monitored for </xsl:text>
				<xsl:choose>
					<xsl:when test="$baseAll/ord:OrderRequirements/ord:TrailMonitoringRequirement/ord:Duration != ''">
						<xsl:value-of select="$baseAll/ord:OrderRequirements/ord:TrailMonitoringRequirement/ord:Duration"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>0</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text> days so that the court can be sure you are complying with the requirements of this order.</xsl:text>
			</fo:block>
		</xsl:if>
        <!-- Petty Sessional Area -->
        <fo:block space-after="10pt">
            <xsl:text>The local justice area you live in is </xsl:text>
            <xsl:value-of select="$baseAll/ord:PettySessionalArea/ord:CourtHouseName"/>
            <xsl:text>.</xsl:text>
        </fo:block>
    </xsl:template>
    <!-- Warning Title -->
    <xsl:template match="nar:CO_WarningTitle">
        <fo:inline>
            <xsl:text>Warning</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display CO Warning text -->
    <xsl:template match="nar:CO_Warning">
        <fo:block>
            <xsl:text>If you do not comply with this order, you will be brought back to court.  The court may then</xsl:text>
            <fo:list-block provisional-distance-between-starts="5mm">
                <xsl:call-template name="bullet">
                    <xsl:with-param name="text">
                        <xsl:text>change the order by adding extra requirements</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
            </fo:list-block>
            <fo:list-block provisional-distance-between-starts="5mm">
                <xsl:call-template name="bullet">
                    <xsl:with-param name="text">
                        <xsl:text>pass a different sentence for the original offences.  You could be sent to </xsl:text>
                        <xsl:choose>
                            <xsl:when test="$baseAll/ord:WarningDetentionType='Detention'">
                                <xsl:text>detention.</xsl:text>
                            </xsl:when>
                            <xsl:when test="$baseAll/ord:WarningDetentionType='Imprisonment'">
                                <xsl:text>prison.</xsl:text>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </fo:list-block>
        </fo:block>
    </xsl:template>
    <!-- Note title -->
    <xsl:template match="nar:CO_NoteTitle">
        <fo:inline>
            <xsl:text>Note</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display CO Notes text -->
    <xsl:template match="nar:CO_Note">
        <xsl:text>Either you or your </xsl:text>
        <xsl:value-of select="$Officer2"/>
        <xsl:text> can ask the court to look again at this order and the court can then change it or cancel it if it feels that is the right thing to do.  It can also pass a different sentence for the original offences.  If you wish to do this you should get in touch with the court at the address above.</xsl:text>
    </xsl:template>
    <!-- Template to display CO Signed text -->
    <xsl:template match="nar:CO_Signed">
        <!-- call template to display signed info -->
        <xsl:call-template name="SignedInfo"/>
    </xsl:template>
    <!-- Offence title -->
    <xsl:template match="nar:CO_OffencesTitle">
        <fo:inline>
            <xsl:text>Offences</xsl:text>
        </fo:inline>
    </xsl:template>
    <!-- Template to display CO Offences text -->
    <xsl:template match="nar:CO_Offences">
        <xsl:choose>
            <xsl:when test="count($baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge/ord:OffenceStatement)=1">
                <xsl:call-template name="FormatBulletTextArea">
                    <xsl:with-param name="string" select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge[1]/ord:OffenceStatement"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$baseAll/ord:OrderHeader/ord:Defendant/ord:Charges/ord:Charge">
                    <xsl:if test="position()!=1">
                        <xsl:if test="ord:OffenceStatement != ''">
                            <xsl:value-of select="ord:CaseNumber"/>
                            <xsl:text> / </xsl:text>
                            <xsl:value-of select="ord:OffenceStatement"/>
                            <fo:block/>
                        </xsl:if>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
        <fo:block/>
        <!-- Check to if there are any linked offences for the same defendant to display -->
        <xsl:if test="$baseAll/ord:LinkedOffences">
            <fo:block>
                <xsl:for-each select="$baseAll/ord:LinkedOffences/ord:LinkedOffence">
                    <xsl:value-of select="ord:LinkedCaseNumber"/>
                    <xsl:text> / </xsl:text>
                    <xsl:value-of select="ord:LinkedOffenceStatement"/>
                    <fo:block/>
                </xsl:for-each>
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- Additional Req title-->
	<xsl:template match="nar:CO_AddReq_Title">
		<fo:inline>
			<xsl:text>Additional Notes</xsl:text>
		</fo:inline>
	</xsl:template>
	<!-- Template to display Additional Notes details -->
	<xsl:template match="nar:CO_AdditionalNotes">
		<xsl:call-template name="FormatTextArea">
			<xsl:with-param name="string" select="$baseAll/ord:AdditionalNotes"/>
		</xsl:call-template>
	</xsl:template>
    <!-- ********************************** -->
    <!-- COMMUNITY ORDER END -->
    <!-- ********************************** -->
</xsl:stylesheet>