package mg.teste;

import com.framework.annotations.HandleURL;

import java.util.ArrayList;

import com.framework.annotations.AnnotationController;
import com.framework.annotations.PathParam;
import com.framework.models.ModelView;
import com.framework.annotations.RequestParam;

@AnnotationController(path="/api")
public class Teste {
    @HandleURL("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @HandleURL("/teste")
    public void about() {}

    @HandleURL("/test")
    public ModelView testPage() {
        ArrayList<String> depts = new ArrayList<>();
        depts.add("INFORMATIQUE");
        depts.add("MARKETING");
        depts.add("FINANCES");
        
        ModelView model = new ModelView("index.jsp");  // sans le slash devant !
        model.addData("name", "Koto");
        model.addData("age", 20);
        model.addData("departements", depts);

        return model;
    }

    @HandleURL(value = "/employe/{id}/salary")
    public ModelView getSalaire(@PathParam("id") int employeId, String nom, int bibi) {
        ModelView mv = new ModelView("salary.jsp");
        mv.addData("employeId", employeId);
        mv.addData("salaire", 5000 + employeId * 10);
        return mv;
    }

    @HandleURL(value = "/user/{username}")
    public ModelView getUser(@PathParam("username") String username, String nom, int bibi) {
        ModelView mv = new ModelView("user.jsp");
        mv.addData("username", username);
        return mv;
    }

    // Page avec formulaire
    @HandleURL(value = "/inscription")
    public ModelView showForm() {
        return new ModelView("inscription.jsp");
    }

    // Traitement du formulaire 
    @HandleURL(value = "/inscription/traiter")
    public ModelView traiterForm(
            @RequestParam("nom") String nom,
            @RequestParam("age") int age,
            @RequestParam("email") String email,
            @RequestParam("departement") String dept) {

        ModelView mv = new ModelView("confirm.jsp");
        mv.addData("nom", nom);
        mv.addData("age", age);
        mv.addData("email", email);
        mv.addData("departement", dept);
        mv.addData("message", "Inscription reussie !");

        return mv;
    }
}