var	size;
var	sorting = 'asc';
var search;
var urlPath = '/app/admin/users';
var urlString = '';

window.onload = function(){
    size = document.querySelector(`#size`);
    search = document.querySelector(`#search`);
    sorting_desc = document.querySelector(`#desc`);
    sorting_asc = document.querySelector(`#asc`);
    sorting_desc.onclick = function(){
        sorting = sorting_desc.value;
        showResults(`${urlPath}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    sorting_asc.onclick = function(){
        sorting = sorting_asc.value;
        showResults(`${urlPath}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    size.onkeyup = function(){
         showResults(`${urlPath}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
    search.onkeyup = function(){
        showResults(`${urlPath}?size=${size.value}&search=${search.value}&sorting=${sorting}`);
    }
	showResults(urlPath);
}

const makePageNavigation = pages => {
    let str = '';
    for(let i = 0; i < pages; i++)
        str += `<a class="page-btn" name="page${i}">${i+1}</a>`;
    return str;
}

const makeHTML = data => {
    let str = '';
	for(let index in data){
		console.log((data)[index]);
        str += `<tr>`
            + `<td><a href="users/${(data)[index].id}">${(data)[index].username}</a></td>`
            + `<td>${(data)[index].firstName}</td>`
            + `<td>${(data)[index].lastName}</td>`
            + `<td>${(data)[index].phone}</td>`
        + `</tr>`;
     }
     return str;
}

function addListeners(pages){
    let a;
    for(let i = 0; i < pages; i++){
        a = document.querySelector(`[name="page${i}"]`);
        a.onclick = function(){
            showResults(`${urlPath}?page=${i}&size=${size.value}&search=${search.value}&sorting=${sorting}`);
        }
    }
}

function showResults(url){
	ajaxJS(url, function(response){
        let tbody = document.getElementById('pageable-list');
        let div = document.getElementById('page-navigation');

        let totalElements = response.totalElements;
        let pageSize = response.size;
        let pages = Math.ceil(totalElements/pageSize);

        tbody.innerHTML = makeHTML(response.content);
        div.innerHTML = makePageNavigation(pages);

        addListeners(pages);
	});
}

function ajaxJS(url, callback){
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