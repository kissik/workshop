window.onload = function(){
    let request = getRequestObject();
    request.onreadystatechange=function(){
        handleResponse(request);
    }
}

function handleResponse(request){
    if (request.readyState===4 && request.state===200)
        console.log(request);
}

function getRequestObject(){
    return window.XMLHttpRequest;
}