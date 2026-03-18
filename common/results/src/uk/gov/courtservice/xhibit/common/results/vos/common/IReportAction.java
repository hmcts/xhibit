package uk.gov.courtservice.xhibit.common.results.vos.common;

import java.net.URL;
import java.util.Map;

public interface IReportAction {
	public static final String DISPLAY_MODE = "display";
	public Map buildMap(String path, URL url);

}
