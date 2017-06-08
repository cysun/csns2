<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?term=${term.code}' />">${department.name}</a></li>
<li>Import Section</li>
</ul>

<form action="import1" method="get">
<table class="viewtable autowidth">
<tr><th>Term</th><th>Course</th><th>Instructor</th></tr>
<tr>
  <td>${term}</td>
  <td>
    <select name="course">
      <c:forEach items="${courses}" var="course">
        <option value="${course.id}">${course.code}</option>
      </c:forEach>
    </select>
  </td>
  <td>
    <select name="instructor">
      <c:forEach items="${instructors}" var="instructor">
        <option value="${instructor.id}">${instructor.name}</option>
      </c:forEach>
    </select>
  </td>
</tr>
</table>
<input type="hidden" name="term" value="${term.code}" />
<p><input type="submit" name="next" value="Next" class="subbutton" /></p>
</form>
