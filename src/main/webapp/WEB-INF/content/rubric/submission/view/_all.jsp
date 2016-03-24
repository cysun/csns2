<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="tab-all">
<h4>Instructor Evaluations: ${submission.instructorEvaluationCount}</h4>
<c:if test="${submission.instructorEvaluationCount > 0}">
<table class="general2 autowidth">
<thead>
  <tr>
    <th>Evaluator</th>
    <c:forEach items="${rubric.indicators}" var="indicator">
      <th>${indicator.name}</th>
    </c:forEach>
    <th>Comments</th>
  </tr>
</thead>
<tbody>
  <c:forEach items="${submission.instructorEvaluations}" var="ievaluation">
  <c:if test="${ievaluation.completed}">
  <tr>
    <td class="nowrap">${ievaluation.evaluator.name}</td>
    <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
      <td class="center">
        <c:if test="${ievaluation.ratings[status.index] > 0}">
          ${ievaluation.ratings[status.index]}
        </c:if>
      </td>
    </c:forEach>
    <td class="center">
      <c:if test="${not empty ievaluation.comments}">
      <a href="javascript:void(0)" class="view-comments" data-eval-id="${ievaluation.id}">View</a>
      <div class="comments-dialog" data-eval-id="${ievaluation.id}">${ievaluation.comments}</div>
      </c:if>
    </td>
  </tr>
  </c:if>
  </c:forEach>
</tbody>
</table>
</c:if>

<h4>Peer Evaluations: ${submission.peerEvaluationCount}</h4>
<c:if test="${submission.peerEvaluationCount > 0}">
<table class="general2 autowidth">
<thead>
  <tr>
    <th>Evaluator</th>
    <c:forEach items="${rubric.indicators}" var="indicator">
      <th>${indicator.name}</th>
    </c:forEach>
    <th>Comments</th>
  </tr>
</thead>
<tbody>
  <c:forEach items="${submission.peerEvaluations}" var="pevaluation">
  <c:if test="${pevaluation.completed}">
  <tr>
    <td class="nowrap">${pevaluation.evaluator.name}</td>
    <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
      <td class="center">
        <c:if test="${pevaluation.ratings[status.index] > 0}">
          ${pevaluation.ratings[status.index]}
        </c:if>
      </td>
    </c:forEach>
    <td class="center">
      <c:if test="${not empty pevaluation.comments}">
      <a href="javascript:void(0)" class="view-comments" data-eval-id="${pevaluation.id}">View</a>
      <div class="comments-dialog" data-eval-id="${pevaluation.id}">${pevaluation.comments}</div>
      </c:if>
    </td>
  </tr>
  </c:if>
  </c:forEach>
</tbody>
</table>
</c:if>

<h4>External Evaluations: ${submission.externalEvaluationCount}</h4>
<c:if test="${submission.externalEvaluationCount > 0}">
<table class="general2 autowidth">
<thead>
  <tr>
    <th>Evaluator</th>
    <c:forEach items="${rubric.indicators}" var="indicator">
      <th>${indicator.name}</th>
    </c:forEach>
    <th>Comments</th>
  </tr>
</thead>
<tbody>
  <c:forEach items="${submission.externalEvaluations}" var="eevaluation">
  <c:if test="${eevaluation.completed}">
  <tr>
    <td class="nowrap">${eevaluation.evaluator.name}</td>
    <c:forEach items="${rubric.indicators}" var="indicator" varStatus="status">
      <td class="center">
        <c:if test="${eevaluation.ratings[status.index] > 0}">
          ${eevaluation.ratings[status.index]}
        </c:if>
      </td>
    </c:forEach>
    <td class="center">
      <c:if test="${not empty eevaluation.comments}">
      <a href="javascript:void(0)" class="view-comments" data-eval-id="${eevaluation.id}">View</a>
      <div class="comments-dialog" data-eval-id="${eevaluation.id}">${eevaluation.comments}</div>
      </c:if>
    </td>
  </tr>
  </c:if>
  </c:forEach>
</tbody>
</table>
</c:if>
</div> <!--  end of tab-all -->
