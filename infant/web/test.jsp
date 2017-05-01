<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/10/18 0018
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试页面</title>
    <!-- 引用jquery -->
    <script src="js/jquery.min.js"></script>
    <!-- 引用ajaxfileupload.js -->
    <script src="js/ajaxfileupload.js"></script>
    <script src="js/upload.js"></script>
</head>
<body>
<%--<form action="http://localhost:8082/admin/imgTest" method="post" enctype="multipart/form-data" >
    <!-- 隐藏file标签 -->
    <input id="fileToUpload1" type="file" name="file"><br/>
    <input type="text" name="type" value="0"><br/>
    <input type="submit" value="提交">
</form>--%>

下面用ajaxFileUpload
<!-- 上传图片 -->
<input type="file" name="file" id="fileToUpload1">
<button id="upload" onclick="return false;">上传</button>
<!-- 存储图片地址，并显示图片 -->
<input type="hidden" name="pictureSrc" id="pictureSrc">
<div id="show"></div>
<script>

</script>

</body>
</html>
