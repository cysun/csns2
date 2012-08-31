<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${assignment.section}"/>

<script>
$(function(){
    $("#dueDate").datetimepicker({
        inline: true,
        showSecond: true,
        timeFormat: 'hh:mm:ss'
    });
    $("select").click(function(){
       if( $(this).val() == "ONLINE")
           window.location.href = "online/list?sectionId=${section.id}";
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Add Assignment</li>
<li class="align_right">
  <select name="assignmentType">
    <option value="REGULAR">Regular</option>
    <option value="ONLINE">Online</option>
  </select>
</li>
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
    <th>Total Points</th>
    <td><form:input path="totalPoints" cssClass="leftinput" size="30" maxlength="255" /></td>
  </tr>

  <tr>
    <th>Allowed File Extensions</th>
    <td>
      <form:input path="fileExtensions" cssClass="leftinput" size="30" maxlength="255" />
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

  <tr><th></th><td><input class="subbutton" type="submit" value="Add" /></td></tr>
</table>
</form:form>
