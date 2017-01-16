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
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="list">${program.department.name}</a></li>
<li><a class="bc" href="view?id=${program.id}">${program.name}</a></li>
<li>Edit</li>
</ul>

<form:form modelAttribute="program">
<table class="general">
  <tr>
    <th class="shrink">Name *</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" required="true" />
    </td>
  </tr>
  <tr>
    <th>Description</th>
    <td><form:textarea path="description" rows="5" cols="80" /></td>
  </tr>
  <tr>
    <th></th>
    <td><input type="submit" class="subbutton" value="Save" /></td>
  </tr>
</table>
</form:form>
