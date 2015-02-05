<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}"/>

<script>
$(function(){
    document.title = "${section.course.code} ${assignment.alias}";
    $("input[type='text'],textarea").css("background-color", "#f0f0f0");
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taken#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="<c:url value='/submission/view?assignmentId=${assignment.id}' />">${assignment.name}</a></li>
<li>Description</li>
</ul>

${assignment.description.text}
