<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String username = (String) request.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>User profil</title>

    <!-- Bootstrap CSS (CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        /* Styles personnalisés */
        body {
            background: #f5f7fb;
            font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        }
        .profile-card {
            margin-top: 4rem;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(29, 41, 70, 0.08);
        }
        .avatar {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            background: linear-gradient(135deg,#667eea,#764ba2);
            color: #fff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 1.25rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <% if (username != null) { %>
            <div class="card profile-card mx-auto p-4" style="max-width:720px;">
                <div class="d-flex align-items-center gap-3">
                    <div class="avatar"><%= username.substring(0,1).toUpperCase() %></div>
                    <div>
                        <h1 class="h4 mb-0">Bienvenue !</h1>
                        <div class="text-muted">Utilisateur connecté</div>
                    </div>
                </div>

                <hr>

                <div class="mt-3">
                    <h5 class="mb-1">Nom d'utilisateur</h5>
                    <p class="mb-0"><strong><%= username %></strong></p>
                </div>
            </div>
        <% } else { %>
            <div class="text-center mt-5">
                <div class="alert alert-warning d-inline-block">Aucun utilisateur connecté</div>
            </div>
        <% } %>
    </div>

    <!-- Bootstrap JS bundle (CDN) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
// ...existing code...