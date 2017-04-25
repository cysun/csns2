<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/user/import?type=grade' />">Import</a></li>
<li>Grade Import Results</li>
</ul>

<table class="general autowidth">
<tr>
  <th>Entries Processed</th><td>${results.entriesProcessed}</td>
</tr>
<tr>
  <th>Accounts Created</th><td>${results.accountsCreated}</td>
</tr>
<tr>
  <th>Grades Added</th><td>${results.gradesAdded}</td>
</tr>
<tr>
  <th>Grades Updated</th><td>${results.gradesUpdated}</td>
</tr>
<tr>
  <th>Nonexistent Courses</th>
  <td>
    <c:if test="${fn:length(results.nocourses) > 0}">
      <ol>
        <c:forEach items="${results.nocourses}" var="course">
        <li>${course}</li>
        </c:forEach>
      </ol>
    </c:if>
  </td>
</tr>
</table>
