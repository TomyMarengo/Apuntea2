<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Apuntea</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="/css/main.css" />
    <link rel="stylesheet" href="/css/general/elements.css" />
    <link rel="stylesheet" href="/css/general/sizes.css" />
    <link rel="stylesheet" href="/css/general/backgrounds.css" />
    <link rel="stylesheet" href="/css/general/texts.css" />
    <link rel="stylesheet" href="/css/general/buttons.css" />
    <link rel="stylesheet" href="/css/general/icons.css" />
    <link rel="stylesheet" href="/css/general/boxes.css" />
    <link rel="stylesheet" href="/css/sections/navbar.css" />
    <link rel="stylesheet" href="/css/sections/landing/graph.css" />
    <link rel="stylesheet" href="/css/sections/landing/carousel.css" />
    <link rel="stylesheet" href="/css/sections/landing/comments.css" />

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

    <!-- NAVBAR -->
    <fragment:navbar/>

    <!-- "¿SABÍAS QUÉ?" SECTION -->
    <div class="container mt-5 mw-500">
        <div class="card bg-transparent border-0">
            <div class="card-body">
                <h5 class="card-title fw-bold">¿Sabías qué?</h5>
                <div class="d-flex align-items-center mb-3">
                    <img src="image/teacher.png" alt="Teacher" class="me-2" style="width: 40px; height: 40px;">
                    <p class="mb-0">Al compartir apuntes, ganas niveles y te destacas en la comunidad académica. Los usuarios más
                        activos entran en el ranking y obtienen beneficios exclusivos.</p>
                </div>
                <div class="d-flex align-items-center mb-3">
                    <input type="text" class="form-control custom-input me-2 rounded-box bg-bg"
                           placeholder="Busca un apunte, materia, universidad...">
                    <button class="btn rounded-box button-white">
                        <spring:message code="search"/>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- "EXPLORA NUESTRAS OPCIONES" SECTION -->
    <div class="container mw-500">
        <div class="card bg-transparent border-0">
            <div class="card-body d-flex flex-column align-items-center">
                <div class="d-flex align-items-center mb-3">
                    <img src="/svg/rocket.svg" alt="Rocket" class="me-2 icon-s fill-text">
                    <h5 class="fw-bold">Explora nuestras opciones</h5>
                </div>
                <div class="d-flex w-75 justify-content-around">
                    <button class="btn rounded-box button-primary">Registrate</button>
                    <a href="notes/search"><button class="btn rounded-box button-secondary">Descubre</button></a>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-5 m-0 ">
        <!-- CARROUSEL LAST NOTES -->
        <div class="d-flex flex-column col-xl-6">
            <div class="d-flex justify-content-center" style="padding-left: 40px;">
                <h5 class="fw-bold" style="margin-left: 120px; margin-right: 75px">Últimos apuntes</h5>
                <div>
                    <button class="carousel-button" type="button" data-bs-target="#carouselUniversity" data-bs-slide="prev">
                        <img src="/svg/prev.svg" alt="Previous" class="icon-l fill-dark-primary">
                    </button>
                    <button class="carousel-button" type="button" data-bs-target="#carouselUniversity" data-bs-slide="next">
                        <img src="/svg/next.svg" alt="Next" class="icon-l fill-dark-primary">
                    </button>
                </div>

            </div>

            <div id="carouselUniversity" class="carousel slide mt-3">
                <div class="carousel-inner">
                    <!-- First "page" of carrousel -->
                    <div class="carousel-item mb-5 active">
                        <!-- University name and graph -->
                        <div class="container mw-600">
                            <div class="card box h-100 bg-bg">
                                <div class="card-body d-flex align-items-center h-100 justify-content-around flex-wrap">
                                    <!-- University Icon and Name (in the right) -->
                                    <div class="d-flex align-items-center university-title ">
                                        <img src="/svg/graduation-cap.svg" alt="University Icon" class="icon-s fill-text">
                                        <h5 class=" mx-2 w-100"><strong>ITBA</strong></h5>
                                    </div>
                                    <!-- Graph with circles and lines -->
                                    <div class="graph h-100">
                                        <div class="circle circle1"></div>
                                        <div class="line line1"></div>
                                        <div class="circle circle2"></div>
                                        <div class="line line2"></div>
                                        <div class="circle circle3"></div>
                                        <div class="line line3"></div>
                                        <div class="circle circle4"></div>
                                        <div class="line line4"></div>
                                        <div class="circle circle5"></div>
                                        <div class="info1 d-flex flex-column fw-bold text-center w-25">
                                            <p>1000</p>
                                            <p>Alumnos</p>
                                        </div>
                                        <div class="info2 d-flex flex-column fw-bold text-center w-25">
                                            <p>50</p>
                                            <p>Apuntes</p>
                                        </div>
                                        <!-- Add more circles and lines as needed -->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Example notes -->
                        <div class="container mw-600">
                            <div class="card box h-100 py-1 bg-dark-bg">
                                <div class="card-body d-flex flex-column gap-2">
                                    <a href="https://www.google.com" class="bar-link">
                                        <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex align-items-center text-s">
                                            <span class="col-4 text-start">Ingeniería Informática</span>
                                            <span class="col-6">Teoría de Lenguajes y Autómatas</span>
                                            <span class="col-2 text-end">09/12/22</span>
                                        </button>
                                    </a>
                                    <a href="https://www.google.com" class="bar-link">
                                        <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                            <span class="col-4 text-start">Ingeniería Química</span>
                                            <span class="col-6">Química I</span>
                                            <span class="col-2 text-end">06/09/22</span>
                                        </button>
                                    </a>
                                    <a href="https://www.google.com" class="bar-link">
                                        <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                            <span class="col-4 text-start">Ingeniería Informática</span>
                                            <span class="col-6">Matemática Discreta</span>
                                            <span class="col-2 text-end">11/09/02</span>
                                        </button>
                                    </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Second "page" of carrousel -->
                    <div class="carousel-item mb-5">
                        <!-- University name and graph -->
                        <div class="container mw-600">
                            <div class="card box h-100 bg-bg">
                                <div class="card-body d-flex align-items-center h-100 justify-content-around flex-wrap">
                                    <!-- University Icon and Name (in the right) -->
                                    <div class="d-flex align-items-center university-title ">
                                        <img src="/svg/graduation-cap.svg" alt="University Icon" class="icon-s fill-text">
                                        <h5 class=" mx-2 w-100"><strong>UBA</strong></h5>
                                    </div>
                                    <!-- Graph with circles and lines -->
                                    <div class="graph h-100">
                                        <div class="circle circle1"></div>
                                        <div class="line line1"></div>
                                        <div class="circle circle2"></div>
                                        <div class="line line2"></div>
                                        <div class="circle circle3"></div>
                                        <div class="line line3"></div>
                                        <div class="circle circle4"></div>
                                        <div class="line line4"></div>
                                        <div class="circle circle5"></div>
                                        <div class="info1 d-flex flex-column fw-bold text-center w-25">
                                            <p>1000</p>
                                            <p>Alumnos</p>
                                        </div>
                                        <div class="info2 d-flex flex-column fw-bold text-center w-25">
                                            <p>50</p>
                                            <p>Apuntes</p>
                                        </div>
                                        <!-- Add more circles and lines as needed -->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Example notes -->
                        <div class="container mw-600">
                            <div class="card box h-100 py-1 bg-dark-bg">
                                <div class="card-body d-flex flex-column gap-2">
                                    <a href="https://www.google.com" class="bar-link">
                                        <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                            <span class="col-4 text-start">Sociologia</span>
                                            <span class="col-6">Ciencia Política</span>
                                            <span class="col-2 text-end">06/10/20</span>
                                        </button>
                                    </a>
                                    <a href="https://www.google.com" class="bar-link">
                                        <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                            <span class="col-4 text-start">Arquitectura</span>
                                            <span class="col-6">Estructuras I</span>
                                            <span class="col-2 text-end">01/12/02</span>
                                        </button>
                                    </a>
                                    <a href="https://www.google.com" class="bar-link">
                                        <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                            <span class="col-4 text-start">Administración de Empresas</span>
                                            <span class="col-6">Economía II</span>
                                            <span class="col-2 text-end">12/02/18</span>
                                        </button>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- LIST LAST COMMENTS -->
        <div id="last-comments" class="d-flex flex-column col-xl-6">
            <h5 class="text-center fw-bold">Últimos comentarios</h5>
            <div class="container mt-3">
                <div class="row justify-content-center">
                    <!-- A COMMENT -->
                    <div class="col-lg-12 col-xl-6 mb-4 mw-500">
                        <a href="https://www.google.com" class="card box h-100 text-decoration-none button-shadow">
                            <div class="card-body">
                                <h5 class="card-title fb-">
                                    <img src="/svg/arrow-trend-up.svg" alt="University Icon" class="icon-s fill-dark-primary mx-2">
                                    <strong>Matemática Discreta</strong>
                                </h5>
                                <p class="card-text"><strong>Jonathan ha comentado</strong></p>
                                <p class="card-text mt-2">Buen apunte, pero el de Apuntes Britu es mejor.</p>
                            </div>
                        </a>
                    </div>
                    <!-- A COMMENT -->
                    <div class="col-lg-12 col-xl-6 mb-4 mw-500">
                        <a href="https://www.google.com" class="card box h-100 text-decoration-none button-shadow">
                            <div class="card-body">
                                <h5 class="card-title fb-">
                                    <img src="/svg/arrow-trend-up.svg" alt="University Icon" class="icon-s fill-dark-primary mx-2">
                                    <strong>Teoría de Lenguajes y Autómatas</strong>
                                </h5>
                                <p class="card-text"><strong>David ha comentado</strong></p>
                                <p class="card-text mt-2">El de Apuntes Abru le pasa el trapo, aunque pesa 100MB más...</p>
                            </div>
                        </a>
                    </div>
                    <!-- A COMMENT -->
                    <div class="col-lg-12 col-xl-6 mb-4 mw-500">
                        <a href="https://www.google.com" class="card box h-100 text-decoration-none button-shadow">
                            <div class="card-body">
                                <h5 class="card-title fb-">
                                    <img src="/svg/arrow-trend-up.svg" alt="University Icon" class="icon-s fill-dark-primary mx-2">
                                    <strong>Matemática III</strong>
                                </h5>
                                <p class="card-text"><strong>Tomás ha comentado</strong></p>
                                <p class="card-text mt-2">Muy bueno!</p>
                            </div>
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <script src="/js/scripts.js"></script>
</body>

</html>