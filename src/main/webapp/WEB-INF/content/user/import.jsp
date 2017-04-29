<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
function selectImport()
{
    if( $("select[name='import']").val() == "grade" )
    {
        $("#user-import").hide();
        $("#grade-import").show();
    }
    else
    {
        $("#grade-import").hide();
        $("#user-import").show();
    }
}
$(function(){
    $("#loading").hide();
    $("form").submit(function(){
        $("#loading").show();
    });
    selectImport();
    $("select[name='import']").change(selectImport);
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<c:if test="${not empty department}">
  <li><a class="bc" href="<c:url value='/department/${dept}/people' />">${department.name}</a></li>
</c:if>
<li>Import</li>
</ul>

<select name="import">
  <option value="user">Import Users</option>
  <option value="grade" <c:if test="${param.type == 'grade'}">selected</c:if>>Import Grades</option>
</select>

<div id="user-import">
<p>Please upload the Excel file (.xls or .xlsx) that contains the user data.
The order of the columns does not matter, but the first row of the file must
be a header row with the following column names:</p>
<ul>
  <li>First Name</li>
  <li>Last Name</li>
  <li>CIN</li>
  <li>Email (optional)</li>
  <li>Term (optional)</li>
  <li>Plan (optional)</li>
  <li>Standing (optional)</li>
</ul>
<form action="import" method="post" enctype="multipart/form-data">
File: <input type="file" name="file" style="width: 25%;" class="leftinput" required
  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
  <button class="subbutton">Import</button>
</form>
</div>

<div id="grade-import">
<p>Please upload the Excel file (.xls or .xlsx) that contains the user data.
The order of the columns does not matter, but the first row of the file must
be a header row with the following column names:</p>
<ul>
  <li>ID (i.e. CIN)</li>
  <li>Name, in the form of "Last Name,First Name".</li>
  <li>Email (optional)</li>
  <li>Acad Plan (optional)</li>
  <li>Term</li>
  <li>Subject</li>
  <li>Catalog</li>
  <li>Section</li>
  <li>Grade</li>
</ul>
<form action="importGrades" method="post" enctype="multipart/form-data">
File: <input type="file" name="file" style="width: 25%;" class="leftinput" required
  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
  <button class="subbutton">Import</button>
</form>
</div>

<div id="loading" class="loading">
  <img src="<c:url value='/img/style/loading.gif' />">
</div>
