<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/jsp/common/head.jsp" %>

<div class="container mt-5">
    <h1>Mi Perfil</h1>
    <hr>

    <%-- Leemos el usuario directamente de la sesión --%>
    <c:if test="${not empty sessionScope.usuarioLogueado}">
        <p><strong>Nombre:</strong> ${sessionScope.usuarioLogueado.nombre}</p>
        <p><strong>Apellido:</strong> ${sessionScope.usuarioLogueado.apellido}</p>
        <p><strong>Email:</strong> ${sessionScope.usuarioLogueado.email}</p>
        <p><strong>DNI:</strong> ${sessionScope.usuarioLogueado.dni}</p>
        <p><strong>Rol:</strong> ${sessionScope.usuarioLogueado.rol}</p>
    </c:if>

</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>