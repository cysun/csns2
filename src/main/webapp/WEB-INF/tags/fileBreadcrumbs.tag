<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="file" rtexprvalue="true" type="csns.model.core.File" %>

<security:authorize access="anonymous"><li>Files</li></security:authorize>
<security:authorize access="authenticated">
  <c:if test="${empty file}">
     <li><security:authentication property="principal.firstName" />'s Files</li>
  </c:if>
  <c:if test="${not empty file}">
    <li><a class="bc" href="<c:url value='/file/' />"><security:authentication property="principal.firstName" />'s Files</a></li>
    <c:forEach items="${file.ancestors}" var="ancestor">
      <li><a class="bc" href="<c:url value='/file/view?id=${ancestor.id}' />">${ancestor.name}</a></li>
    </c:forEach>
    <li>${file.name}</li>
  </c:if>
</security:authorize>
