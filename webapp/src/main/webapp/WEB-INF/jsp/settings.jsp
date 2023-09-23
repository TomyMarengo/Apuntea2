<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="settings"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href=<c:url value="/css/sections/navbar.css"/>/>

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet"/>

</head>

<body>

<!-- NAVBAR -->
<fragment:navbar/>

<!-- BOTTOM-NAVBAR -->
<fragment:bottom-navbar title="profile:Perfil" extraLinks="settings:Ajustes">
</fragment:bottom-navbar>


<!-- CONFIGURACION -->
<div class="container">
    <div class="row row-cols-md-2 row-cols-1 gx-md-4 gy-4 gy-md-0">
        <!-- NOTIFICACIONES -->
        <div class="col col-lg-3">
            <div class="card box">
                <div class="card-body d-flex flex-column gap-3">
                    <h5 class="card-title fw-bold">Notificaciones</h5>
                    <div class="form-check form-switch p-0">
                        <label class="form-check-label" for="nuevosApuntes">
                            Nuevos apuntes
                        </label>
                        <input class="form-check-input" type="checkbox" id="nuevosApuntes">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="nuevosComentarios">
                            Nuevos comentarios
                        </label>
                        <input class="form-check-input" type="checkbox" id="nuevosComentarios">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="nuevasCalificaciones">
                            Nuevas calificaciones
                        </label>
                        <input class="form-check-input" type="checkbox" id="nuevasCalificaciones">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="sobreNosotros">
                            Sobre nosotros
                        </label>
                        <input class="form-check-input" type="checkbox" id="sobreNosotros">
                    </div>
                    <h6 class="card-title fw-bold mt-3">Período</h6>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="inmediato">
                            Inmediato
                        </label>
                        <input class="form-check-input" type="checkbox" id="inmediato">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="resumenDiario">
                            Resumen diario
                        </label>
                        <input class="form-check-input" type="checkbox" id="resumenDiario">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="resumenSemanal">
                            Resumen semanal
                        </label>
                        <input class="form-check-input" type="checkbox" id="resumenSemanal">
                    </div>
                </div>
            </div>
        </div>

        <div class="col col-lg-9">
            <div class="row row-cols-lg-3 row-cols-1 gy-4 gx-lg-4">
                <!-- PRIVACIDAD -->
                <div class="col">
                    <div class="card box">
                        <div class="card-body d-flex flex-column gap-3">
                            <h5 class="card-title fw-bold">Privacidad</h5>
                            <div class="form-check form-switch p-0">
                                <label class="form-check-label" for="perfilPrivado">
                                    Perfil privado
                                </label>
                                <input class="form-check-input" type="checkbox" id="perfilPrivado">
                            </div>
                            <div class="form-check form-switch d-flex justify-content-between p-0">
                                <label class="form-check-label" for="apuntesPrivados">
                                    Apuntes privados
                                </label>
                                <input class="form-check-input" type="checkbox" id="apuntesPrivados">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- INTERFAZ Y APARIENCIA -->
                <div class="col">
                    <div class="card box">
                        <div class="card-body d-flex flex-column gap-3">
                            <h5 class="card-title fw-bold">Interfaz y Apariencia</h5>
                            <div class="form-check form-switch p-0">
                                <label class="form-check-label" for="modoOscuro">
                                    Modo oscuro
                                </label>
                                <input class="form-check-input" type="checkbox" id="modoOscuro">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- CUENTA Y PERFIL -->
                <div class="col">
                    <div class="card box">
                        <div class="card-body d-flex flex-column gap-3">
                            <h5 class="card-title fw-bold">Cuenta y Perfil</h5>
                            <a href="#" class="card-link">Cambio de contraseña</a>
                            <a href="#" class="card-link">Eliminar cuenta</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>

</body>

</html>