<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li><a href="<c:url value='${section.siteUrl}/block/list' />" class="bc">Blocks</a></li>
<li>Add Block</li>
</ul>

<form:form modelAttribute="block">
<table class="general">
  <tr>
    <th class="shrink">Block Type</th>
    <td>
      <form:select path="type">
        <form:options items="${blockTypes}" />
      </form:select>
  </tr>
  <tr>
    <th>Block Name</th>
    <td>
      <form:input path="name" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>
  <tr>
    <th></th>
    <td><input class="subbutton" type="submit" name="add" value="Add" /></td>
  </tr>
</table>
</form:form>
