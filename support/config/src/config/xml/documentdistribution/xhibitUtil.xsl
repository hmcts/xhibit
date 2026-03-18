<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:cs="http://www.courtservice.gov.uk/schemas/courtservice"
      xmlns:apd="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
      xmlns:dt="http://xsltsl.org/date-time"
      elementFormDefault="qualified" attributeFormDefault="unqualified"
      version="1.2">
  <xsl:import href="../xsltsl/date-time.xsl"/>
  <xsl:output method="html" indent="yes"/>

<!--
CHANGE HISTORY
Changes 1.0 to 1.1:
  CourtHouseSittings moved into CourtLists/CourtList
Changes 1.1 to 1.2:
  Transform CourtLists/CourtList@HearingType from values such as "TRL" to "For Trial"
-->

  <xsl:template name="displayDayDate">
    <xsl:param name="input"/>

    <xsl:variable name="month" select="substring($input,6,2)"/>
    <xsl:variable name="day" select="substring($input,9,2)"/>
    <xsl:variable name="year" select="substring($input,1,4)"/>


    <xsl:variable name="dayOfWeek">
      <xsl:call-template name="dt:calculate-day-of-the-week">
        <xsl:with-param name="year" select="$year"/>
        <xsl:with-param name="month" select="$month"/>
        <xsl:with-param name="day" select="$day"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="$dayOfWeek=0">Sunday</xsl:when>
      <xsl:when test="$dayOfWeek=0">Monday</xsl:when>
      <xsl:when test="$dayOfWeek=0">Tuesday</xsl:when>
      <xsl:when test="$dayOfWeek=0">Wednesday</xsl:when>
      <xsl:when test="$dayOfWeek=0">Thursday</xsl:when>
      <xsl:when test="$dayOfWeek=5">Friday</xsl:when>
      <xsl:when test="$dayOfWeek=0">Saturday</xsl:when>
    </xsl:choose>

    <xsl:text> </xsl:text>
    <xsl:call-template name="displayDate">
      <xsl:with-param name="input"><xsl:value-of select="$input"/></xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="displayDate">
    <xsl:param name="input"/>

    <xsl:variable name="month" select="substring($input,6,2)"/>
    <xsl:variable name="day" select="substring($input,9,2)"/>
    <xsl:variable name="year" select="substring($input,1,4)"/>

    <xsl:value-of select="$day"/>
    <xsl:text> </xsl:text>

    <xsl:choose>
      <xsl:when test="$month='01'">January</xsl:when>
      <xsl:when test="$month='02'">February</xsl:when>
      <xsl:when test="$month='03'">March</xsl:when>
      <xsl:when test="$month='04'">April</xsl:when>
      <xsl:when test="$month='05'">May</xsl:when>
      <xsl:when test="$month='06'">June</xsl:when>
      <xsl:when test="$month='07'">July</xsl:when>
      <xsl:when test="$month='08'">August</xsl:when>
      <xsl:when test="$month='09'">September</xsl:when>
      <xsl:when test="$month='10'">October</xsl:when>
      <xsl:when test="$month='11'">November</xsl:when>
      <xsl:when test="$month='12'">December</xsl:when>
    </xsl:choose>
    <xsl:text> </xsl:text>
    <xsl:value-of select="$year"/>
  </xsl:template>

  <xsl:template name="DecodeHearingType">
    <xsl:param name="HearingTypeCode"/>
    <xsl:param name="CaseType"/>

    <xsl:choose>
      <xsl:when test="$HearingTypeCode='ABG'">For Appeal under the Betting, Gaming and Lotteries Act 1963</xsl:when>
      <xsl:when test="$HearingTypeCode='ACF'">For Appeal in respect of Firearms Certificate</xsl:when>
      <xsl:when test="$HearingTypeCode='ACN'">For Appeal against Conviction</xsl:when>
      <xsl:when test="$HearingTypeCode='ACO'">For Appeal against Compensation Order</xsl:when>
      <xsl:when test="$HearingTypeCode='ACS'">For Appeal against Conviction and Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='ACY'">For Appeal under the Children and Young Persons Act 1969</xsl:when>
      <xsl:when test="$HearingTypeCode='ADJ'">For Appeal against District Judge's Order</xsl:when>
      <xsl:when test="$HearingTypeCode='AEH'">Admissibility of Evidence - Half day</xsl:when>
      <xsl:when test="$HearingTypeCode='AEW'">
        <xsl:choose>
          <xsl:when test="$CaseType='C'">Admissibility of Evidence - full day</xsl:when>
          <xsl:otherwise>Admissibility of Evidence - Whole Day</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$HearingTypeCode='AGA'">For Appeal under the Gaming Act 1968</xsl:when>
      <xsl:when test="$HearingTypeCode='AHA'">For Appeal under the Housing Act 1957</xsl:when>
      <xsl:when test="$HearingTypeCode='AHC'">For Appeal in respect of Hackney Carriage Licence</xsl:when>
      <xsl:when test="$HearingTypeCode='ALA'">For Appeal under the Lotteries and Amusements Act 1976</xsl:when>
      <xsl:when test="$HearingTypeCode='ALD'">For Appeal under the Land Drainage Act 1976</xsl:when>
      <xsl:when test="$HearingTypeCode='ALJ'">For Appeal against Licensing Justices Decision</xsl:when>
      <xsl:when test="$HearingTypeCode='APH'">Appeal (Part Heard)</xsl:when>
      <xsl:when test="$HearingTypeCode='APL'">For Appeal</xsl:when>
      <xsl:when test="$HearingTypeCode='APN'">For Application</xsl:when>
      <xsl:when test="$HearingTypeCode='ASE'">For Appeal against Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='BAC'">Breach of Attendance Centre Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BBO'">Breach of Bind Over</xsl:when>
      <xsl:when test="$HearingTypeCode='BCD'">Breach of Conditional Discharge</xsl:when>
      <xsl:when test="$HearingTypeCode='BCF'">Breach of Requirements of Curfew Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BCP'">Breach of Requirements of both Community Service Order &amp; Probation Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BCS'">Breach of Requirements of Community Service Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BEX'">Breach of Exclusion Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BFA'">For Application to Break Fixture</xsl:when>
      <xsl:when test="$HearingTypeCode='BLA'">For Application for Bail</xsl:when>
      <xsl:when test="$HearingTypeCode='BOO'">Breach of Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BPS'">Breach of Partly Suspended Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='BRG'">For Application to Rescind Grant of Bail</xsl:when>
      <xsl:when test="$HearingTypeCode='BRP'">Breach of Requirements of Probation Order</xsl:when>
      <xsl:when test="$HearingTypeCode='BSS'">Breach of Suspended Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='BVA'">For Application to Vary Bail Conditions</xsl:when>
      <xsl:when test="$HearingTypeCode='CBR'">Committal for Breaches</xsl:when>
      <xsl:when test="$HearingTypeCode='CCT'">Contempt of Court Proceedings</xsl:when>
      <xsl:when test="$HearingTypeCode='CMA'">For Application to Commit</xsl:when>
      <xsl:when test="$HearingTypeCode='CON'">Continued</xsl:when>
      <xsl:when test="$HearingTypeCode='CPT'">For Contempt</xsl:when>
      <xsl:when test="$HearingTypeCode='CSE'">Committal for Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='CSP'">Committal for Sentence (Part Heard)</xsl:when>
      <xsl:when test="$HearingTypeCode='CSS'">Committals for Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='CTA'">For Application to Extend Custody Time Limit</xsl:when>
      <xsl:when test="$HearingTypeCode='CTL'">Custody Time Limit application</xsl:when>
      <xsl:when test="$HearingTypeCode='DCA'">For Application for Dismissal of Charges</xsl:when>
      <xsl:when test="$HearingTypeCode='DIR'">For Directions</xsl:when>
      <xsl:when test="$HearingTypeCode='DPR'">For Deferred Sentence (Prosecution Released)</xsl:when>
      <xsl:when test="$HearingTypeCode='DRA'">For Application under the Drug Trafficking Act 1994</xsl:when>
      <xsl:when test="$HearingTypeCode='DRR'">For Deferred Sentence (Respondent Released)</xsl:when>
      <xsl:when test="$HearingTypeCode='DRS'">For Application under the Drug Trafficking Act 1994, and Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='DSE'">For Deferred Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='DTA'">For Application under the Drug Trafficking Offences Act 1986</xsl:when>
      <xsl:when test="$HearingTypeCode='DTS'">For Application under Drug Trafficking Offences Act 1986, and Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='EBW'">For Execution of Bench Warrant</xsl:when>
      <xsl:when test="$HearingTypeCode='FHA'">For Application to Fix Hearing Date</xsl:when>
      <xsl:when test="$HearingTypeCode='HRG'">For Hearing</xsl:when>
      <xsl:when test="$HearingTypeCode='INJ'">For Application for Injunction</xsl:when>
      <xsl:when test="$HearingTypeCode='JGT'">For Judgment</xsl:when>
      <xsl:when test="$HearingTypeCode='LAA'">For Application to Add to List</xsl:when>
      <xsl:when test="$HearingTypeCode='MAA'">For Mention (Appellant to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='MAP'">For Mention (All Parties to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='MBO'">For Motion By Order</xsl:when>
      <xsl:when test="$HearingTypeCode='MDA'">For Mention (Defendant to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='MEF'">For Mention and to Fix</xsl:when>
      <xsl:when test="$HearingTypeCode='MEN'">For Mention</xsl:when>
      <xsl:when test="$HearingTypeCode='MNW'">For Mention (No Witnesses Required)</xsl:when>
      <xsl:when test="$HearingTypeCode='MOA'">For Mention (Officer to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='PAD'">For Plea and Directions</xsl:when>
      <xsl:when test="$HearingTypeCode='PAF'">For Plea and to Fix</xsl:when>
      <xsl:when test="$HearingTypeCode='PCA'">For Application under the Police and Criminal Evidence Act</xsl:when>
      <xsl:when test="$HearingTypeCode='PHD'">Part Heard</xsl:when>
      <xsl:when test="$HearingTypeCode='PLE'">For Plea</xsl:when>
      <xsl:when test="$HearingTypeCode='PLY'">For Preliminary Hearing</xsl:when>
      <xsl:when test="$HearingTypeCode='PPH'">Plea (Part Heard)</xsl:when>
      <xsl:when test="$HearingTypeCode='PPT'">For Plea/Pre-Trial Review</xsl:when>
      <xsl:when test="$HearingTypeCode='PRA'">For Application under the Proceeds of Crime Act 1995</xsl:when>
      <xsl:when test="$HearingTypeCode='PRY'">For Preparatory Hearing</xsl:when>
      <xsl:when test="$HearingTypeCode='PTN'">For Plea to be Taken</xsl:when>
      <xsl:when test="$HearingTypeCode='PTR'">For Pre-Trial Review</xsl:when>
      <xsl:when test="$HearingTypeCode='RBA'">For Application to Revoke both Community Service Order &amp; Probation Order</xsl:when>
      <xsl:when test="$HearingTypeCode='RCA'">For Application to Revoke Community Service Order</xsl:when>
      <xsl:when test="$HearingTypeCode='RCF'">For Application to Revoke Curfew Order</xsl:when>
      <xsl:when test="$HearingTypeCode='RDA'">For Application to Remove Driving Disqualification</xsl:when>
      <xsl:when test="$HearingTypeCode='REP'">For Plea and Reparation</xsl:when>
      <xsl:when test="$HearingTypeCode='REV'">For Review</xsl:when>
      <xsl:when test="$HearingTypeCode='RIN'">For Return Injunction</xsl:when>
      <xsl:when test="$HearingTypeCode='RJT'">For Reserved Judgment</xsl:when>
      <xsl:when test="$HearingTypeCode='RLA'">For Application to Remove from List</xsl:when>
      <xsl:when test="$HearingTypeCode='RPA'">For Application to Revoke Probation Order</xsl:when>
      <xsl:when test="$HearingTypeCode='RVA'">For Application for Review</xsl:when>
      <xsl:when test="$HearingTypeCode='SAC'">For Sentence (at another Court)</xsl:when>
      <xsl:when test="$HearingTypeCode='SCE'">To Show Cause</xsl:when>
      <xsl:when test="$HearingTypeCode='SCJ'">For Juror to Show Cause</xsl:when>
      <xsl:when test="$HearingTypeCode='SCS'">For Solicitor to Show Cause</xsl:when>
      <xsl:when test="$HearingTypeCode='SCW'">For Witness to Show Cause</xsl:when>
      <xsl:when test="$HearingTypeCode='SCY'">For Surety to Show Cause</xsl:when>
      <xsl:when test="$HearingTypeCode='SDA'">For Application to Suspend Driving Disqualification</xsl:when>
      <xsl:when test="$HearingTypeCode='SEN'">For Sentence</xsl:when>
      <xsl:when test="$HearingTypeCode='SOA'">For Sentence (Officer to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='SPA'">For Sentence (Prosecution to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='SPO'">For Sentence (Prosecution and Officer to Attend)</xsl:when>
      <xsl:when test="$HearingTypeCode='SPR'">For Sentence (Prosecution Released)</xsl:when>
      <xsl:when test="$HearingTypeCode='SUB'">For Surrender to Bail</xsl:when>
      <xsl:when test="$HearingTypeCode='TBK'">For Trial (Backer)</xsl:when>
      <xsl:when test="$HearingTypeCode='TFL'">For Trial (Floater)</xsl:when>
      <xsl:when test="$HearingTypeCode='TFW'">For Trial (First Warning)</xsl:when>
      <xsl:when test="$HearingTypeCode='TIS'">For Trial of Issue</xsl:when>
      <xsl:when test="$HearingTypeCode='TNW'">For Trial (No Witnesses)</xsl:when>
      <xsl:when test="$HearingTypeCode='TPH'">Trial (Part Heard)</xsl:when>
      <xsl:when test="$HearingTypeCode='TPI'">For Trial of Preliminary Issue</xsl:when>
      <xsl:when test="$HearingTypeCode='TPR'">For Trial (Priority)</xsl:when>
      <xsl:when test="$HearingTypeCode='TPW'">For Trial (Previously Warned)</xsl:when>
      <xsl:when test="$HearingTypeCode='TRA'">For Application to Terminate Restriction Order</xsl:when>
      <xsl:when test="$HearingTypeCode='TRE'">For Trial (Reserve)</xsl:when>
      <xsl:when test="$HearingTypeCode='TRL'">For Trial</xsl:when>
      <xsl:when test="$HearingTypeCode='TWC'">For Trial (Fixed for this Week)</xsl:when>
      <xsl:when test="$HearingTypeCode='XPA'">For Application (Ex Parte)</xsl:when>
      <xsl:otherwise><xsl:value-of select="$HearingTypeCode"/></xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="cs:SittingAt">
    <xsl:choose>
      <xsl:when test="string-length(.)=8">
        <xsl:value-of select="substring(.,1,5)"/>
      </xsl:when>
      <xsl:when test="string-length(.)=7">
        <xsl:value-of select="substring(.,1,4)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="."/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="cs:CourtHouseAddress">
    <xsl:for-each select="apd:Line">
      <xsl:value-of select="."/>
      <xsl:if test="position()!=last()">, </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="toUpper">
    <xsl:param name="content" />
     <xsl:value-of select="translate($content,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
  </xsl:template>

  <xsl:template match="cs:Judge">
      <xsl:value-of select="apd:CitizenNameTitle"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="apd:CitizenNameForename"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="apd:CitizenNameSurname"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="apd:CitizenNameSuffix"/>
  </xsl:template>

</xsl:stylesheet>
