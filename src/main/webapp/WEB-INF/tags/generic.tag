<%@ tag isELIgnored="false" description="Overall Page template" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="kissik" uri="/WEB-INF/tld/kissik.tld" %>
<%@ include file="/WEB-INF/jsp/urls.jspf" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="messages"/>

<%@attribute name="head" fragment="true" %>

<html>
  <head>
        <jsp:invoke fragment="head" />
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css"/>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
        <script src="/js/pageable-utilities.js"></script>
        <link rel="stylesheet" type="text/css" href="/css/style.css">
        <link rel="stylesheet" type="text/css" href="/css/error-style.css">
        <link href="https://fonts.googleapis.com/css?family=Pattaya&display=swap" rel="stylesheet">
  </head>
  <body>
    <header>
        <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
              <a class="navbar-brand" href="/" title="<fmt:message key="app.title"/>">
                <fmt:message key="app.title"/>
              </a>
              <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
                 aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                 <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="navbarCollapse">
                 <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/">
                            <span class="sr-only">Home</span>
                            <fmt:message key="app.home"/>
                        </a>
                     </li>
                        <kissik:authorize access="isAnonymous()">
                            <li class="nav-item active">
                                <a class="nav-link" href="/app/registration">
                                    <fmt:message key="app.registration"/>
                                </a>
                            </li>
                        </kissik:authorize>
                        <kissik:authorize access="hasRole('ADMIN')">
                            <li class="nav-item active">
                                <a class="nav-link" href="/app/admin/page">
                                    <fmt:message key="app.pageTitle.admin"/>
                                </a>
                            </li>
                        </kissik:authorize>
                        <kissik:authorize access="hasRole('MANAGER')">
                            <li class="nav-item active">
                                <a class="nav-link" href="/app/manager/page">
                                    <fmt:message key="app.pageTitle.manager"/>
                                </a>
                            </li>
                        </kissik:authorize>
                        <kissik:authorize access="hasRole('WORKMAN')">
                            <li class="nav-item active">
                                <a class="nav-link" href="/app/workman/page">
                                    <fmt:message key="app.pageTitle.workman"/>
                                </a>
                            </li>
                        </kissik:authorize>
                        <kissik:authorize access="hasRole('USER')">
                            <li class="nav-item active">
                                <a class="nav-link" href="/app/user/page">
                                    <fmt:message key="app.pageTitle.user"/>
                                </a>
                            </li>
                        </kissik:authorize>


                 </ul>
              </div>

            <div class="navbar-nav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a href="?lang=en">
                            <fmt:message key="app.lang.english"/>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="?lang=uk">
                            <fmt:message key="app.lang.ukrainian"/>
                        </a>
                    </li>
                </ul>
            </div>

            <div>
            <div class="navbar-nav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <kissik:authorize access="isAnonymous()">
                            <div class="workshop-sessionInfo">
                                <fmt:message key="subhead.welcome" />
                                <a class="workshop-login-logout-btn" href="/app${loginUrl}">
                                    <fmt:message key="subhead.login" />
                                </a>
                            </div>
            		    </kissik:authorize>
            		</li>
            		<li class="nav-item">
                        <kissik:authorize access="isAuthenticated()">
                            <div class="workshop-sessionInfo">
                                <c:choose>
                                    <c:when test="${lang eq 'uk'}">
                                        ${user.fullNameOrigin}
                                    </c:when>
                                    <c:otherwise>
                                        ${user.fullName}
                                    </c:otherwise>
                                </c:choose>
                                <a class="workshop-login-logout-btn" href="/app${logoutUrl}">
                                    <fmt:message key="subhead.logout" />
                                </a>
                            </div>
                        </kissik:authorize>
                    </li>
                 </ul>
            </div>
        </nav>
    </header>
    <div class="content">
        <jsp:doBody/>
    </div>
    <footer class="footer">
        <div class="container">
            <p class="text-center text-uppercase text-muted">&copy;<fmt:message key="app.footer" /></p>
        </div>
    </footer>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
  </body>
</html>