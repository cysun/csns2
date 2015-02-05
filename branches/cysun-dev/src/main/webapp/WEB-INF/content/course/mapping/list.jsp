<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/course/search' />">Courses</a></li>
<li><a class="bc" href="../../courses">${department.name}</a></li>
<li>Course Mappings</li>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="create"><img alt="[Create Course Mapping]"
  title="Create Course Mapping" src="<c:url value='/img/icons/mapping_add.png' />" /></a></li>
</security:authorize>
</ul>

<table class="viewtable">
<thead>
  <tr><th>Group 1</th><th>Group 2</th>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <th></th>
  </security:authorize>
  </tr>
</thead>
<tbody>
<c:forEach items="${mappings}" var="mapping">
<tr>
  <td>
    <c:forEach items="${mapping.group1}" var="course">
      <div class="course-mapping-item">${course.code} ${course.name}</div>
    </c:forEach>
  </td>
  <td>
    <c:forEach items="${mapping.group2}" var="course">
      <div class="course-mapping-item">${course.code} ${course.name}</div>
    </c:forEach>
  </td>
  <security:authorize access="authenticated and principal.isAdmin('${dept}')">
  <td>
    <a href="edit?id=${mapping.id}"><img title="Edit Mapping" alt="[Edit Mapping]"
       border="0" src="<c:url value='/img/icons/mapping_edit.png' />" /></a>
  </td>
  </security:authorize>
</tr>
</c:forEach>
</tbody>
</table>
