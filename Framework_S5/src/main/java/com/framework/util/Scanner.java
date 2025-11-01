package com.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.framework.annotations.AnnotationController;
import com.framework.annotations.HandleURL;

public class Scanner {
    public static void scanControllers() {
        System.out.println("🚀 Scanner robuste démarré...");
        
        try {
            // Obtenir tous les packages du classpath
            List<Class<?>> allClasses = findAllClasses();
            
            System.out.println("📊 " + allClasses.size() + " classes trouvées dans le classpath");
            
            // Filtrer les contrôleurs
            // Filtrer les contrôleurs
            List<Class<?>> controllers = new ArrayList<>();
            for (Class<?> clazz : allClasses) {
                if (clazz.isAnnotationPresent(AnnotationController.class)) {
                    controllers.add(clazz);
                }
            }
            System.out.println("🎯 " + controllers.size() + " contrôleurs trouvés");
            
            // Afficher les détails des contrôleurs
            for (Class<?> controller : controllers) {
                printControllerInfo(controller);
            }
            
        } catch (Exception e) {
            System.err.println("💥 Erreur critique: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<Class<?>> findAllClasses() {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        try {
            // Scanner via le classpath système
            String classpath = System.getProperty("java.class.path");
            String[] entries = classpath.split(File.pathSeparator);
            
            for (String entry : entries) {
                File file = new File(entry);
                if (file.exists()) {
                    if (file.isDirectory()) {
                        // C'est un répertoire de classes
                        findClassesInDirectory(file, file, classes, classLoader);
                    }
                    // On ignore les JARs pour l'instant
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du scanning: " + e.getMessage());
        }
        
        return classes;
    }
    
    private static void findClassesInDirectory(File root, File directory, 
                                             List<Class<?>> classes, 
                                             ClassLoader classLoader) {
        File[] files = directory.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                findClassesInDirectory(root, file, classes, classLoader);
            } else if (file.getName().endsWith(".class")) {
                String className = getFullyQualifiedName(root, file);
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    System.out.println("⚠️ Classe non trouvée: " + className);
                } catch (NoClassDefFoundError e) {
                    System.out.println("⚠️ Dépendance manquante pour: " + className);
                } catch (Throwable t) {
                    // Ignorer les autres erreurs
                }
            }
        }
    }
    
    private static String getFullyQualifiedName(File root, File classFile) {
        // Obtenir le chemin relatif
        String relativePath = root.toURI().relativize(classFile.toURI()).getPath();
        
        // Nettoyer le chemin
        String className = relativePath
            .replace('/', '.')
            .replace('\\', '.')
            .replace(".class", "");
        
        return className;
    }
    
    private static void printControllerInfo(Class<?> controllerClass) {
        AnnotationController controller = controllerClass.getAnnotation(AnnotationController.class);
        System.out.println("\n✅ CONTROLLER: " + controllerClass.getName());
        System.out.println("   📍 Chemin: " + controller.path());
        // Méthodes avec HandleURL
        boolean foundMethods = false;
        for (var method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(HandleURL.class)) {
                HandleURL mapping = method.getAnnotation(HandleURL.class);
                System.out.println("   🔗 " + method.getName() + "() -> " + mapping.value());
                foundMethods = true;
            }
        }
        
        if (!foundMethods) {
            System.out.println("   ℹ️  Aucune méthode avec @HandleURL trouvée");
        }
    }
}
