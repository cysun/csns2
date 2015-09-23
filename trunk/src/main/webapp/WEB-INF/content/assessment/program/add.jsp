<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
</script>

<ul id="title">
<li><a class="bc" href="list">Program Assessment</a></li>
<li>Add Program</li>
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

  <tr><th></th><td><input type="submit" class="subbutton" value="Add" /></td></tr>
</table>
</form:form>
