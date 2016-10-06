package it.ecosystem.training.coolsearch.web.portlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.portlet.bridge.soy.SoyPortlet;

import it.ecosystem.training.coolsearch.web.constants.CoolSearchPortletKeys;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=cool-search-web Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.name=" + CoolSearchPortletKeys.COOL_SEARCH,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class CoolSearchPortlet extends SoyPortlet {
}
