<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}" />
<c:set var="questionSheet" value="${assignment.questionSheet}"/>

<script>
function deleteSection()
{
    var msg = "Are you sure you want to delete this section?";
    if( confirm(msg) )
        window.location.href = "deleteSection?assignmentId=${assignment.id}&sectionIndex=${param.sectionIndex}";
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="edit?id=${assignment.id}">${assignment.name}</a></li>
<li>Section <csns:romanNumber value="${sectionIndex+1}" /></li>
<li class="align_right"><a href="javascript:deleteSection()"><img title="Delete Section"
  alt="[Delete Section]" src="<c:url value='/img/icons/page_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="questionSection">
<form:textarea path="description" />
<p><input class="subbutton" type="submit" value="Save" /></p>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
