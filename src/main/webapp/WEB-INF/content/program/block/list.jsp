<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/program/search' />">Programs</a></li>
<li><a class="bc" href="../list">${program.department.name}</a></li>
<li><a class="bc" href="../view?id=${program.id}">${program.name}</a></li>
<li>Blocks</li>
<li class="align_right"><a href="add?programId=${program.id}"><img title="Add Block"
    alt="[Add Block]" src="<c:url value='/img/icons/brick_add.png' />" /></a></li>
</ul>

<h3 class="site-title">${program.name}</h3>
<div class="site-description">${program.description}</div>

<c:forEach items="${program.blocks}" var="block">
<div class="site-block">
<div class="site-block-title">${block.name}
  <div class="site-block-operations">
    <a href="edit?id=${block.id}&programId=${program.id}"><img
       title="Edit Block" alt="[Edit Block]" src="<c:url value='/img/icons/brick_edit.png' />" /></a>
    <a href="javascript:void(0)"><img
       title="Add Course" alt="[Add Course]" src="<c:url value='/img/icons/plugin_add.png' />" /></a>
  </div>
</div>
<div class="site-block-content">
<h4>Units Required: ${block.unitsRequired}</h4>
${block.description}
<ul>
<c:forEach items="${block.courses}" var="course">
  <li>${course.code} ${course.name} (${course.units})</li>
</c:forEach>
</ul>
</div> <!-- end of site-block-content -->
</div> <!-- end of site-block -->
</c:forEach>
