<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("#preregStart").datepicker();
    $("#preregEnd").datepicker();
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
function removeSchedule( id )
{
    var msg = "Are you sure you want to remove this schedule?";
    if( confirm(msg) )
        window.location.href = "remove?id=" + id;
}
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<ul id="title">
<li><a class="bc" href="list">Schedules</a></li>
<li><a class="bc" href="view?id=${schedule.id}">${schedule.term}</a></li>
<li>Edit Schedule</li>
<li class="align_right"><a href="javascript:removeSchedule(${schedule.id})"><img title="Remove Schedule"
  alt="[Remove Schedule]" src="<c:url value='/img/icons/calendar_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="schedule">
<table class="general">
  <tr>
    <th class="shrink">Term</th>
    <td>
      <form:select path="term" items="${terms}" itemValue="code" itemLabel="fullName" />
    </td>
  </tr>

  <tr>
    <th>Description</th>
    <td>
      <form:textarea path="description" rows="5" cols="80" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="start">Pre-Registration Start</csns:help></th>
    <td>
      <form:input path="preregStart" cssClass="leftinput" size="10" maxlength="10" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="end">Pre-Registration End</csns:help></th>
    <td>
      <form:input path="preregEnd" cssClass="leftinput" size="10" maxlength="10" />
    </td>
  </tr>

  <tr>
    <th>Default Section Capacity</th>
    <td>
      <form:input path="defaultSectionCapacity" cssClass="leftinput" size="5" maxlength="5" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="undergradLimit">Default Registration Limit for Undergrad</csns:help></th>
    <td>
      <form:input path="defaultUndergradRegLimit" cssClass="leftinput" size="5" maxlength="5" />
    </td>
  </tr>

  <tr>
    <th><csns:help name="gradLimit">Default Registration Limit for Grad</csns:help></th>
    <td>
      <form:input path="defaultGradRegLimit" cssClass="leftinput" size="5" maxlength="5" />
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>

<div id="help-start" class="help">
The date when the students can start pre-registration.
</div>

<div id="help-end" class="help">
The date when pre-registration closes.
</div>

<div id="help-undergradLimit" class="help">
The max number of classes an undergraduate student can pre-register.
</div>

<div id="help-gradLimit" class="help">
The max number of classes a graduate student can pre-register.
</div>
