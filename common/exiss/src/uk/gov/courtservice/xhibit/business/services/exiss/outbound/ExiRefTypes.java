package uk.gov.courtservice.xhibit.business.services.exiss.outbound;

public enum ExiRefTypes {
    AR("AR", "Appeal Record Sheet")
   ,BO("BO", "Bail Order")
   ,BW("BW", "Bench Warrant")
   ,CH("CH", "Charges")
   ,CO("CO", "Community Order")
   ,CPO("CPO", "Community Punishment Order")
   ,CPR("CPR", "Community Punishment Rehabilitation Order")
   ,CRO("CRO", "Community Rehabilitation Order")
   ,DISCASE("DISCASE", "Discontinued Case")
   ,DL("DL", "Daily List")
   ,DLP("DLP", "Prison Daily List")
   ,EVENT("EVENT", "Court LOG Event")
   ,FL("FL", "Firm List")
   ,IO("IO", "Imprisonment Order")
   ,MC("MC", "Memorandum Of Conviction")
   ,NA("NA", "Notice Of Appeal")
   ,NEWCASE("NEWCASE", "New Case")
   ,DELIVERERROR("DELIVERERROR", "Deliver Error")
   ,RL("RL", "Running List")
   ,RO("RO", "Remand Order")
   ,SR("SR", "Committal (for Sentence) Record Sheet")
   ,SS("SS", "Skeleton Schedule")
   ,TR("TR", "Trial Record Sheet")
   ,UPDCASE("UPDCASE", "Updated Case")
   ,WL("WL", "Warned List")
   ,WW("WW", "Witness Warrant Order")
   ,YOI("YOI", "Young Offender Order");    

    private final String internalCode;
    private final String internalName;
     
     private ExiRefTypes(String internalCode, String internalName) {
         this.internalCode = internalCode;
         this.internalName = internalName;
     }
     
     public String getInternalCode() {
         return internalCode;
     }
     
     public String getInternalName() {
         return internalName;
     }
}
