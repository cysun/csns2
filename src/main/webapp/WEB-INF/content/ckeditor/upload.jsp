<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>File Uploaded</title>
</head>
<body>
<script type="text/javascript">
    window.parent.CKEDITOR.tools.callFunction(
        ${param.CKEditorFuncNum},
        '<c:url value="/download?fileId=${file.id}" />',
        '');
</script>
</body></html>
