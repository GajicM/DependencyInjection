package testapp.beans;

import framework.annotations.Bean;

@Bean(value = "Prototype")
public class PrototypeBean {
    public String crazyMethodName() {
        return "ProtoybeBean";
    }
}
