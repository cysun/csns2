<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#course-code").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            if( ui.item )
                $("<input>").attr({
                    type: "hidden",
                    name: "course",
                    value: ui.item.id
                }).appendTo("form");
        }
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
<li><a class="bc" href="../schedule/list">Schedules</a></li>
<li><a class="bc" href="../schedule/view?id=${section.schedule.id}">${section.schedule.term}</a></li>
<li>Add Section</li>
</ul>

<form:form modelAttribute="section">
<table class="general autowidth">
  <tr>
    <th>Course *</th>
    <td>
      <input id="course-code" name="course-code" class="forminput"
             style="width: 150px;" required="required" />
    </td>
  </tr>
  <tr>
    <th>Section Number *</th>
    <td>
      <form:input path="sectionNumber" cssClass="forminput"
                  cssStyle="width: 150px;" required="required" />
    </td>
  </tr>
  <tr>
    <th>Type *</th>
    <td>
      <form:select path="type">
        <form:option value="LEC" />
        <form:option value="LAB" />
        <form:option value="REC" />
        <form:option value="SUP" />
      </form:select>
    </td>
  </tr>
  <tr>
    <th>Capacity *</th>
    <td>
      <form:input path="capacity" cssClass="forminput" cssStyle="width: 150px;"
                  required="required" />
    </td>
  </tr>
  <tr>
    <th>Class Number</th>
    <td>
      <form:input path="classNumber" cssClass="forminput" cssStyle="width: 150px;" />
    </td>
  </tr>
  <tr>
    <th><csns:help name="days">Days</csns:help></th>
    <td>
      <form:input path="days" cssClass="forminput" cssStyle="width: 150px;" />
    </td>
  </tr>
  <tr>
    <th>Start Time</th>
    <td>
      <form:input path="startTime" cssClass="forminput" cssStyle="width: 150px;" />
    </td>
  </tr>
  <tr>
    <th>End Time</th>
    <td>
      <form:input path="endTime" cssClass="forminput" cssStyle="width: 150px;" />
    </td>
  </tr>
  <tr>
    <th>Location</th>
    <td>
      <form:input path="location" cssClass="forminput" cssStyle="width: 150px;" />
    </td>
  </tr>
  <tr>
    <th><csns:help name="notes">Notes</csns:help></th>
    <td>
      <form:input path="notes" cssClass="forminput" cssStyle="width: 600px;" />
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" value="Add" />
    </td>
  </tr>
</table>
</form:form>

<div id="help-days" class="help">MTWRF for Monday, Tuesday, Wednesday, Thursday,
and Friday.</div>

<div id="help-notes" class="help">Additional information about the section.
For example, if this is a special topic class, you may want to specify the
topic here.</div>
