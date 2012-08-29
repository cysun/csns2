<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="questionSheet" value="${survey.questionSheet}"/>

<script>
$(function(){
    $("#publishDate").datepicker();
    $("#closeDate").datepicker();
});
function removeSurvey( id )
{
    var msg = "Are you sure you want to remove this survey?";
    if( confirm(msg) )
        window.location.href = "remove?id=" + id;
}
</script>

<ul id="title">
<li><a class="bc" href="list">Surveys</a></li>
<li><csns:truncate value="${survey.name}" /></li>
<li class="align_right"><a href="editQuestionSheet?surveyId=${survey.id}"><img title="Edit Questions"
  alt="[Edit Questions]" src="<c:url value='/img/icons/page_edit.png' />" /></a></li>
<li class="align_right"><a href="javascript:removeSurvey(${survey.id})"><img title="Remove Survey"
  alt="[Remove Survey]" src="<c:url value='/img/icons/script_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="survey">
<table class="general">
  <tr>
    <th>Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Type</th>
    <td>
<c:if test="${survey.published}">${survey.type}</c:if>
<c:if test="${not survey.published}">
      <form:select path="type">
        <form:option value="Anonymous"/>
        <form:option value="Recorded"/>
        <form:option value="Named"/>
      </form:select>
</c:if>
    </td>
  </tr>

  <tr>
    <th>Description</th>
    <td>
      <form:textarea path="questionSheet.description" cssStyle="width: 99%;" rows="15" cols="80" />
    </td>
  </tr>

  <tr>
    <th>Number of Sections</th>
    <td>
      <form:input path="questionSheet.numOfSections" cssClass="leftinput" size="30" maxlength="2" />
    </td>
  </tr>

  <tr>
    <th>Publish Date</th>
    <td>
      <form:input path="publishDate" cssClass="leftinput" size="30" maxlength="30" />
    </td>
  </tr>

  <tr>
    <th>Close Date</th>
    <td>
      <form:input path="closeDate" cssClass="leftinput" size="30" maxlength="30" />
    </td>
  </tr>

  <tr>
    <th></th>
    <td>
      <input class="subbutton" type="submit" name="next" value="Next" />
      <input class="subbutton" type="submit" value="Save" />
    </td>
  </tr>
</table>
</form:form>

<script>
CKEDITOR.replaceAll();
</script>
