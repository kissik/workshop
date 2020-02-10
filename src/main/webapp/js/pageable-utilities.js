var defaultSizeValue = 5;
var defaultStringValue = '';
var	size;
var	sorting = 'asc';
var search;
var language = 'en';

const ajaxJS = (url, callback) => {
    let xhr = new XMLHttpRequest();
    console.log(xhr);
    xhr.onreadystatechange = function(){
        if (xhr.status === 200 && xhr.readyState === 4){
            console.log(xhr.response);
            callback(JSON.parse(xhr.response));
        }
    }
    xhr.open('GET', url, true);
    xhr.send();
}

const clearTextFields = () => {
    search = document.querySelector(`#search`);
    search.value = defaultStringValue;
}

const clear = tag => {
	while (tag.firstChild) {
		tag.removeChild(tag.firstChild);
	}
}
const makeHTML = (data, callback) => {
    let documentFragment = document.createDocumentFragment();
	for(let index in data){
	    documentFragment.appendChild(makeRow(data[index], index, callback));
     }
    return documentFragment;
}

const makePageNavigation = pages => {
    let documentFragment = document.createDocumentFragment();
    for(let i = 0; i < pages; i++)
        documentFragment.appendChild(makePages(i));
    return documentFragment;
}

const makePages = i => {
    let anchor = document.createElement('label');
    anchor.setAttribute('class','btn btn-light');
    anchor.style.marginLeft='5px';
    anchor.appendChild(document.createTextNode(i+1));
    anchor.onclick = () => {
        showResults(`${urlPath}?page=${i}&size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    return anchor;
}

const showResults = (url, callback) => {
	ajaxJS(url, (response) => {
        let tbody = document.getElementById('pageable-list');
        let div = document.getElementById('page-navigation');
        clear(div);
        clear(tbody);
        div.setAttribute('class','container');
        div.style.margin = 'auto';
        div.style.textAlign = 'center';
        let totalElements = response.totalElements;
        let pageSize = response.size;
        let pages = Math.ceil(totalElements/pageSize);
        language = response.language;
        tbody.appendChild(makeHTML(response.content, callback));
        div.appendChild(makePageNavigation(pages));
	});
}

const setSize = () => {
    size = document.querySelector(`#size`);
    size.value = defaultSizeValue;
}

const addListeners = (url) => {
    sorting_desc.onclick = () => {
        sorting = sorting_desc.value;
        showResults(`${url}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    sorting_asc.onclick = () => {
        sorting = sorting_asc.value;
        showResults(`${url}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    size.onkeyup = () => {
         showResults(`${url}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    search.onkeyup = () => {
        showResults(`${url}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
}

const wizard = (urlPath, callback) => {
    size = document.querySelector(`#size`);
    search = document.querySelector(`#search`);
    sorting_desc = document.querySelector(`#desc`);
    sorting_asc = document.querySelector(`#asc`);
    addListeners(urlPath);
	showResults(urlPath, callback);
}