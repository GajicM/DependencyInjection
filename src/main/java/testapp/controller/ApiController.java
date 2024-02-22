package testapp.controller;


import framework.annotations.*;
import testapp.services.Servis;

@Controller @Qualifier("NekiIntefejs")
public class ApiController implements NekiIntefejs {
    @Autowired
    Servis servis;
        @Path("/api") @GET
        public String index() {
            return "Hello from ApiController";
        }
        @Path("/") @GET
        public String show() {
            return "Hello from ApiController show";
        }
        @POST @Path("/create")
        public String create() {
            return "Hello from ApiController create";
        }

        public String update() {
            return "Hello from ApiController update";
        }

        public String delete() {
            return "Hello from ApiController delete";
        }
}
