<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
   $("#ok").click(function(){
      window.location.href = "../view?id=${message.mailinglist.id}";
   });
});
</script>

<ul id="title">
<li><a class="bc" href="../list">Mailing Lists</a></li>
<li><a class="bc" href="../view?id=${message.mailinglist.id}">${message.mailinglist.name}</a></li>
<li>${message.subject}</li>
</ul>

<table class="general">
<tr>
  <th>Subject</th>
  <td><c:out value="${message.subject}" escapeXml="true" /></td>
</tr>
<tr>
  <th>Author</th>
  <td>${message.author.name}</td>
</tr>
<tr>
  <th>Message:</th>
  <td>${csns:text2html(message.content)}</td>
</tr>
<c:if test="${fn:length(message.attachments) > 0}">
<tr>
  <th>Attachments:</th>
  <td>
  <ul class="iconlist">
  <c:forEach items="${message.attachments}" var="attachment">
    <li class="blue_arrow"><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></li>
  </c:forEach>
  </ul>
  </td>
</tr>
</c:if>
<tr>
  <th></th>
  <td><button id="ok" type="button" class="subbutton">OK</button></td>
</tr>
</table>
