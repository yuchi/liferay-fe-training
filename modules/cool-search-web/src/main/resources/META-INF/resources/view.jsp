<%@ include file="/init.jsp" %>

<div id="<portlet:namespace />coolSearch"></div>

<aui:script require="it.ecosystem.training.coolsearch.web@1.0.0/CoolSearch.es">

	var CoolSearch = itEcosystemTrainingCoolsearchWeb100CoolSearchEs.default;

	var coolSearch = new CoolSearch({
		// configurations
	}, document.getElementById('<portlet:namespace />coolSearch'));

</aui:script>
