<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages"/>

<t:generic>
    <jsp:attribute name="head">
        <title><fmt:message key="newUserRegistration.pageTitle" /></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
        		<h1><fmt:message key="newUserRegistration.pageTitle" /></h1>
        		<form class="main" method = "post">
        			<p> <fmt:message key="newUserRegistration.message.allFieldsRequired" /></p>
        			<div class="form-group">
    				    <label for="username" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.username" />
    				    </label>
    				    <c:set var="accountInfo">${account.username}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="usernameFieldValue">${account.username}</c:set>
                        </c:if>
    				    <input required name="username" type="text" class="form-control" id="username" value=${usernameFieldValue} >
                    	<c:set var="usernameErrors">${errors.username}</c:set>
                            <c:if test="${not empty usernameErrors}">
        					    <div class="alert alert-danger"><fmt:message key="${errors.username}" /></div>
        				    </c:if>
        		    </div>
        			<div class="form-group">
        				<label for="password" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.password" />
    				    </label>
    				    <input required name="password" type="password" showPassword="false" class="form-control" id="password"/>
                    </div>
        			<div class="form-group">
        				<label for="confirmPassword" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.confirmPassword" />
    				    </label>
    				    <input required name="confirmPassword" type="password" showPassword="false" class="form-control" id="confirmPassword"/>
                    	<c:set var="passwordErrors">${errors.password}</c:set>
                        <c:if test="${not empty passwordErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.password}" /></div>
        				</c:if>
        			</div>
        			<div class="form-group">
        				<label for="firstName" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.firstName" />
    				    </label>
    				    <c:set var="accountInfo">${account.firstName}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="firstNameFieldValue">${account.firstName}</c:set>
                        </c:if>
    				    <input required name="firstName" type="text" class="form-control" id="firstName" value=${firstNameFieldValue} >
                    	<c:set var="firstNameErrors">${errors.firstName}</c:set>
                        <c:if test="${not empty firstNameErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.firstName}" /></div>
        				</c:if>
        			</div>
        			<div class="form-group">
        				<label for="lastName" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.lastName" />
    				    </label>
    				    <c:set var="accountInfo">${account.lastName}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="lastNameFieldValue">${account.lastName}</c:set>
                        </c:if>
    				    <input required name="lastName" type="text" class="form-control" id="lastName" value=${lastNameFieldValue} >
                    	<c:set var="lastNameErrors">${errors.lastName}</c:set>
                        <c:if test="${not empty lastNameErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.lastName}" /></div>
        				</c:if>
        			</div>
                    <div class="form-group">
        				<label for="firstNameOrigin" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.firstNameOrigin" />
    				    </label>
    				    <c:set var="accountInfo">${account.firstNameOrigin}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="firstNameOriginFieldValue">${account.firstNameOrigin}</c:set>
                        </c:if>
    				    <input required name="firstNameOrigin" type="text" class="form-control" id="firstNameOrigin"  value=${firstNameOriginFieldValue} >
                    	<c:set var="firstNameOriginErrors">${errors.firstNameOrigin}</c:set>
                        <c:if test="${not empty firstNameOriginErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.firstNameOrigin}" /></div>
        				</c:if>
        			</div>
        			<div class="form-group">
        				<label for="lastNameOrigin" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.lastNameOrigin" />
    				    </label>
    				    <c:set var="accountInfo">${account.lastNameOrigin}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="lastNameOriginFieldValue">${account.lastNameOrigin}</c:set>
                        </c:if>
    				    <input required name="lastNameOrigin" type="text" class="form-control" id="lastNameOrigin"  value=${lastNameOriginFieldValue} >
                    	<c:set var="lastNameOriginErrors">${errors.lastNameOrigin}</c:set>
                        <c:if test="${not empty lastNameOriginErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.lastNameOrigin}" /></div>
        				</c:if>
        			</div>
        			<div class="form-group">
        				<label for="email" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.email" />
    				    </label>
    				    <c:set var="accountInfo">${account.email}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="emailFieldValue">${account.email}</c:set>
                        </c:if>
    				    <input required name="email" type="text" class="form-control" id="email"  value=${emailFieldValue} >
    				    <c:set var="emailErrors">${errors.email}</c:set>
                        <c:if test="${not empty emailErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.email}" /></div>
        				</c:if>
        			</div>
        			<div class="form-group">
        				<label for="phone" class="workshop-field-label">
    				        <fmt:message key="newUserRegistration.label.phone" />
    				    </label>
    				    <c:set var="accountInfo">${account.phone}</c:set>
    				    <c:if test="${not empty accountInfo}">
                            <c:set var="phoneFieldValue">${account.phone}</c:set>
                        </c:if>
    				    <input required name="phone" type="text" class="form-control" id="phone"  value=${phoneFieldValue} >
    				    <c:set var="phoneErrors">${errors.phone}</c:set>
                        <c:if test="${not empty phoneErrors}">
        				    <div class="alert alert-danger"><fmt:message key="${errors.phone}" /></div>
        				</c:if>
        			</div>
        			<div class="form-group">
        			    <input type="submit" class="btn btn-primary" value="<fmt:message key="newUserRegistration.label.submit" />" />
        			</div>
        		</form>
        </div>
                 <script  type="text/javascript">
                        		$(function(){
                        			$('form').submit(function() {
                        				$(':submit',this).attr('disabled', 'disabled');
                        			});
                        		})
                 </script>
                 <script src="/js/registration-form.js"></script>
    </jsp:body>
</t:generic>