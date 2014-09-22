<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Edit Class Info</li>
</ul>

<div class="site-title">${site.section.course.code} ${site.section.course.name}</div>
<div class="site-quarter">${site.section.quarter}</div>

<form action="add" method="post">
<table class="general autowidth" style="margin-left: 10px">
<c:forEach items="${section.site.infoEntries}" var="infoEntry" varStatus="status">
<tr>
  <th>${infoEntry.name}</th>
  <td>${infoEntry.value}</td>
  <td class="center"><a href="delete?index=${status.index}"><img title="Delete"
      alt="[Delete]" border="0" src="<c:url value='/img/icons/delete.png' />" /></a></td>
</tr>
</c:forEach>
<tr>
  <th><input name="name" class="forminput" /></th>
  <td><input name="value" class="forminput" /></td>
  <td><button class="subbutton">Add</button>
</tr>
</table>
</form>
