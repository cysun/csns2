<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="tab-summary">
<c:if test="${not empty iEvalStats}">
<h4>Instructor Evaluations: ${iEvalStats[0].count}</h4>
<c:if test="${iEvalStats[0].count > 0}">
<table class="general2 autowidth">
<tr><th>Indicator</th><th>Mean</th><th>Median</th><th>Min</th><th>Max</th></tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <td class="center"><fmt:formatNumber pattern=".00" value="${iEvalStats[status.index+1].mean}" /></td>
  <td class="center"><fmt:formatNumber pattern=".0" value="${iEvalStats[status.index+1].median}" /></td>
  <td class="center"><fmt:formatNumber value="${iEvalStats[status.index+1].min}" /></td>
  <td class="center"><fmt:formatNumber value="${iEvalStats[status.index+1].max}" /></td>
</tr>
</c:forEach>
<tr>
  <td class="overall" >Overall</td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].mean}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].median}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].min}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${iEvalStats[0].max}" /></td>
</tr>
</table>
<p>Comments:</p>
<ul>
<c:forEach items="${submission.instructorEvaluations}" var="ieval">
  <c:if test="${not empty ieval.comments}">
  <li>${ieval.comments}</li>
  </c:if>
</c:forEach>
</ul>
</c:if>
</c:if>

<c:if test="${not empty sEvalStats}">
<h4>Peer Evaluations: ${sEvalStats[0].count}</h4>
<c:if test="${sEvalStats[0].count > 0}">
<table class="general2 autowidth">
<tr><th>Indicator</th><th>Mean</th><th>Median</th><th>Min</th><th>Max</th></tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <td class="center"><fmt:formatNumber pattern=".00" value="${sEvalStats[status.index+1].mean}" /></td>
  <td class="center"><fmt:formatNumber pattern=".0" value="${sEvalStats[status.index+1].median}" /></td>
  <td class="center"><fmt:formatNumber value="${sEvalStats[status.index+1].min}" /></td>
  <td class="center"><fmt:formatNumber value="${sEvalStats[status.index+1].max}" /></td>
</tr>
</c:forEach>
<tr>
  <td class="overall" >Overall</td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].mean}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].median}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].min}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${sEvalStats[0].max}" /></td>
</tr>
</table>
<p>Comments:</p>
<ul>
<c:forEach items="${submission.peerEvaluations}" var="peval">
  <c:if test="${not empty peval.comments}">
  <li>${peval.comments}</li>
  </c:if>
</c:forEach>
</ul>
</c:if>
</c:if>

<c:if test="${not empty eEvalStats}">
<h4>External Evaluations: ${eEvalStats[0].count}</h4>
<c:if test="${eEvalStats[0].count > 0}">
<table class="general2 autowidth">
<tr><th>Indicator</th><th>Mean</th><th>Median</th><th>Min</th><th>Max</th></tr>
<c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
<tr>
  <td>${indicator.name}</td>
  <td class="center"><fmt:formatNumber pattern=".00" value="${eEvalStats[status.index+1].mean}" /></td>
  <td class="center"><fmt:formatNumber pattern=".0" value="${eEvalStats[status.index+1].median}" /></td>
  <td class="center"><fmt:formatNumber value="${eEvalStats[status.index+1].min}" /></td>
  <td class="center"><fmt:formatNumber value="${eEvalStats[status.index+1].max}" /></td>
</tr>
</c:forEach>
<tr>
  <td class="overall" >Overall</td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].mean}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].median}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].min}" /></td>
  <td class="overall center"><fmt:formatNumber pattern=".00" value="${eEvalStats[0].max}" /></td>
</tr>
</table>
<p>Comments:</p>
<ul>
<c:forEach items="${submission.externalEvaluations}" var="eeval">
  <c:if test="${not empty eeval.comments}">
  <li>${eeval.comments}</li>
  </c:if>
</c:forEach>
</ul>
</c:if>
</c:if>
</div> <!--  end of tab-summary -->
