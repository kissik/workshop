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
             <ol class="breadcrumb">
                     	<li class="breadcrumb-item active"><a href="${homeUrl}"><fmt:message key="home.pageTitle" /></a></li>
                     </ol>
                     <h1><fmt:message key="usersList.pageTitle" /></h1>
                     <input id="search" class="col-4 rounded border" type="texp" placeholder="search">
                     <input id="size" class="col-2 rounded border" type="number" min="2" max="6" value="5">
                     <input name="sorting" class="hidden" type="radio" id="asc" value="asc" checked><label class="col-2" for="asc">asc</label>
                     <input name="sorting" class="hidden" type="radio" id="desc" value="desc"><label class="col-2" for="desc">desc</label>
                     <div id="page-navigation"></div>
                     <table id="request-list-table" class="sortable">
                         <thead>
                     	    <tr>
                     		    <th class="string"><fmt:message key="usersList.table.user" /></th>
                     			<th class="string"><fmt:message key="usersList.table.firstName" /></th>
                     			<th class="string"><fmt:message key="usersList.table.lastName" /></th>
                     			<th class="string"><fmt:message key="usersList.table.phone" /></th>
                     		</tr>
                     	</thead>
                     	<tbody id="pageable-list">
                     	</tbody>
                     </table>

                     <script src="/js/pageable-accounts.js"></script>
        </div>
    </jsp:body>
</t:generic>