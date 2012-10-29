<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
function addAttachment()
{
    $("#attachments").append("<br /><input name='file' class='leftinput' style='width: 100%;' type='file' size='75' />");
}
</script>

<ul id="title">
<li><a class="bc" href="../list">Forums</a></li>
<li><a class="bc" href="../view?id=${post.topic.forum.id}">${post.topic.forum.shortName}</a><li>
<li><a class="bc" href="view?id=${post.topic.id}"><csns:truncate value="${post.topic.name}" length="60" /></a></li>
<li>Edit post</li>
</ul>

<form:form modelAttribute="post" enctype="multipart/form-data">
<table class="general">
  <tr>
    <th>Subject:</th>
    <td>
      <form:input path="subject" cssClass="leftinput" cssStyle="width: 98%;" />
      <div class="error"><form:errors path="subject" /></div>
    </td>
  </tr>
  <tr>
    <th>Content:</th>
    <td>
      <form:textarea id="textcontent" path="content" cssStyle="width: 99%;" rows="15" cols="80" />
      <div class="error"><form:errors path="content" /></div>
    </td>
  </tr>
  <tr>
    <th>Attachments
      (<a href="javascript:addAttachment()">+</a>):
    </th>
    <td id="attachments">
      <input name="file" class="leftinput" style="width: 100%;" type="file" size="75" />
    </td>
  </tr>
<c:if test="${fn:length(post.attachments) > 0}">
  <tr>
    <th></th>
    <td>
      <ul>
        <c:forEach items="${post.attachments}" var="attachment">
        <li>
          <a href="<c:url value='/download.html?fileId=${attachment.id}' />">${attachment.name}</a>
          [<a href="deletePostAttachment.html?postId=${post.id}&amp;fileId=${attachment.id}">Delete</a>]
        </li>
        </c:forEach>
      </ul>
    </td>
  </tr>
</c:if>
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
