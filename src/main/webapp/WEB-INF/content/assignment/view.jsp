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
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" /></li>
<li class="align_right"><a href="edit?id=${assignment.id}"><img title="Edit Assignment"
  alt="[Edit Assignment]" src="<c:url value='/img/icons/script_edit.png' />" /></a></li>
</ul>

${assignment.description.text}
