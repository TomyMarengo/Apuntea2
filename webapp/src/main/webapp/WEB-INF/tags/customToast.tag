<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="message" required="true"%>
<%@ attribute name="id" required="true"%>

<div class="customToast">
    <div class="toast-container position-fixed bottom-0 end-0 p-3">
        <div id="${id}" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-body justify-content-between d-flex">
                <spring:message code="${message}"/>
                <button type="button" class="btn-close align-content-center" data-bs-dismiss="toast"
                        aria-label="Close"></button>
            </div>
        </div>
    </div>
</div>



