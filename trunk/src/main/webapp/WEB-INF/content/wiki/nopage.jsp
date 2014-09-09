<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<csns:wikiBreadcrumbs path="${path}" />
<csns:wikiSearchForm />
</ul>

<div id="wiki_content">
<p>The page <span class="tt">${path}</span> you are trying to access does not
exist. Do you want to create this page?
<a href="<c:url value='/wiki/edit?path=${path}' />">Yes</a> |
<a href="<c:url value='/wiki/content/' />">No</a>
</p>
</div>
