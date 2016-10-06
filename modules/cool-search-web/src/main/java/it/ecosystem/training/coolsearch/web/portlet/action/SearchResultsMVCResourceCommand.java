package it.ecosystem.training.coolsearch.web.portlet.action;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
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
		"mvc.command.name=/cool_search/users"
	},
	service = MVCResourceCommand.class
)
public class SearchResultsMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest,
			ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String keywords = ParamUtil.getString(resourceRequest, "keywords");

		List<User> results = new ArrayList<>();

		if (Validator.isNotNull(keywords)) {
			results = UserLocalServiceUtil.search(
				themeDisplay.getCompanyId(), keywords,
				WorkflowConstants.STATUS_ANY, null, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, new UserScreenNameComparator());
		}

		JSONPortletResponseUtil.writeJSON(
			resourceRequest, resourceResponse,
			JSONFactoryUtil.looseSerialize(results));
	}

}
