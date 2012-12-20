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
    $("select[name='assignmentType']").click(function(){
       if( $(this).val() == "ONLINE")
           window.location.href = "online/create?sectionId=${section.id}";
    });

    $(".res").hide();
    if($("#description\\.type").val() != "None")
        $("#res"+$("#description\\.type").val()).show();
    $("#description\\.type").click(function(){
        $(".res").hide();
        $("#res"+$(this).val()).show();
    });

    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Create Assignment
  <select name="assignmentType" style="margin-left: 2em;">
    <option value="REGULAR">Regular</option>
    <option value="ONLINE">Online</option>
  </select>
</li>
</ul>

<form:form modelAttribute="assignment" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Description</th>
    <td>
      <form:select path="description.type">
        <form:options items="${resourceTypes}" />
      </form:select>
  </tr>

  <tr id="resTEXT" class="res">
    <th></th>
    <td>
      <form:textarea path="description.text" rows="5" cols="80" />
      <div class="error"><form:errors path="description.text" /></div>
    </td>
  </tr>

  <tr id="resFILE" class="res">
    <th></th>
    <td>
      <input name="file" type="file" size="80" style="width: 99%;" class="leftinput">
      <div class="error"><form:errors path="description.file" /></div>
    </td>
  </tr>

  <tr id="resURL" class="res">
    <th></th>
    <td>
      <form:input path="description.url" cssClass="leftinput" cssStyle="width: 99%;" placeholder="http://" />
      <div class="error"><form:errors path="description.url" /></div>
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
