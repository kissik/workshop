<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages"/>

<t:generic>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="../css/main-page-style.css">
        <title>
            Workshop
        </title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <div id="carouselExampleControls" class="carousel slide bs-slider box-slider" data-ride="carousel" data-pause="hover" data-interval="false" >
            		<!-- Indicators -->
            		<ol class="carousel-indicators">
            			<li data-target="#carouselExampleControls" data-slide-to="0" class="active"></li>
            			<li data-target="#carouselExampleControls" data-slide-to="1"></li>
            			<li data-target="#carouselExampleControls" data-slide-to="2"></li>
            		</ol>
            		<div class="carousel-inner" role="listbox">
            			<div class="carousel-item active">
            				<div id="home" class="first-section" style="background-image:url('img/slider-01.jpg');">
            					<div class="dtab">
            						<div class="container">
            							<div class="row">
            								<div class="col-md-12 col-sm-12 text-right">
            									<div class="big-tagline">
            										<h2><strong><fmt:message key="app.pageTitle" /></strong> Company</h2>
            										<p class="lead">Turn It On Again!</p>
            											<a href="/app/registration" class="workshop-index-page-btn"><fmt:message key="app.registration"/></a>
            											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            											<a href="/app/login" class="workshop-index-page-btn"><fmt:message key="subhead.login" /></a>
            									</div>
            								</div>
            							</div><!-- end row -->
            						</div><!-- end container -->
            					</div>
            				</div><!-- end section -->
            			</div>
            			<div class="carousel-item">
            				<div id="home" class="first-section" style="background-image:url('img/slider-02.jpg');">
            					<div class="dtab">
            						<div class="container">
            							<div class="row">
            								<div class="col-md-12 col-sm-12 text-left">
            									<div class="big-tagline">
            										<h2 data-animation="animated zoomInRight">Find the right <strong><fmt:message key="app.pageTitle" /></strong></h2>
            										<p class="lead" data-animation="animated fadeInLeft">Turn It On Again!</p>
            											<a href="/app/registration" class="workshop-index-page-btn"><fmt:message key="app.registration"/></a>
            											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            											<a href="/app/login" class="workshop-index-page-btn"><fmt:message key="subhead.login" /></a>
            									</div>
            								</div>
            							</div><!-- end row -->
            						</div><!-- end container -->
            					</div>
            				</div><!-- end section -->
            			</div>
            			<div class="carousel-item">
            				<div id="home" class="first-section" style="background-image:url('img/slider-03.jpg');">
            					<div class="dtab">
            						<div class="container">
            							<div class="row">
            								<div class="col-md-12 col-sm-12 text-center">
            									<div class="big-tagline">
            										<h2 data-animation="animated zoomInRight"><strong><fmt:message key="app.pageTitle" /></strong> Company</h2>
            										<p class="lead" data-animation="animated fadeInLeft">Turn It On Again!</p>
            											<a href="/app/registration" class="workshop-index-page-btn"><fmt:message key="app.registration"/></a>
            											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            											<a href="/app/login" class="workshop-index-page-btn"><fmt:message key="subhead.login" /></a>
            									</div>
            								</div>
            							</div><!-- end row -->
            						</div><!-- end container -->
            					</div>
            				</div><!-- end section -->
            			</div>
            			<!-- Left Control -->
            			<a class="new-effect carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
            				<span class="fa fa-angle-left" aria-hidden="true"></span>
            				<span class="sr-only">Previous</span>
            			</a>

            			<!-- Right Control -->
            			<a class="new-effect carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
            				<span class="fa fa-angle-right" aria-hidden="true"></span>
            				<span class="sr-only">Next</span>
            			</a>
            		</div>
            	</div>
        </div>
    </jsp:body>
</t:generic>