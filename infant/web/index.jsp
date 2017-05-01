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
</head>
<body>
<select id="first" onChange="change()">
    <option selected="selected">湖北</option>
    <option>广东</option>
</select>

<select id="second">
    <option>黄冈</option>
    <option selected="selected">武汉</option>
</select>


<script>
    function change()
    {
        var x = document.getElementById("first");
        var y = document.getElementById("second");
        y.options.length = 0; // 清除second下拉框的所有内容
        if(x.selectedIndex == 0)
        {
            y.options.add(new Option("黄冈", "0"));
            y.options.add(new Option("武汉", "1", false, true));  // 默认选中省会城市
        }

        if(x.selectedIndex == 1)
        {
            y.options.add(new Option("深圳", "0"));
            y.options.add(new Option("广州", "1", false, true));  // 默认选中省会城市
            y.options.add(new Option("肇庆", "2"));
        }

    }
</script>

</form>
</body>
</html>
