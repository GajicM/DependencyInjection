package testapp.beans;


import framework.annotations.Autowired;
import framework.annotations.Component;
import testapp.controller.ApiController;

@Component
public class ComponentaNeka {
    @Autowired
    ApiController apiController;
}
