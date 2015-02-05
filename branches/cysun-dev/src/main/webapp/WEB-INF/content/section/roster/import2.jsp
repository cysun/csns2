<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="section" value="${rosterImporter.section}"/>

<script>
$(function(){
    $("#tabs").tabs({
        cache: false
    });
    $("#finish").click(function(){
       window.location.href = "<c:url value='/section/roster?id=${section.id}' />"; 
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
  <tr>
    <th></th><th>CIN</th><th>Last Name</th><th>First Name</th><th>Middle Name</th>
    <th>Added to Class</th><th>New Account</th>
  </tr>
  <c:forEach items="${rosterImporter.importedStudents}" var="student" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${student.cin}</td>
    <td>${student.lastName}</td>
    <td>${student.firstName}</td>
    <td>${student.middleName}</td>
    <td>${student.newEnrollment}</td>
    <td>${student.newAccount}</td>
  </tr>
  </c:forEach>
</table>
<p><button id="finish" class="subbutton">Finish</button></p>
</div>
