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
  <li><a href="add?sectionId=${section.id}">Add</a></li>
</ul>

<div id="import">
<p>Please copy and paste the class roster from GET to the text area below:</p>

<form:form modelAttribute="rosterImporter">
  <form:textarea path="text" rows="20" cols="80" cssStyle="width: 100%; border: 1px solid;" />
  <p><input type="hidden" name="_page" value="0" />
  <input type="submit" name="_target1" value="Next" class="subbutton" /></p>
</form:form>
</div>

</div>
