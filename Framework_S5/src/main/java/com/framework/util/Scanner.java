package com.framework.util;

//import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.List;
import java.util.Map;
import java.util.Set;

import com.framework.annotations.AnnotationController;
import com.framework.annotations.HandleURL;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class Scanner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Set<Class<?>> allClasses = scanClasses(context);
        Map<String, ControllerInfo> urlMap = new HashMap<>();

        for (Class<?> clazz : allClasses) {
            if (clazz.isAnnotationPresent(AnnotationController.class)) {
                AnnotationController ctrlAnno = clazz.getAnnotation(AnnotationController.class);
                String basePath = normalizePath(ctrlAnno.path());

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(HandleURL.class)) {
                        HandleURL mappingAnno = method.getAnnotation(HandleURL.class);
                        String url = normalizePath(mappingAnno.value());
                        String fullUrl = basePath + url;
                        urlMap.put(fullUrl, new ControllerInfo(clazz, method));
                    }
                }
            }
        }

        // Stocker la map dans le contexte pour l'utiliser plus tard
        context.setAttribute("urlMap", urlMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Rien à faire ici
    }

    private Set<Class<?>> scanClasses(ServletContext context) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            scanDirectory("/WEB-INF/classes/", "", classes, context);
        } catch (Exception e) {
            e.printStackTrace(); // Loggez en production
        }
        return classes;
    }

    private void scanDirectory(String path, String packageName, Set<Class<?>> classes, ServletContext context) throws IOException, ClassNotFoundException {
        Set<String> resourcePaths = context.getResourcePaths(path);
        if (resourcePaths == null) return;

        for (String resourcePath : resourcePaths) {
            if (resourcePath.endsWith("/")) {
                // Répertoire : récursion
                String subPackage = resourcePath.substring(path.length()).replace("/", ".");
                scanDirectory(resourcePath, packageName + subPackage, classes, context);
            } else if (resourcePath.endsWith(".class")) {
                // Fichier classe
                String className = (packageName + resourcePath.substring(path.length(), resourcePath.length() - 6))
                        .replace("/", ".")
                        .replaceAll("^\\.", ""); // Nettoyer les points initiaux
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    // Ignorer les classes non chargeables (ex: anonymes ou dépendances manquantes)
                }
            }
        }
    }

    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) return "/";
        if (!path.startsWith("/")) path = "/" + path;
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
        return path;
    }
}
