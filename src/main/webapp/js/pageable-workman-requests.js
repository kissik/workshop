var urlPath = `/app/workman/${requests}`;

window.onload = function(){
    wizard(urlPath);
}

const makeRow = (rowData, index) => {
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    let label = document.createElement('label');
    let hiddenId = `hidden-request-data-${index}`;

    tableRow.appendChild(createHiddenRequestDiv(hiddenId, rowData));

    anchor.setAttribute('href', '#');
    anchor.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.width = '100vw';
        div.style.height = '100vh';
        div.style.display = 'flex';
        div.style.position = 'absolute';
        div.style.top = 0;
        div.style.left = 0;
        div.style.background = 'rgba(3,3,3,0.7)';
        div.style.zIndex = 2000;
    }

    label.setAttribute('for', 'view-request-modal-window');

    label.appendChild(document.createTextNode(rowData.title));
    anchor.appendChild(label);
    tableData.appendChild(anchor);
    tableData.style.width = '25%';
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(rowData.status.code))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(rowData.description))
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.style.width = '25%';
    tableData.appendChild(
        document
            .createTextNode(rowData.author.fullName))
    tableRow.appendChild(tableData);
    return tableRow;
}

const createHiddenRequestDiv = (hiddenId, rowData) => {

    let hiddenRequestDiv = document.createElement('div');

    hiddenRequestDiv.setAttribute('id', hiddenId);
    hiddenRequestDiv.setAttribute('class', 'hidden-request-data');
    hiddenRequestDiv.style.display = 'none';

    let hiddenDesk = document.createElement('div');
    hiddenDesk.setAttribute('class','contact-form');
    hiddenDesk.style.textAlign = "left";
    hiddenDesk.style.padding = "30px 15px";
    hiddenDesk.style.background = "#F4F7FB";

    let labelClose = document.createElement('button');
    labelClose.setAttribute('type','button');
    labelClose.setAttribute('class','close');
    labelClose.setAttribute('aria-label','Close');
    labelClose.appendChild(document.createTextNode('\u274c'));
    labelClose.style.color = "#007bff";
    labelClose.style.fontSize = "250%";
    labelClose.onclick = () => {
        let div = document.getElementById(hiddenId);
        div.style.display = 'none';
    }
    hiddenDesk.appendChild(labelClose);

    let fieldHeading = document.createElement('h1');
    fieldHeading.appendChild(document.createTextNode(rowData.title));
    hiddenDesk.appendChild(fieldHeading);

    fieldHeading = document.createElement('h2');
    let field = document.createElement('span');
    field.appendChild(document.createTextNode(rowData.status.code));
    field.setAttribute('class','badge badge-info');
    fieldHeading.appendChild(field);
    hiddenDesk.appendChild(fieldHeading);

    field = document.createElement('a');
    field.setAttribute('class','badge badge-primary');
    field.setAttribute('title','author');
    field.setAttribute('href',`mailto:${rowData.author.email}`);
    field.appendChild(document.createTextNode(`${rowData.author.username}`));
    hiddenDesk.appendChild(field);

    field = document.createElement('a');
    field.setAttribute('class','badge badge-success');
    field.setAttribute('title','modified by');
    field.setAttribute('href',`mailto:${rowData.user.email}`);
    field.appendChild(document.createTextNode(`${rowData.user.username}`));
    hiddenDesk.appendChild(field);

    field = document.createElement('p');
    field.appendChild(document.createTextNode(rowData.description));
    hiddenDesk.appendChild(field);
    hiddenRequestDiv.appendChild(hiddenDesk);
    return hiddenRequestDiv;
}