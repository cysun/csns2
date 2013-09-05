<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $('#date').datepicker({
        inline: true,
        changeMonth: true,
        changeYear: true,
        yearRange: "-10:+00"
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
<li><a class="bc" href="overview">MFT</a></li>
<li><a class="bc" href="score">Scores</a></li>
<li>Import</li>
</ul>

<form:form modelAttribute="importer">
<table class="general autowidth">
<tr>
  <th><csns:help name="date">Date</csns:help></th>
  <td>
    <form:input path="date" cssClass="forminput" cssStyle="width: 94px;" placeholder="MM/DD/YYYY" />
    <div class="error"><form:errors path="date" /></div>
  </td>
</tr>
</table>
<form:textarea path="text" rows="20" cols="80" cssStyle="width: 100%; border: 1px solid;" />
<div class="error"><form:errors path="text" /></div>
<p>
  <input type="hidden" name="_page" value="0" />
  <input type="submit" name="_target1" value="Next" class="subbutton" />
</p>
</form:form>

<div id="help-date" class="help">
The <em>Closed On</em> date on the MFT score report, i.e. the date on which the
students took the test.</div>
