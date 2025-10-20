<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<c:choose>
    <%-- ======================================================== --%>
    <%--  VISTA LOGUEADA --%>
    <%-- ======================================================== --%>
    <c:when test="${not empty sessionScope.usuarioLogueado}">
        <div class="container text-center py-5">
            <h1 class="display-3 fw-bold mb-4">¡Hola de nuevo, ${sessionScope.usuarioLogueado.nombre}!</h1>
            <p class="display-6">Gestioná tus turnos desde aquí.</p>
            <div class="d-grid gap-3 d-sm-flex justify-content-sm-center mb-5 mt-4">
                <c:if test="${sessionScope.usuarioLogueado.rol == 'PACIENTE'}">
                    <a href="${pageContext.request.contextPath}/misturnos" class="btn btn-primary btn-lg px-4 py-3 fw-bold">Mis Turnos</a>
                    <a href="${pageContext.request.contextPath}/solicitar-turno" class="btn btn-outline-success btn-lg px-4 py-3">Solicitar Turno</a>
                </c:if>
                <c:if test="${sessionScope.usuarioLogueado.rol == 'MEDICO'}">
                    <a href="${pageContext.request.contextPath}/agenda" class="btn btn-primary btn-lg px-4 py-3 fw-bold">Ver mi Agenda</a>
                </c:if>
                <%-- <a class="nav-link" href="${pageContext.request.contextPath}/gestion-turnos">Gestionar Turnos</a> --%>
            </div>
        </div>
    </c:when>

    <%-- ======================================================== --%>
    <%--  VISTA INVITADO --%>
    <%-- ======================================================== --%>
    <c:otherwise>
        <div class="container text-center py-5">
            <h1 class="display-3 fw-bold mb-4">Gestioná tus turnos médicos de forma rápida y sencilla</h1>
            <p class="display-6">La forma más fácil de administrar tus citas médicas.</p>
            <div class="d-grid gap-3 d-sm-flex justify-content-sm-center mb-5 mt-4">
                 <a href="${pageContext.request.contextPath}/registro" class="btn btn-primary btn-lg px-4 py-3 fw-bold">
                    <i class="bi bi-person-plus-fill me-2" aria-hidden="true"></i>Crear mi Cuenta Gratis
                </a>
            </div>
        </div>

        <div class="container px-4 py-5" id="featured-3" role="region" aria-labelledby="features-title">
            <h2 class="pb-2 border-bottom text-center" id="features-title">¿Cómo Funciona?</h2>
            <div class="row g-4 py-5 row-cols-1 row-cols-lg-3">
                <div class="feature col text-center">
                    <div class="feature-icon-small d-inline-flex align-items-center justify-content-center text-bg-primary bg-gradient fs-4 rounded-3 p-3 mb-3">
                        <i class="bi bi-person-plus-fill text-white" aria-hidden="true"></i>
                    </div>
                    <h3 class="fs-2">1. Registrate</h3>
                    <p>Creá tu cuenta como médico o paciente en menos de un minuto. Es rápido, fácil y seguro.</p>
                </div>
                <div class="feature col text-center">
                    <div class="feature-icon-small d-inline-flex align-items-center justify-content-center text-bg-primary bg-gradient fs-4 rounded-3 p-3 mb-3">
                        <i class="bi bi-calendar-check text-white" aria-hidden="true"></i>
                    </div>
                    <h3 class="fs-2">2. Gestioná</h3>
                    <p>Solicitá turnos con tus médicos de confianza o administrá tu agenda de pacientes desde un solo lugar.</p>
                </div>
                <div class="feature col text-center">
                    <div class="feature-icon-small d-inline-flex align-items-center justify-content-center text-bg-primary bg-gradient fs-4 rounded-3 p-3 mb-3">
                        <i class="bi bi-bell-fill text-white" aria-hidden="true"></i>
                    </div>
                    <h3 class="fs-2">3. Organizate</h3>
                    <p>Recibí recordatorios y mantené un control total de tu historial de citas. No más olvidos ni papeles.</p>
                </div>
            </div>
        </div>

        <div class="container-fluid bg-light py-5">
            <div class="container">
                <h2 class="pb-2 border-bottom text-center mb-4" id="carousel-title">Nuestros Profesionales e Instalaciones</h2>

                <div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel" role="region" aria-labelledby="carousel-title">
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1" aria-label="Slide 2"></button>
                        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2" aria-label="Slide 3"></button>
                    </div>
                    <div class="carousel-inner mx-auto" style="max-height: 500px; background-color: #333;">
                        <div class="carousel-item active">
                            <img src="${pageContext.request.contextPath}/images/carrusel-1.jpg" class="d-block w-100"
                            style="height: 500px; object-fit: cover; opacity: 0.7;" alt="Médicos profesionales sonriendo">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Médicos de Confianza</h5>
                                <p>Accedé a una red de profesionales de primer nivel.</p>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="${pageContext.request.contextPath}/images/carrusel-2.jpg" class="d-block w-100"
                            style="height: 500px; object-fit: cover; opacity: 0.7;" alt="Sala de espera de una clínica moderna y luminosa">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Instalaciones Modernas</h5>
                                <p>Atendete en clínicas equipadas con la última tecnología.</p>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="${pageContext.request.contextPath}/images/carrusel-3.jpg" class="d-block w-100"
                            style="height: 500px; object-fit: cover; opacity: 0.7;" alt="Fachada de un hospital moderno de varios pisos">
                            <div class="carousel-caption d-none d-md-block">
                                <h5>Atención Integral</h5>
                                <p>Cobertura completa para todas tus necesidades médicas.</p>
                            </div>
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>