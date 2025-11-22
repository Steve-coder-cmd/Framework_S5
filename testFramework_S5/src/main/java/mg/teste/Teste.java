package mg.teste;

import com.framework.annotations.HandleURL;

import java.util.ArrayList;

import com.framework.annotations.AnnotationController;
import com.framework.annotations.PathParam;
import com.framework.models.ModelView;

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
}