var urlPath = `/app/manager/${requests}`;
var urlEditRequest = `/app/manager/edit-request`;

window.onload = () => {
    wizard(urlPath, update_request);
}

const update_request = (hiddenDesk, rowData, hiddenId) => {
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
    let price = document.createElement('input');
    let button = document.createElement('input');

    rowData.status.nextStatuses.map((nextStatus) => {
        let option = document.createElement('option');
        option.setAttribute('value', nextStatus.key);
        option.appendChild(document.createTextNode(nextStatus.value));
        select.appendChild(option);
    });
    select.setAttribute('name', 'status');
    select.setAttribute('id',`select-${hiddenId}`);
    select.setAttribute('class','form-control');
    select.setAttribute('style','background: white; margin: 10px 0; border: 1px solid #dee2e6;');
    button.setAttribute('type', 'submit');
    button.setAttribute('class','btn btn-form-submit');
    form.setAttribute('action',urlEditRequest);
    div.setAttribute('class','form-group');
    textarea.setAttribute('class','form-control caps');
    textarea.setAttribute('id',`cause-${hiddenId}`);
    textarea.setAttribute('rows','5');
    textarea.setAttribute('name','cause');
    textarea.setAttribute('required','true');
    textarea.setAttribute('style','background: white; margin: 10px 0; border: 1px solid #dee2e6;');
    price.setAttribute('name','price');
    price.setAttribute('id',`price-${hiddenId}`);
    price.setAttribute('required','true');
    price.setAttribute('type','number');
    price.setAttribute('class','form-control');
    price.setAttribute('style','background: white; margin: 10px 0; border: 1px solid #dee2e6;');
    div.appendChild(textarea);
    form.appendChild(select);
    form.appendChild(div);
    div = document.createElement('div');
    div.setAttribute('class','form-group');
    div.appendChild(price);
    form.appendChild(div);
    form.appendChild(button);
    console.log(rowData);
    hiddenDesk.appendChild(form);
}

