<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
function selectImport()
{
    if( $("select[name='import']").val() == "grade" )
    {
        $("#user-instructions").hide();
        $("#grade-instructions").show();
        $("#file-form").attr("action", "importGrades");
    }
    else
    {
        $("#grade-instructions").hide();
        $("#user-instructions").show();
        $("#file-form").attr("action", "import");
    }
}
$(function(){
    $("#loading").hide();
    $("#file-form").submit(function(){
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
<%--
  <option value="grade" <c:if test="${param.type == 'grade'}">selected</c:if>>Import Grades</option>
 --%>
</select>

<div id="user-instructions">
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
</div>

<div id="grade-instructions">
<p>Please upload the Excel file (.xls or .xlsx) that contains the user data.
The order of the columns does not matter, but the first row of the file must
be a header row with the following column names:</p>
<ul>
  <li>CIN</li>
  <li>Name, in the form of "Last Name,First Name".</li>
  <li>Email (optional)</li>
  <li>Acad Plan (optional)</li>
  <li>Term</li>
  <li>Subject</li>
  <li>Catalog</li>
  <li>Section</li>
  <li>Grade</li>
</ul>
</div>

<form id="file-form" method="post" enctype="multipart/form-data">
File: <input type="file" name="file" style="width: 25%;" class="leftinput" required
  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
  <input type="submit" name="impport" value="Import" class="subbutton" />
</form>

<div id="loading" class="loading">
  <img src="<c:url value='/img/style/loading.gif' />">
</div>
