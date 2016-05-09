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
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<c:if test="${not empty department}">
  <li><a class="bc" href="<c:url value='/department/${dept}/people' />">${department.name}</a></li>
</c:if>
<li>Import Users</li>
</ul>

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

<form id="file-form" method="post" enctype="multipart/form-data">
File: <input type="file" name="file" style="width: 25%;" class="leftinput" required
  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
  <input type="submit" name="impport" value="Import" class="subbutton" />
</form>

<div id="loading" class="loading">
  <img src="<c:url value='/img/style/loading.gif' />">
</div>
