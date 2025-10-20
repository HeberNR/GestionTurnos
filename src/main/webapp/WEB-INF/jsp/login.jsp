<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="container-fluid">
    <div class="row min-vh-100">
        <div class="col-12 col-md-6 d-flex align-items-center justify-content-center">
            <div class="p-4 p-md-5 w-100" style="max-width: 450px;">

                <h2 class="mb-4 text-center text-primary" id="login-titulo">Iniciar Sesión</h2>
                <p class="text-center text-muted mb-4">Bienvenido de nuevo.</p>

                <c:if test="${not empty sessionScope.mensajeFlash}">
                    <div class="alert alert-success" role="alert">
                        ${sessionScope.mensajeFlash}
                    </div>
                    <c:remove var="mensajeFlash" scope="session" />
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="POST" role="form" aria-labelledby="login-titulo">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required aria-required="true" autocomplete="email">
                    </div>
                    <div class="mb-3">
                        <label for="pass" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="pass" name="pass" required aria-required="true" autocomplete="current-password">
                    </div>

                    <button type="submit" class="btn btn-primary w-100 fw-bold mt-3">INGRESAR</button>
                </form>
                <p class="text-center mt-3"><a href="${pageContext.request.contextPath}/registro" class="text-decoration-none">¿No tienes cuenta? Regístrate</a></p>
            </div>
        </div>
        <div class="col-md-6 d-none d-md-block p-0"
             style="background: url('${pageContext.request.contextPath}/images/imgLogin.jpg') center center no-repeat; background-size: cover;"
             aria-hidden="true">
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>