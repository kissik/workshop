<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${param.lang}" />
<fmt:setBundle basename="messages"/>

<t:generic>
    <jsp:attribute name="head">
        <title><fmt:message key="error.title"/></title>
    </jsp:attribute>
    <jsp:body>
    	<div class="workshop-error-body-class">
	        <div class="workshop-error-message-wrapper">
				<h1><fmt:message key="error.title" /></h1>
				<p>${param.lang}</p>
				<c:if test="${param.failedconnection == true}"><p><fmt:message key="error.dbconnection" /></p></c:if>
				<p><fmt:message key="error.contact.admin" /></p>
			</div>
		</div>
    </jsp:body>
</t:generic>
