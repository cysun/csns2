<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <c:if test="${topic.pinned}">
        $("#pin").hide();
    </c:if>
    <c:if test="${not topic.pinned}">
        $("#unpin").hide();
    </c:if>
});
function subscribe()
{
    $.ajax({
        url: "<c:url value='/subscription/topic/subscribe' />",
        data: { "id": ${topic.id}, "ajax": true },
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
        url: "<c:url value='/subscription/topic/unsubscribe' />",
        data: { "id": ${topic.id}, "ajax": true },
        success: function(){
            $("#unsubscribe").hide();
            $("#subscribe").show();
        },
        cache: false
    });
}
function deleteTopic()
{
    var msg = "Are you sure you want to delete this topic?";
    if( confirm(msg) )
        window.location.href = "delete?id=${topic.id}";
}
function pin()
{
    $.ajax({
        url: "pin?id=${topic.id}",
        success: function(){
            $("#pin").hide();
            $("#unpin").show();
        },
        cache: false
    });
}
function unpin()
{
    $.ajax({
        url: "unpin?id=${topic.id}",
        success: function(){
            $("#unpin").hide();
            $("#pin").show();
        },
        cache: false
    });
}
</script>

<ul id="title">
<li><a class="bc" href="../list">Forums</a></li>
<li><a class="bc" href="../view?id=${topic.forum.id}">${topic.forum.shortName}</a><li>
<li><csns:truncate value="${topic.name}" length="60" /></li>
<security:authorize access="authenticated">
<li id="subscribe" class="align_right"><a href="javascript:subscribe()"><img alt="[Subscribe to Topic]"
  title="Subscribe to This Topic" src="<c:url value='/img/icons/star_add.png' />" /></a></li>
<li id="unsubscribe" class="align_right"><a href="javascript:unsubscribe()"><img alt="[Unsubscribe from Topic]"
  title="Unsubscribe from This Topic" src="<c:url value='/img/icons/star_delete.png' />" /></a></li>
<c:if test="${isModerator}">
<li class="align_right"><a href="javascript:deleteTopic()"><img alt="[Delete Topic]"
  title="Delete This Topic" src="<c:url value='/img/icons/forums_topic_delete.png' />" /></a></li>
<li id="pin" class="align_right"><a href="javascript:pin()"><img alt="[Pin Topic]"
  title="Pin This Topic" src="<c:url value='/img/icons/pin.png' />" /></a></li>
<li id="unpin" class="align_right"><a href="javascript:unpin()"><img alt="[Unpin Topic]"
  title="Unpin This Topic" src="<c:url value='/img/icons/unpin.png' />" /></a></li>
</c:if>
</security:authorize>
</ul>

<security:authorize access="authenticated">
<div id="forums_menu">
   <a href="create?forumId=${topic.forum.id}">New Topic</a>
   <a href="reply?id=${topic.id}">Reply</a>
</div>
</security:authorize>

<display:table name="${topic.posts}" uid="post" requestURI="view" pagesize="50" class="topic" style="width: 100%;">
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
  
  <display:column title="Author">
    <div class="name">${post.author.username}</div>
    <div class="posts">Posts: ${post.author.numOfForumPosts}</div>
  </display:column>
  
  <display:column title="Message">
          
    <div class="postdetails">
      Posted <fmt:formatDate value="${post.date}" pattern="HH:mm MMM dd, yyyy" /> |
      <security:authorize access="authenticated">
      <c:if test="${user.id == post.author.id || isModerator}">
      <a href="edit?postId=${post.id}">Edit</a> |
      </c:if>
      <a href="reply?id=${topic.id}&amp;postId=${post.id}">Reply with Quote</a>
      </security:authorize>
    </div>
    
    <div class="postbody">${post.content}</div>
    <c:if test="${fn:length(post.attachments) > 0}">
    <div class="attachments">
    <b>Attachments</b>:
    <ul>
      <c:forEach items="${post.attachments}" var="attachment">
      <li><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></li>
      </c:forEach>
    </ul>
    </div>
    </c:if>
    <c:if test="${not empty post.editedBy}">
    <div class="edited-note">Last edited by ${post.editedBy.username} at
      <fmt:formatDate value="${post.editDate}" pattern="HH:mm MMM dd, yyyy" />.
    </div>
    </c:if>
  </display:column>
</display:table>

<security:authorize access="authenticated">
<br />
<div id="forums_menu">
<a href="create?forumId=${topic.forum.id}">New Topic</a>
<a href="reply?id=${topic.id}">Reply</a>
</div>
</security:authorize>
