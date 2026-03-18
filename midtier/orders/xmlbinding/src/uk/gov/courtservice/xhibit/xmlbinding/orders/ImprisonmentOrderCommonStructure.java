package uk.gov.courtservice.xhibit.xmlbinding.orders;

public interface ImprisonmentOrderCommonStructure {
    void setConvictionDate(org.exolab.castor.types.Date convictionDate);
    void setIOCommittingCourt(uk.gov.courtservice.xhibit.xmlbinding.generated.orders.IOCommittingCourt IOCommittingCourt);
    void setAssociatedCases(uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AssociatedCases associatedCases);
    //void setDeportationSection(uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DeportationSection deportationSection);
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.IOCommittingCourt getIOCommittingCourt();
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CustodialSentence getCustodialSentence();
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Section28 getSection28();
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ReturnToImprisonment getReturnToImprisonment();
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes getAdditionalNotes();
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AssociatedCases getAssociatedCases();
    uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader getOrderHeader();
}
