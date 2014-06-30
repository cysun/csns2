<%--
  - This file is part of the CSNetwork Services (CSNS) project.
  -
  - Copyright 2012, Chengyu Sun (csun@calstatela.edu).
  -
  - CSNS is free software: you can redistribute it and/or modify it under the
  - terms of the GNU Affero General Public License as published by the Free
  - Software Foundation, either version 3 of the License, or (at your option)
  - any later version.
  -
  - CSNS is distributed in the hope that it will be useful, but WITHOUT ANY
  - WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  - FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
  - more details.
  - 
  - You should have received a copy of the GNU Affero General Public License
  - along with CSNS. If not, see http://www.gnu.org/licenses/agpl.html.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><tiles:insertAttribute name="title" defaultValue="CSNS" defaultValueType="string" /></title>
  <link href="<c:url value='/favicon.ico' />" type="image/x-icon" rel="shortcut icon" /><tiles:useAttribute
    id="cssUrls" name="cssUrls" ignore="true" /><c:forEach items="${cssUrls}" var="cssUrl">
  <link rel="stylesheet" href="<c:url value='${cssUrl}' />" /></c:forEach>
  <link rel="stylesheet" href="<c:url value='/css/layout.css' />">
  <link rel="stylesheet" href="<c:url value='/css/style.css' />">
  <link rel="stylesheet" href="<c:url value='/css/menu.css' />">
  <link rel="stylesheet" href="<c:url value='/css/diff.css' />">
  <script src="<c:url value='${jquery.url}' />"></script><tiles:useAttribute
    id="jsUrls" name="jsUrls" ignore="true" /><c:forEach items="${jsUrls}" var="jsUrl">
  <script src="<c:url value='${jsUrl}' />"></script></c:forEach>
<script>
function set_content_height()
{
	var h = $(window).height()-$("#csns_header").height()-$("#csns_menu").height()-$("#csns_footer").height()-60;
	$("#csns_content #content").css("min-height", h+"px"); 
}
$(function(){
	set_content_height();
    $(window).resize(set_content_height);
});
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-52219601-1', 'auto');
	  ga('send', 'pageview');
</script>
</head>
<body>
<tiles:insertAttribute name="header" defaultValue="header.jsp" defaultValueType="template" />
<tiles:insertAttribute name="menu" defaultValue="menu.jsp" defaultValueType="template" />

<div id="csns_content"><div id="content">
  <tiles:insertAttribute name="content" />
</div></div>

<tiles:insertAttribute name="footer" defaultValue="footer.jsp" defaultValueType="template"/>
</body>
</html>
