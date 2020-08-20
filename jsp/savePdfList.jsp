<%@ taglib prefix="jca" uri="http://www.ptc.com/windchill/taglib/components"%>
<%@ page import="com.ptc.netmarkets.object.objectResource,
                 com.ptc.netmarkets.model.*, 
				 java.util.*,
				 java.lang.*,
				 com.ptc.netmarkets.model.NmOid, 
				 com.ptc.netmarkets.util.misc.NmContext, 
				 wt.doc.WTDocument, 
				 wt.part.WTPart, 
				 wt.facade.rema.RemaFacade,
				 ru.ruselprom.signs.*,
				 ru.ruselprom.signs.data.*,
				 ru.ruselprom.signs.exceptions.*"
%>

<%@ include file="/netmarkets/jsp/components/beginWizard.jspf"%>

<%
	sessionBean.setDoneCB(false);
	NmCommandBean cb1 = new NmCommandBean();
	cb1.setInBeginJsp        (true);
	cb1.setOpenerCompContext (request.getParameter("compContext"));
	cb1.setOpenerElemAddress (NmCommandBean.convert(request.getParameter("openerElemAddress")));
	cb1.setCompContext       (NmCommandBean.convert(request.getParameter("compContext")));
	cb1.setElemAddress       (NmCommandBean.convert(request.getParameter("elemAddress")));
	cb1.setSessionBean       (sessionBean);
	cb1.setRequest           (request);
	cb1.setResponse          (response);
	cb1.setOut               (out);
	cb1.setWtcontextBean     (wtcontext);
	cb1.setContextBean       (nmcontext);

	ArrayList<NmOid> oidList = cb1.getSelectedOidForPopup();
        cb1.setSelected(oidList);
	
	String filePath = "D:\\Ruselprom\\Projects\\pdf with signs\\";

	SignaturesApp signaturesApp  = new SignaturesApp();
	String result = "";
	
	out.println("<br><h1>" + oidList.size() + "<h1>");
	int i = 0;
	for(NmOid oid : oidList) {
		if (oid.toString().contains("wt.epm.EPMDocument")) {
			out.println("<br><h1>" + oid + " - " + signaturesApp.start(oid.toString(), filePath) + "<h1>");
		} else {
			out.println("<br><h1>" + oid + " - Неправильный тип объекта. Требуется EPMDocument!" + "<h1>");
		}
		out.println("<br><h1>" + i + "<h1>");
		i++;
	}
	
%>

<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf"%>

<%@ include file="/netmarkets/jsp/util/end.jspf"%>
