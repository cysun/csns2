<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/department/${department.abbreviation}/' />">${department.name}</a></li>
<li><a class="bc" href="list">Forums</a></li>
<li><csns:truncate value="${forum.name}" /><li>
<li class="align_right"><a href="subscriptions"><img alt="[Subscribe to Forum]"
  title="Subscribe to This Forum" src="<c:url value='/img/icons/award_star_add.png' />" /></a></li>
<li class="align_right"><a href="subscriptions"><img alt="[Forum Subscriptions]"
  title="Forum Subscriptons" src="<c:url value='/img/icons/star.png' />" /></a></li>
</ul>

<div id="forums_menu"> 
<security:authorize access="authenticated">
<span>
<a href="createTopic.html?forumId=${forum.id}">New Topic</a>
</span>
</security:authorize>

<form action="searchPosts.html" method="post">
<input class="input_search" type="text" name="query" />
<input type="hidden" name="forumId" value="${forum.id}" />
<input class="subbutton" type="submit" name="search" value="Search" />
</form>
</div>
<display:table name="${topics}" uid="topic" requestURI="viewForum.html" pagesize="40" class="forums">
<display:setProperty name="paging.banner.onepage" value="" />
<display:setProperty name="paging.banner.group_size" value="8" />
<display:setProperty name="paging.banner.no_items_found" value="" />
<display:setProperty name="paging.banner.one_item_found" value="" />
<display:setProperty name="paging.banner.all_items_found" value="" />
<display:setProperty name="paging.banner.some_items_found" value="" />
<display:setProperty name="paging.banner.placement" value="both" />
<display:setProperty name="paging.banner.first">
    <div class="pagelinks">First {0} <a href="{4}">Last</a></div>
</display:setProperty>
<display:setProperty name="paging.banner.last">
    <div class="pagelinks"><a href="{1}">First</a> {0} Last</div>
</display:setProperty>
<display:setProperty name="paging.banner.full">
    <div class="pagelinks"><a href="{1}">First</a> {0} <a href="{4}">Last</a></div>
</display:setProperty>
  <display:column title="Topics" class="cat" maxLength="100">
    <a href="viewTopic.html?topicId=${topic.id}">
    <c:if test="${topic.pinned}">
    <img border="0" alt="[Sticky]" title="Sticky Topic" src="<c:url value='/img/icons/forums-pin.png'/>" />
    </c:if>
    <c:if test="${not topic.pinned}">
    <img border="0" alt="[Topic]" title="Regular Topic" src="<c:url value='/img/icons/forums-topic.png'/>" />
    </c:if>
    ${topic.name}</a>
  </display:column>
  <display:column title="Replies" class="bg1 postdetails center" style="width:7%;">${topic.numOfReplies}</display:column>
  <display:column title="Author" class="bg1 name center" style="width:7%;">${topic.firstPost.author.username}</display:column>
  <display:column title="Views" class="bg1 postdetails center" style="width:7%;">${topic.numOfViews}</display:column>
  <display:column title="Last Post" class="bg1 postdetails" style="width:13%;">
  <div class="last_post">
    <fmt:formatDate value="${topic.lastPost.date}" pattern="HH:mm MMM dd, yyyy" /><br />
    by <em>${topic.lastPost.author.username}</em></div>
  </display:column>
</display:table>
