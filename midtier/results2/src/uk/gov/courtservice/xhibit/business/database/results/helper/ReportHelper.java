package uk.gov.courtservice.xhibit.business.database.results.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ReportHelper<S,D,SD> {
	private Class<S> s;
	private Class<D> d;
	private Class<SD> sd;
	private S site1;
	private D site2;
	
	public ReportHelper(Class<S> s, Class<D> d, Class<SD> sd) throws Exception {
		this.s = s;
		this.d = d;
		this.sd = sd;
		site1 = this.s.newInstance();
		site2 = this.d.newInstance();
	}
	
	@SuppressWarnings("unchecked")
	public List buildSummaryAndDetailList(List rrcaSummary,List rrcaDetail) throws Exception {
		List reportSummaryDetail = new ArrayList();
		List allSummaryDetail = new ArrayList();
		List rrcaSite = new ArrayList<List>();
		ListIterator<S> litSummary = rrcaSummary.listIterator();
		ListIterator<D> litDetail = rrcaDetail.listIterator();
		List rrcaSummaryDetail = new ArrayList<D>();
		Method getSummary = s.getMethod("getCourt_site_name");
		Method getDetail = d.getMethod("getCourt_site_name");
		Method setDetail = d.getMethod("setCourt_site_name",String.class);
		S allSites = null;
		
		//Add summary and detail pairs for same court site name into list, and add to summary and detail object.
		while (litSummary.hasNext()) {
			litDetail = rrcaDetail.listIterator();
			site1 = (S) litSummary.next();
			while (litDetail.hasNext()) {
				site2 = (D) litDetail.next();
				if (getSummary.invoke(site1).equals(getDetail.invoke(site2))) {
					reportSummaryDetail.add(site1);
					reportSummaryDetail.add(site2);
					allSummaryDetail.add(site1);
					allSummaryDetail.add(site2);
					rrcaSite.add(reportSummaryDetail);  
					reportSummaryDetail = new ArrayList();
					break;
				}
			} 		
		}
		
		litSummary = rrcaSummary.listIterator();
		litDetail = rrcaDetail.listIterator();
		
		//add missing first page to summary and detail object, by finding no matching court site name in allSummaryDetail list
		//and add to summary and detail object
		while (litSummary.hasNext()) {
			site1 = (S) litSummary.next();
			if (!allSummaryDetail.contains(site1)) {
				if ("All".equals(getSummary.invoke(site1))) {
					allSites = site1;
				} else {
					addSummaryAndDetail(site1, setDetail, getSummary, rrcaSite);
				}
			}
		}
		
		if (allSites != null)
				addSummaryAndDetail(allSites, setDetail, getSummary, rrcaSite);
		
		//add site and summary object to list.
		for (Object o:rrcaSite) {
			SD newSummary = sd.newInstance();
			Method add = sd.getMethod("setSummaryDetail",List.class);
			add.invoke(newSummary, (List) o);
			rrcaSummaryDetail.add(newSummary);
		}
	
		return rrcaSummaryDetail;
	}
	
	@SuppressWarnings("unchecked")
	private void addSummaryAndDetail(S site, Method setDetail, Method getSummary, List rrcaSite) throws Exception{
		List siteSummary = new ArrayList();
		siteSummary.add(site);
		D tempDetail = d.newInstance();
		setDetail.invoke(tempDetail, getSummary.invoke(site));
		siteSummary.add(tempDetail); 
		rrcaSite.add(siteSummary);
	}
}

