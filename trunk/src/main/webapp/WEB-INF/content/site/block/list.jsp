<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Blocks</li>
<li class="align_right"><a href="<c:url value='${section.siteUrl}/block/add' />"><img title="Add Block"
  alt="[Add Block]" src="<c:url value='/img/icons/brick_add.png' />" /></a></li>
</ul>

<div class="site-title">${section.course.code} ${section.course.name}</div>
<div class="site-quarter">${section.quarter}</div>

<c:forEach items="${section.site.blocks}" var="block">
<%-- Regular Block --%>
<c:if test="${block.type == 'REGULAR'}">
<div class="site-block">
<div class="site-block-title">${block.name}
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
  <c:forEach items="${block.items}" var="item">
  <tr>
    <td><a href="../item/${item.id}">${item.name}</a></td>
    <td><a href="editItem?blockId=${block.id}&amp;itemId=${item.id}"><img title="Edit Item"
           alt="[Edit Item]" src="<c:url value='/img/icons/plugin_edit.png' />" /></a></td>
  </tr>
  </c:forEach>
</table>
</c:if>
</div>
</div>
</c:if>
</c:forEach>
