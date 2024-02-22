package testapp.controller;

import framework.annotations.*;
import testapp.services.PraviServis;
import testapp.services.Servis;

@Controller @Qualifier("petarpan")
public class DrugiController implements NekiIntefejs{
@Autowired
public Servis servis;
    @Autowired(verbose = true)
public PraviServis praviServis;
@Autowired
public String index;

@Path("/nesto") @GET
public String writeSomething(){
  return praviServis.doSomething();
}

    @Override
    public String toString() {
        return "DrugiController{" +
                "servis=" + servis +
                '}';
    }
}
