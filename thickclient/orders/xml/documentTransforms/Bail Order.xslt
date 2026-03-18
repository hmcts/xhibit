<?xml version="1.0" encoding="UTF-8"?>
<?xmlspysamplexml D:\projects\Prototype\xml\exampleXML\Bail Order.xml?>
<!--  $Revision: 1.9 $  $Author: tzj8k5 $ $Date: 2003/01/24 16:47:15 $-->
<xsl:stylesheet version="1.0" xmlns:bs7666="http://www.govtalk.gov.uk/people/bs7666" xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice" xmlns:nar="http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative"    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>
	
      <xsl:template match="/">
      			<xsl:apply-templates select="/Order/Narrative/Body"/>
      </xsl:template>
      
	<xsl:template match="Body">
		<html>
			<body>
				<table class="body-table">
					<tbody>
				<xsl:apply-templates select="Section|If"/>
				</tbody>
  			  </table>
			</body>
		</html>
	</xsl:template>
	
	<!-- Data Tag Components -->
	
	<xsl:template name="CallableAddress">
		<xsl:for-each select="AddressLine">
			<xsl:value-of select="."></xsl:value-of>,
		</xsl:for-each>
		<xsl:value-of select="PostCode"/>
	</xsl:template>
	
	<xsl:template name="MaleFemale">
		<xsl:choose>
			<xsl:when test="./Sex='male'">him</xsl:when>
			<xsl:when test="./Sex='female'">her</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="HimselfHerself">
		<xsl:choose>
			<xsl:when test="./Sex='male'">himself</xsl:when>
			<xsl:when test="./Sex='female'">herself</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="CourtHouseType">
		<xsl:choose>
			<xsl:when test="./CourtHouseType='magistrates court'">Magistrates Court</xsl:when>
			<xsl:when test="./CourtHouseType='crown court'">Crown Court</xsl:when>
			<xsl:otherwise>unspecified court</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="Term">
		<xsl:value-of select="./Years"/> years <xsl:value-of select="./Months"/> months
	</xsl:template>
	
	<xsl:template name="TermIncDays">
		<xsl:value-of select="./Years"/> years <xsl:value-of select="./Months"/> months <xsl:value-of select="./Days"/> days
	</xsl:template>
	
	<xsl:template name="TermType">
		<xsl:if test=".='concurrent'">
		concurrent
		</xsl:if>
		<xsl:if test=".='consecutive'">
		consecutive
		</xsl:if>
	</xsl:template>
	
	<!-- used to display appropriate defendant or appellant depending on the first letter of the Case Number, used in the Bail Order: if A then appellant otherwise defendant-->
	<xsl:template name="AppellantDefendant">
		<xsl:for-each select="/Order/OrderData/*/OrderHeader">
			<xsl:choose>
				<xsl:when test="substring(CaseNumber,1,1)='A'">
				appellant
				</xsl:when>
				<xsl:otherwise>
				defendant
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>


	<!-- Templates shared by Data Tags -->
	
	<xsl:template match="MonetaryValue">
		<xsl:choose>
			<xsl:when test="Currency='GBP'">&#163;</xsl:when>
			<xsl:when test="Currency='EURO'">&#8364;</xsl:when>
			<xsl:otherwise><xsl:value-of select="Currency"/></xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="Amount"/>
	</xsl:template>
	
	<xsl:template match="AppellantDefendant">
		<xsl:call-template name="AppellantDefendant"/>
	</xsl:template>

	<xsl:template match="Address">
		<xsl:call-template name="CallableAddress"/>
	</xsl:template>
	
	<!-- Data Tags -->
	
	<xsl:template match="OrderDate">
		<xsl:value-of select="/Order/OrderData/*/OrderHeader/OrderDate"/>
	</xsl:template>
	
	<xsl:template match="BailGrantedType">
		<xsl:value-of select="/Order/OrderData/BailOrder/BailDecision"/>
	</xsl:template>
	
	<xsl:template match="Reasons">
		<xsl:value-of select="/Order/OrderData/*/Reasons"/>
	</xsl:template>
	
	<xsl:template match="DefendantFullName">
		<xsl:for-each select="/Order/OrderData/*/OrderHeader/Defendant/PersonalDetails/Name">
					<xsl:value-of select="CitizenNameTitle"/> &#160;
					<xsl:value-of select="CitizenNameForename"/>&#160;
					<xsl:value-of select="CitizenNameSurname"/>&#160;
					<xsl:value-of select="CitizenNameSuffix"/> &#160;
		</xsl:for-each>
	</xsl:template>
	
      <xsl:template match="DefendantAddress">
		<xsl:for-each select="/Order/OrderData/*/OrderHeader/Defendant/PersonalDetails/Address">
				<xsl:call-template name="CallableAddress"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="DefendantDOB">
		<xsl:value-of select="/Order/OrderData/*/OrderHeader/Defendant/PersonalDetails/DateOfBirth/BirthDate"/>
	</xsl:template>
	
	<xsl:template match="NextAppearanceDate">
		<xsl:choose>
			<xsl:when test="/Order/OrderData/*/NextAppearance/AppearanceDateTime/@selected='true'"><xsl:value-of select="/Order/OrderData/*/NextAppearance/AppearanceDateTime/AppearanceDate"/> at <xsl:value-of select="/Order/OrderData/*/NextAppearance/AppearanceDateTime/AppearanceTime"/></xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="text()"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="NextAppearanceCourtType">
		<xsl:for-each select="/Order/OrderData/*/NextAppearance/AppearanceCourt">
			<xsl:call-template name="CourtHouseType"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="NextAppearanceCourtHouseName">
			<xsl:value-of select="/Order/OrderData/*/NextAppearance/AppearanceCourt/CourtHouseName"/>
	</xsl:template>
	
	<xsl:template match="PreConditions">
	
		<!-- Bail Order Pre Conditions -->
		<xsl:if test="/Order/OrderData/BailOrder">
	          <!-- Surety -->	
			<xsl:for-each select="/Order/OrderData/*/PreConditions[@selected='true']">
				<p>(A) To be complied with <b>before</b> release from custody</p>
				<xsl:for-each select="Surety[@selected='true']">
						To provide 
						<xsl:value-of select="Plural"/>
						in the sum of 
						<xsl:apply-templates select="MonetaryValue"/>
						to secure the surrender of the <xsl:call-template name="AppellantDefendant"/>
 to custody at the time and place 
						directed (recognisance(s) of 
						<xsl:value-of select="Plural"/>
						endorsed on Form 5102D: 'Bail: recognisance of a surety')<br/>
				</xsl:for-each>
					
				<xsl:for-each select="Passport[@selected='true' and (Surrendered='true' or Retained='true')]">
					Passport to be
					<xsl:if test="Surrendered='true'"> surrendered to </xsl:if>
					<xsl:if test="Retained = 'true'"><xsl:if test="Surrendered='true'"> and </xsl:if>retained by </xsl:if>
					the police<br/>
				</xsl:for-each>
				
				<xsl:for-each select="Security[@selected='true']">
					To provide a security in the sum of
					<xsl:apply-templates select="MonetaryValue"/>
					to be deposited with the court.
				</xsl:for-each>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
		
	<xsl:template match="PostConditions">
		
		<!-- Bail Order Post Conditions -->
		<xsl:if test="/Order/OrderData/BailOrder">
			<xsl:for-each select="/Order/OrderData/*/PostConditions[@selected='true']">
				<p>(B) To be complied with <b>after</b> release from custody</p>
				<xsl:for-each select="LiveSleep[@selected='true']">
					To live and sleep each night
				<xsl:choose>
					<xsl:when test="AtFollowingAddress='false'">
					at above address</xsl:when>
					<xsl:otherwise>
						at: <xsl:apply-templates select="Address"/>
					</xsl:otherwise>
				</xsl:choose>.<br/>
				</xsl:for-each>
				
				<xsl:if test="NotifyPolice/@selected='true'">To notify police of any change of address.<br/>	</xsl:if>
				<xsl:for-each select="Curfew[@selected='true']">
					To observe the curfew between the hours of 
					<xsl:value-of select="From"/> and <xsl:value-of select="To"/><br/>
				</xsl:for-each>
				
				<xsl:for-each select="PoliceReport[@selected='true']">
					To report to: 
					<xsl:value-of select="Station"/>
					Police Station each: 
					<xsl:value-of select="Period"/>
					between 
					<xsl:value-of select="From"/> and <xsl:value-of select="To"/>.<br/>
				</xsl:for-each>
				
				<xsl:if test="Available/@selected='true'">To be available as and when required to enable 	enquiries or reports to be made.<br/></xsl:if>
				
				<xsl:if test="Contact/@selected='true'">Not to contact directly or indirectly: <xsl:value-of 	select="Contact"/>.<br/></xsl:if>
				
				<xsl:for-each select="Distance[@selected='true']">
					Not to come within <xsl:value-of select="TheDistance"/> of <xsl:value-of 	select="Location"/> except to see a solicitor by prior written appointment.<br/>
				</xsl:for-each>
				<xsl:if test="Other[@selected='true']">Other <i>(please say here):</i><xsl:value-of 	select="Other"/>.</xsl:if>			
			</xsl:for-each>
		</xsl:if>		
	</xsl:template>
	
	<xsl:template match="CustodyLocation">
		<xsl:value-of select="/Order/OrderData/*/CustodyLocation"/>
	</xsl:template>

	<xsl:template match="ReportIndicator">
		<xsl:if test="/Order/OrderData/*/ReportDetails[@selected='true']">
			for a report on <xsl:value-of select="/Order/OrderData/*/ReportDetails"/> 
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="CommittedDate">
		<xsl:value-of select="/Order/OrderData/*/RemandReason/CrownCourt/Date"/> 
	</xsl:template>
	
	<xsl:template match="CommittingCourtHouseName">
		<xsl:value-of select="/Order/OrderData/*/RemandReason/CrownCourt/CourtHouse/CourtHouseName"/> 
	</xsl:template>

	<xsl:template match="CommitSent">
		<xsl:choose>
			<xsl:when test="/Order/OrderData/*/RemandReason/CommitSent='committed'">
			committed
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/RemandReason/CommitSent='sent'">
			sent for trial
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="VoluntaryBillOfIndictment">
		<xsl:for-each select="/Order/OrderData/*/RemandReason/VoluntaryBillOfIndictment[@selected='true']">
			on a Voluntary Bill of Indictment dated <xsl:value-of select="."/><br/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="CertificateOfTransfer">
		<xsl:for-each select="/Order/OrderData/*/RemandReason/CertificateOfTransfer[@selected='true']">
			on a Certificate of Transfer dated <xsl:value-of select="."/><br/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="AppealAgainstBailGranted">
		<xsl:if test="/Order/OrderData/*/RemandReason/AppealAgainstBailGranted[@selected='true']">
			as a result of an appeal by the Prosecution against the grant of bail<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="IndictedConvicted">
		<xsl:if test="/Order/OrderData/*/RemandReason/IndictedConvicted/@selected='true'">
			and has been
			<xsl:choose>
				<xsl:when test="/Order/OrderData/*/RemandType='indicted'">
				indicted for crime.<br/>
				</xsl:when>
				<xsl:when test="/Order/OrderData/*/RemandType='convicted'">
				convicted for crime.<br/>
				</xsl:when>
			</xsl:choose>			
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="AssociatedCases">
		Associated Cases:<br/>
		<xsl:for-each select="/Order/OrderData/*/AssociatedCases/AssociatedCase[@selected='true']">
			<xsl:value-of select="."></xsl:value-of><br/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Forthwith">
		<xsl:if test="/Order/OrderData/*/Release='unconditional'">
			bring
			<xsl:for-each select="/Order/OrderData/*/OrderHeader/Defendant/PersonalDetails">
				<xsl:call-template name="MaleFemale"/>
			</xsl:for-each>
			forthwith before the Crown Court or a Magistrates Court
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="ReleaseOnBail">
		<xsl:if test="/Order/OrderData/*/Release='conditional'">
				release
				<xsl:for-each select="/Order/OrderData/*/OrderHeader/Defendant/PersonalDetails">
					<xsl:call-template name="MaleFemale"/>
				</xsl:for-each> 
				on bail unconditionally (subject to the following conditions(s)): <br/>
				<xsl:if test="/Order/OrderData/*/PreConditions[@selected='true']">
					A. To be complied with <b>before</b> release on bail: to provide
					<xsl:value-of select="/Order/OrderData/*/PreConditions/Surety/Plural"/>
					in the sum of 
					<xsl:for-each select="/Order/OrderData/*/PreConditions/Surety">
						<xsl:apply-templates select="MonetaryValue"/>
					</xsl:for-each>
					to secure the surrender of the defendant to custody at the time and place directed<br/>
				</xsl:if>
				<xsl:if test="/Order/OrderData/*/PreConditions[@selected='true'] and /Order/OrderData/*/PostConditions[@selected='true']">and <br/></xsl:if>
				<xsl:if test="/Order/OrderData/*/PostConditions[@selected='true']">
					B. To to complied with <b>after</b> release on bail: <br/>
					<xsl:value-of select="/Order/OrderData/*/PostConditions/PostConditionDetails"/>
				</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="HimHer">
		<xsl:for-each select="/Order/OrderData/*/OrderHeader/Defendant/PersonalDetails">
			<xsl:call-template name="HimselfHerself"/>
		</xsl:for-each> 
	</xsl:template>

	<xsl:template match="ConvictionDate">
		<xsl:value-of select="/Order/OrderData/*/ConvictionDate"/>
	</xsl:template>

	<xsl:template match="CommittedForSentance">
		<xsl:if test="/Order/OrderData/*/CommittingCourt/@selected ='true'">
			at <xsl:value-of select="/Order/OrderData/*/CommittingCourt/CourtHouseName"/> magistrates court and committed for sentance to the Crown Court.<br/>
		</xsl:if>
	</xsl:template>

	<!-- used for Imprisonment Order -->
	<xsl:template match="ImprisonLife">
		<xsl:choose>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='life'">
				imprisonment for life.		
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='period'">
				<xsl:for-each select="/Order/OrderData/*/CustodialSentence/Term">
					<xsl:call-template name="Term"/> imprisonment
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<!-- used for YOI Order -->
	<xsl:template match="ImprisonmentType">
		<xsl:choose>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='life'">
				for life.		
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='section9394'">
				under the life sentence provisions for young offenders of sections 93 and 94 of the Powers of Criminal Courts (Sentencing) Act of 2000
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='hmpleasure'">
				during His Majesty's pleasure
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='section91'">
				under the long sentence provisions of section 91 of the Powers of Criminal Courts (Sentencing) Act 2000
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/CustodialSentence/ImprisonmentType ='period'">
				<xsl:for-each select="/Order/OrderData/*/CustodialSentence/Term">
					<xsl:call-template name="TermIncDays"/>
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="SentenceOption">
		<xsl:if test="/Order/OrderData/*/CustodialSentence/TermType/@selected='true'">
			This sentance was ordered to be
			<xsl:for-each select="/Order/OrderData/*/CustodialSentence/TermType">
				<xsl:call-template name="TermType"/>
			</xsl:for-each>
			 to any other periods of imprisonment to which the defendant was subject prior to making of this order.
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="YOISentenceOption">
		<xsl:if test="/Order/OrderData/*/CustodialSentence/TermType/@selected='true'">
			This sentance was ordered to be
			<xsl:for-each select="/Order/OrderData/*/CustodialSentence/TermType">
				<xsl:call-template name="TermType"/>
			</xsl:for-each>
			 to any other periods of
			 <xsl:for-each select="/Order/OrderData/*/CustodialSentence/DetentionOrImprisonment">
				<xsl:choose>
					<xsl:when test=".='Detention'">
					detention
					</xsl:when>
					<xsl:when test=".='Imprisonment'">
					imprisonment
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
			 the defendant was subject prior to making of this order.
		</xsl:if>
	</xsl:template>

	
	<xsl:template match="CustodialTerm">
		<xsl:for-each select="/Order/OrderData/*/CustodialSentence/Term">
			<xsl:call-template name="Term"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="ExtendedPeriod">
		<xsl:for-each select="/Order/OrderData/*/CustodialSentence/ExtendedSentence/ExtensionPeriod">
			<xsl:call-template name="Term"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="ExtendedSentenceType">
		<xsl:for-each select="/Order/OrderData/*/CustodialSentence/ExtendedSentence/SentenceType">
			<xsl:call-template name="TermType"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="Section44">
		<xsl:if test="/Order/OrderData/*/CustodialSentence/ExtendedSentence/Section44/@selected='true'">
					<P	>The provisions of section 44 of the Criminal Justice Act 1991, as substituted by section 59 of the Crime and Disorder Act 1998 (as amended by paragraph 141 of Schedule 9 to the Powers of Criminal Courts (Sentencing) Act 2000) apply in this case.</P>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="Qualifier">
		<xsl:choose>
			<xsl:when test="/Order/OrderData/*/ReturnToImprisonment/OffenceDate/@Qualifier='on'">
				on
			</xsl:when>
			<xsl:when test="/Order/OrderData/*/ReturnToImprisonment/OffenceDate/@Qualifier='on or before'">
				no later than
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="OffenceDate">
		<xsl:value-of select="/Order/OrderData/*/ReturnToImprisonment/OffenceDate"/>
	</xsl:template>
	
	<xsl:template match="SentencingCourtType">
		<xsl:for-each select="/Order/OrderData/*/ReturnToImprisonment/SentencingCourt/CourtHouse">
			<xsl:call-template name="CourtHouseType"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="SentencingCourtName">
		<xsl:value-of select="/Order/OrderData/*/ReturnToImprisonment/SentencingCourt/CourtHouse/CourtHouseName"/>
	</xsl:template>
	
	<xsl:template match="SentencingDate">
		<xsl:value-of select="/Order/OrderData/*/ReturnToImprisonment/SentencingCourt/Date"/>
	</xsl:template>

	<xsl:template match="ReturnPeriod">
		<xsl:if test="/Order/OrderData/*/ReturnToImprisonment/ReturnPeriod/Max116='yes'">
		the maximum period specified by section 116.
		</xsl:if>
		<xsl:if test="/Order/OrderData/*/ReturnToImprisonment/ReturnPeriod/Max116='no'">
			<xsl:value-of select="/Order/OrderData/*/ReturnToImprisonment/ReturnPeriod/PeriodInMonths"/> months.
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="ServedPeriod">
		<xsl:if test="/Order/OrderData/*/ReturnToImprisonment/ReturnPeriod/@TermType='before'">
			before
		</xsl:if>	
		<xsl:if test="/Order/OrderData/*/ReturnToImprisonment/ReturnPeriod/@TermType='concurrent'">
			concurrently with
		</xsl:if>	
	</xsl:template>
	
	<xsl:template match="TotalPeriodOfReturn">
		<xsl:for-each select="/Order/OrderData/*/ReturnToImprisonment">
			<xsl:if test="TotalPeriodOfReturn/@selected='true'">
			<P>
			The total of the period of return
			<xsl:if test="TotalPeriodOfReturn/@IncludesNewOffenceTerm='yes'">
				and of any custodial term for a new offence
			</xsl:if>
			is <xsl:value-of select="TotalPeriodOfReturn"/> months and because this total period of imprisonment is 12 months or less, Section 40A of the Criminal Justice Act 1991, as substituted by section 116 of the Powers of Criminal Courts (Sentencing) Act 2000, applies.</P>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="LifePrisonerPeriod">
		<xsl:for-each select="/Order/OrderData/*/Section28/DiscretionaryRelevantPart">
			<xsl:call-template name="Term"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="AdditionalNotes">
		<xsl:value-of select="/Order/OrderData/*/AdditionalNotes"/>
	</xsl:template>
	
	<xsl:template match="DetentionOrImprisonment">
		<xsl:for-each select="/Order/OrderData/*/CustodialSentence/DetentionOrImprisonment">
			<xsl:choose>
				<xsl:when test=".='Detention'">
				detention
				</xsl:when>
				<xsl:when test=".='Imprisonment'">
				imprisonment
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="RtnYOIPrison">
		<xsl:for-each select="/Order/OrderData/*/ReturnToImprisonment">
			<xsl:choose>
				<xsl:when test="ReturnPeriod/@DetentionType='Detention'">
				a young offender institution
				</xsl:when>
				<xsl:when test="ReturnPeriod/@DetentionType='Imprisonment'">
				prison
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	
	<!-- display only if imprisonment selected within the YOI screen -->
	<xsl:template match="ImprisonOption">
		<xsl:if test="/Order/OrderData/*/CustodialSentence/DetentionOrImprisonment='Imprisonment'">
			<P>The Crown Court had, or would have had but for the statutory restrictions upon the imprisonment of young offenders, power to impose imprisonment on the defendant.</P>
		</xsl:if>
	</xsl:template>
	
	<!-- used for addition of two dates -->
	<xsl:template match="AdditionOfDates">
		<!-- years value -->
		<xsl:value-of select="(/Order/OrderData/*/CustodialSentence/Term/Years + /Order/OrderData/*/CustodialSentence/ExtendedSentence/ExtensionPeriod/Years) + floor(((/Order/OrderData/*/CustodialSentence/Term/Months + /Order/OrderData/*/CustodialSentence/ExtendedSentence/ExtensionPeriod/Months) div 12))"/> years

		<!-- months value -->
		<xsl:value-of select="(/Order/OrderData/*/CustodialSentence/Term/Months + /Order/OrderData/*/CustodialSentence/ExtendedSentence/ExtensionPeriod/Months) mod 12"/> months
	</xsl:template>

	<!--Format Tags-->
	
	<xsl:template match="H1|H2|H3|H4|H5|H6|H7|P|TR|TD">
			<xsl:copy>
				<xsl:apply-templates select="*|text()"/>
			</xsl:copy>
	</xsl:template>
	
	<xsl:template match="List">
		<ul class="list">
			<xsl:for-each select="ListItem">
						<li class="list-item"><xsl:apply-templates select="*|text()"/></li>
			</xsl:for-each>
		</ul>
	</xsl:template>
	
	<xsl:template match="Line">
				<xsl:apply-templates select="*|text()"/><br/>	
	</xsl:template>
	
	<xsl:template match="Table">
			<xsl:copy>
				<xsl:apply-templates select="*|text()"/>
			</xsl:copy>
	</xsl:template>
	
	<xsl:template match="Label">
				<xsl:apply-templates select="*|text()"/>
	</xsl:template>

	<xsl:template match="Section/Body">
				<xsl:apply-templates select="*|text()"/>
				<hr class="section-body-hrule"/>
	</xsl:template>
	
	<xsl:template match="If">
		<xsl:choose> 
			
			<xsl:when test="Condition='ConditionalBail' and /Order/OrderData/*/BailDecision = 'conditional'">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='ReportIndicator' and /Order/OrderData/*/ReportDetails[@selected='true']">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='RemandCommitted' and /Order/OrderData/*/RemandReason[@selected='true'] and /Order/OrderData/*/RemandReason/IsBefore[@selected='false']">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='RemandIsBefore' and /Order/OrderData/*/RemandReason[@selected='true'] and /Order/OrderData/*/RemandReason/IsBefore[@selected='true']">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='ChapterIII' and /Order/OrderData/*/ChapterIII='yes'">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='ExtendedSentence' and /Order/OrderData/*/CustodialSentence/ExtendedSentence/@selected='true' and /Order/OrderData/*/CustodialSentence/ImprisonmentType='period'">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='ReturnOfDefendant' and /Order/OrderData/*/ReturnToImprisonment[@selected='true']">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='DiscretionaryLife' and /Order/OrderData/*/Section28[@selected='true'] and /Order/OrderData/*/CustodialSentence/ImprisonmentType='life'">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='Section86' and /Order/OrderData/*/Section86='yes'">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>
			
			<xsl:when test="Condition='AdditionalNotes' and /Order/OrderData/*/AdditionalNotes[@selected='true']">
				<xsl:apply-templates select="Body/* | Body/text()"/>
			</xsl:when>

		</xsl:choose> 
	</xsl:template>

	<xsl:template match="Section">
					<tr class="section-row">
						<td class="section-label"><xsl:apply-templates select="Label"/></td>
						<td class="section-body"><xsl:apply-templates select="Body"/></td>
					</tr>
	</xsl:template>

	<!--<xsl:template match="text()|@*"/>-->
	
</xsl:stylesheet>
