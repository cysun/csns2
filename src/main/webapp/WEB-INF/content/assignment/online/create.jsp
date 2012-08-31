<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${assignment.section}"/>

<script>
$(function(){
    $("#publishDate").datetimepicker({
        inline: true,
        showSecond: true,
        timeFormat: 'hh:mm:ss'
    });
    $("#dueDate").datetimepicker({
        inline: true,
        showSecond: true,
        timeFormat: 'hh:mm:ss'
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><a class="bc" href="list?sectionId=${section.id}">Online Assignments</a></li>
<li>Create Assignment</li>
</ul>

<form:form modelAttribute="assignment">
<table class="general">
  <tr>
    <th>Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Alias</th>
    <td>
      <form:input path="alias" cssClass="leftinput" size="30" maxlength="10" />
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
    <th>Due Date</th>
    <td>
      <form:input path="dueDate" cssClass="leftinput" size="30" maxlength="30" />
    </td>
  </tr>

  <tr>
    <th>Available After Due Date</th>
    <td>
      <form:checkbox path="availableAfterDueDate" />
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>

<script>
CKEDITOR.replaceAll();
</script>
