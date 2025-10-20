<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Gestión de Turnos</h1>
        <a href="${pageContext.request.contextPath}/turno-form" class="btn btn-primary">
            <i class="bi bi-plus-circle me-2" aria-hidden="true"></i>Nuevo Turno
        </a>
    </div>

    <div class="card mb-4">
        <div class="card-header" id="filtro-titulo">
            <i class="bi bi-funnel-fill me-2" aria-hidden="true"></i>Filtrar por Estado
        </div>
        <div class="card-body">
            <%-- 'role="form"' y 'aria-labelledby' le dan un nombre y propósito al formulario. --%>
            <form action="${pageContext.request.contextPath}/gestion-turnos" method="GET" class="row g-3 align-items-end" role="form" aria-labelledby="filtro-titulo">
                <div class="col-md-5">
                    <label for="filtroEstado" class="form-label">Estado del Turno</label>
                    <select id="filtroEstado" name="filtroEstado" class="form-select">
                        <option value="">Mostrar Todos</option>
                        <c:forEach var="estado" items="${requestScope.estadosPosibles}">
                            <option value="${estado}" ${requestScope.filtroEstadoActual == estado ? 'selected' : ''}>${estado}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-7">
                    <button type="submit" class="btn btn-success me-2">
                        <i class="bi bi-search" aria-hidden="true"></i> Filtrar
                    </button>
                    <a href="${pageContext.request.contextPath}/gestion-turnos" class="btn btn-secondary">Limpiar Filtro</a>
                </div>
            </form>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <caption class="visually-hidden">
                Lista de turnos registrados en el sistema, con opciones para filtrar, editar y eliminar.
            </caption>
            <thead class="table-dark">
                <tr>
                    <th scope="col">Fecha y Hora</th>
                    <th scope="col">Paciente</th>
                    <th scope="col">Médico</th>
                    <th scope="col">Motivo</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="turno" items="${requestScope.listaTurnos}">
                    <tr>
                        <td>${turno.fecha} ${turno.hora}</td>
                        <td>${turno.paciente.apellido}, ${turno.paciente.nombre}</td>
                        <td>${turno.medico.apellido}, ${turno.medico.nombre}</td>
                        <td>${turno.motivo}</td>
                        <td><span class="badge bg-info">${turno.estado}</span></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/turno-form?idTurno=${turno.idTurno}"
                               class="btn btn-sm btn-warning"
                               aria-label="Editar turno de ${turno.paciente.apellido} del ${turno.fecha}">
                                <i class="bi bi-pencil-square" aria-hidden="true"></i>
                            </a>

                            <form action="${pageContext.request.contextPath}/turno-delete" method="POST" style="display: inline;" onsubmit="return confirm('¿Estás seguro de que deseas eliminar este turno?');">
                                <input type="hidden" name="idTurno" value="${turno.idTurno}">

                                <button type="submit" class="btn btn-sm btn-danger"
                                        aria-label="Eliminar turno de ${turno.paciente.apellido} del ${turno.fecha}">
                                    <i class="bi bi-trash3" aria-hidden="true"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>