<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
function deleteSection()
{
    var msg = "Are you sure you want to delete this section?";
    if( confirm(msg) )
        window.location.href = "deleteSection?surveyId=${survey.id}&sectionIndex=${param.sectionIndex}";
}
</script>

<ul id="title">
<li><a class="bc" href="list">Surveys</a></li>
<li><a class="bc" href="editQuestionSheet?surveyId=${survey.id}&amp;sectionIndex=${param.sectionIndex}"><csns:truncate
  value="${survey.name}" /></a></li>
<li>Section <csns:romanNumber value="${param.sectionIndex+1}" /></li>
<c:if test="${not survey.published}">
<li class="align_right"><a href="javascript:deleteSection()"><img title="Delete Section"
  alt="[Delete Section]" src="<c:url value='/img/icons/page_delete.png' />" /></a></li>
</c:if>
</ul>

<form:form modelAttribute="questionSection">
<form:textarea path="description" />
<p><input class="subbutton" type="submit" value="Save" /></p>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
