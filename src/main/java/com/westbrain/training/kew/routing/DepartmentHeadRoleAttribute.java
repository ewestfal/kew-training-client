package com.westbrain.training.kew.routing;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.AbstractRoleAttribute;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.westbrain.training.kew.SpringContext;
import com.westbrain.training.kew.api.DepartmentService;

public class DepartmentHeadRoleAttribute extends AbstractRoleAttribute {

	private static final long serialVersionUID = -431014665321272591L;

	private DepartmentService departmentService;
	
	/**
	 * This class is created by KEW and is not managed by Spring, so need access to the context in order
	 * to get the DepartmentService
	 */
	public DepartmentHeadRoleAttribute() {
		this.departmentService = SpringContext.getApplicationContext().getBean(DepartmentService.class);
	}
	
	/**
	 * This method simply identifies the roles that this attribute provides. It only provides a single
	 * "Department Head" role.
	 */
	@Override
	public List<RoleName> getRoleNames() {
		List<RoleName> roleNames = new ArrayList<RoleName>();
		roleNames.add(new RoleName("DepartmentHeadRoleAttribute", "DepartmentHead", "Department Head")); 
		return roleNames;
	}

	/**
	 * This method uses XPath to find all department codes in the submitted XML, it then returns each of
	 * those as an identifier for a qualified role (since we will route to one person for each department code).
	 * 
	 * Note that because our department request only allows a single department to be selected, this will only
	 * ever return one department code but we have implemented it to be flexible in case there is a document in 
	 * the future that supports routing to multiple departments.
	 */
	@Override
	public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
		List<String> qualifiedRoleNames = new ArrayList<String>();
		XPath xPath = XPathHelper.newXPath();
		try {
			InputSource source = new InputSource(new StringReader(documentContent.getDocContent()));
			NodeList departmentCodeNodes = (NodeList)xPath.evaluate("//departmentRequest/departmentCode", source, XPathConstants.NODESET);
			for (int i = 0; i < departmentCodeNodes.getLength(); i++) {
	        	Node departmentCodeNode = departmentCodeNodes.item(i);
	        	String departmentCode = departmentCodeNode.getTextContent();
	        	qualifiedRoleNames.add(departmentCode);
			}
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Failed to execute XPath against document content", e);
		}
		return qualifiedRoleNames;
	}

	/**
	 * This method will be called for each result returned from getQualifiedRoleNames. In our case it
	 * will pass the department code to us in the "qualifiedRole" parameter. So we simply need to identify
	 * the department head for that department and
	 */
	@Override
	public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName,
			String qualifiedRole) {
		
		// TODO - implement me!
		
		return null;
	}
	

}
