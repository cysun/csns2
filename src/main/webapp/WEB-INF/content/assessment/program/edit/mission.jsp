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
<li>Edit Mission Statement</li>
</ul>

<form method="post">
<textarea id="mission" name="value" rows="5" cols="80">${program.mission}</textarea><br />
<input type="submit" class="subbutton" value="Save" />
</form>
