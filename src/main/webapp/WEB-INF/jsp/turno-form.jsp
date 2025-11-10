<%@ page contentType="text-html;charset=UTF-8" language="java" %>
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
                                <label for="idPacienteUsuarioExistente" class="form-label">Paciente Existente</label>
                                <select class="form-select" id="idPacienteUsuarioExistente" name="idPacienteUsuarioExistente">
                                    <option value="0" selected>-- Seleccione (si ya existe) --</option>

                                    <c:forEach var="pac" items="${requestScope.listaPacientes}">
                                        <option value="${pac.idUsuario}"
                                                ${turno.paciente.idUsuario == pac.idUsuario ? 'selected' : ''}
                                                data-dni="${pac.dni}"
                                                data-nombre="${pac.nombre}"
                                                data-apellido="${pac.apellido}"
                                                data-telefono="${pac.telefono}"
                                                data-obra-social="${pac.obraSocial}">
                                            ${pac.apellido}, ${pac.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <hr>
                        <h5 class="mb-3 text-secondary" id="nuevo-paciente-titulo">O, Registrar Nuevo Paciente:</h5>

                        <div class="row" role="group" aria-labelledby="nuevo-paciente-titulo">
                            <div class="col-md-4 mb-3">
                                <label for="pacienteDNI" class="form-label">DNI (solo números)</label>
                                <input type="number" class="form-control" id="pacienteDNI" name="pacienteDNI">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="pacienteNombre" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="pacienteNombre" name="pacienteNombre">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="pacienteApellido" class="form-label">Apellido</label>
                                <input type="text" class="form-control" id="pacienteApellido" name="pacienteApellido">
                            </div>
                        </div>

                        <div class="row" role="group" aria-labelledby="nuevo-paciente-titulo">
                            <div class="col-md-6 mb-3">
                                <label for="pacienteTelefono" class="form-label">Teléfono</label>
                                <input type="tel" class="form-control" id="pacienteTelefono" name="pacienteTelefono">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="pacienteObraSocial" class="form-label">Obra Social</label>
                                <input type="text" class="form-control" id="pacienteObraSocial" name="pacienteObraSocial">
                            </div>
                        </div>
                        <hr>
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
                                    <option value="${est.toString()}" ${turno.estado == est ? 'selected' : ''}>${est}</option>
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

<script>
    document.addEventListener("DOMContentLoaded", function() {

        // 1. Obtenemos los elementos del formulario que nos interesan
        const pacienteSelect = document.getElementById("idPacienteUsuarioExistente");

        const dniInput = document.getElementById("pacienteDNI");
        const nombreInput = document.getElementById("pacienteNombre");
        const apellidoInput = document.getElementById("pacienteApellido");
        const telefonoInput = document.getElementById("pacienteTelefono");
        const obraSocialInput = document.getElementById("pacienteObraSocial");

        // Un array con todos los campos para manejarlos fácil
        const camposPaciente = [dniInput, nombreInput, apellidoInput, telefonoInput, obraSocialInput];

        // 2. Creamos la función que se ejecutará cada vez que cambie el dropdown
        function actualizarCampos() {
            const selectedId = pacienteSelect.value;

            if (selectedId !== "0") {
                const selectedOption = pacienteSelect.querySelector('option[value="' + selectedId + '"]');

                const data = selectedOption.dataset;

                dniInput.value = data.dni;
                nombreInput.value = data.nombre;
                apellidoInput.value = data.apellido;
                telefonoInput.value = data.telefono;
                obraSocialInput.value = data.obraSocial;

                camposPaciente.forEach(campo => {
                    campo.disabled = true;
                    campo.classList.add('bg-light');
                });

            } else {
                camposPaciente.forEach(campo => {
                    campo.value = "";
                    campo.disabled = false;
                    campo.classList.remove('bg-light');
                });
            }
        }

        // 3. Le decimos al dropdown que ejecute nuestra función cada vez que "cambie"
        pacienteSelect.addEventListener("change", actualizarCampos);

        // 4. Ejecutamos la función una vez apenas carga la página
        // Esto sirve para el caso "Editar Turno", por si ya venía un paciente seleccionado.
        actualizarCampos();
    });
</script>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>