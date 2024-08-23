<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<%@ include file="../layouts/header.jsp" %>

<h1 class="page-header my-4"><i class="far fa-file-alt"></i> ${board.title}</h1>

<div class="d-flex justify-content-between">
    <div><i class="fas fa-user"></i> ${board.writer}</div>
    <div>
        <i class="fas fa-clock"></i>
        <fmt:formatDate pattern="yyyy-MM-dd" value="${board.regDate}"/>
    </div>
</div>
<div class="text-end">
    <c:forEach var="file" items="${board.attaches}">
        <div class="attach-file-item">
            <!-- 이 링크는 파일을 다운로드하는 경로로, Spring Controller에서 /board/download/{file.no}로 매핑된 메서드를 찾아서 처리하게 됩니다. -->
            <a href="/board/download/${file.no}" class="file-link">
                <i class="fa-solid fa-floppy-disk"></i>
                    ${file.filename} (${file.fileSize})<br>
            </a>
        </div>
    </c:forEach>
</div>

<hr/>

<div>
    <pre>${board.content}</pre>
</div>
<div class="mt-4">
    <!-- 이 버튼은 게시물 리스트 페이지로 이동하는 링크입니다.
         예를 들어, Spring Controller에서 "/list" 경로를 매핑하여 게시물 목록을 보여주는 메서드가 있을 것입니다. -->
    <a href="list" class="btn btn-primary"><i class="fas fa-list"></i> 목록</a>

    <!-- 이 버튼은 게시물 수정 페이지로 이동하는 링크입니다.
         Spring Controller에서 "/update" 경로와 쿼리 파라미터 no를 매핑한 메서드가 해당 페이지를 반환하도록 구현됩니다. -->
    <a href="update?no=${board.no}" class="btn btn-primary"><i class="far fa-edit"></i> 수정</a>

    <!-- 이 버튼은 게시물 삭제를 요청하는 버튼입니다.
         실제로는 클릭 시 자바스크립트로 삭제 폼을 제출하는 로직이 있을 것입니다. -->
    <a href="#" class="btn btn-primary delete"><i class="fas fa-trash-alt"></i> 삭제</a>
</div>

<!-- 이 폼은 게시물 삭제 요청을 서버로 전송하기 위한 폼입니다.
Spring Controller에서 "/delete" 경로를 매핑한 POST 메서드가 이 요청을 처리하게 됩니다. -->
<form action="delete" method="post" id="deleteForm">
    <input type="hidden" name="no" value="${board.no}"/>
</form>

<!-- 이 스크립트 파일은 자바스크립트 로직을 포함하고 있으며, 일반적으로 스프링에서 정적 자원을 제공할 때 "/resources/js/board.js" 경로로 접근할 수 있습니다. -->
<script src="/resources/js/board.js"></script>

<%@ include file="../layouts/footer.jsp" %>
