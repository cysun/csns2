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
    $(".edit").click(function(){
        var index = $(this).closest(".entry").attr("data-old-index");
        window.location.href = "edit?index=" + index;
    });
});
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught' />" class="bc">Instructor's Home</a></li>
<li><a href="<c:url value='${section.siteUrl}' />" class="bc">${section.course.code}-${section.number}</a></li>
<li>Class Info</li>
</ul>

<div class="site-title">${section.course.code} ${section.course.name}</div>
<div class="site-quarter">${section.quarter}</div>

<form action="add" method="post">
<table class="general autowidth" style="margin-left: 10px">
<tbody id="sortables">
<c:forEach items="${section.site.infoEntries}" var="infoEntry" varStatus="status">
<tr class="entry" data-old-index="${status.index}">
  <th>${infoEntry.name}</th>
  <td>${infoEntry.value}</td>
  <td class="center"><img class="edit" title="Edit" alt="[Edit]" border="0"
      src="<c:url value='/img/icons/table_edit.png' />" /></td>
</tr>
</c:forEach>
<tr class="input">
  <th><input name="name" class="forminput" style="width: 150px;" /></th>
  <td><input name="value" class="forminput" style="width: 600px;" /></td>
  <td><button class="subbutton">Add</button>
</tr>
</tbody>
</table>
</form>
