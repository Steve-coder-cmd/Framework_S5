<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription</title>

    <!-- Bootstrap CSS (CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Site CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/site.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="card mx-auto mt-5 p-4" style="max-width:540px;">
            <h2 class="h4 mb-3">Inscription</h2>

            <form action="${pageContext.request.contextPath}/api/inscription/traiter" method="post" class="row g-3">
                <div class="col-12">
                    <label for="nom" class="form-label visually-hidden">Nom</label>
                    <input id="nom" name="nom" type="text" class="form-control" placeholder="Ton nom" required />
                </div>

                <div class="col-md-6">
                    <label for="age" class="form-label visually-hidden">Âge</label>
                    <input id="age" name="age" type="number" class="form-control" placeholder="Ton âge" required />
                </div>

                <div class="col-md-6">
                    <label for="email" class="form-label visually-hidden">Email</label>
                    <input id="email" name="email" type="email" class="form-control" placeholder="Ton email" required />
                </div>

                <div class="col-12">
                    <label for="departement" class="form-label">Département</label>
                    <select id="departement" name="departement" class="form-select" required>
                        <option value="INFORMATIQUE">Informatique</option>
                        <option value="MARKETING">Marketing</option>
                        <option value="FINANCES">Finances</option>
                    </select>
                </div>

                <div class="col-12 text-end">
                    <button type="submit" class="btn btn-primary">S'inscrire</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap JS bundle (CDN) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>