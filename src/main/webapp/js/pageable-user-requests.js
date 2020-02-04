var urlPath = `/app/user/${requests}`;
var urlPathHistory = `/app/user/history-${requests}`;

const onWork = () => {
    setSize();
    clearTextFields();
    wizard(urlPath);
}

const closed = () => {
    setSize();
    clearTextFields();
    wizard(urlPathHistory);
}

window.onload = () => {
    wizard(urlPath);
}