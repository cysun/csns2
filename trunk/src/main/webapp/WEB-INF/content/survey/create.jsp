<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<c:set var="questionSheet" value="${survey.questionSheet}"/>

<script>
$(function(){
    $("#publishDate").datepicker();
    $("#closeDate").datepicker();
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
    <th><csns:help name="type">Type</csns:help></th>
    <td>
      <form:select path="type">
        <form:options items="${surveyTypes}" />
      </form:select>
    </td>
  </tr>

  <tr>
    <th><csns:help name="description">Description</csns:help></th>
    <td>
      <form:textarea path="questionSheet.description" cssStyle="width: 99%;" rows="15" cols="80" />
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
    <th><csns:help name="closedate">Close Date</csns:help></th>
    <td>
      <form:input path="closeDate" cssClass="leftinput" size="30" maxlength="30" />
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>

<div id="help-type" class="help">
<p>There are three types of surveys: <em>Anonymous</em>, <em>Named</em>,
and <em>Recorded</em>.</p>
<p>An <em>anonymous</em> survey is open to the public, i.e. no CSNS account
is required.</p>
<p>A <em>named</em> survey requires the users to log onto CSNS to take the survey.
The system records the identity of the user for each survey response.</p>
<p>A <em>recorded</em> survey also requires the users to log onto CSNS to take
the survey, but the system only records whether a user has taken a survey or
not, i.e. a survey response is not connected to a particular user to
maintain some level of anonymity.</p></div>

<div id="help-description" class="help">
One or two paragraphs that explain the purpose of the survey and
give the instructions on how to take the survey.</div>

<div id="help-section" class="help">
A survey may have multiple <em>sections</em>, each with its own description
and a list of questions. Each section will be displayed on a separate page.</div>

<div id="help-pubdate" class="help">
<em>Publish Date</em> controls when the survey is made available to the
public.</div>

<div id="help-closedate" class="help">
The survey will be closed automatically at midnight on the <em>close date</em>.
The close date can be changed at any time.</div>
