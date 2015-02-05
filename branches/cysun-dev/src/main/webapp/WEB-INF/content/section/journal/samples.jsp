<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
$(function(){
    $(".enrollment-toggle").change(function(){
        $.ajax({
           url: "toggleEnrollment",
           data: {
               "journalId": ${section.journal.id},
               "enrollmentId": $(this).attr("data-enrollment-id")
           },
           cache: false
        });
    });
    $("table").tablesorter({
        headers: { 0: {sorter: false} },
        sortList: [[1,0], [0,0]]
    });
});
</script>

<ul id="title">
<li><a href="<c:url value='/section/taught?quarter=${section.quarter.code}#section-${section.id}' />"
       class="bc" >${section.course.code} - ${section.number}</a></li>
<li><a href="view?sectionId=${section.id}" class="bc">Course Journal</a></li>
<li>Student Samples</li>
</ul>

<table class="viewtable autowidth">
  <thead>
    <tr><th>Student</th><th>Grade</th></tr>
  </thead>
  <tbody>
  <c:forEach items="${section.enrollments}" var="enrollment">
  <tr>
    <td>
      <input type="checkbox" class="enrollment-toggle" data-enrollment-id="${enrollment.id}"
        <c:if test="${section.journal.studentSamples.contains(enrollment)}">checked="checked"</c:if> />
      ${enrollment.student.name}
    </td>
    <td class="center"><a href="../grade?enrollmentId=${enrollment.id}">${enrollment.grade.symbol}</a></td>
  </tr>
  </c:forEach>
  </tbody>
</table>
