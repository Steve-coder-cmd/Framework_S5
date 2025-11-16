package mg.teste;

import com.framework.annotations.HandleURL;
import com.framework.annotations.AnnotationController;

@AnnotationController(path="/api")
public class Teste {
    @HandleURL("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @HandleURL("/teste")
    public void about() {}
}