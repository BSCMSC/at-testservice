package apitests.config;


import org.aeonbits.owner.Config;

@Config.Sources({"classpath:application.properties"})
public interface ConfigVars extends Config {

    @Key("host")
    String host();

}