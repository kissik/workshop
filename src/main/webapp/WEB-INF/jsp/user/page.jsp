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
             <c:if test="${param.created == true}">
                <div class="alert alert-success"><fmt:message key="request.create.message" /></div>
             </c:if>
             <h1>
                <fmt:message key="requestsList.pageTitle" />
             </h1>
             <input type="checkbox" class="input-modal-window" id="new-request-modal-window">
             <div class="modal hidden-new-request-modal-window">
                <div class="center">
                    <div class="request-form request-form-ribbon-top">
                        <form id="requestForm" data-toggle="validator" novalidate="true" action="/app/user/new-request">
                            <h1><fmt:message key="newRequest.pageTitle" /></h1>
             				<div class="form-group">
             				    <input type="text" class="form-control caps" name="title" placeholder="<fmt:message key="newRequest.label.title" />" required>
             				</div>
             				<div class="form-group">
             				    <textarea class="form-control caps" rows="5" placeholder="<fmt:message key="newRequest.label.description" />" name="description" required></textarea>
             				</div>
             				<div class="form-group">
                                <button type="submit" id="form-submit" class="btn btn-form-submit">
             				        <span><fmt:message key="newRequest.label.submit" /></span>
             					</button>
             				</div>
             				<div class="form-group">
                                <label class="btn btn-form-submit btn-form-reset" for="new-request-modal-window">
                                    <fmt:message key="newRequest.label.reset" />
                                </label>
                            </div>
             			</form>
                    </div>
                </div>
             </div>
             <div class="workshop-grid-container">
             <div class="workshop-grid-left-3 wrapper">
                <div class="list-group-flush components">
                    <label class="workshop-create-request-btn btn-lg" for="new-request-modal-window" >
                        <fmt:message key="request.create" />
                    </label>
                    <label class="list-group-item list-group-item-action" onclick="onWork()">
                        <fmt:message key="request.in.work" />
                    </label>
                    <label class="list-group-item list-group-item-action" onclick="closed()">
                        <fmt:message key="request.were.closed" />
                    </label>
                </div>
             </div>
             <div class="hidden">
                <fieldset class="rating">
                    <input type="radio" id="star5" name="rating" value="5" /><label class = "full" for="star5"></label>
                    <input type="radio" id="star4" name="rating" value="4" /><label class = "full" for="star4"></label>
                	<input type="radio" id="star3" name="rating" value="3" /><label class = "full" for="star3"></label>
                	<input type="radio" id="star2" name="rating" value="2" /><label class = "full" for="star2"></label>
                	<input type="radio" id="star1" name="rating" value="1" /><label class = "full" for="star1"></label>
                </fieldset>
             </div>
             <div class="workshop-grid-right-9">
             <div class="form-group">
                <input id="search" class="col-4 rounded border" type="texp" placeholder="<fmt:message key="table.search" />">
                <input id="size" class="col-2 rounded border" type="number" min="2" max="6" value="5">
                <input name="sorting" class="hidden" type="radio" id="asc" value="asc" checked><label class="col-2" for="asc"><fmt:message key="table.asc" /></label>
                <input name="sorting" class="hidden" type="radio" id="desc" value="desc"><label class="col-2" for="desc"><fmt:message key="table.desc" /></label>
                <div id="page-navigation"></div>
             </div>
             <table id="request-list-table" class="">
                <thead>
                    <tr>
                        <th class="string"><fmt:message key="requestsList.table.title" /></th>
                        <th class="string"><fmt:message key="requestsList.table.status" /></th>
                        <th class="string"><fmt:message key="requestsList.table.description" /></th>
                        <th class="string"><fmt:message key="requestsList.table.author" /></th>
                    </tr>
                </thead>
                <tbody id="pageable-list">
                </tbody>
             </table>
            <script src="/js/pageable-requests.js"></script>
            <script src="/js/pageable-user-requests.js"></script>
            </div>
            </div>
        </div>
    </jsp:body>
</t:generic>