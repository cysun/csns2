<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<csns:wikiBreadcrumbs path="${path}" />
</ul>

<div id="wiki_content">
<h4>Revision Comparison of Page <span class="tt">${path}</span></h4>

<div class="revDiffResult">
${diffResult}
</div>
</div>
