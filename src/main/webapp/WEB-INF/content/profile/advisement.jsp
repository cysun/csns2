<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${fn:length(records) == 0}">
<p>No advisement information on record.</p>
</c:if>

<c:if test="${fn:length(records) > 0}">
<table class="general2">
<c:forEach items="${records}" var="record">
<c:if test="${not record.forAdvisorsOnly}">
<tr>
  <th align="left"> ${record.advisor.name}
    <fmt:formatDate value="${record.date}" pattern="yyyy-MM-dd HH:mm:ss" />
  </th>
</tr>
<tr>
 <td>${record.comment}
    <c:if test="${fn:length(record.attachments) > 0}">
    <div class="general-attachments">
    <ul>
      <c:forEach items="${record.attachments}" var="attachment">
      <li><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></li>
      </c:forEach>
    </ul>
    </div>
    </c:if>
</td>
</tr>
</c:if>
</c:forEach>
</table>
</c:if>
