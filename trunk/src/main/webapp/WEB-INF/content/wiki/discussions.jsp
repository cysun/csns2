<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<csns:wikiBreadcrumbs path="${page.path}" />
</ul>

<div id="opbar">
<a href="<c:url value='/wiki/discuss?pageId=${page.id}' />">Start a Discussion</a>
</div>

<div id="wiki_content">
<c:if test="${fn:length(page.discussions) == 0}">
<p>There is no discussion about page <span class="tt">${page.path}</span> yet.</p>
</c:if>

<c:if test="${fn:length(page.discussions) > 0}">
<h2>Discussion Topics</h2>
<ul class="iconlist">
<c:forEach items="${page.discussions}" var="discussion">
  <li class="blue_arrow"><a href="<c:url
    value='/department/${dept}/forum/topic/view?id=${discussion.id}'/>">${discussion.name}</a></li>
</c:forEach>
</ul>
</c:if>
</div>
