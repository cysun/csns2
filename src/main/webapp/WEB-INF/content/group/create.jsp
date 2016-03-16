<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">${group.department.name}</a></li>
<li>New User Group</li>
</ul>

<form:form modelAttribute="group">
<table class="general">
  <tr>
    <th class="shrink">Name *</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 30%;" maxlength="50" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th>Short Description</th>
    <td>
      <form:input path="description" cssClass="leftinput" cssStyle="width: 99%;" maxlength="255" />
    </td>
  </tr>
  <tr><th></th><td><input class="subbutton" type="submit" value="Create" /></td></tr>
</table>
</form:form>
