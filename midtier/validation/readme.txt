

1) The following lines need to be added to the end of the file "/software/apps/xhibit_2/common/aqjms/aqjms.properties" and the WebLogic midtier restarted. This will bind the "xhb_validation_queue" into the local WebLogic JNDI tree as "AQJMS_XhbValidationQueue".

QueueName2=xhb_validation_queue
QueueJNDIName2=AQJMS_XhbValidationQueue

2) Add the following schemas to the schema dir "/software/apps/xhibit_2/common/schema" creating if necessary.

      AddressTypes-v5-2.xsd
      apd-v5-2.xsd
      AppealRecordSheet-v5-2.xsd
      BailOrder-v5-2.xsd
      BenchWarrant-v5-2.xsd
      BS7666-v5-2.xsd
      CaseInfo-v5-2.xsd
      CitizenIdentificationTypes-v5-2.xsd
      CommittalRecordSheet-v5-2.xsd
      CommonSimpleTypes-v5-2.xsd
      CommunityOrder-v5-2.xsd
      CommunityPunishmentOrder-v5-2.xsd
      CommunityPunishmentRehabOrder-v5-2.xsd
      CommunityRehabOrder-v5-2.xsd
      ContactTypes-v5-2.xsd
      CourtService-v5-2.xsd
      DailyList-v5-2.xsd
      FirmList-v5-2.xsd
      ImprisonmentOrder-v5-2.xsd
      Indictment-v5-2.xsd
      PersonalDetailsTypes-v5-2.xsd
      PreSentenceReport-v5-2.xsd
      RemandOrder-v5-2.xsd
      RunningList-v5-2.xsd
      Skeleton-v5-2.xsd
      TrialRecordSheet-v5-2.xsd
      WarnedList-v5-2.xsd
      YoungOffenderOrder-v5-2.xsd

Development:

Buid

ant -f midtier\validation\build.xml

Deploy

java -cp %WL_HOME%\server\lib\weblogic.jar weblogic.Deployer -username weblogic -password password -adminurl t3://10.63.127.7:7000 -targets MidCluster -upload -deploy midtier\validation\build\lib\Validation.jar
