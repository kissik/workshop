var urlPath = `/app/user/${requests}`;
var urlPathHistory = `/app/user/history-${requests}`;
var urlEditHistoryRequest = `/app/user/edit-history-request`;

window.onload = () => {
    wizard(urlPath);
}

const onWork = () => {
    setSize();
    clearTextFields();
    wizard(urlPath);
}

const closed = () => {
    setSize();
    clearTextFields();
    wizard(urlPathHistory, updateHistoryRequest);
}

const updateHistoryRequest = (hiddenDesk, rowData, hiddenId) => {
    let form = document.createElement('form');
    form.setAttribute('id',`form-${hiddenId}`);
    let requestId = document.createElement('input');
    requestId.setAttribute('name','id');
    requestId.setAttribute('value', rowData.id);
    requestId.setAttribute('style','display:none');
    form.appendChild(requestId);

    let select = document.createElement('select');
    let div = document.createElement('div');
    let textarea = document.createElement('textarea');
    let fieldsetRating = document.querySelector(`fieldset.rating`);
    let clone = fieldsetRating.cloneNode(true);
    var children = clone.childNodes;
    [].forEach.call(children, (child) => {
        if ((child.tagName === 'INPUT')&&(child.hasAttribute('id'))){
            child.setAttribute('id', `${child.getAttribute('id')}-${hiddenId}`);
            console.log('id : ' + child.getAttribute('id'));
        }
        if ((child.tagName === 'LABEL')&&(child.hasAttribute('for'))){
            child.setAttribute('for', `${child.getAttribute('for')}-${hiddenId}`);
            console.log('for : ' + child.getAttribute('for'));
        }
    })

    div.appendChild(clone);

    let button = document.createElement('input');

    button.setAttribute('type', 'submit');
    button.setAttribute('class','btn btn-form-submit');
    form.setAttribute('action', urlEditHistoryRequest);
    div.setAttribute('class','form-group');
    textarea.appendChild(document.createTextNode(rowData.review));
    textarea.setAttribute('class','form-control caps');
    textarea.setAttribute('id',`cause-${hiddenId}`);
    textarea.setAttribute('rows','3');
    textarea.setAttribute('name','review');
    textarea.setAttribute('required','true');
    textarea.setAttribute('style','background: white; margin: 10px 0; border: 1px solid #dee2e6;');
    div.appendChild(textarea);
    form.appendChild(div);
    div = document.createElement('div');
    div.setAttribute('class','form-group');

    form.appendChild(div);
    form.appendChild(button);
    console.log(rowData);
    hiddenDesk.appendChild(form);
}
