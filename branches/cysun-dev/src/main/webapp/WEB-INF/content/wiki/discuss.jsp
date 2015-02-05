<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
function addAttachment()
{
    $("#attachments").append("<br /><input name='file' class='leftinput' style='width: 100%;' type='file' size='75' />");
}
</script>

<ul id="title">
<csns:wikiBreadcrumbs path="${page.path}" />
<li><a class="bc" href="<c:url value='/wiki/discussions?id=${page.id}' />">Discussions</a>
<li>New Discussion</li>
</ul>

<form:form modelAttribute="post" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>Page</th>
    <td><span class="tt">${page.path}</span></td>
  </tr>
  <tr>
    <th>Subject</th>
    <td>
      <form:input path="subject" cssClass="leftinput" cssStyle="width: 98%;" />
      <div class="error"><form:errors path="subject" /></div>
    </td>
  </tr>
  <tr>
    <th>Content</th>
    <td>
      <form:textarea id="textcontent" path="content" cssStyle="width: 99%;" rows="15" cols="80" />
      <div class="error"><form:errors path="content" /></div>
    </td>
  </tr>
  <tr>
    <th>Attachments
      (<a href="javascript:addAttachment()">+</a>)
    </th>
    <td id="attachments">
      <input name="file" class="leftinput" style="width: 100%;" type="file" size="75" />
    </td>
  </tr>
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" name="submit" value="Submit" />
    </td>
  </tr>
</table>
</form:form>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
