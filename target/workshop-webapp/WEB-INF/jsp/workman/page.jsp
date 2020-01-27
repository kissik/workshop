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
        <title>
            Workshop
        </title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
             <p>
                Welcome, WORKMAN!
                <fmt:message key="home.welcome" />
            </p>
        </div>
    </jsp:body>
</t:generic>