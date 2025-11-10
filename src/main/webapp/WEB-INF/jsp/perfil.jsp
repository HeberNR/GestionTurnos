<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="container mt-5">
    <c:if test="${not empty sessionScope.mensajeFlash}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.mensajeFlash}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="mensajeFlash" scope="session"/>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1>Mi Perfil</h1>
        <a href="${pageContext.request.contextPath}/mi-perfil/editar" class="btn btn-outline-primary">
            Editar Perfil
        </a>
    </div>
    <hr>

    <div class="card">
        <div class="card-body">
            <c:if test="${not empty sessionScope.usuarioLogueado}">
                <%-- Usamos ${usuario} como atajo para ${sessionScope.usuarioLogueado} --%>
                <c:set var="usuario" value="${sessionScope.usuarioLogueado}"/>

                <p><strong>Nombre:</strong> ${usuario.nombre}</p>
                <p><strong>Apellido:</strong> ${usuario.apellido}</p>
                <p><strong>Email:</strong> ${usuario.email}</p>
                <p><strong>DNI:</strong> ${usuario.dni}</p>
                <p><strong>Teléfono:</strong> ${usuario.telefono}</multiline>
                <p><strong>Rol:</strong> ${usuario.rol}</p>

                <c:if test="${usuario.rol == 'MEDICO'}">
                    <hr>
                    <p><strong>Matrícula:</strong> ${usuario.matricula}</p>
                    <p><strong>Especialidad:</strong> ${usuario.especialidad}</p>
                </c:if>

                <c:if test="${usuario.rol == 'PACIENTE'}">
                    <hr>
                    <p><strong>Obra Social:</strong> ${usuario.obraSocial}</p>
                </c:if>
            </c:if>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>