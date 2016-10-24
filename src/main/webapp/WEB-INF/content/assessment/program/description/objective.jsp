<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Default"
        });
    });
});
</script>

<style>
.objective {
    padding: 1em;
    margin: 2em;
    border-style: solid;
    border-color: lightgrey;
    border-width: 2px;
    border-radius: 5px;
}
</style>

<ul id="title">
<li><a href="../list" class="bc">Program Assessment</a></li>
<li><a href="../view?id=${objective.program.id}#objectives" class="bc">${objective.program.name}</a></li>
<li>PEO #${objective.index+1}</li>
<li class="align_right"><a href="?fieldId=${objective.id}&amp;edit=true"><img alt="[Edit Details]"
  title="Edit Details" src="<c:url value='/img/icons/table_chart_edit.png' />" /></a></li>
</ul>

<div class="objective">${objective.text}</div>

<c:if test="${empty param.edit}">
${objective.description}
</c:if>

<c:if test="${not empty param.edit}">
<form method="post">
<textarea id="description" name="description">${objective.description}</textarea> <br />
<input class="subbutton" type="submit" name="save" value="Save" />
</form>
</c:if>
