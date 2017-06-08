<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#loading").hide();
    $("#back").click(function(){
        $.ajax({
            url: "import-cancel",
            data: {
                "_cid": "${_cid}"
            }
        });
        history.back();
    });
    $("#save").click(function(){
        $.ajax({
           url: "import-confirm",
           data: {
               "_cid": "${_cid}",
               term: "${term.code}",
               course: "${course.id}",
               instructor: "${instructor.id}",
               number: "${number}"
           },
           success: function(data){
               window.location.href="../section?id="+data;
           }
        });
        $("#loading").show();
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?term=${term.code}'
    />">${department.name}</a></li>
<li>Import Section</li>
</ul>

<p>Please examine the imported records. If everything
looks fine, click the Save button to save them; otherwise click the Cancel
button to start over.</p>

<table class="viewtable autowidth">
  <tr><th>Term</th><th>Course</th><th>Instructor</th><th>Section</th></tr>
  <tr>
    <td>${term}</td>
    <td>${course.code}</td>
    <td>${instructor.name}</td>
    <td>
      <c:if test="${number == -1}">NEW</c:if>
      <c:if test="${number != -1}">${number}</c:if>
    </td>
  </tr>
</table>

<p></p>

<table class="viewtable autowidth">
  <tr>
    <th></th><th>CIN</th><th>Last Name</th><th>First Name</th><th>Middle Name</th><th>Grade</th>
    <c:if test="${number > 0}">
      <th>Old Grade</th><th>Add to Class</th><th>New Account</th>
    </c:if>
  </tr>
  <c:forEach items="${importedUsers}" var="importedUser" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${importedUser.cin}</td>
    <td>${importedUser.lastName}</td>
    <td>${importedUser.firstName}</td>
    <td>${importedUser.middleName}</td>
    <td>${importedUser.grade}</td>
    <c:if test="${number > 0}">
      <td>${importedUser.oldGrade}</td>
      <td>${importedUser.newEnrollment}</td>
      <td>${importedUser.newAccount}</td>
    </c:if>
  </tr>
  </c:forEach>
</table>

<p>
  <button id="back" class="subbutton">Back</button>
  <button id="save" class="subbutton">Save</button>
</p>

<div id="loading" class="loading">
  <img src="<c:url value='/img/style/loading.gif' />">
</div>

