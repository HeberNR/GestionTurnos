<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestor de Turnos Médicos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg bg-primary navbar-dark shadow-sm" role="navigation" aria-label="Menú principal">
  <div class="container-fluid">
    <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/" aria-label="Gestor de Turnos - Ir a la página de inicio">
      <i class="bi bi-calendar2-heart-fill me-2 fs-4" aria-hidden="true"></i>
      <span class="fw-bold">Gestor de Turnos</span>
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
        aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link active" href="${pageContext.request.contextPath}/" aria-current="page">Inicio</a>
        </li>

        <c:if test="${not empty sessionScope.usuarioLogueado}">
            <c:if test="${sessionScope.usuarioLogueado.rol == 'PACIENTE'}">
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/misturnos">Mis Turnos</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/solicitar-turno">Solicitar Turno</a>
              </li>
            </c:if>
            <c:if test="${sessionScope.usuarioLogueado.rol == 'MEDICO'}">
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/gestion-turnos">Gestionar Turnos</a>
              </li>
            </c:if>
        </c:if>
      </ul>

      <ul class="navbar-nav ms-auto d-flex align-items-center">
          <c:choose>
            <c:when test="${not empty sessionScope.usuarioLogueado}">
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle text-white" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false" id="userMenuButton">
                  <i class="bi bi-person-circle me-1" aria-hidden="true"></i>
                  Hola, <span class="fw-bold">${sessionScope.usuarioLogueado.nombre}</span>
                </a>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenuButton">
                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil"><i class="bi bi-person-vcard me-2" aria-hidden="true"></i>Mi Perfil</a></li>
                  <li><hr class="dropdown-divider"></li>
                  <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"
                    aria-hidden="true"></i>Cerrar Sesión</a></li>
                </ul>
              </li>
            </c:when>
            <c:otherwise>
              <li class="nav-item me-2">
                <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/registro">Registrarse</a>
              </li>
              <li class="nav-item">
                <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/login">Iniciar Sesión</a>
              </li>
            </c:otherwise>
          </c:choose>
      </ul>
    </div>
  </div>
</nav>
<main role="main" class="flex-grow-1">