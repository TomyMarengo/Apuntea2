<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ tag pageEncoding="UTF-8" %>

<%@ attribute name="pageNumber" required="true"%>
<%@ attribute name="maxPage" required="true"%>

<section class="container d-flex justify-content-center mt-3">
    <nav aria-label="...">
        <ul class="pagination">
            <c:if test="${pageNumber gt 1}">
                <li class="page-item">
                        <%--suppress XmlDuplicatedId --%>
                    <a class="page-link" id="previousPage"><spring:message code="search.pagination.previous"/></a>
                </li>
            </c:if>
            <c:if test="${pageNumber le 1}">
                <li class="page-item disabled">
                        <%--suppress XmlDuplicatedId --%>
                    <a class="page-link" id="previousPage"><spring:message code="search.pagination.previous"/></a>
                </li>
            </c:if>

            <c:if test="${pageNumber gt 2}">
                <li class="page-item">
                    <a class="page-link" data-page="1"><c:out value="1"/></a>
                </li>
            </c:if>

            <c:if test="${pageNumber gt 3}">
                <li class="page-item disabled">
                    <a class="page-link">...</a>
                </li>
            </c:if>

            <c:if test="${pageNumber gt 1}">
                <li class="page-item">
                    <a class="page-link" data-page="${pageNumber - 1}"><c:out
                            value="${pageNumber - 1}"/></a>
                </li>
            </c:if>

            <li class="page-item active" aria-current="page">
                <a class="page-link" data-page="${pageNumber}"><c:out
                        value="${pageNumber}"/></a>
            </li>

            <c:if test="${pageNumber lt maxPage}">
                <li class="page-item">
                    <a class="page-link" data-page="${pageNumber + 1}"><c:out
                            value="${pageNumber + 1}"/></a>
                </li>
            </c:if>

            <c:if test="${pageNumber lt maxPage - 2}">
                <li class="page-item disabled">
                    <a class="page-link">...</a>
                </li>
            </c:if>

            <c:if test="${pageNumber lt maxPage - 1}">
                <li class="page-item">
                    <a class="page-link" data-page="${maxPage}"><c:out value="${maxPage}"/></a>
                </li>
            </c:if>

            <c:if test="${pageNumber lt maxPage}">
                <li class="page-item">
                        <%--suppress XmlDuplicatedId --%>
                    <a class="page-link" id="nextPage"><spring:message code="search.pagination.next"/></a>
                </li>
            </c:if>
            <c:if test="${pageNumber ge maxPage}">
                <li class="page-item disabled">
                        <%--suppress XmlDuplicatedId --%>
                    <a class="page-link" id="nextPage"><spring:message code="search.pagination.next"/></a>
                </li>
            </c:if>
        </ul>
    </nav>
</section>

<script src="<c:url value="/js/pagination.js"/>"></script>


