<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script>
$(function(){
<c:if test="${not empty subscription}">
    $("#subscribe").hide();
</c:if>
<c:if test="${empty subscription}">
    $("#unsubscribe").hide();
</c:if>
});

function subscribe()
{
    $.ajax({
        url: "<c:url value='/subscription/mailinglist/subscribe' />",
        data: { "id": ${mailinglist.id}, "ajax": true },
        success: function(){
            $("#subscribe").hide();
            $("#unsubscribe").show();
        },
        cache: false
    });
}

function unsubscribe()
{
    $.ajax({
        url: "<c:url value='/subscription/mailinglist/unsubscribe' />",
        data: { "id": ${mailinglist.id}, "ajax": true },
        success: function(){
            $("#unsubscribe").hide();
            $("#subscribe").show();
        },
        cache: false
    });
}
</script>

<ul id="title">
<li><a class="bc" href="list">Mailing Lists</a></li>
<li>${mailinglist.name}</li>
<security:authorize access="authenticated">
<c:if test="${isFaculty}">
<li class="align_right"><a id="email" href="message/compose?listId=${mailinglist.id}"><img title="Email to List"
    alt="[Email to List]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</c:if>
<li id="subscribe" class="align_right"><a href="javascript:subscribe()"><img alt="[Subscribe to List]"
  title="Subscribe to This Mailing List" src="<c:url value='/img/icons/star_add.png' />" /></a></li>
<li id="unsubscribe" class="align_right"><a href="javascript:unsubscribe()"><img alt="[Unsubscribe from List]"
  title="Unsubscribe from This Mailing List" src="<c:url value='/img/icons/star_delete.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='/profile#ui-tabs-4' />"><img alt="[Mailing List Subscriptions]"
  title="Mailing List Subscriptions" src="<c:url value='/img/icons/star.png' />" /></a></li>
</security:authorize>
</ul>

<c:if test="${fn:length(messages) == 0}">
No messages were sent to this mailing list.
</c:if>

<c:if test="${fn:length(messages) > 0}">
<form action="message/search" method="get">
<p class="right"><input  name="term" type="text" class="leftinput" style="width: 15em;" />
<input name="listId" type="hidden" value="${mailinglist.id}" />
<input name="search" type="submit" value="Search" class="subbutton" /></p>
</form>

<table class="viewtable">
<tr><th>Author</th><th>Subject</th><th>Date</th></tr>
<c:forEach items="${messages}" var="message">
<tr>
  <td>${message.author.name}</td>
  <td><a href="message/view?id=${message.id}">${message.subject}</a></td>
  <td class="center"><fmt:formatDate value="${message.date}" pattern="MM/dd/yyyy" /></td>
</tr>
</c:forEach>
</table>
</c:if>
