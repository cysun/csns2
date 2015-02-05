<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
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
<li>Reply</li>
</ul>

<form:form modelAttribute="post" action="reply" enctype="multipart/form-data" cssStyle="margin-bottom:20px;">
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
  <tr>
    <th></th>
    <td>
      <input type="submit" class="subbutton" name="submit" value="Submit" />
    </td>
  </tr>
</table>
</form:form>

<display:table name="${post.topic.posts}" uid="tpost" requestURI="reply" pagesize="5" class="topic" style="width: 100%;">
<display:setProperty name="paging.banner.onepage" value="" />
<display:setProperty name="paging.banner.group_size" value="8" />
<display:setProperty name="paging.banner.no_items_found" value="" />
<display:setProperty name="paging.banner.one_item_found" value="" />
<display:setProperty name="paging.banner.all_items_found" value="" />
<display:setProperty name="paging.banner.some_items_found" value="" />
<display:setProperty name="paging.banner.placement" value="both" />
<display:setProperty name="paging.banner.first">
    <div class="pagelinks">First {0} <a href="{4}">Last</a></div>
</display:setProperty>
<display:setProperty name="paging.banner.last">
    <div class="pagelinks"><a href="{1}">First</a> {0} Last</div>
</display:setProperty>
<display:setProperty name="paging.banner.full">
    <div class="pagelinks"><a href="{1}">First</a> {0} <a href="{4}">Last</a></div>
</display:setProperty>
  
  <display:column title="Author">
    <div class="name">${tpost.author.username}</div>
    <div class="posts">Posts: ${tpost.author.numOfForumPosts}</div>
  </display:column>
  
  <display:column title="Message">
          
    <div class="postdetails">
      Posted <fmt:formatDate value="${tpost.date}" pattern="HH:mm MMM dd, yyyy" />
    </div>
    
    <div class="postbody">${tpost.content}</div>
    <c:if test="${fn:length(post.attachments) > 0}">
    <div class="attachments">
    <b>Attachments</b>:
    <ul>
      <c:forEach items="${tpost.attachments}" var="attachment">
      <li><a href="<c:url value='/download.html?fileId=${attachment.id}' />">${attachment.name}</a></li>
      </c:forEach>
    </ul>
    </div>
    </c:if>
    <c:if test="${not empty tpost.editedBy}">
    <div class="edited-note">Last edited by ${tpost.editedBy.username} at
      <fmt:formatDate value="${tpost.editDate}" pattern="HH:mm MMM dd, yyyy" />.
    </div>
    </c:if>
  </display:column>
</display:table>

<script type="text/javascript">
  CKEDITOR.replaceAll();
</script>
