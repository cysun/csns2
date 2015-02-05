<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="department" value="${importer.department}" />
<c:set var="section" value="${importer.section}" />

<script>
$(function(){
   $("#section\\.course option[value='${section.course.id}']").attr("selected", "selected");
   $("#section\\.instructors option[value='${section.instructors[0].id}']").attr("selected", "selected"); 
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?quarter=${section.quarter.code}' />">${department.name}</a></li>
<li>Import Section</li>
</ul>

<form:form modelAttribute="importer">
<table class="viewtable autowidth">
<tr><th>Quarter</th><th>Course</th><th>Instructor</th></tr>
<tr>
  <td>${section.quarter}</td>
  <td>
    <form:select path="section.course" items="${importer.courses}"
      itemLabel="code" itemValue="id" />
  </td>
  <td>
    <form:select path="section.instructors" items="${importer.instructors}" 
      itemLabel="name" itemValue="id" multiple="false" />
  </td>
</tr>
</table>
<p><input type="hidden" name="_page" value="0" />
<input type="submit" name="_target1" value="Next" class="subbutton" /></p>
</form:form>
