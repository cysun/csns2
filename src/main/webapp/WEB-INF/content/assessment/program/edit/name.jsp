<ul id="title">
<li><a href="list" class="bc">Program Assessment</a></li>
<li><a href="../edit?id=${program.id}" class="bc">${program.name}</a></li>
<li>Edit Program Name</li>
</ul>

<form method="post">
<table class="general">
  <tr>
    <th class="shrink">Name</th>
    <td>
      <input name="value" class="leftinput" style="width: 99%;"
             maxlength="255" value="${program.name}" />
    </td>
  </tr>
  <tr><th></th><td><input type="submit" class="subbutton" value="Save" /></td></tr>
</table>
</form>
