
import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';

import templates from './CoolSearch.soy';

export default class CoolSearch extends Component {

	created() {
		this.on('keywordsChanged', () => this.updateResults());
	}

	updateSearch(event) {
		this.keywords = event.target.value;
	}

	updateResults(keywords) {
		if (this.xhr) {
			this.xhr.abort();
		}

		this.xhr = jQuery.ajax({
			url: this.searchURL,
			data: {
				[this.keywordsParam]: this.keywords
			}
		});

		this.xhr.then(results => this.results = results);
	}

	prevent(event) {
		event.preventDefault();
	}

}

CoolSearch.STATE = {
	"keywordsParam": {
		value: "keywords"
	},
	"keywords": {
		value: ""
	},
	"results": {
		value: []
	},
	"searchURL": {
		value: ""
	}
};

Soy.register(CoolSearch, templates);
