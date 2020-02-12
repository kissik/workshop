<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>

<c:set var="email" value="${account.email}" />
<c:set var="userPath" value="/app/admin/accounts/${account.id}" />
<c:url var="userUrl" value="/${userPath}" />

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages"/>

<c:url var="editUserUrl" value="${userPath}/edit" />
<c:url var="deleteUserUrl" value="/app/admin/delete-account" />

<spring:message var="editUser" code="user.edit" />

<t:generic>
    <jsp:attribute name="head">
        <title><c:out value="${account.fullName}" /></title>
    </jsp:attribute>
    <jsp:body>
    <div class="container main-content">
        <ol class="breadcrumb">
        	<li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle" /></a></li>
        	<li class="breadcrumb-item active"><a href="/app/admin/page"><fmt:message key="usersList.pageTitle" /></a></li>
        </ol>
        <c:if test="${param.saved == true}">
            <div class="info alert"><fmt:message key="user.save" /></div>
        </c:if>
        <c:if test="${param.deleted == true}">
            <div class="info alert"><fmt:message key="user.delete.true" /></div>
        </c:if>
        <c:if test="${param.deleted == false}">
            <div class="info alert-danger"><fmt:message key="user.delete.false" /></div>
        </c:if>
        <h1><c:out value="${account.fullName}" /></h1>
        <form action="${deleteUserUrl}">
		    <input type="text" name=id class="hidden" value=${account.id}>
		    <input type="submit" class="btn btn-danger" value=<fmt:message key="user.delete" /> >
		</form>
		<div>
			<div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.login" /></div>
					<div class="col-sm-6">${account.username}</div>
				</div>
			</div>
			<div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.firstName" /></div>
					<div class="col-sm-6">${account.firstName}</div>
				</div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.lastName" /></div>
					<div class="col-sm-6">${account.lastName}</div>
				</div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.firstNameOrigin" /></div>
					<div class="col-sm-6">${account.firstNameOrigin}</div>
				</div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.lastNameOrigin" /></div>
					<div class="col-sm-6">${account.lastNameOrigin}</div>
				</div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.email" /></div>
					<div class="col-sm-6">
						<a href="mailto:${email}">${email}</a>
					</div>
				</div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.phone" /></div>
					<div class="col-sm-6">
						<span>${account.phone}</span>
					</div>
				</div>
			</div>
			<div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.enable" /></div>
					<div class="col-sm-6"><c:out value="${account.enabled}" /></div>
				</div>
				<div class="form-group row">
					<div class="col-sm-2 workshop-field-label"><fmt:message key="user.roles" /></div>
					<div class="col-sm-6">
						<c:forEach var="role" items="${account.roles}">
							<c:out value="${role.name}" /><br />
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<a href="${editUserUrl}" class="btn btn-primary" title="${editUser}"><fmt:message key="user.edit" /></a>
    </div>
    </jsp:body>
</t:generic>