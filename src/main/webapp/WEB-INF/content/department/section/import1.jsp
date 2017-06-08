<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
$(function(){
    $("#number").change(function(){
        var number = $(this).val();
        if( number > 0 )
        {
            $.ajax({
                url: "grades",
                data: {
                    term: "${term.code}",
                    course: "${course.id}",
                    number: number
                },
                success: function(data){
                    $("#text").text(data);
                    $("input[name='number']").val(number);
                }
            });
        }
        else
        {
            $("#text").text("");
            $("input[name='number']").val("-1");
        }
    });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?term=${term.code}'
    />">${department.name}</a></li>
<li>Import Section</li>
</ul>

<p>The grades can be downloaded as a <span class="tt">.xls</span> file from GET
under Main Menu -&gt; Curriculum Management -&gt; Grading -&gt;  Grade Roster.
<em>Unfortunately, the file is actually an HTML file instead of an Excel
file (the incompetence of GET is just mind-boggling), so you'll have to
first open the file using MS Excel, then save it as an Excel workbook.</em>
After that you can upload the Excel file below. The order of the columns in
the file does not matter, but the first row of the file must be a header
row with at least the following column names:</p>
<ul>
  <li>ID (i.e. CIN)</li>
  <li>Name</li>
  <li>Official Grade</li>
</ul>
  
<p>Before uploading the file, please select the correct section number. Do NOT
select <span class="tt">NEW</span> if the section already exists. Note that CSNS
section numbers do not match GET section numbers, so please choose the section
number <em>very carefully</em>.</p>

<form action="import2" method="post" enctype="multipart/form-data">
<p><input type="file" name="file" required /> <button class="subbutton">Upload</button></p>
<input type="hidden" name="term" value="${term.code}" />
<input type="hidden" name="course" value="${course.id}" />
<input type="hidden" name="instructor" value="${instructor.id}" />
<input type="hidden" name="number" value="-1" />
</form>

<table class="viewtable autowidth">
  <tr><th>Term</th><th>Course</th><th>Instructor</th><th>Section</th></tr>
  <tr>
    <td>${term}</td>
    <td>${course.code}</td>
    <td>${instructor.name}</td>
    <td>
      <select id="number">
        <option value="-1">NEW</option>
        <c:forEach items="${sections}" var="section">
          <option value="${section.number}">${section.number}</option>
        </c:forEach>
      </select>
    </td>
  </tr>
</table>

<pre id="text" class="autowidth"></pre>

