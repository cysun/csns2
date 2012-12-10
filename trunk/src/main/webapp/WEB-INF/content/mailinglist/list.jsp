<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul id="title">
<li>Mailing Lists</li>
</ul>

<table class="viewtable">
  <tr><th>Name</th><th>Description</th><th>Subscribers</th></tr>
  <c:forEach items="${department.mailinglists}" var="mailinglist">
  <tr>
    <td><a href="view?id=${mailinglist.id}">${mailinglist.name}</a></td>
    <td>${mailinglist.description}</td>
    <td class="center">${mailinglist.subscriptionCount}</td>
  </tr>
  </c:forEach>
</table>
