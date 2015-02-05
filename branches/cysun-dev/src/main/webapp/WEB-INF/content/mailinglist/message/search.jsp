<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function(){
    $("button").click(function(){
       window.location.href = "../view?id=${mailinglist.id}"; 
    });
});
</script>

<ul id="title">
<li><a class="bc" href="../list">Mailing List</a></li>
<li><a class="bc" href="../view?id=${mailinglist.id}">${mailinglist.name}</a></li>
<li>Search Results</li>
</ul>

<c:if test="${fn:length(messages) == 0}">
<p>No messages found.</p>
</c:if>

<c:if test="${fn:length(messages) > 0}">
<p>Found ${fn:length(messages)} message(s) that match <i>${param.term}</i>.</p>

<table class="general">
<c:forEach items="${messages}" var="message" varStatus="status">
<tr>
  <th>Message ${status.index+1}:</th>
  <td><a class="link" href="view?id=${message.id}">${message.subject}</a>, sent by ${message.author.name}.</td>
</tr>
<tr>
  <th></th>
  <td>${message.content} ...</td>
</tr>
</c:forEach>
</table>
</c:if>
<p><button type="button" class="subbutton">OK</button></p>
