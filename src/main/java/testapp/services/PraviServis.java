package testapp.services;

import framework.annotations.Autowired;
import framework.annotations.Service;
import testapp.beans.PrototypeBean;

@Service
public class PraviServis {
    @Autowired(verbose = true)
    PrototypeBean prviBean;
 @Autowired
    PrototypeBean drugiBean;

    public String doSomething() {
        return "Hello from PraviServis";
    }

    @Override
    public String toString() {
        return "PraviServis{" +
                "prviBean=" + prviBean +
                ", drugiBean=" + drugiBean +
                '}';
    }
}
