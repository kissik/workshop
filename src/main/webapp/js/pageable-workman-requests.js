var urlPath = `/app/workman/${requests}`;
var urlEditRequest = `/app/workman/edit-request`;

window.onload = () => {
    wizard(urlPath, updateRequest);
}

const updateRequest = (hiddenDesk, rowData, hiddenId) => {
    let form = document.createElement('form');
    form.setAttribute('id',`form-${hiddenId}`);
    let requestId = document.createElement('input');
    requestId.setAttribute('name','id');
    requestId.setAttribute('value', rowData.id);
    requestId.setAttribute('style','display:none');
    form.appendChild(requestId);
    let select = document.createElement('select');
    let div = document.createElement('div');
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
    form.appendChild(select);
    form.appendChild(button);
    hiddenDesk.appendChild(form);
}
