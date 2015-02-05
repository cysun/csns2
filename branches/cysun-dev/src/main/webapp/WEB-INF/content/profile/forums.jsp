<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
function unsubscribe( forumId )
{
    var msg = "Do you want to unsubscribe from this forum?";
    if( confirm(msg) )
        $.ajax({
            url: "<c:url value='/subscription/forum/unsubscribe' />",
            data: { "id": forumId, "ajax": true },
            success: function(){
                var current_index = $("#tabs").tabs("option", "active");
                $("#tabs").tabs("load", current_index);
            },
            cache: false
        });
}

$(function(){
    $("input[name='name']").autocomplete({
        source: "<c:url value='/autocomplete/forum' />",
        select: function(event, ui) {
            if( ui.item )
                $("<input>").attr({
                    type: "hidden",
                    name: "forumId",
                    value: ui.item.id
                }).appendTo($(this).parent());
        }
    });
    $("button[name='clear']").click(function(event){
        event.preventDefault();
        $("input[name='forumId']").remove();
        $("input[name='name']").val("");
    });
    $("button[name='subscribe']").click(function(event){
        event.preventDefault();
        $.ajax({
            url: "<c:url value='/subscription/forum/subscribe' />",
            data: { "id": $("input[name='forumId']").val(), "ajax": true },
            success: function(){
                var current_index = $("#tabs").tabs("option", "active");
                $("#tabs").tabs("load", current_index);
            },
            cache: false
        });
    });
});
</script>

<div> <!-- for tab -->

<p><input type="text" name="name" class="forminput" style="width: 500px;" 
    placeholder="Search for forums to subscribe" />
<button class="subbutton" name="subscribe">Subscribe</button>
<button class="subbutton" name="clear">Clear</button>
</p>

<c:if test="${fn:length(departmentForums) > 0}">
<h3>Department Forums</h3>
<table class="viewtable autowidth">
  <tr><th>Department</th><th>Forum</th><th>Unsubscribe</th></tr>
  <c:forEach items="${departmentForums}" var="forum">
  <tr id="forum-${forum.id}">
    <td>${forum.department.name}</td>
    <td>${forum.name}</td>
    <td class="center">
      <a href="javascript:unsubscribe(${forum.id})"><img title="Unsubscribe" alt="[Unsubscribe]"
         src="<c:url value='/img/icons/star_delete.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</c:if>

<c:if test="${fn:length(courseForums) > 0}">
<h3>Course Forums</h3>
<table class="viewtable autowidth">
  <tr><th>Forum</th><th>Unsubscribe</th></tr>
  <c:forEach items="${courseForums}" var="forum">
  <tr>
    <td>${forum.course.code} ${forum.course.name}</td>
    <td class="center">
      <a href="javascript:unsubscribe(${forum.id})"><img title="Unsubscribe" alt="[Unsubscribe]"
         src="<c:url value='/img/icons/star_delete.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</c:if>

<c:if test="${fn:length(otherForums) > 0}">
<h3>Other Forums</h3>
<table class="viewtable autowidth">
  <tr><th>Forum</th><th>Unsubscribe</th></tr>
  <c:forEach items="${otherForums}" var="forum">
  <tr>
    <td>${forum.name}</td>
    <td class="center">
      <a href="javascript:unsubscribe(${forum.id})"><img title="Unsubscribe" alt="[Unsubscribe]"
         src="<c:url value='/img/icons/star_delete.png' />" /></a>
    </td>
  </tr>
  </c:forEach>
</table>
</c:if>

</div>
