<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/wiki/content/' />">Wiki</a></li>
<csns:wikiBreadcrumbs path="${path}" />
<li class="align_right">
  <form action="<c:url value='/wiki/search' />" method="post">
    <input class="formselect" type="text" name="term" size="25" />
    <input class="subbutton" type="submit" name="search" value="Search" />
  </form>
</li>
</ul>

<div id="wiki_content">
<p>The page <span class="tt">${path}</span> you are trying to access does not
exist. Do you want to create this page?
<a href="<c:url value='/department/${dept}/wiki/edit?path=${path}' />">Yes</a>
<a href="<c:url value='/department/${dept}/wiki/content/' />">No</a>
</p>
</div>
