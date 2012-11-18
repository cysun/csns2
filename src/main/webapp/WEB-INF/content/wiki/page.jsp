<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns"%>

<script>
$(function(){
    document.title = "${revision.subject}";
    // $("span").removeAttr("style");
    // $("table").removeAttr("border");
    <c:if test="${not empty subscription}">
    $("#subscribe").hide();
    </c:if>
    <c:if test="${empty subscription}">
    $("#unsubscribe").hide();
    </c:if>
});

function revert()
{
    var message = "Are you sure you want to revert the page back to this revision?";
    var url = "<c:url value='/department/${dept}/wiki/revert?revisionId=' />" + "${param.revisionId}";
    if( confirm(message) ) window.location.href = url;
}

function subscribe()
{
    $.ajax({
        url: "<c:url value='/subscription/page/subscribe' />",
        data: { "id": ${revision.page.id}, "ajax": true },
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
        url: "<c:url value='/subscription/page/unsubscribe' />",
        data: { "id": ${revision.page.id}, "ajax": true },
        success: function(){
            $("#unsubscribe").hide();
            $("#subscribe").show();
        },
        cache: false
    });
}
</script>

<ul id="title">
  <csns:wikiBreadcrumbs dept="${dept}" path="${path}" />
  <li class="align_right">
    <form action="<c:url value='/department/${dept}/wiki/search' />" method="post">
      <input class="formselect" type="text" name="term" size="25" />
      <input class="subbutton" type="submit" name="search" value="Search" />
    </form>
  </li>
</ul>

<div id="opbar">
<security:authorize access="authenticated">
  <a id="subscribe" href="javascript:subscribe()">Subscribe</a>
  <a id="unsubscribe" href="javascript:unsubscribe()">Unsubscribe</a>
</security:authorize>
  <a href="<c:url value='/department/${dept}/wiki/discussions?id=${revision.page.id}' />">Discussions</a>
  <a href="<c:url value='/department/${dept}/wiki/revisions?id=${revision.page.id}' />">Revisions</a>
<security:authorize access="authenticated">
<c:if test="${not revision.page.locked or user.id == revision.page.owner.id or isAdmin}">
  <c:if test="${user.id == revision.page.owner.id or isAdmin}">
  <a href="<c:url value='/department/${dept}/wiki/move?from=${path}' />">Move</a>
  </c:if>
  <a href="<c:url value='/department/${dept}/wiki/edit?path=${path}' />">Edit</a>
  <c:if test="${not empty param.revisionId}">
  <a href="javascript:revert()">Revert to This Revision</a>
  </c:if>
</c:if>
</security:authorize>

</div>

<table id="wiki">
<tr>
  <c:if test="${not empty sidebar}">
    <td><div id="wiki_sidebar">${sidebar.content}</div></td>
  </c:if>
  <td id="wiki_content">${revision.content}</td>
</tr>
</table>

<div id="wiki_pageviews">This page has been viewed ${revision.page.views} times.</div>
