// ...existing code...
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.lang.Integer"%>
<%
    Integer employeId = (Integer) request.getAttribute("employeId");
    Integer salaire = (Integer) request.getAttribute("salaire");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Salaire de l'employé</title>

    <!-- Bootstrap CSS (CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Site CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/site.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <% if (employeId != null) { %>
            <div class="card salary-card mx-auto p-4 mt-5" style="max-width:720px;">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h2 class="h5 mb-1">Salaire de l'employé</h2>
                        <div class="text-muted">ID employé : <strong><%= employeId %></strong></div>
                    </div>
                    <div class="salary-amount fs-4 fw-bold text-primary">
                        <%= salaire %> € 
                    </div>
                </div>

                <hr>

                <p class="mb-0">Calculé dynamiquement par ton framework avec @PathParam !</p>
            </div>
        <% } else { %>
            <div class="text-center mt-5">
                <div class="alert alert-warning d-inline-block">Aucun salarié trouvé</div>
            </div>
        <% } %>
    </div>

    <!-- Bootstrap JS bundle (CDN) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
