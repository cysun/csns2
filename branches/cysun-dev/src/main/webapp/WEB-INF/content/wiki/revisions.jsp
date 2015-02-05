<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<script>
function compareRevisions()
{
	var numOfRevisionsChecked = 0;
    var elements = document.getElementsByName( "revisionId" );
    for( var i=0 ; i < elements.length ; ++i )
        if( elements[i].checked ) ++numOfRevisionsChecked;

    if( numOfRevisionsChecked == 2 )
    {
        var form = document.getElementById( "revisionsForm" );
        form.action = "<c:url value='/wiki/compare' />";
        form.submit();
    }
    else
        alert( "Please select two revisions to compare." );
}
</script>

<ul id="title">
<csns:wikiBreadcrumbs path="${page.path}" />
</ul>

<div id="wiki_content">
<h3>Revision History of Page <span class="tt">${page.path}</span></h3>

<p><a href="javascript:compareRevisions()">Compare Revisions</a></p>

<form id="revisionsForm" method="post" action="#">
<c:set var="numOfRevisions" value="${fn:length(revisions)}" />
<display:table name="${revisions}" uid="revision"
    requestURI="revisions" pagesize="40" style="width: 100%;">
<display:setProperty name="paging.banner.onepage" value="" />
<display:setProperty name="paging.banner.no_items_found" value="" />
<display:setProperty name="paging.banner.one_item_found" value="" />
<display:setProperty name="paging.banner.all_items_found" value="" />
<display:setProperty name="paging.banner.some_items_found" value="" />
  <display:column>
    <input type="checkbox" name="revisionId" value="${revision.id}" />
    <a href="<c:url value='${page.path}' /><c:if
    test='${revision_rowNum != 1}'>?revisionId=${revision.id}</c:if>">Revision
    ${numOfRevisions - revision_rowNum + 1}</a> at <fmt:formatDate
    value="${revision.date}" pattern="HH:mm:ss MM/dd/yyyy" />
    by ${revision.author.username}.
  </display:column>
</display:table>
</form>
</div>
