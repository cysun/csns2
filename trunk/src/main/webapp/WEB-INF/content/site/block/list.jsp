<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<div class="site-block">
<div class="site-block-title">${block.name}
  <div class="site-block-operations">
    <a href="<c:url value='${section.siteUrl}/block/edit?id=${block.id}' />"><img title="Edit Block"
       alt="[Edit Block]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
    <a href="<c:url value='${section.siteUrl}/block/addItem' />"><img title="Add Item"
       alt="[Add Item]" src="<c:url value='/img/icons/plugin_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
  <ul>
    <li>This is an item.</li>
    <li>This is another item.</li>
  </ul>
</div>
</div> <!-- end of site-block -->
</c:forEach>
