package cn.wunhwan.security.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson Json 对象工厂
 *
 * @author WunHwan
 * @since 2021.06.6
 **/
public final class JacksonJsonObjectFactory implements ObjectFactory<ObjectMapper> {

  private final ObjectMapper mapper;

  public JacksonJsonObjectFactory() {
    this.mapper = new ObjectMapper();
  }

  @Override
  public ObjectMapper get() {
    return mapper;
  }

  public String serializableToJson(Object target) {
    try {
      return mapper.writeValueAsString(target);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException(exception);
    }
  }

  public <T> T parseToBean(String value, Class<T> parseType) {
    try {
      return mapper.readValue(value, parseType);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException(exception);
    }
  }

  public enum Singleton {
    /**
     * 单例对象
     */
    SINGLETON(new JacksonJsonObjectFactory());

    private final JacksonJsonObjectFactory objectFactory;

    Singleton(JacksonJsonObjectFactory objectFactory) {
      this.objectFactory = objectFactory;
    }

    public JacksonJsonObjectFactory getObjectFactory() {
      return objectFactory;
    }
  }
}
