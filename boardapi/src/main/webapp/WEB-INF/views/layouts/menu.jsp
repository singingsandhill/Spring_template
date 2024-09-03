<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-md bg-primary navbar-dark sticky-top">
    <a class="navbar-brand" href="#">
        <i class="fa-solid fa-house"></i>
        Backend
    </a>

    <button class="navbar-toggler" type="button" data-bs-tog    gle="collapse" data-bs-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <%--  좌측 메뉴 구성 --%>
        <ul class="navbar-nav">
            <ol class="nav-item">
                <a class="nav-link" href="#">메뉴 1</a>
            </ol>
            <ol class="nav-item">
                <a class="nav-link" href="#">메뉴 2</a>
            </ol>
            <ol class="nav-item">
                <a class="nav-link" href="#">메뉴 3</a>
            </ol>
        </ul>
        <%--  우측 메뉴 구성 --%>
        <ul class="navbar-nav ms-auto">
            <ol class="nav-item">
                <a class="nav-link" href="#">
                    <img src="https://randomuser.me/api/portraits/men/12.jpg" class="avatar-sm" />
                admin
                </a>
            </ol>
            <ol class="nav-item">
                <a class="nav-link" href="#">
                    <i class="fa-solid fa-right-from-bracket"></i>
                    로그아웃
                </a>
            </ol>
        </ul>
    </div>
</nav>