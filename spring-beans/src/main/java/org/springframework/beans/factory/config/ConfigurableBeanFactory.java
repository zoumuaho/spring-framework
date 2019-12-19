/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.config;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link org.springframework.beans.factory.BeanFactory}
 * interface.
 *
 * <p>This bean factory interface is not meant to be used in normal application
 * code: Stick to {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * needs. This extended interface is just meant to allow for framework-internal
 * plug'n'play and for special access to bean factory configuration methods.
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.ListableBeanFactory
 * @see ConfigurableListableBeanFactory
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * Custom scopes can be added via {@code registerScope}.
	 * @see #registerScope
	 * 单例
	 */
	String SCOPE_SINGLETON = "singleton";

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * Custom scopes can be added via {@code registerScope}.
	 * @see #registerScope
	 * 原型
	 */
	String SCOPE_PROTOTYPE = "prototype";


	/**
	 * Set the parent of this bean factory.
	 * <p>Note that the parent cannot be changed: It should only be set outside
	 * a constructor if it isn't available at the time of factory instantiation.
	 * 搭配HierarchicalBeanFactory接口的getParentBeanFactory方法
	 * @param parentBeanFactory the parent BeanFactory
	 * @throws IllegalStateException if this factory is already associated with
	 * a parent BeanFactory
	 * @see #getParentBeanFactory()
	 */
	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

	/**
	 * 设置、返回工厂的类加载器
	 * @param beanClassLoader
	 */
	void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

	/**
	 * Return this factory's class loader for loading bean classes
	 * (only {@code null} if even the system ClassLoader isn't accessible).
	 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
	 */
	@Nullable
	ClassLoader getBeanClassLoader();

	/**
	 * 设置、返回一个临时的类加载器
	 * @param tempClassLoader
	 */
	void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

	/**
	 * Return the temporary ClassLoader to use for type matching purposes,
	 * if any.
	 * @since 2.5
	 */
	@Nullable
	ClassLoader getTempClassLoader();

	/**
	 * 设置、是否缓存元数据，如果false，那么每次请求实例，都会从类加载器重新加载（热加载）
	 * @param cacheBeanMetadata
	 */
	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	/**
	 * 是否缓存元数据
	 * @return
	 */
	boolean isCacheBeanMetadata();

	/**
	 * Bean表达式分解器
	 * @param resolver
	 */
	void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

	/**
	 * Return the resolution strategy for expressions in bean definition values.
	 * @since 3.0
	 */
	@Nullable
	BeanExpressionResolver getBeanExpressionResolver();

	/**
	 * 设置、返回一个转换服务
	 * @param conversionService
	 */
	void setConversionService(@Nullable ConversionService conversionService);

	/**
	 * Return the associated ConversionService, if any.
	 * @since 3.0
	 */
	@Nullable
	ConversionService getConversionService();

	/**
	 * 设置属性编辑登记员...
	 * @param registrar
	 */
	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

	/**
	 * 注册常用属性编辑器
	 * @param requiredType
	 * @param propertyEditorClass
	 */
	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

	/**
	 * 用工厂中注册的通用的编辑器初始化指定的属性编辑注册器
	 * @param registry
	 */
	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

	/**
	 * 设置、得到一个类型转换器
	 * @param typeConverter
	 */
	void setTypeConverter(TypeConverter typeConverter);

	/**
	 * Obtain a type converter as used by this BeanFactory. This may be a fresh
	 * instance for each call, since TypeConverters are usually <i>not</i> thread-safe.
	 * <p>If the default PropertyEditor mechanism is active, the returned
	 * TypeConverter will be aware of all custom editors that have been registered.
	 * @since 2.5
	 */
	TypeConverter getTypeConverter();

	/**
	 *  增加一个嵌入式的StringValueResolver
	 * @param valueResolver
	 */
	void addEmbeddedValueResolver(StringValueResolver valueResolver);

	/**
	 * Determine whether an embedded value resolver has been registered with this
	 * bean factory, to be applied through {@link #resolveEmbeddedValue(String)}.
	 * @since 4.3
	 */
	boolean hasEmbeddedValueResolver();

	/**
	 * 分解指定的嵌入式的值
	 * @param value
	 * @return
	 */
	@Nullable
	String resolveEmbeddedValue(String value);

	/**
	 * /设置一个Bean后处 理器
	 * @param beanPostProcessor
	 */
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	/**
	 * 返回Bean后处理器的数量
	 * @return
	 */
	int getBeanPostProcessorCount();

	/**
	 * 注册范围
	 * @param scopeName
	 * @param scope
	 */
	void registerScope(String scopeName, Scope scope);

	/**
	 * 返回注册的范围名
	 * @return
	 */
	String[] getRegisteredScopeNames();

	/**
	 * 返回指定的范围
	 * @param scopeName
	 * @return
	 */
	@Nullable
	Scope getRegisteredScope(String scopeName);

	/**
	 * 返回本工厂的一个安全访问上下文
	 * @return
	 */
	AccessControlContext getAccessControlContext();

	/**
	 * 从其他的工厂复制 相关的所有配置
	 * @param otherFactory
	 */
	void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

	/**
	 * 给指定的Bean注册别名
	 * @param beanName
	 * @param alias
	 * @throws BeanDefinitionStoreException
	 */
	void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

	/**
	 * 根据指定的 StringValueResolver移除所有的别名
	 * @param valueResolver
	 */
	void resolveAliases(StringValueResolver valueResolver);

	/**
	 * 返回指定Bean合并后的Bean定义
	 * @param beanName
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * 判断指 定Bean是否为一个工厂Bean
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

	/**
	 * 设置一个Bean是 否正在创建
	 * @param beanName
	 * @param inCreation
	 */
	void setCurrentlyInCreation(String beanName, boolean inCreation);

	/**
	 * 返回指定Bean是否已经成功创建
	 * @param beanName
	 * @return
	 */
	boolean isCurrentlyInCreation(String beanName);

	/**
	 * 注册一个 依赖于指定bean的Bean
	 * @param beanName
	 * @param dependentBeanName
	 */
	void registerDependentBean(String beanName, String dependentBeanName);

	/**
	 * 返回依赖于指定Bean的所欲Bean名
	 * @param beanName
	 * @return
	 */
	String[] getDependentBeans(String beanName);

	/**
	 * 返回指定Bean依赖的所有Bean名
	 * @param beanName
	 * @return
	 */
	String[] getDependenciesForBean(String beanName);

	/**
	 * 销毁指定的Bean
	 * @param beanName
	 * @param beanInstance
	 */
	void destroyBean(String beanName, Object beanInstance);

	/**
	 * 销毁指定的范围Bean
	 * @param beanName
	 */
	void destroyScopedBean(String beanName);

	/**
	 * 销毁所有的单例类
	 */
	void destroySingletons();

}
