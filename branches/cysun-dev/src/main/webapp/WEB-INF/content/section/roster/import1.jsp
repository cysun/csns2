<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${rosterImporter.section}"/>

<script>
$(function(){
    $("#tabs").tabs({
        cache: false
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Enroll Students</li>
</ul>

<div id="tabs">
<ul>
  <li><a href="#import">Import</a></li>
</ul>
</div>

<div id="import">
<table class="viewtable">
  <tr><th></th><th>CIN</th><th>Last Name</th><th>First Name</th><th>Middle Name</th></tr>
  <c:forEach items="${rosterImporter.importedStudents}" var="student" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${student.cin}</td>
    <td>${student.lastName}</td>
    <td>${student.firstName}</td>
    <td>${student.middleName}</td>
  </tr>
  </c:forEach>
</table>

<form:form modelAttribute="rosterImposter"><p>
  <input type="hidden" name="_page" value="1" />
  <input type="submit" name="_target0" value="Back" class="subbutton" />
  <input type="submit" name="_finish" value="Import" class="subbutton" />
</p></form:form>
</div>
