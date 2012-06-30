<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table class="viewtable">
<tr><th>CIN</th><th>Name</th><th>Primary Email</th></tr>
<c:forEach items="${users}" var="user">
<tr>
  <td><c:if test="${not user.cinEncrypted}">${user.cin}</c:if><br /></td>
  <td>${user.name}</td>
  <td>${user.primaryEmail}</td>
</tr>
</c:forEach>
</table>
