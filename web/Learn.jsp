<%@ page import="Utils.Db" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Utils.Pattern" %>
<%@ page import="java.util.Arrays" %>
<%--
  Created by IntelliJ IDEA.
  User: isanhae
  Date: 2020/05/22
  Time: 5:15 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8");
    String[] Arr = request.getParameterValues("arr[]");
    String line = "";
    String teach=request.getParameter("teach");
    System.out.println(teach);

    for (int i = 0; i < Arr.length; i++) {
        String word = Arr[i];
        line += word;
        if (i != Arr.length - 1) {
            line += ",";
        }
    }
//  학습할 데이터를 문자열 형태로 db에 저장
    Db manager = new Db();
    manager.insert(line, teach);
//    학습된 패턴 출력
    ArrayList<Pattern> patterns = manager.getPatterns();
    for(Pattern pattern:patterns) {
        System.out.println(Arrays.toString(pattern.getP()));
    }

   out.println("{ 'result' :'ok'}");
%>
