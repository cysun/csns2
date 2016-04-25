<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#loading").hide();
    $("#file-form").submit(function(){
        $("#loading").show();
    });
});
</script>

<ul id="title">
<li><a class="bc" href="list">Schedules</a></li>
<li><a class="bc" href="view?id=${schedule.id}">${schedule.term}</a></li>
<li>Import Sections</li>
</ul>

<p>Please upload the Excel file (.xls or .xlsx) that contains the sections. The
first row of the file must be a column header row, and the following columns
must be included: Subj, Cat, Sect, Class Nbr, Title, Day, Start, End, Bldg/Room,
and Type. For example:</p>
<table class="general2 autowidth">
<tr>
  <th>Subj</th><th>Cat</th><th>Sect</th><th>Class Nbr</th><th>Title</th>
  <th>Day</th><th>Start</th><th>End</th><th>Bldg/Room</th><th>Type</th>
</tr>
<tr>
  <td>CS</td><td>1010</td><td>01</td><td>90907</td>
  <td>INTRO HIGHER EDUC-COMP SCI</td><td>MW</td><td>11:00:00 AM</td>
  <td>11:50:00 AM</td><td>ET C255E</td><td>LEC</td>
</tr>
</table>

<p style="margin: 1em 0px;"><em>Note that sections that are already in the
schedule will not be imported, and courses that are not in the system will
not be imported either.</em></p>

<form id="file-form" method="post" enctype="multipart/form-data">
File: <input type="file" name="file" style="width: 25%;" class="leftinput" required
  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
  <input type="submit" name="impport" value="Import" class="subbutton" />
</form>

<div id="loading" class="loading">
  <img src="<c:url value='/img/style/loading.gif' />">
</div>
