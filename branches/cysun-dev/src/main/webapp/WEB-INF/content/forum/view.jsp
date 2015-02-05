<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

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
        url: "<c:url value='/subscription/forum/subscribe' />",
        data: { "id": ${forum.id}, "ajax": true },
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
        url: "<c:url value='/subscription/forum/unsubscribe' />",
        data: { "id": ${forum.id}, "ajax": true },
        success: function(){
            $("#unsubscribe").hide();
            $("#subscribe").show();
        },
        cache: false
    });
}
</script>

<ul id="title">
<li><a class="bc" href="list">Forums</a></li>
<li><csns:truncate value="${forum.name}" /><li>
<security:authorize access="authenticated">
<li id="subscribe" class="align_right"><a href="javascript:subscribe()"><img alt="[Subscribe to Forum]"
  title="Subscribe to This Forum" src="<c:url value='/img/icons/star_add.png' />" /></a></li>
<li id="unsubscribe" class="align_right"><a href="javascript:unsubscribe()"><img alt="[Unsubscribe from Forum]"
  title="Unsubscribe from This Forum" src="<c:url value='/img/icons/star_delete.png' />" /></a></li>
<li class="align_right"><a href="<c:url value='/profile#ui-tabs-1' />"><img alt="[Forum Subscriptions]"
  title="Forum Subscriptons" src="<c:url value='/img/icons/star.png' />" /></a></li>
</security:authorize>
<security:authorize access="${not empty forum.department} and authenticated and principal.isAdmin('${forum.department.abbreviation}')">
<li class="align_right"><a href="edit?id=${forum.id}"><img alt="[Edit Forum]"
  title="Edit Forum" src="<c:url value='/img/icons/comments_edit.png' />" /></a></li>
</security:authorize>
</ul>

<div id="forums_menu"> 
<security:authorize access="authenticated">
<span>
<a href="topic/create?forumId=${forum.id}">New Topic</a>
</span>
</security:authorize>

<form action="topic/search" method="get">
<input class="input_search" type="text" name="term" />
<input type="hidden" name="forumId" value="${forum.id}" />
<input class="subbutton" type="submit" name="search" value="Search" />
</form>
</div>

<display:table name="${forum.topics}" uid="topic" requestURI="view" pagesize="30" class="forums">
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
    <a href="topic/view?id=${topic.id}">
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
