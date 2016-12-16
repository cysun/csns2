<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/user/search' />">Users</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/people#group' />">${group.department.name}</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/group/view?id=${group.id}' />">${group.name}</a></li>
<li>Import Users</li>
</ul>

<p>Please examine the records and see if they are parsed correctly. If everything
looks fine, click the Import button to import the data.</p>

<table class="viewtable autowidth">
  <tr>
    <th></th><th>CIN</th><th>First Name</th><th>Last Name</th><th>New Account</th><th>New Member</th>
  </tr>
  <c:forEach items="${importer.importedUsers}" var="user" varStatus="status">
  <tr>
    <td>${status.index+1}</td>
    <td>${user.cin}</td>
    <td>${user.firstName}</td>
    <td>${user.lastName}</td>
    <td>${user.newAccount}</td>
    <td>${user.newMember}</td>
  </tr>
  </c:forEach>
</table>

<form:form modelAttribute="importer">
<input type="hidden" name="_page" value="1" />
<p><button class="subbutton" name="_target" value="0">Back</button>
<input type="submit" name="_finish" value="Import" class="subbutton" /></p>
</form:form>
