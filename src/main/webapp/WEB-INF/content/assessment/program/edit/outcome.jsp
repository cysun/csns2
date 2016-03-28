<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
function removeOutcome()
{
    var msg = "Are you sure you want to remove this outcome?";
    if( confirm(msg) )
        window.location.href = "remove?programId=${program.id}&index=${param.index}";
}
</script>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="../edit?id=${program.id}" class="bc">${program.name}</a></li>
<li>Edit Outcome</li>
<li class="align_right"><a href="javascript:removeOutcome()"><img alt="[Remove Outcome]"
  title="Remove Outcome" src="<c:url value='/img/icons/page_delete.png' />" /></a></li>
</ul>

<form method="post">
<textarea id="outcome" name="value" rows="5" cols="80">${program.outcomes[param.index].text}</textarea><br />
<input type="submit" class="subbutton" value="Save" />
</form>
