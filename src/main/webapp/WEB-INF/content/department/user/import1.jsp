<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="department" value="${importer.department}" />

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/people' />">${department.name}</a></li>
<li>Import Students</li>
</ul>

<p>Please examine the records and see if they are parsed correctly. If everything
looks fine, click the Import button to import the data.</p>

<p>Standing: ${importer.standing.symbol}</p>

<table class="viewtable autowidth">
  <tr>
    <th></th><th>Quarter</th><th>CIN</th><th>First Name</th><th>Last Name</th><th>New Account</th>
  </tr>
  <c:forEach items="${importer.importedStudents}" var="student" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${student.quarter}</td>
    <td>${student.cin}</td>
    <td>${student.firstName}</td>
    <td>${student.lastName}</td>
    <td>${student.newAccount}</td>
  </tr>
  </c:forEach>
</table>

<form:form modelAttribute="importer">
<input type="hidden" name="_page" value="1" />
<p><input type="submit" name="_target0" value="Back" class="subbutton" />
<input type="submit" name="_finish" value="Import" class="subbutton" /></p>
</form:form>
