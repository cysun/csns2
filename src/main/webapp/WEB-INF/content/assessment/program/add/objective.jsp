<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    });
});
</script>

<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="../edit?id=${program.id}" class="bc">${program.name}</a></li>
<li>Add Objective</li>
</ul>

<form method="post">
<textarea id="objective" name="value" rows="5" cols="80"></textarea><br />
<input type="submit" class="subbutton" value="Add" />
</form>
