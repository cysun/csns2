<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
$(function(){
    $("div.help").dialog({
        autoOpen: false,
        modal: true
    });
});
function help( name )
{
    $("#help-"+name).dialog("open");
}
</script>

<c:if test="${fn:length(advisementRecords) == 0}">
<p>No advisement information on record.</p>
</c:if>

<p><a id="addAdvisement" class="toggle" href="javascript:void(0)">Advise</a></p>

<form id="addAdvisementForm" class="default-hide" action="advisement/add"
      enctype="multipart/form-data" method="post">
  <textarea id="t" name="comment" rows="5" cols="80"></textarea>
  <p><csns:help name="fao" img="false">For advisors only</csns:help>:
  <input type="checkbox" name="forAdvisorsOnly" value="true" /></p>
  <p><input type="hidden" name="userId" value="${user.id}" />
  <input type="submit" name="submit" class="subbutton" value="OK" /></p>
</form>

<c:if test="${fn:length(advisementRecords) > 0}">
<table class="general2">
<c:forEach items="${advisementRecords}" var="record">
<tr>
  <th align="left">
    <span style="margin-right: 1em;"> ${record.advisor.name}</span>
    <fmt:formatDate value="${record.date}" pattern="yyyy-MM-dd HH:mm" />
  </th>
  <th align="right">
    <c:if test="${self.id == record.advisor.id}">
      <a href="advisement/edit?id=${record.id}"><img title="Edit Advisement Record"
         alt="[Edit Advisement Record]" src="<c:url value='/img/icons/page_edit.png' />"
         style="margin-right: 1em;"/></a>
    </c:if>
    <a id="email" href="advisement/email?id=${record.id}"><img title="Email Advisement Record"
    alt="[Email Advisement Record]" src="<c:url value='/img/icons/email_edit.png' />" /></a>
  </th>
</tr>
<tr>
  <td colspan="2" <c:if test="${record.forAdvisorsOnly}">class="yellow"</c:if>>${record.comment}
    <c:if test="${fn:length(record.attachments) > 0}">
    <div class="general-attachments">
    <ul>
      <c:forEach items="${record.attachments}" var="attachment">
      <li><a href="<c:url value='/download?fileId=${attachment.id}' />">${attachment.name}</a></li>
      </c:forEach>
    </ul>
    </div>
    </c:if>
  </td>
</tr>
</c:forEach>
</table>
</c:if>

<div id="help-fao" class="help">
The student won't be able to see the comments marked as <em>for advisors
only</em>.</div>
