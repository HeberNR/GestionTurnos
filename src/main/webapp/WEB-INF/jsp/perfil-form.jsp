<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="container mt-5 mb-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h3 id="form-titulo">${requestScope.titulo}</h3>
                </div>
                <div class="card-body">

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger" role="alert">
                            ${requestScope.error}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/mi-perfil/editar" method="POST" role="form" aria-labelledby="form-titulo">

                        <h5 class="text-secondary mb-3">Datos de Cuenta (No editables)</h5>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" value="${usuario.email}" disabled readonly>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="dni" class="form-label">DNI</label>
                                <input type="text" class="form-control" id="dni" value="${usuario.dni}" disabled readonly>
                            </div>
                        </div>

                        <hr>
                        <h5 class="text-secondary mb-3">Datos Personales (Editables)</h5>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="nombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" value="${usuario.nombre}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apellido" class="form-label">Apellido</label>
                                <input type="text" class="form-control" id="apellido" name="apellido" value="${usuario.apellido}" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="telefono" class="form-label">Teléfono</label>
                            <input type="tel" class="form-control" id="telefono" name="telefono" value="${usuario.telefono}">
                        </div>

                        <c:if test="${usuario.rol == 'MEDICO'}">
                            <hr>
                            <h5 class="text-secondary mb-3">Datos de Médico</h5>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="matricula" class="form-label">Matrícula</label>
                                    <input type="text" class="form-control" id="matricula" name="matricula" value="${usuario.matricula}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="especialidad" class="form-label">Especialidad</label>
                                    <input type="text" class="form-control" id="especialidad" name="especialidad" value="${usuario.especialidad}">
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${usuario.rol == 'PACIENTE'}">
                            <hr>
                            <h5 class="text-secondary mb-3">Datos de Paciente</h5>
                            <div class="mb-3">
                                <label for="obraSocial" class="form-label">Obra Social</label>
                                <input type="text" class="form-control" id="obraSocial" name="obraSocial" value="${usuario.obraSocial}">
                            </div>
                        </c:if>

                        <div class="d-flex justify-content-end mt-4">
                            <a href="${pageContext.request.contextPath}/perfil" class="btn btn-secondary me-2">Cancelar</a>
                            <button type="submit" class="btn btn-success">Guardar Cambios</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>