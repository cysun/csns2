<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
function removeProgram()
{
    var msg = "Are you sure you want to remove this program?";
    if( confirm(msg) )
        window.location.href = "remove?id=${program.id}";
}
</script>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="view?id=${program.id}" class="bc"><csns:truncate value="${program.name}" length="50" /></a></li>
<li>Edit Program</li>
<li class="align_right"><a href="javascript:removeProgram()"><img alt="[Remove Program]"
  title="Remove Program" src="<c:url value='/img/icons/report_delete.png' />" /></a></li>
</ul>

<form:form modelAttribute="program">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Vision Statement</th>
    <td><form:textarea path="vision" rows="5" cols="80" /></td>
  </tr>

  <tr>
    <th>Mission Statement</th>
    <td><form:textarea path="mission" rows="5" cols="80" /></td>
  </tr>

  <tr><th></th><td><input type="submit" class="subbutton" value="Save" /></td></tr>
</table>
</form:form>
