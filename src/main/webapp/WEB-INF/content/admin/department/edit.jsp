<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a class="bc" href="list">Departments</a></li>
<li>Edit Department</li>
</ul>

<form:form modelAttribute="department">
<table class="general">
  <tr>
    <th>Name:</th>
    <td>
      <form:input path="name" cssClass="leftinput" size="40" maxlength="255" />
      <div class="error"><form:errors path="name" /></div>
    </td>
  </tr>

  <tr>
    <th>Abbreviation:</th>
    <td>
      <form:input path="abbreviation" cssClass="leftinput" size="40" maxlength="10" />
      <div class="error"><form:errors path="abbreviation" /></div>
    </td>
  </tr>

  <tr>
    <th>Administrators:</th>
    <td>
      <c:forEach items="${department.administrators}" var="administrator" varStatus="status">
      ${administrator.name}<c:if test="${not status.last}">, </c:if>
      </c:forEach>
    </td>
  </tr>

  <tr><th></th><td><input class="subbutton" type="submit" value="Save" /></td></tr>
</table>
</form:form>
