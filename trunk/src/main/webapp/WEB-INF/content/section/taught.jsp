<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
/* <![CDATA[ */
$(function(){
    $("select[name='quarter'] option").each(function(){
       if( $(this).val() == ${quarter.code}) 
           $(this).attr('selected', true);
    });
    $("select[name='quarter']").change(function(){
        var quarter = $("select[name='quarter'] option:selected").val();
        window.location.href = "taught?quarter=" + quarter;
    });
});
/* ]]> */
</script>

<ul id="title">
  <li>Sections</li>
  <li class="align_right">
    <select class="formselect" name="quarter">
      <c:forEach var="q" items="${quarters}"><option value="${q.code}">${q}</option></c:forEach>
    </select>
  </li>
</ul>

<c:forEach var="section" items="${sections}">
<table class="outer_viewtable">
  <tr class="rowtypea">
    <td>${section.course.code} ${section.course.name} - ${section.number}</td>
    <td class="action">
      <a href="<c:url value='/section/edit?id=${section.id}' />"> <img alt="[Edit]"
         title="Edit" src="<c:url value='/img/icons/table_edit.png'/>" /></a>
    </td>
  </tr>
  <tr> 
    <td colspan="2">Assignments go here.</td>
  </tr>
  <tr class="rowtypeb">
      <td colspan="2">
        <a href="assignment?sectionId=${section.id}">Add Assignment</a> |
        <a href="viewStudents.html?sectionId=${section.id}">View Students</a> |
        <a href="importRoster.html?sectionId=${section.id}">Enroll Students</a> |
        <a href="viewEnrollments.html?sectionId=${section.id}">Enter Grades</a> |
        <a href="<c:url value='/assessment/editCourseJournal.html?sectionId=${section.id}'/>">Assessment</a>
      </td>
  </tr>
</table>
</c:forEach>
