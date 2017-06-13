<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
$(function(){
    /* sort the program table and hide the <thead> element */
    $("#program").tablesorter({
        sortList: [[0,0]]
    });
    $(".hidden").hide();
});
</script>

<table class="general autowidth">
<tr>
  <th>Major</th>
  <td style="min-width: 4em;">${user.major.name}</td>
</tr>  
<tr>
  <th>Program</th>
  <td style="min-width: 4em;">${user.personalProgram.program.name}</td>
</tr>
</table>

<c:if test="${not empty user.personalProgram}">
<%-- Approval Block --%>
<c:if test="${user.personalProgram.approved}">
<p><em>Reviewed and approved by ${user.personalProgram.approvedBy.name} on
<fmt:formatDate value="${user.personalProgram.approveDate}" type="date" />.</em></p>
</c:if>

<%-- personal program --%>
<div id="personal-program">
<table id="program" class="general2 autowidth">
<thead class="hidden"><tr><th colspan="6"></th></tr></thead>
<c:forEach items="${user.personalProgram.blocks}" var="block">
<tbody>
  <tr>
    <th class="block-title" data-block-id="${block.id}" colspan="6" style="text-align: left;">
      <c:if test="${block.requirementsMet}">
        <span class="requirements-indicator requirements-met">&nbsp;</span>
      </c:if>
      <c:if test="${not block.requirementsMet}">
        <span class="requirements-indicator requirements-not-met">&nbsp;</span>
      </c:if>
      ${block.programBlock.name}
      <c:if test="${block.programBlock.requireAll}">(All Courses Required)</c:if>
      <c:if test="${not block.programBlock.requireAll}">(${block.programBlock.unitsRequired} Units Required)</c:if>
    </th>
  </tr>
</tbody>
<tbody>
  <c:forEach items="${block.entries}" var="entry">
  <tr class="entry" data-entry-id="${entry.id}">
    <td class="entry-code">${entry.course.code}</td>
    <td>${entry.course.name}</td>
    <td>${entry.course.units}</td>
    <c:if test="${empty entry.enrollment}">
      <td colspan="3" class="notes center">
        <c:choose>
          <c:when test="${entry.requirementMet}">Requirement Satisfied</c:when>
          <c:when test="${entry.prereqMet}">Prerequisites Met</c:when>
        </c:choose>
      </td>
    </c:if>
    <c:if test="${not empty entry.enrollment}">
      <td>${entry.enrollment.section.term}</td>
      <td>${entry.enrollment.section.course.code}</td>
      <td>${entry.enrollment.grade.symbol}</td>
    </c:if>
  </tr>
  </c:forEach>
</tbody>
</c:forEach>
</table>
</div>
</c:if>
