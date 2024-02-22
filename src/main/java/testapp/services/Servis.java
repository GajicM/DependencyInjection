package testapp.services;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;
import framework.annotations.Service;
import testapp.beans.BeanBean;
import testapp.beans.ComponentaNeka;
import testapp.controller.NekiIntefejs;

@Service
public class Servis {
    @Autowired @Qualifier("NekiIntefejs")
    public NekiIntefejs apiController;
    @Autowired @Qualifier("NekiIntefejs")
    public NekiIntefejs apiController2;

    @Autowired
    public BeanBean beanBean;


   // @Autowired @Qualifier("petarpan")
  //  public NekiIntefejs drugiController;


    @Autowired
    public ComponentaNeka componentaNeka;

    @Override
    public String toString() {
        return "Servis{" +
                "apiController=" + apiController +
                ", apiControllerBezQualifer=" + apiController2 +
                ", beanBean=" + beanBean +
                ", componentaNeka=" + componentaNeka +
                '}';
    }
}
