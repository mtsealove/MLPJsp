<%--
  Created by IntelliJ IDEA.
  User: isanhae
  Date: 2020/05/22
  Time: 5:21 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>학습 패턴 저장</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!-- jquery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <!-- ajax -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script>
        var arr = Array();
        $(function () {
            for (var i = 0; i < 5; i++) {
                arr.push(Array());
                for (var j = 0; j < 5; j++) {
                    arr[i].push(-1);
                }
            }
            setCell();
            $('#save-btn').click(function () {
                if ($('#teach').val() == '') {
                    alert('학습 문자를 입력하세요.');
                    return;
                }
                if (confirm('학습을 진행하시겠습니까?')) {
                    var array = Array();
                    for (var i = 0; i < 5; i++) {
                        for (var j = 0; j < 5; j++) {
                            array.push(arr[i][j]);
                        }
                    }
                    $.post('Learn.jsp', {arr: array, teach: $('#teach').val()}, function (data) {
                        alert('학습이 진행되었습니다.');
                        location.reload();
                    });
                }
            });
        });

        // 클릭으로 배열 설정
        function setCell() {
            $('.cell').click(function () {
                var i = $(this).data('i');
                var j = $(this).data('j');
                if (arr[i][j] == -1) {
                    $(this).addClass('active');
                    arr[i][j] = 1;
                } else {
                    $(this).removeClass('active');
                    arr[i][j] = -1;
                }
            });
        }

    </script>
    <style>
        .cell {
            width: 50px;
            height: 50px;
            background-color: #FFCC00;
            border: none;
        }

        .active {
            background-color: #001f46;
        }

        body {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        .horizontal {
            display: flex;
            flex-direction: row;
            align-items: center;
        }

        .between {
            justify-content: space-between;
        }
    </style>
</head>

<body>
<div style="width: 250px; display: flex; flex-direction: column; align-items: center">
    <div>
        <h3>학습 문자 입력</h3>
        <table style="border: none; border-spacing: 0px">
            <% for (int i = 0; i < 5; i++) { %>
            <tr>
                <% for (int j = 0; j < 5; j++) { %>
                <td>
                    <button type="button" class="cell" data-i="<%=i%>" data-j="<%=j%>"></button>
                </td>
                <% }%>
            </tr>
            <% } %>
        </table>
    </div>
    <br>
    <input name="teach" class="form-control" placeholder="학습 패턴 ex)1001001000" id="teach">
    <br>
    <div style="width: 100%">
        <button type="button" id="save-btn" class="btn btn-primary btn-block">저장하기</button>
        <button type="button" class="btn btn-outline-danger btn-block" onclick="location.href='Pattern.jsp'">패턴 목록 보기</button>
        <button onclick="location.href='Check.jsp'" type="button" class="btn btn-outline-primary btn-block">비교</button>
    </div>
</div>

</body>
</html>
