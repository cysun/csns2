<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function(){
    document.title = "${section.course.code}-${section.number} Class Info";
    $("#sortables").sortable({
        cancel: ".input",
        update: function(event,ui){
            $.ajax({
                type: "POST",
                url:  "reorder",
                data: {
                    "oldIndex": ui.item.attr("data-old-index"),
                    "newIndex": ui.item.index()
                },
                success: function(){
                    $(".entry").each(function(index){
                        $(this).attr("data-old-index", index);
                    });
                }
            });
        }
    });
    $(".delete").click(function(){
        var index = $(this).closest(".entry").attr("data-old-index");
        window.location.href = "delete?index=" + index;
    });
});
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Edit Class Info</li>
</ul>

<div class="site-title">${site.section.course.code} ${site.section.course.name}</div>
<div class="site-quarter">${site.section.quarter}</div>

<form action="add" method="post">
<table class="general autowidth" style="margin-left: 10px">
<tbody id="sortables">
<c:forEach items="${section.site.infoEntries}" var="infoEntry" varStatus="status">
<tr class="entry" data-old-index="${status.index}">
  <th>${infoEntry.name}</th>
  <td>${infoEntry.value}</td>
  <td class="center"><img class="delete" title="Delete"
      alt="[Delete]" border="0" src="<c:url value='/img/icons/delete.png' />" /></td>
</tr>
</c:forEach>
<tr class="input">
  <th><input name="name" class="forminput" /></th>
  <td><input name="value" class="forminput" /></td>
  <td><button class="subbutton">Add</button>
</tr>
</tbody>
</table>
</form>
