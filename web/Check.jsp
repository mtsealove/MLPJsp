<%--
  Created by IntelliJ IDEA.
  User: isanhae
  Date: 2020/05/01
  Time: 6:26 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>비교</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!-- jquery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
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
                if (confirm('비교를 진행하시겠습니까?')) {
                    var array = Array();
                    for (var i = 0; i < 5; i++) {
                        for (var j = 0; j < 5; j++) {
                            array.push(arr[i][j]);
                        }
                    }
                    $('.cell_output').removeClass('active');
                    $('#console').html('');
                    webSocket.send(array.join(','));
                    // 비교 페이지로 배열 데이터 전송
                }
            });
        });


        function setCell() {
            $('.cell_input').click(function () {
                var i = $(this).data('i');
                var j = $(this).data('j');
                if (arr[i][j] == -1) {
                    $(this).addClass('active');
                    arr[i][j] = 1;
                } else {
                    $(this).removeClass('active');
                    arr[i][j] = -1;
                }

            })
        }

        var webSocket = new WebSocket('ws://localhost:8080/MLPJsp_war_exploded/Compare');
        webSocket.onerror = function (event) {
            console.log(event);
        };

        webSocket.onopen = function (event) {

        };

        webSocket.onmessage = function (event) {
            // console.log(event.data);
            // epoch 출력
            if (event.data.indexOf("epoch") != -1) {
                $('#console').prepend("<p class='console'>" + event.data + "</p>");
            } else if (event.data.indexOf("[") != -1) {
                // 결과 받음
                var res = event.data;
                res=res.substr(1, res.length-2);
                var data=res.split(',');
                console.log(data);
                var result=Array();
                // 1차원 배열을 2차원 배열로 변환
                for(var i=0; i<data.length; i++) {
                    if(i%5==0) {
                        result.push(Array());
                    }
                    result[result.length-1].push(data[i]);
                }
                console.log(result);
                // 변환된 데이터를 화면에 표시
                for(var i=0; i<5; i++) {
                    for(var j=0; j<5; j++) {
                        if(result[i][j]==1) {
                            $('.cell_output[data-i='+i+'][data-j='+j+']').addClass('active');
                        }
                    }
                }
            }
        };

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

        .horizontal {
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;
        }

        body {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        p.console {
            margin: 0px;
        }
    </style>
</head>
<body>
<div class="horizontal">
    <div>
        <h3>입력</h3>
        <table style="border: none; border-spacing: 0px">
            <% for (int i = 0; i < 5; i++) { %>
            <tr>
                <% for (int j = 0; j < 5; j++) { %>
                <td>
                    <button type="button" class="cell cell_input" data-i="<%=i%>" data-j="<%=j%>"></button>
                </td>
                <% }%>
            </tr>
            <% } %>
        </table>
    </div>
    <div style="width: 20px"></div>
    <div>
        <h3>출력</h3>
        <table style="border: none; border-spacing: 0px">
            <% for (int i = 0; i < 5; i++) { %>
            <tr>
                <% for (int j = 0; j < 5; j++) { %>
                <td>
                    <button type="button" class="cell cell_output" data-i="<%=i%>" data-j="<%=j%>"></button>
                </td>
                <% }%>
            </tr>
            <% } %>
        </table>
    </div>
    <div style="width: 20px"></div>
    <div>
        <h3>Console</h3>
        <div class="card" style="width: 260px; height: 260px">
            <div class="card-body" id="console" style="overflow: scroll">

            </div>
        </div>
    </div>
</div>
<br>
<div class="horizontal">
    <button type="button" class="btn btn-outline-warning" onclick="location.reload()">새로고침</button>
    <div style="width: 20px"></div>
    <button type="button" class="btn btn-outline-danger" onclick="location.href='Pattern.jsp'">패턴 목록 보기</button>
    <div style="width: 20px"></div>
    <button type="button" id="save-btn" class="btn btn-primary">비교하기</button>
    <div style="width: 20px"></div>
    <button onclick="location.href='index.jsp'" type="button" class="btn btn-outline-primary">학습하기</button>
</div>
</body>
</html>
