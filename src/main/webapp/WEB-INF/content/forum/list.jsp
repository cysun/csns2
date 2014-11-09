<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li>Forums</li>
<security:authorize access="authenticated">
<li class="align_right"><a href="<c:url value='/profile#ui-tabs-4' />"><img alt="[Forum Subscriptions]"
  title="Forum Subscriptons" src="<c:url value='/img/icons/star.png' />" /></a></li>
</security:authorize>
<security:authorize access="authenticated and principal.isAdmin('${dept}')">
<li class="align_right"><a href="create"><img alt="[Create Forum]"
  title="Create Forum" src="<c:url value='/img/icons/comments_add.png' />" /></a></li>
</security:authorize>
</ul>

<security:authorize access="authenticated">
<div id="forums_menu"> 
<span> 
<c:if test="${empty sessionScope.showAll}">
  <a href="list?showAll=true">Show All Course Forums</a>
</c:if>
<c:if test="${not empty sessionScope.showAll}">
  <a href="list?showAll=false">Show Only Subscribed Course Forums</a>
</c:if>
</span>
</div>
</security:authorize>

<h3>${department.fullName} Forums</h3>
<table class="forums">
<tr><th>Forum</th><th>Topics</th><th>Posts</th><th>Last Post</th></tr>
<c:forEach items="${departmentForums}" var="forum" varStatus="status">
<tr<c:if test="${status.index%2 eq 0}"> class="even"</c:if>>
  <td class="cat"><a href="view?id=${forum.id}"><c:if test="${forum.membersOnly}"><img border="0" alt="[Members Only]"
      title="Members Only" src="<c:url value='/img/icons/user_suit.png'/>" />&nbsp;&nbsp;</c:if>${forum.name}</a></td>
  <td width="7%" class="center bg1">${forum.numOfTopics}</td>
  <td width="7%" class="center bg1">${forum.numOfPosts}</td>
  <td width="26%" nowrap="nowrap" class="bg1">
    <div class="last_post">
    <c:if test="${empty forum.lastPost}">No Posts</c:if>
    <c:if test="${not empty forum.lastPost}"><a href="topic/view?id=${forum.lastPost.topic.id}"><csns:truncate
        value="${forum.lastPost.subject}" length="30" /></a> <br />
    by ${forum.lastPost.author.username} @ <fmt:formatDate value="${forum.lastPost.date}" pattern="HH:mm MMM dd, yyyy" />
    </c:if>
    </div>
  </td>
</tr>
</c:forEach>
</table>

<c:if test="${fn:length(courseForums) > 0}">
<h3>Course Forums</h3>
<table class="forums">
<tr><th>Forum</th><th>Topics</th><th>Posts</th><th>Last Post</th></tr>
<c:forEach items="${courseForums}" var="forum" varStatus="status">
<tr<c:if test="${status.index%2 eq 0}"> class="even"</c:if>>
  <td class="cat"><a href="view?id=${forum.id}">${forum.name}</a></td>
  <td width="7%" class="center bg1">${forum.numOfTopics}</td>
  <td width="7%" class="center bg1">${forum.numOfPosts}</td>
  <td width="26%" nowrap="nowrap" class="bg1">
    <div class="last_post">
    <c:if test="${empty forum.lastPost}">No Posts</c:if>
    <c:if test="${not empty forum.lastPost}"><a href="topic/view?id=${forum.lastPost.topic.id}"><csns:truncate
        value="${forum.lastPost.subject}" length="30" /></a> <br />
    by ${forum.lastPost.author.username} @ <fmt:formatDate value="${forum.lastPost.date}" pattern="HH:mm MMM dd, yyyy" />
    </c:if>
    </div>
  </td>
</tr>
</c:forEach>
</table>
</c:if>

<h3>Other Forums</h3>
<table class="forums">
<tr><th>Forum</th><th>Topics</th><th>Posts</th><th>Last Post</th></tr>
<c:forEach items="${systemForums}" var="forum" varStatus="status">
<tr<c:if test="${status.index%2 eq 0}"> class="even"</c:if>>
  <td class="cat"><a href="view?id=${forum.id}">${forum.name}</a></td>
  <td width="7%" class="center bg1">${forum.numOfTopics}</td>
  <td width="7%" class="center bg1">${forum.numOfPosts}</td>
  <td width="26%" nowrap="nowrap" class="bg1">
    <div class="last_post">
    <c:if test="${empty forum.lastPost}">No Posts</c:if>
    <c:if test="${not empty forum.lastPost}"><a href="topic/view?id=${forum.lastPost.topic.id}"><csns:truncate
        value="${forum.lastPost.subject}" length="30" /></a> <br />
    by ${forum.lastPost.author.username} @ <fmt:formatDate value="${forum.lastPost.date}" pattern="HH:mm MMM dd, yyyy" />
    </c:if>
    </div>
  </td>
</tr>
</c:forEach>
</table>
