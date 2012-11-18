<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<csns:wikiBreadcrumbs dept="${dept}" path="${revision.page.path}" />
<li>Edit</li>
</ul>

<form:form modelAttribute="revision" enctype="multipart/form-data">
<input type="hidden" name="path" value="${param.path}" />
<table class="general">
<tr>
  <th>Title</th>
  <td>
    <form:input path="subject" cssClass="leftinput" cssStyle="width: 98%;" />
    <div class="error"><form:errors path="subject" /></div>
  </td>
</tr>
<tr>
  <td colspan="2">
    <div class="error"><form:errors path="content" /></div>
    <form:textarea id="textcontent" path="content" rows="10" cols="60" />
  </td>
</tr>
<tr>
  <th>Options</th>
  <td>
    Include Sidebar: <form:checkbox path="includeSidebar" cssStyle="margin-right: 1em;" />
    Locked: <form:checkbox path="page.locked" cssStyle="margin-right: 1em;" />
    Password: <form:input path="page.password" cssClass="leftinput" /> 
  </td>
</tr>
<tr>
  <th></th>
  <td><input type="submit" class="subbutton" name="submit" value="Save" /></td>
</tr>
</table>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
