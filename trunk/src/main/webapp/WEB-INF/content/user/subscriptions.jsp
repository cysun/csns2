<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div> <!-- for tab -->

<c:if test="${fn:length(mailinglists) > 0}">
<h3>Mailing Lists</h3>
<table class="viewtable autowidth">
  <tr><th>Department</th><th>Mailing List</th><th>Auto-Subscribed</th></tr>
  <c:forEach items="${mailinglists}" var="subscription">
  <tr>
    <td>${subscription.subscribable.department.name}</td>
    <td><span class="tt">${subscription.subscribable.name}</span></td>
    <td class="center">${subscription.autoSubscribed}</td>
  </tr>
  </c:forEach>
</table>
</c:if>

<c:if test="${fn:length(departmentForums) > 0}">
<h3>Department Forums</h3>
<table class="viewtable autowidth">
  <tr><th>Department</th><th>Forum</th><th>Auto-Subscribed</th></tr>
  <c:forEach items="${departmentForums}" var="subscription">
  <tr>
    <td>${subscription.subscribable.department.name}</td>
    <td>${subscription.subscribable.name}</td>
    <td class="center">${subscription.autoSubscribed}</td>
  </tr>
  </c:forEach>
</table>
</c:if>

<c:if test="${fn:length(courseForums) > 0}">
<h3>Course Forums</h3>
<table class="viewtable autowidth">
  <tr><th>Forum</th><th>Auto-Subscribed</th></tr>
  <c:forEach items="${courseForums}" var="subscription">
  <tr>
    <td>${subscription.subscribable.course.code}
      ${subscription.subscribable.course.name}</td>
    <td class="center">${subscription.autoSubscribed}</td>
  </tr>
  </c:forEach>
</table>
</c:if>

<c:if test="${fn:length(otherForums) > 0}">
<h3>Other Forums</h3>
<table class="viewtable autowidth">
  <tr><th>Forum</th><th>Auto-Subscribed</th></tr>
  <c:forEach items="${otherForums}" var="subscription">
  <tr>
    <td>${subscription.subscribable.name}</td>
    <td class="center">${subscription.autoSubscribed}</td>
  </tr>
  </c:forEach>
</table>
</c:if>

</div>
