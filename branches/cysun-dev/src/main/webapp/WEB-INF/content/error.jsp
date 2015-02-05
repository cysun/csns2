<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${empty backUrl}">
  <c:set var="backUrl" value="/" />
</c:if>
<c:if test="${empty title}">
  <c:set var="title" value="error" />
</c:if>
<c:if test="${empty message}">
  <c:set var="message" value="error.message" />
</c:if>

<script>
$(function(){
    $("#ok").click(function(){
       window.location.href = "<c:url value='${backUrl}' />";
    });
});
</script>

<ul id="title">
<li><spring:message code="${title}" /></li>
</ul>
<p><spring:message code="${message}" /></p>
<p><button id="ok" type="button" class="subbutton">OK</button></p>
