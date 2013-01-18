<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="department" value="${importer.department}" />
<c:set var="section" value="${importer.section}" />

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?quarter=${section.quarter.code}' />">${department.name}</a></li>
<li>Import Section</li>
</ul>

<p>Please examine the records and see if they are parsed correctly. If everything
looks fine, click the Import button to import the data.</p>

<table class="viewtable autowidth">
  <tr><th>Quarter</th><th>Course</th><th>Instructor</th><th>Section</th></tr>
  <tr>
    <td>${section.quarter}</td>
    <td>${section.course.code}</td>
    <td>${section.instructors[0].name}</td>
    <td>
      <c:if test="${section.number == -1}">NEW</c:if>
      <c:if test="${section.number != -1}">${section.number}</c:if>
    </td>
  </tr>
</table>

<p></p>

<table class="viewtable autowidth">
  <tr>
    <th></th><th>CIN</th><th>Last Name</th><th>First Name</th><th>Middle Name</th><th>Grade</th>
    <c:if test="${section.number > 0}">
      <th>Old Grade</th><th>Add to Class</th><th>New Account</th>
    </c:if>
  </tr>
  <c:forEach items="${importer.importedStudents}" var="student" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${student.cin}</td>
    <td>${student.lastName}</td>
    <td>${student.firstName}</td>
    <td>${student.middleName}</td>
    <td>${student.grade}</td>
    <c:if test="${section.number > 0}">
      <td>${student.oldGrade}</td>
      <td>${student.newEnrollment}</td>
      <td>${student.newAccount}</td>
    </c:if>
  </tr>
  </c:forEach>
</table>

<form:form modelAttribute="importer">
<input type="hidden" name="_page" value="2" />
<p><input type="submit" name="_target1" value="Back" class="subbutton" />
<input type="submit" name="_finish" value="Import" class="subbutton" /></p>
</form:form>
