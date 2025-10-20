<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="container-fluid">
    <div class="row min-vh-100">
        <div class="col-md-6 d-none d-md-block p-0"
             style="background: url('${pageContext.request.contextPath}/images/imgRegistro.jpg') center center no-repeat; background-size: cover;"
             aria-hidden="true">
        </div>
        <div class="col-12 col-md-6 d-flex align-items-center justify-content-center">
            <div class="p-4 p-md-5 w-100" style="max-width: 500px;">
                <h2 class="mb-4 text-center text-primary" id="registro-titulo">Crea tu Cuenta</h2>
                <p class="text-center text-muted mb-4">Completa tus datos para comenzar.</p>

                <form action="${pageContext.request.contextPath}/registro" method="POST" role="form" aria-labelledby="registro-titulo">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="dni" class="form-label">DNI</label>
                            <input type="number" class="form-control" id="dni" name="dni" required aria-required="true" autocomplete="username">
                        </div>
                        <div class="col-md-6">
                            <label for="telefono" class="form-label">Teléfono</label>
                            <input type="tel" class="form-control" id="telefono" name="telefono" required aria-required="true" autocomplete="tel">
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" required aria-required="true" autocomplete="given-name">
                        </div>
                        <div class="col-md-6">
                            <label for="apellido" class="form-label">Apellido</label>
                            <input type="text" class="form-control" id="apellido" name="apellido" required aria-required="true" autocomplete="family-name">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required aria-required="true" autocomplete="email">
                    </div>
                    <div class="mb-3">
                        <label for="pass" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="pass" name="pass" required aria-required="true" autocomplete="new-password">
                    </div>
                    <div class="mb-3">
                        <label for="rol" class="form-label">Tipo de Usuario</label>
                        <select class="form-select" id="rol" name="rol" required aria-required="true">
                            <option value="" disabled selected>Selecciona tu rol</option>
                            <option value="MEDICO">Médico</option>
                            <option value="PACIENTE">Paciente</option>
                        </select>
                    </div>

                    <div id="camposMedico" class="d-none" role="region" aria-live="polite">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="matricula" class="form-label">Matrícula</label>
                                <input type="text" class="form-control" id="matricula" name="matricula">
                            </div>
                            <div class="col-md-6">
                                <label for="especialidad" class="form-label">Especialidad</label>
                                <input type="text" class="form-control" id="especialidad" name="especialidad">
                            </div>
                        </div>
                    </div>
                    <div id="campoPaciente" class="d-none" role="region" aria-live="polite">
                        <div class="mb-3">
                            <label for="obraSocial" class="form-label">Obra Social</label>
                            <input type="text" class="form-control" id="obraSocial" name="obraSocial">
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 fw-bold mt-3">REGISTRARSE</button>
                </form>
                <p class="text-center mt-3"><a href="${pageContext.request.contextPath}/login" class="text-decoration-none">¿Ya tienes cuenta? Inicia Sesión</a></p>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const rolSelect = document.getElementById('rol');
        const camposMedico = document.getElementById('camposMedico');
        const campoPaciente = document.getElementById('campoPaciente');
        const matriculaInput = document.getElementById('matricula');
        const especialidadInput = document.getElementById('especialidad');
        const obraSocialInput = document.getElementById('obraSocial');

        rolSelect.addEventListener('change', function () {
            const selectedRol = this.value;

            camposMedico.classList.add('d-none');
            campoPaciente.classList.add('d-none');

            matriculaInput.required = false;
            matriculaInput.setAttribute('aria-required', 'false');
            especialidadInput.required = false;
            especialidadInput.setAttribute('aria-required', 'false');
            obraSocialInput.required = false;
            obraSocialInput.setAttribute('aria-required', 'false');


            if (selectedRol === 'MEDICO') {
                camposMedico.classList.remove('d-none'); // Mostramos la región de médico
                matriculaInput.required = true;
                matriculaInput.setAttribute('aria-required', 'true');
                especialidadInput.required = true;
                especialidadInput.setAttribute('aria-required', 'true');
            } else if (selectedRol === 'PACIENTE') {
                campoPaciente.classList.remove('d-none'); // Mostramos la región de paciente
                obraSocialInput.required = true;
                obraSocialInput.setAttribute('aria-required', 'true');
            }
        });
    });
</script>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>