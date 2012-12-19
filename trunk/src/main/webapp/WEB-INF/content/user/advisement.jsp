<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${fn:length(advisementRecords) == 0}">
<p>No advisement information on record.</p>
</c:if>

<p><a id="addAdvisement" class="toggle" href="javascript:void(0)">Advise</a></p>

<form id="addAdvisementForm" action="advisement/add" enctype="multipart/form-data" method="post">
  <textarea id="t" name="comment" rows="5" cols="80"></textarea>
  <p>For advisors only: <input type="checkbox" name="forAdvisorsOnly" value="true" /></p>
  <p><input type="hidden" name="userId" value="${user.id}" />
  <input type="submit" name="submit" class="subbutton" value="OK" /></p>
</form>

<c:if test="${fn:length(advisementRecords) > 0}">
<table class="general2">
<c:forEach items="${advisementRecords}" var="record">
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
</c:forEach>
</table>
</c:if>
