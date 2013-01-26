<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 <script>
$(function(){
    $("table").tablesorter();
});
</script>

<c:if test="${fn:length(coursesTaken) > 0}">
<table class="viewtable autowidth">
<thead>
  <tr><th>Quarter</th><th>Course</th><th>Instructor</th><th>Grade</th></tr>
</thead>
<tbody>
  <c:forEach items="${coursesTaken}" var="courseTaken">
  <tr>
    <td>${courseTaken.section.quarter}</td>
    <td>${courseTaken.section.course.code}</td>
    <td>${courseTaken.section.instructors[0].name}</td>
    <td><span style="margin-left: 1em;">${courseTaken.grade.symbol}</span></td>
  </tr>
  </c:forEach>
</tbody>
</table>
</c:if>
<c:if test="${fn:length(coursesTaken) == 0}">
<p>No course information on record.</p>
</c:if>
