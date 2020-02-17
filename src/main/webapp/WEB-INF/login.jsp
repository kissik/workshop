<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>

<c:url var="postLoginUrl" value="/app/login" />

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages"/>

<t:generic>
    <jsp:attribute name="head">
    	<title><fmt:message key="login.in.please" /></title>
    </jsp:attribute>
    <jsp:body>
    <div class="container main-content">
        <h1><fmt:message key="login.in.please" /></h1>
        	<form action="${postLoginUrl}" method="post" >
    			<c:if test="${error}" >
    				<div class="alert alert-danger"><fmt:message key="login.warning.alert" /></div>
    			</c:if>
    				<div class="form-group">
    				    <label for="username" class="workshop-field-label"><fmt:message key="login.login" /></label>
    				    <input type="text" class="form-control" id="username" name="username" aria-describedby="loginHelp" placeholder="<fmt:message key="login.login.input" />">
                        <small id="loginHelp" class="form-text text-muted"><fmt:message key="login.never.share" /></small>
                    </div>
    				<div class="form-group">
    					<label for="password" class="workshop-field-label"><fmt:message key="login.password" /></label>
                        <input type="password" class="form-control" id="password" name="password"  placeholder="<fmt:message key="login.password.input" />">
    				</div>
    				<div class="form-group">
    					<input class="btn btn-primary" type="submit" value="<fmt:message key="login.in" />" />
    				</div>
            </form>
        </div>
    </jsp:body>
</t:generic>
