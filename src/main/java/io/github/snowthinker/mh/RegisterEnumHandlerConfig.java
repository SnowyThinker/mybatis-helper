package io.github.snowthinker.mh;

import java.util.List;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterEnumHandlerConfig implements ConfigurationCustomizer {

    @Override
    public void customize(Configuration configuration) {
        log.debug("ConfigurationCustomizer init....");
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        try {
            final List<Class<?>> allAssignedClass = ClassUtil.getAllAssignedClass(BaseEnum.class);
            allAssignedClass.forEach((clazz) -> typeHandlerRegistry.register(clazz, GeneralTypeHandler.class));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
