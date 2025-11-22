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
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Map.Entry;

//import com.framework.annotations.PathParam;
import com.framework.models.ModelView;
import com.framework.util.ControllerInfo;
import com.framework.util.PathPattern;


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
            Map<PathPattern, ControllerInfo> urlMap = 
                (Map<PathPattern, ControllerInfo>) getServletContext().getAttribute("urlMap");

            ControllerInfo info = null;
            Map<String, String> pathParams = null;

            for (Entry<PathPattern, ControllerInfo> entry : urlMap.entrySet()) {
                PathPattern pattern = entry.getKey();
                if (pattern.matches(path)) {
                    info = entry.getValue();
                    pathParams = pattern.extractParameters(path);
                    break;
                }
            }
            
            if (info != null) {
                // Mapping trouvé : afficher infos controller/méthode                

                // Valeur de retour de la méthode 
                Method methodURL = info.getMethod();
                try {
                    Object controllerInstance = info.getControllerClass().getDeclaredConstructor().newInstance();

                    // Préparer les arguments pour la méthode
                    Parameter[] methodParams = methodURL.getParameters();
                    Object[] args = new Object[methodParams.length];

                    for (int i = 0; i < methodParams.length; i++) {
                        var param = methodParams[i];
                        Object argValue = null;

                        if (param.isAnnotationPresent(com.framework.annotations.PathParam.class)) {
                            // @PathParam  
                            String name = param.getAnnotation(com.framework.annotations.PathParam.class).value();
                            String value = pathParams.get(name);

                            if (value != null) {
                                argValue = convert(value, param.getType());
                            }

                        } else if (param.isAnnotationPresent(com.framework.annotations.RequestParam.class)) {
                            // @RequestParam
                            String paramName = param.getAnnotation(com.framework.annotations.RequestParam.class).value();
                            String value = req.getParameter(paramName);  // <-- vient du formulaire ou query string

                            if (value != null && !value.isEmpty()) {
                                argValue = convert(value, param.getType());
                            } else {
                                // Gérer required + defaultValue plus tard
                                if (param.getType() == String.class) {
                                    argValue = "";
                                }
                            }
                        }

                        args[i] = argValue;
                    }

                    Object returnObject = methodURL.invoke(controllerInstance, args);
                    
                    if(returnObject instanceof String) {    // Type de retour String 
                        resp.setContentType("text/plain;charset=UTF-8");
                        PrintWriter out = resp.getWriter();
                        out.println("Controller: " + info.getControllerClass().getName());
                        out.println("Method name: " + info.getMethod().getName());
                        out.println("Retour de la méthode du controller (String) : " + returnObject);
                    } else if (returnObject instanceof ModelView) { // Type de retour ModelView
                        ModelView mv = (ModelView) returnObject;
                        processModelView(req, resp, mv);
                    } else {
                        PrintWriter out = resp.getWriter();
                        resp.setContentType("text/plain;charset=UTF-8");
                        out.println("Le type de retour de la méthode du controller n'est ni de type ModelView ni String!");
                    }
                } catch (InstantiationException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
                    System.out.println("Erreur lors de la création de l'instance du controller : " + e.getMessage());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Erreur lors de l'invocation de la méthode : " + e.getMessage());
                }
            } else {
                // Ni statique ni mapping : 404 custom
                showFrameworkPage(req, resp);
            }
        }
    }
    
    private Object convert(String value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value) || "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
        }

        return value; // fallback
    }

    private void processModelView(HttpServletRequest req, HttpServletResponse resp, ModelView mv) throws ServletException, IOException {
        if(!mv.getData().isEmpty()){
            for (String key : mv.getData().keySet()) {
                req.setAttribute(key, mv.getData().get(key));
            }
        }
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/" + mv.getView());
        dispatcher.forward(req, resp);
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
            response.setCharacterEncoding("UTF-8");
            out.println(responseBody);
        }

    }

    
}