<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
function clone( siteId )
{
    var msg = "Do you want to clone this site?";
    if( confirm(msg) )
        window.location.href = "<c:url value='/site/create?sectionId=${section.id}' />"
                + "&siteId=" + siteId;
}
</script>

<c:if test="${empty section}">
<p>Cannot find the class section you are looking for. Please check if the URL
is correct.</p>
</c:if>

<c:if test="${not empty section}">
<p>The class website for <em>${section.course.code} Section ${section.number},
${section.quarter}</em> does not exist.</p>
<c:if test="${isInstructor}">
<p>Do you want to create this site?</p>
<ul>
  <li style="margin: 0 0 5px 0;"><a href="<c:url value='/section/taught/' />">No</a></li>
  <li style="margin: 0 0 5px 0;"><a href="<c:url value='/site/create?sectionId=${section.id}' />">Yes,
    create a new site.</a></li>
  <c:if test="${fn:length(sites) > 0 }">
  <li style="margin: 0 0 5px 0;">Yes, create a new site by cloning an existing site:</li>
  </c:if>
</ul>
<c:if test="${fn:length(sites) > 0 }">
<div style="margin-left: 40px;">
<table class="general autowidth">
  <c:forEach items="${sites}" var="site">
  <tr>
    <td><a href="<c:url value='${site.section.siteUrl}' />">${site.section.course.code}-${site.section.number},
      ${site.section.quarter}</a></td>
    <td><a href="javascript:clone(${site.id})"><img alt="[Clone Site]" 
      title="Clone Site" src="<c:url value='/img/icons/table_code.png'/>" /></a></td>
  </tr>
  </c:forEach>
</table>
</div>
</c:if>
</c:if> <%-- end of isInstructor --%>
</c:if> <%-- end of "not empty section" --%>
