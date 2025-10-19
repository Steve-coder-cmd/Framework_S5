package mg.teste;

import com.framework.annotations.HandleURL;

public class Teste {
    @HandleURL("/hello")
    public void hello() {}

    @HandleURL("/teste")
    public void about() {}
}