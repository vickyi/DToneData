<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Loading...</title>
    <script src="statics/libs/jquery.min.js"></script>
</head>

<script type="text/javascript">
    var uid = '<%=request.getAttribute("uid") %>';
    var data = "uid=" + uid;
    $.ajax({
        type: "POST",
        url: "sys/ssoLogin",
        data: data,
        dataType: "json",
        success: function (result) {
            if (result.code == 0) {
                parent.location.href = 'index.html';
            } else {
                parent.location.href = 'index.html';
            }
        }
    });
</script>
<body>
</body>
</html>