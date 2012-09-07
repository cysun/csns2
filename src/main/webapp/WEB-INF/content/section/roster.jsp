<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${gradeSheet.section}"/>

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[0,0]]
   });
   $("select").each(function(){
       $(this).change(function(event){
           $("#span-"+event.target.id).load("grade?enrollmentId=" + event.target.id + "&gradeId=" + $(this).val());
           $("#span-"+event.target.id).addClass("bold");
       });
   });
   $("#selectAll").toggle(
       function(){ $(":checkbox[name='userId']").attr("checked",true); },
       function(){ $(":checkbox[name='userId']").attr("checked",false); }
   );
   $("#email").click(function(){
       if( $(":checkbox[name='userId']:checked").length == 0 )
           alert( "Please select the student(s) to contact." );
       else
           $("#studentsForm").attr("action", "<c:url value='/email/compose' />").submit();
   });
   $("#drop").click(function(){
       if( $(":checkbox[name='userId']:checked").length == 0 )
           alert( "Please select the student(s) you want to drop." );
       else
           if( confirm("Are you sure you want to drop these students from the class?") )
               $("#studentsForm").attr("action", "roster/drop").submit();
   });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/taught' />">${section.quarter}</a></li>
<li><a class="bc" href="<c:url value='/section/taught#section-${section.id}' />">${section.course.code} - ${section.number}</a></li>
<li>Students</li>
<li class="align_right"><a href="export?id=${section.id}&amp;format=excel"><img title="Export to Excel"
    alt="[Export to Excel]" src="<c:url value='/img/icons/export_excel.png' />" /></a></li>
<li class="align_right"><a id="drop" href="javascript:void(0)"><img title="Drop Student(s)"
    alt="[Drop Student(s)]" src="<c:url value='/img/icons/user_delete.png' />" /></a></li>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Student(s)"
    alt="[Email Student(s)]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</ul>

<form id="studentsForm" method="post">
<table class="viewtable">
<thead>
  <tr>
    <th><input id="selectAll" type="checkbox" /></th>
    <th>Name</th><th>Grade</th>
    <c:forEach items="${section.assignments}" var="assignment">
      <th>${assignment.alias}</th>
    </c:forEach>
  </tr>
</thead>
<tbody>
  <c:forEach items="${gradeSheet.studentGrades}" var="studentGrade">
  <c:set var="enrollment" value="${studentGrade.key}" />
    <tr>
      <td class="center"><input type="checkbox" name="userId" value="${enrollment.student.id}" /></td>
      <td><a href="grade?enrollmentId=${enrollment.id}">${enrollment.student.lastName},
        ${enrollment.student.firstName}</a></td>
      <td class="center">
        <span id="span-${enrollment.id}">
          <c:if test="${empty enrollment.grade}">
            <select id="${enrollment.id}">
              <option value="-">-</option>
              <c:forEach items="${grades}" var="grade">
                <option value="${grade.id}">${grade.symbol}</option>
              </c:forEach>
            </select>
          </c:if>
          <c:if test="${not empty enrollment.grade and enrollment.gradeMailed}">${enrollment.grade.symbol}</c:if>
          <c:if test="${not empty enrollment.grade and not enrollment.gradeMailed}"><b>${enrollment.grade.symbol}</b></c:if>
        </span>
      </td>
      <c:forEach items="${studentGrade.value}" var="grade">
        <td class="center">${grade}</td>
      </c:forEach>
    </tr>
  </c:forEach>
</tbody>
</table>
<input type="hidden" name="sectionId" value="${section.id}" />
<input type="hidden" name="backUrl" value="/section/roster?id=${section.id}" />
</form>
