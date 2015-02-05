<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="csns" uri="http://cs.calstatela.edu/csns" %>

<ul id="title">
<li><a class="bc" href="<c:url value='/wiki/content/' />">Wiki</a></li>
<csns:wikiSearchForm />
</ul>

<div id="wiki_content">
<c:if test="${empty results}">
  <h4>No pages found.</h4>
</c:if>
<c:if test="${not empty results}">
  <h4>Found ${fn:length(results)} page(s) that match <i>${param.term}</i>.</h4>
  <c:forEach items="${results}" var="result" varStatus="status">
  <p>${status.count}.
    <a href="<c:url value='${result.path}' />"><b>${result.subject}</b></a></p>
  <p style="margin-left: 2em;">${result.content}</p>
  </c:forEach>
</c:if>
</div>
