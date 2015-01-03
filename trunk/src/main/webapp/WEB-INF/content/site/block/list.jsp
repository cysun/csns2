<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    document.title = "${section.course.code}-${section.number} Blocks";
    $("#sortableBlocks").sortable({
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "reorder",
                data: {
                    "id": ui.item.attr("data-id"),
                    "newIndex": ui.item.index()
                }
            });
        }
    });
    $("#sortableBlocks").disableSelection();
    $(".sortableElements").sortable({
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "reorderItem",
                data: {
                    "blockId": ui.item.attr("data-block-id"),
                    "itemId": ui.item.attr("data-item-id"),
                    "newIndex": ui.item.index()
                }
            });
        }
    });
    $(".sortableElements").disableSelection();
    $(".block-toggle").change(function(){
        $.ajax({
           url: "toggle",
           data: {
               "id": $(this).attr("data-id")
           }
        });
    });
    $(".item-toggle").change(function(){
        $.ajax({
           url: "toggleItem",
           data: {
               "blockId": $(this).attr("data-block-id"),
               "itemId": $(this).attr("data-item-id")
           }
        });
    });
});
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Blocks</li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/block/add' />"><img title="Add Block"
  alt="[Add Block]" src="<c:url value='/img/icons/brick_add.png' />" /></a></li>
</ul>

<div class="site-title">${section.course.code} ${section.course.name}</div>
<div class="site-quarter">${section.quarter}</div>

<div id="sortableBlocks">
<c:forEach items="${section.site.blocks}" var="block">
<%-- Regular Block --%>
<c:if test="${block.type == 'REGULAR'}">
<div class="site-block" data-id="${block.id}">
<div class="site-block-title">
  <input type="checkbox" class="block-toggle" data-id="${block.id}"
   <c:if test="${not block.hidden}">checked="checked"</c:if> />
  ${block.name}
  <div class="site-block-operations">
    <a href="<c:url value='${section.siteUrl}/block/edit?id=${block.id}' />"><img
       title="Edit Block" alt="[Edit Block]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
    <a href="<c:url value='${section.siteUrl}/block/addItem?blockId=${block.id}' />"><img
       title="Add Item" alt="[Add Item]" src="<c:url value='/img/icons/plugin_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<c:if test="${fn:length(block.items) > 0}">
<table class="viewtable autowidth">
  <tbody class="sortableElements">
  <c:forEach items="${block.items}" var="item">
  <tr data-block-id="${block.id}" data-item-id="${item.id}">
    <td>
      <input type="checkbox" class="item-toggle" data-block-id="${block.id}" data-item-id="${item.id}"
         <c:if test="${not item.hidden}">checked="checked"</c:if> />
      <a href="../item/${item.id}">${item.name}</a>
    </td>
    <td><a href="editItem?blockId=${block.id}&amp;itemId=${item.id}"><img title="Edit Item"
           alt="[Edit Item]" src="<c:url value='/img/icons/plugin_edit.png' />" /></a></td>
  </tr>
  </c:forEach>
  </tbody>
</table>
</c:if>
</div>
</div>
</c:if>
<%-- Assignments Block --%>
<c:if test="${block.type == 'ASSIGNMENTS'}">
<div class="site-block" data-id="${block.id}">
<div class="site-block-title">
  <input type="checkbox" class="block-toggle" data-id="${block.id}"
   <c:if test="${not block.hidden}">checked="checked"</c:if> />
  ${block.name}
  <div class="site-block-operations">
    <a href="<c:url value='${section.siteUrl}/block/edit?id=${block.id}' />"><img
       title="Edit Block" alt="[Edit Block]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
  <ul>
    <c:forEach items="${section.assignments}" var="assignment">
    <li>${assignment.name}, Due: <csns:dueDate date="${assignment.dueDate.time}"
        datePast="${assignment.pastDue}" datePattern="EEEE, MMMM dd" /></li>
    </c:forEach>
    <c:forEach items="${section.rubricAssignments}" var="assignment">
    <li>${assignment.name}, Due: <csns:dueDate date="${assignment.dueDate.time}"
        datePast="${assignment.pastDue}" datePattern="EEEE, MMMM dd" /></li>
    </c:forEach>
  </ul>
</div>
</div>
</c:if>
<%-- Announcements Block --%>
<c:if test="${block.type == 'ANNOUNCEMENTS'}">
<div class="site-block" data-id="${block.id}">
<div class="site-block-title">
  <input type="checkbox" class="block-toggle" data-id="${block.id}"
   <c:if test="${not block.hidden}">checked="checked"</c:if> />
  ${block.name}
  <div class="site-block-operations">
    <a href="<c:url value='${section.siteUrl}/block/edit?id=${block.id}' />"><img
       title="Edit Block" alt="[Edit Block]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
    <a href="addAnnouncement"><img title="Add Announcement" alt="[Add Announcement]"
       src="<c:url value='/img/icons/comment_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<c:if test="${fn:length(section.site.announcements) > 0}">
<table class="viewtable autowidth">
  <c:forEach items="${section.site.announcements}" var="announcement">
  <tr>
    <td><fmt:formatDate value="${announcement.date}" pattern="YYYY-MM-dd" /></td>
    <td>${announcement.content}</td>
    <td><a href="editAnnouncement?id=${announcement.id}"><img title="Edit Announcement"
           alt="[Edit Announcement]" src="<c:url value='/img/icons/comment_edit.png' />" /></a></td>
  </tr>
  </c:forEach>
</table>
</c:if>
</div>
</div>
</c:if>
</c:forEach>
</div>
