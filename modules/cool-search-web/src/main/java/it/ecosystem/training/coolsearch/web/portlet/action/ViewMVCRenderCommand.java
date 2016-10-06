package it.ecosystem.training.coolsearch.web.portlet.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.util.comparator.UserScreenNameComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import it.ecosystem.training.coolsearch.web.constants.CoolSearchPortletKeys;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CoolSearchPortletKeys.COOL_SEARCH,
		"mvc.command.name=/", "mvc.command.name=/cool_search/view"
	},
	service = MVCRenderCommand.class
)
public class ViewMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Template template = getTemplate(renderRequest);

		LiferayPortletURL formURL =
			(LiferayPortletURL)renderResponse.createRenderURL();

		ResourceURL searchURL = renderResponse.createResourceURL();

		searchURL.setResourceID("/cool_search/users");

		String keywords = ParamUtil.getString(renderRequest, "keywords");

		List<User> users = new ArrayList<>();

		if (Validator.isNotNull(keywords)) {
			users = UserLocalServiceUtil.search(
				themeDisplay.getCompanyId(), keywords,
				WorkflowConstants.STATUS_ANY, null, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, new UserScreenNameComparator());
		}

		List<Map<String, Object>> results = getResults(users);

		template.put("formURL", formURL.toString());
		template.put("formParams", formURL.getReservedParameterMap());

		template.put("keywords", keywords);
		template.put(
			"keywordsParam", renderResponse.getNamespace() + "keywords");
		template.put("results", results);
		template.put("searchURL", searchURL.toString());

		// Function References

		template.put("updateSearch", "");
		template.put("prevent", "");

		return "CoolSearch";
	}

	protected <T> Map<String, Object> getResult(T object) {
		Map<String, Object> result = new HashMap<>();

		String json = JSONFactoryUtil.looseSerialize(object);
		JSONObject jsonObject;

		try {
			jsonObject = JSONFactoryUtil.createJSONObject(json);
		}
		catch (JSONException e) {
			return null;
		}

		Iterator<String> itr = jsonObject.keys();

		while (itr.hasNext()) {
			String key = itr.next();

			if (!jsonObject.isNull(key)) {
				result.put(key, jsonObject.get(key));
			}
		}

		return result;
	}

	protected <T> List<Map<String, Object>> getResults(List<T> objects) {
		return objects
			.stream()
			.map(this::getResult)
			.filter(Validator::isNotNull)
			.collect(Collectors.toList());
	}

	protected Template getTemplate(RenderRequest renderRequest) {
		return (Template)renderRequest.getAttribute(WebKeys.TEMPLATE);
	}

}
