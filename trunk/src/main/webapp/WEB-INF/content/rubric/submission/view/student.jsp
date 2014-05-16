<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="assignment" value="${submission.assignment}" />
<c:set var="section" value="${assignment.section}" />
<c:set var="rubric" value="${assignment.rubric}" />

<script>
$(function(){
    $("#ok").click(function(){
        window.location.href = "list?assignmentId=${assignment.id}";
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/evaluated#section-${section.id}' />">${section.course.code}
  - ${section.number}</a></li>
<li><a class="bc" href="list?assignmentId=${assignment.id}"><csns:truncate
  value="${assignment.name}" length="50" /></a></li>
<li><csns:truncate value="${submission.student.name}" length="25" /></li>
</ul>

<c:if test="${not assignment.published}">
<p>This assignment is not published yet.</p>
</c:if>

<p><button id="ok" class="subbutton">OK</button></p>
