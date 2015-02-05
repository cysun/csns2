<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
$(function(){
    $("textarea").each(function(){
        CKEDITOR.replace( $(this).attr("id"), {
          toolbar : "Basic"
        });
    }); 
    $("#cancel").click(function(){
        window.location.href = "../view?id=${record.student.id}#ui-tabs-3";
    });
});
</script>

<ul id="title">
<li><a class="bc" href="search">Users</a></li>
<li><a class="bc" href="../view?id=${record.student.id}#ui-tabs-3">${record.student.name}</a></li>
<li>Email Advisement Record</li>
</ul>

<form:form modelAttribute="email">
<table class="general">
  <tr><th>From</th><td>${email.author.name}</td></tr>
  <tr><th>To</th><td>${email.recipients[0].name}</td></tr>
  <tr>
    <th>Subject</th>
    <td>
      <form:input path="subject" cssClass="leftinput" cssStyle="width: 99%;" />
      <div class="error"><form:errors path="subject" /></div>
    </td>
  </tr>
  <tr>
    <th>Content</th>
    <td>
      <form:textarea id="textcontent" path="content" cssClass="leftinput" cssStyle="width: 99%;" rows="15" cols="75" />
      <div class="error"><form:errors path="content" /></div>
    </td>
  </tr>
<c:if test="${fn:length(record.attachments) > 0}">
  <tr>
    <th>Attachments:</th>
    <td>
      <ul>
      <c:forEach items="${record.attachments}" var="attachment">
        <li><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></li>
      </c:forEach>
      </ul>
    </td>
  </tr>
</c:if>  <tr>
    <th></th>
    <td>
      <button id="cancel" type="button" class="subbutton">Cancel</button> &nbsp; 
      <input type="submit" class="subbutton" name="send" value="Send" />
    </td>
  </tr>
</table>
</form:form>
