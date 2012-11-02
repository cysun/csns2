<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul id="title">
<li><a class="bc" href="../list">Forums</a></li>
<li><a class="bc" href="../view?id=${forum.id}">${forum.shortName}</a><li>
<li>Search Results</li>
</ul>

<div id="forums_menu"> 
<form action="search" method="get">
  <input type="text" class="input_search" name="term" value="${param.term}" />
  <input type="hidden" name="forumId" value="${forum.id}" />
  <input type="submit" class="subbutton" name="search" value="Search" />
</form>
</div>

<c:if test="${fn:length(posts) == 0}">
<h4>No posts found.</h4>
</c:if>

<c:if test="${fn:length(posts) > 0}">
<table class="forums">
<tr><th colspan="2">Found ${fn:length(posts)} post(s) that match <i>${param.term}</i>.</th></tr>
<c:forEach items="${posts}" var="post" varStatus="status">
<tr<c:if test="${(status.index+1)%2 eq 0}"> class="even"</c:if>>
  <td class="count center">${status.index+1}</td>
  <td class="cat">
    <a href="view?id=${post.topic.id}">
      <span class="search-result-title"><b>${post.subject}</b>, by <em>${post.author.username}</em>.</span><br />
      <span class="search-result-body">${post.content}</span>
    </a>
  </td>
</tr>
</c:forEach>
</table>
</c:if>
