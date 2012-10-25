<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/department/${department.abbreviation}/' />">${department.name}</a></li>
<li>Forums</li>
<security:authorize access="authenticated">
<li class="align_right"><a href="subscriptions"><img alt="[Forum Subscriptions]"
  title="Forum Subscriptons" src="<c:url value='/img/icons/star.png' />" /></a></li>
</security:authorize>
</ul>

<div id="forums_menu"> 

<security:authorize access="authenticated">
<span> 
<c:if test="${empty sessionScope.showAll}">
  <a href="forums?showAll=true">Show All Course Forums</a>
</c:if>
<c:if test="${not empty sessionScope.showAll}">
  <a href="forums?showAll=false">Show Only Subscribed Course Forums</a>
</c:if>
</span>
</security:authorize>
 
<form action="searchPosts.html" method="post">
<input class="input_search" type="text" name="query" />
<input class="subbutton" type="submit" name="search" value="Search" />
</form>

</div>

<h3>Department Forums</h3>
<table class="forums">
<tr><th>Forum</th><th>Topics</th><th>Posts</th><th>Last Post</th></tr>
<c:forEach items="${department.forums}" var="forum" varStatus="status">
<tr<c:if test="${status.index%2 eq 0}"> class="even"</c:if>>
  <td class="cat"><a href="view?id=${forum.id}">${forum.name}</a></td>
  <td width="7%" class="center bg1">${forum.numOfTopics}</td>
  <td width="7%" class="center bg1">${forum.numOfPosts}</td>
  <td width="26%" nowrap="nowrap" class="bg1">
    <div class="last_post">
    <c:if test="${empty forum.lastPost}">No Posts</c:if>
    <c:if test="${not empty forum.lastPost}"><a href="topic/view?idd=${forum.lastPost.topic.id}">${forum.lastPost.shortSubject}</a> <br />
    by ${forum.lastPost.author.username} @ <fmt:formatDate value="${forum.lastPost.date.time}" pattern="HH:mm MMM dd, yyyy" />
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
  <td class="cat"><a href="view?id=${forum.id}">${forum.course.code} ${forum.course.name}</a></td>
  <td width="7%" class="center bg1">${forum.numOfTopics}</td>
  <td width="7%" class="center bg1">${forum.numOfPosts}</td>
  <td width="26%" nowrap="nowrap" class="bg1">
    <div class="last_post">
    <c:if test="${empty forum.lastPost}">No Posts</c:if>
    <c:if test="${not empty forum.lastPost}"><a href="topic/view?idd=${forum.lastPost.topic.id}">${forum.lastPost.shortSubject}</a> <br />
    by ${forum.lastPost.author.username} @ <fmt:formatDate value="${forum.lastPost.date.time}" pattern="HH:mm MMM dd, yyyy" />
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
    <c:if test="${not empty forum.lastPost}"><a href="topic/view?idd=${forum.lastPost.topic.id}">${forum.lastPost.shortSubject}</a> <br />
    by ${forum.lastPost.author.username} @ <fmt:formatDate value="${forum.lastPost.date.time}" pattern="HH:mm MMM dd, yyyy" />
    </c:if>
    </div>
  </td>
</tr>
</c:forEach>
</table>
