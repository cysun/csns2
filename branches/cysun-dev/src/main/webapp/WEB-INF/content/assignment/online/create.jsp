<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="section" value="${assignment.section}"/>

<script>
$(function(){
    $("#publishDate").datetimepicker({
        timeFormat: 'HH:mm:ss'
    });
    $("#dueDate").datetimepicker({
        timeFormat: 'HH:mm:ss'
    });
    $("select[name='assignmentType']").click(function(){
        if( $(this).val() == "REGULAR" )
            window.location.href = "../create?sectionId=${section.id}";
        else if( $(this).val() == "RUBRIC" )
            window.location.href = "../../rubric/assignment/create?sectionId=${section.id}";
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
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Create Assignment
  <select name="assignmentType" style="margin-left: 2em;">
    <option value="ONLINE">Online</option>
    <option value="REGULAR">Regular</option>
    <option value="RUBRIC">Rubric</option>
  </select>
</li>
<li class="align_right">
  <form action="search" method="get">
    <input name="term" type="text" size="40" />
    <input name="sectionId" type="hidden" value="${param.sectionId}" />
    <input name="search" type="submit" value="Search" class="subbutton" />
  </form>
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
    <th>Description</th>
    <td>
      <form:textarea path="questionSheet.description" cssStyle="width: 99%;" rows="15" cols="80" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="alias">Alias</csns:help></th>
    <td>
      <form:input path="alias" cssClass="leftinput" size="30" maxlength="10" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="section">Number of Sections</csns:help></th>
    <td>
      <form:input path="questionSheet.numOfSections" cssClass="leftinput" size="30" maxlength="2" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="pubdate">Publish Date</csns:help></th>
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
    <th><csns:help name="aadd">Available After Due Date</csns:help></th>
    <td>
      <form:checkbox path="availableAfterDueDate" />
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>

<div id="help-alias" class="help">The <em>alias</em> of an assignment is a
shorthand of the assignment name, e.g. "HW1" for "Homework 1". It's used
as column title in the grade sheet.</div>

<div id="help-section" class="help">
An online assignment may have multiple <em>sections</em>, each with its own
description and a list of questions. Each section is displayed on a separate
page.</div>

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
it and then disabling it later.</p>
</div>
