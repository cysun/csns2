<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="user" rtexprvalue="true" type="csns.model.core.User" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<c:choose>
  <c:when test="${not empty pageScope.user and fn:endsWith(user.email, '@localhost')}">
    <span style="text-decoration: line-through;"><jsp:doBody /></span></c:when>
  <c:otherwise>
    <jsp:doBody />
  </c:otherwise>
</c:choose>
