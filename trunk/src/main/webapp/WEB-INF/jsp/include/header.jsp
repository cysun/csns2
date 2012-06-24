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
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="<c:url value='/css/layout.css' />">
  <link rel="stylesheet" href="<c:url value='/css/style.css' />">
  <link rel="stylesheet" href="<c:url value='/css/menu.css' />">
</head>
<body>
<div id="csns_header">
  <div class="wrap">
  <a href="<c:url value='/' />"><img border="0" id="csns_logo" src="<c:url value='/img/layout/csns_logo.png' />" alt="csns" /></a>
  <%@include file="lpanel.jsp" %>
</div>
</div>
<div id="csns_menu">
  <%@include file="menu.jsp" %>
</div>
<div id="csns_content">
<div id="content">
