<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="path" required="true" rtexprvalue="true" type="java.lang.String" %>

<c:set var="prefix" value="/wiki/content/" />
<li><a class="bc" href="<c:url value='${prefix}' />">Wiki</a></li>
<c:set var="currentFullPath" value="${prefix}" />
<c:forTokens items="${fn:substringAfter(path,prefix)}" delims="/" var="p" varStatus="status">
  <c:if test="${not status.last}">
    <c:set var="currentPath" value="${p}" />
    <c:set var="currentFullPath" value="${currentFullPath}${currentPath}/" />
  </c:if>
  <c:if test="${status.last}">
    <c:set var="currentPath">${p}<c:if test="${csns:endsWith(path, '/')}">/</c:if></c:set>
    <c:set var="currentFullPath" value="${currentFullPath}${currentPath}" />
  </c:if>
  <li><a class="bc" href="<c:url value='${currentFullPath}' />">${currentPath}</a></li>
</c:forTokens>
