// ...existing code...
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.lang.Integer"%>
<%
    String nom = (String) request.getAttribute("nom");
    Integer age = (Integer) request.getAttribute("age");
    String email = (String) request.getAttribute("email");
    String departement = (String) request.getAttribute("departement");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Confirmation</title>

    <!-- Bootstrap CSS (CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Site CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/site.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="card mx-auto mt-5 p-4" style="max-width:720px;">
            <div class="d-flex align-items-center gap-3 mb-3">
                <div class="avatar"><%= (nom != null && nom.length() > 0) ? nom.substring(0,1).toUpperCase() : "U" %></div>
                <div>
                    <h1 class="h5 mb-0">Félicitations <%= nom %> !</h1>
                    <div class="text-muted">Inscription réussie</div>
                </div>
            </div>

            <hr>

            <p class="mb-1">Tu as <strong><%= age %></strong> ans</p>
            <p class="mb-1">Email : <strong><%= email %></strong></p>
            <p class="mb-0">Département : <strong><%= departement %></strong></p>

            <div class="mt-4 text-end">
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">Accueil</a>
                <a href="${pageContext.request.contextPath}/inscription.jsp" class="btn btn-primary">Modifier</a>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS bundle (CDN) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
