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
                    <form action="${pageContext.request.contextPath}/turno-form" method="POST" role="form" aria-labelledby="form-titulo">

                        <c:if test="${not empty turno}">
                            <input type="hidden" name="idTurno" value="${turno.idTurno}">
                        </c:if>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="medicoId" class="form-label">Médico</label>
                                <select class="form-select" id="medicoId" name="medicoId" required aria-required="true">
                                    <option value="">Seleccione un médico</option>
                                    <c:forEach var="med" items="${requestScope.listaMedicos}">
                                        <option value="${med.idUsuario}" ${turno.medico.idUsuario == med.idUsuario ? 'selected' : ''}>
                                            ${med.apellido}, ${med.nombre} (${med.especialidad})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="pacienteId" class="form-label">Paciente</label>
                                <select class="form-select" id="pacienteId" name="pacienteId" required aria-required="true">
                                    <option value="">Seleccione un paciente</option>
                                    <c:forEach var="pac" items="${requestScope.listaPacientes}">
                                        <option value="${pac.idUsuario}" ${turno.paciente.idUsuario == pac.idUsuario ? 'selected' : ''}>
                                            ${pac.apellido}, ${pac.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="fecha" class="form-label">Fecha</label>
                                <input type="date" class="form-control" id="fecha" name="fecha" value="${turno.fecha}" required aria-required="true">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="hora" class="form-label">Hora</label>
                                <input type="time" class="form-control" id="hora" name="hora" value="${turno.hora}" required aria-required="true">
                            </div>
                        </div>
                         <div class="mb-3">
                            <label for="estado" class="form-label">Estado</label>
                            <select class="form-select" id="estado" name="estado" required aria-required="true">
                                <c:forEach var="est" items="${requestScope.estadosPosibles}">
                                    <option value="${est}" ${turno.estado == est ? 'selected' : ''}>${est}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="motivo" class="form-label">Motivo de la Consulta</label>
                            <textarea class="form-control" id="motivo" name="motivo" rows="3" required aria-required="true">${turno.motivo}</textarea>
                        </div>

                        <div class="d-flex justify-content-end">
                            <a href="${pageContext.request.contextPath}/gestion-turnos" class="btn btn-secondary me-2">Cancelar</a>
                            <button type="submit" class="btn btn-success">Guardar Turno</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>