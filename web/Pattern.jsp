<%@ page import="Utils.Db" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLSyntaxErrorException" %><%--
  Created by IntelliJ IDEA.
  User: isanhae
  Date: 2020/05/23
  Time: 6:34 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>패턴 목록</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!-- jquery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
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

        #patten-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            width: 600px;
            grid-gap: 50px;
        }
    </style>
</head>
<body>
<h3>패턴 목록</h3>
<br>
<div id="patten-container">
    <% Db db = new Db();
        int[][] outputs = db.getOutput();
        double[][] inputs = db.getInput();
        for (int i = 0; i < outputs.length; i++) {
            int[] output = outputs[i];
            double[] input = inputs[i];

            ArrayList<ArrayList<Double>> inputList = new ArrayList<>();
            for (int j = 0; j < input.length; j++) {
                if (j % 5 == 0) {
                    inputList.add(new ArrayList<>());
                }
                inputList.get(inputList.size() - 1).add(input[j]);
            }
            String teach = "";
            for (int num : output) {
                teach += num;
            }
    %>
    <div>
        <table style="border: none; border-spacing: 0px">
            <% for (int x = 0; x < 5; x++) { %>
            <tr>
                <% for (int j = 0; j < 5; j++) {
                    if (inputList.get(x).get(j) == 1) { %>
                <td>
                    <button type="button" class="cell cell_output active"></button>
                </td>
                <% } else { %>
                <td>
                    <button type="button" class="cell cell_output "></button>
                </td>
                <% } %>

                <% }%>
            </tr>
            <% } %>
        </table>
        <p><%=teach%>
        </p>
    </div>
    <% } %>
</div>
<div class="horizontal">
    <button type="button" id="save-btn" class="btn btn-primary" onclick="location.href='Check.jsp'">비교하기</button>
    <div style="width: 20px"></div>
    <button onclick="location.href='index.jsp'" type="button" class="btn btn-outline-primary">학습하기</button>
</div>
</body>
</html>
