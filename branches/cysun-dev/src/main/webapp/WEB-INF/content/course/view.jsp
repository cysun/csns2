<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/course/search' />">Courses</a></li>
<c:if test="${not empty dept}">
  <li><a class="bc" href="<c:url value='/department/${dept}/courses' />">${fn:toUpperCase(dept)}</a></li>
</c:if>
<li>${course.code}</li>
<security:authorize access="authenticated and (principal.admin or principal.id.toString() == '${course.coordinator.id}')">
<li class="align_right"><a href="edit?id=${course.id}"><img title="Edit Course" alt="[Edit Course]"
    src="<c:url value='/img/icons/table_edit.png' />" /></a></li>
</security:authorize>
</ul>

<table class="general autowidth">
  <tr>
    <th>Department</th>
    <td>${course.department.name}</td>
  <tr>
    <th>Code</th>
    <td>${course.code}</td>
  </tr>
  <tr>
    <th>Name</th>
    <td>${course.name}</td>
  </tr>
  <tr>
    <th>Coordinator</th>
    <td>${course.coordinator.name}</td>
  </tr>
  <tr>
    <th>Description</th>
    <td>
      <c:if test="${course.description != null}">
      <a href="<c:url value='/download?fileId=${course.description.id}' />">View</a>
      </c:if>
    </td>
  </tr>
</table>
