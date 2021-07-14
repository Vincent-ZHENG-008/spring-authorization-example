package cn.wunhwan.security.factory;

import java.util.function.Supplier;

/**
 * 对象工厂模式
 *
 * @author WunHwan
 **/
public interface ObjectFactory<T> extends Supplier<T> {

}
