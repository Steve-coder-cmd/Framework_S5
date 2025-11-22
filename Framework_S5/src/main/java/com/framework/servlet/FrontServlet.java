package com.framework.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.framework.models.ModelView;
import com.framework.util.ControllerInfo;


@WebServlet(name = "FrontServlet", urlPatterns = {"/"}, loadOnStartup = 1)
public class FrontServlet extends HttpServlet {
    
    RequestDispatcher defaultDispatcher;

    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
    }

    private void defaultServe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        defaultDispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        service(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        service(request, response);
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.isEmpty()) {
            path = "/"; // Gérer la racine
        }

        // Vérifier si c'est une ressource statique
        boolean resourceExists = getServletContext().getResource(path) != null;

        if (resourceExists) {
            defaultServe(req, resp);
        } else {
            // Pas de ressource statique : vérifier les mappings de controllers
            @SuppressWarnings("unchecked")
            Map<String, ControllerInfo> urlMap = (Map<String, ControllerInfo>) getServletContext().getAttribute("urlMap");

            ControllerInfo info = urlMap.get(path);
            if (info != null) {
                // Mapping trouvé : afficher infos controller/méthode
                resp.setContentType("text/plain");
                PrintWriter out = resp.getWriter();
                out.println("Controller: " + info.getControllerClass().getName());
                out.println("Method name: " + info.getMethod().getName());

                // Valeur de retour de la méthode 
                Method methodURL = info.getMethod();
                try {
                    Object controllerNewInstance = info.getControllerClass().getDeclaredConstructor().newInstance();
                    Object returnObject = methodURL.invoke(controllerNewInstance);

                    // Type de retour String 
                    if(returnObject instanceof String) {
                        out.println("Retour de la méthode du controller (String) : " + returnObject);

                    } else if (returnObject instanceof ModelView) {
                        ModelView mv = (ModelView) returnObject;
                        out.println("Retour de la méthode du controller (ModelView) : " + mv.toString());
                    } else {
                        out.println("Le type de retour de la méthode du controller n'est ni de type ModelView ni String!");
                    }
                } catch (InstantiationException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
                    System.out.println("Erreur lors de la création de l'instance du controller : " + e.getMessage());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Erreur lors de l'invocation de la méthode ou du controller : " + e.getMessage());
                }
            } else {
                // Ni statique ni mapping : 404 custom
                showFrameworkPage(req, resp);
            }
        }
    }
    
    private void showFrameworkPage(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try (PrintWriter out = response.getWriter()) {
            String uri = request.getRequestURI();
            String responseBody = """
                <html>
                    <head><title>Resource Not Found</title></head>
                    <body>
                        <h1>Unknown resource</h1>
                        <p>The requested URL was not found: <strong>%s</strong></p>
                    </body>
                </html>
                """.formatted(uri);

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("text/html;charset=UTF-8");
            out.println(responseBody);
        }

        // response.setContentType("text/html;charset=UTF-8");
        // PrintWriter out = response.getWriter();
        
        // out.println("<!DOCTYPE html>");
        // out.println("<html lang='fr'>");
        // out.println("<head>");
        // out.println("    <meta charset='UTF-8'>");
        // out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        // out.println("    <title>Framework Java - Page non trouvée</title>");
        // out.println("    <style>");
        // out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 40px; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); color: #212529; min-height: 100vh; }");
        // out.println("        .container { max-width: 600px; margin: 0 auto; background: white; padding: 40px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); border: 1px solid #e9ecef; }");
        // out.println("        h1 { color: #212529; text-align: center; margin-bottom: 30px; padding-bottom: 15px; border-bottom: 2px solid #333; font-weight: 300; font-size: 2.5em; }");
        // out.println("        .message { background: #f8f9fa; padding: 25px; border-radius: 12px; border-left: 4px solid #333; margin: 25px 0; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }");
        // out.println("        .path { font-family: 'Courier New', monospace; background: #e9ecef; padding: 12px 16px; border-radius: 8px; display: inline-block; margin: 15px 0; font-weight: bold; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }");
        // out.println("        .footer { margin-top: 30px; text-align: center; color: #6c757d; font-size: 14px; padding-top: 20px; border-top: 1px solid #dee2e6; }");
        // out.println("        p { line-height: 1.6; margin-bottom: 15px; color: #495057; }");
        // out.println("        h3 { color: #333; margin-bottom: 15px; font-weight: 500; }");
        // out.println("    </style>");
        // out.println("</head>");
        // out.println("<body>");
        // out.println("    <div class='container'>");
        // out.println("        <h1>FRAMEWORK JAVA</h1>");
        
        // out.println("        <div class='message'>");
        // out.println("            <h3>Ressource non trouvée</h3>");
        // out.println("            <p>Voici l'URL demandée :</p>");
        // out.println("            <div class='path'><strong>" + requestedPath + "</strong></div>");
        // out.println("        </div>");
        // out.println("    </div>");
        // out.println("</body>");
        // out.println("</html>");
    }

    
}