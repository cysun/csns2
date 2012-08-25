<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="questionSheet" value="${survey.questionSheet}"/>

<script>
$(function(){
    $("#publishDate").datepicker();
    $("#closeDate").datepicker();
});
</script>

<ul id="title">
<li><a class="bc" href="list">Surveys</a></li>
<li>Create Survey</li>
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
      <form:select path="type">
        <form:option value="Anonymous"/>
        <form:option value="Recorded"/>
        <form:option value="Named"/>
      </form:select>
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

  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>

<script>
CKEDITOR.replaceAll();
</script>
