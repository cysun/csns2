<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#cloneFromUrl").click(function(){
        var msg = "Do you want to clone this site?";
        var siteUrl = $("input[name='siteUrl']").val();
        if( siteUrl && confirm(msg) )
            window.location.href = "<c:url value='/site/create?sectionId=${section.id}' />"
                + "&siteUrl=" + siteUrl;;
    });
});
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
${section.term}</em> does not exist.</p>
<c:if test="${isInstructor}">
<p>Do you want to create this site?</p>
<ul>
  <li style="margin: 0 0 5px 0;"><a href="<c:url value='/section/taught/' />">No</a></li>
  <li style="margin: 0 0 5px 0;"><a href="<c:url value='/site/create?sectionId=${section.id}' />">Yes,
    create a new site.</a></li>
  <li style="margin: 0 0 5px 0;">Yes, create a new site by cloning an existing site:</li>
</ul>
<div style="margin-left: 40px;">
<table class="general autowidth">
  <c:forEach items="${sites}" var="site">
  <tr>
    <td><a href="<c:url value='${site.section.siteUrl}' />">${site.section.course.code}-${site.section.number},
      ${site.section.term}</a></td>
    <td><a href="javascript:clone(${site.id})"><img alt="[Clone Site]" 
      title="Clone Site" src="<c:url value='/img/icons/table_code.png'/>" /></a></td>
  </tr>
  </c:forEach>
  <tr>
    <td><input type="text" name="siteUrl" class="leftinput" style="width: 16em;"
      placeholder="Please copy&paste the site URL here" /></td>
    <td><a id="cloneFromUrl" href="javascript:void(0)"><img alt="[Clone Site]" 
      title="Clone Site" src="<c:url value='/img/icons/table_code.png'/>" /></a></td>
  </tr>
</table>
</div>
</c:if> <%-- end of isInstructor --%>
</c:if> <%-- end of "not empty section" --%>
