<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}"/>

<script>
$(function(){
    $("#publishDate").datetimepicker({
        timeFormat: 'HH:mm:ss'
    });
    $('#dueDate').datetimepicker({
        timeFormat: 'HH:mm:ss'
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
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
});
function deleteAssignment( id )
{
    var msg = "Are you sure you want to delete this assignment?";
    if( confirm(msg) )
        window.location.href = "delete?id=" + id;
}
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li><csns:truncate value="${assignment.name}" length="60" /></li>
<c:if test="${assignment.online}">
<li class="align_right"><a href="online/editQuestionSheet?assignmentId=${assignment.id}"><img title="Edit Questions"
  alt="[Edit Question]" src="<c:url value='/img/icons/page_edit.png' />" /></a></li>
</c:if>
<li class="align_right"><a href="javascript:deleteAssignment(${assignment.id})"><img title="Delete Assignment"
  alt="[Delete Assignment]" src="<c:url value='/img/icons/script_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="assignment" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
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
      <c:if test="${assignment.description.type == 'FILE' and assignment.description.file != null}">
        <span style="margin-left: 2em;">
        <a href="<c:url value='/download?fileId=${assignment.description.file.id}' />">${assignment.description.file.name}</a>
        </span>
      </c:if>
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
    <th><csns:help name="alias">Alias</csns:help></th>
    <td><form:input path="alias" cssClass="leftinput" size="30" maxlength="10" /></td>
  </tr>

  <tr>
    <th>Total Points</th>
    <td><form:input path="totalPoints" cssClass="leftinput" size="30" maxlength="255" /></td>
  </tr>

  <tr>
    <th><csns:help name="filext">Allowed File Extensions</csns:help></th>
    <td><form:input path="fileExtensions" cssClass="leftinput" size="30" maxlength="255" /></td>
  </tr>

  <c:if test="${not assignment.published}">
  <tr>
    <th><csns:help name="pubdate">Publish Date</csns:help></th>
    <td><form:input path="publishDate" cssClass="leftinput" size="30" maxlength="30" /></td>
  </tr>
  </c:if>

  <tr>
    <th>Due Date</th>
    <td><form:input path="dueDate" cssClass="leftinput" size="30" maxlength="30" /></td>
  </tr>

  <tr>
    <th><csns:help name="aadd">Available After Due Date</csns:help></th>
    <td><form:checkbox path="availableAfterDueDate" /></td>
  </tr>

  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="save" value="Save" /></td>
  </tr>
</table>
</form:form>

<div id="help-alias" class="help">The <em>alias</em> of an assignment is a
shorthand of the assignment name, e.g. "HW1" for "Homework 1". It's used
as column title in the grade sheet.</div>

<div id="help-filext" class="help">Use space to separate the file extensions
allowed for this assignment, e.g. <span class="tt">txt java html</span>. If
this field is left empty, the students may upload files with any extension.</div>

<div id="help-pubdate" class="help">
<em>Publish Date</em> controls when the assignment is made available to the
students. For example, if you want to conduct a quiz from 9pm to 9:30pm on
3/3/2010, you can set the publish date to be 3/3/2010 21:00 and the due date
to be 3/3/2010 21:30.</div>

<div id="help-aadd" class="help">
<p><em>Viewable After Due Date</em> determines whether the students can view
the assignment and their own solutions after the due date. If you do not want
the students to see the assignment after the due date (e.g. you may want to
recycle the assignment questions later), then uncheck this option.</p>
<p>Note that this option can be changed any time, which means that you can give
the students temporary access to the assignment after the due date by enabling
it and then disabling it later.</p></div>
