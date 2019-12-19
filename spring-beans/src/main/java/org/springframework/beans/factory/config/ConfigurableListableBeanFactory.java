/*
 * Copyright 2002-2017 the original author or authors.
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

import java.util.Iterator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.lang.Nullable;

/**
 * Configuration interface to be implemented by most listable bean factories.
 * In addition to {@link ConfigurableBeanFactory}, it provides facilities to
 * analyze and modify bean definitions, and to pre-instantiate singletons.
 *
 * <p>This subinterface of {@link org.springframework.beans.factory.BeanFactory}
 * is not meant to be used in normal application code: Stick to
 * {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * use cases. This interface is just meant to allow for framework-internal
 * plug'n'play even when needing access to bean factory configuration methods.
 *
 * <p>
 * 源码说明：
 * 　　1、2个忽略自动装配的的方法。
 * 　　2、1个注册一个可分解依赖的方法。
 * 　　3、1个判断指定的Bean是否有资格作为自动装配的候选者的方法。
 * 　　4、1个根据指定bean名，返回注册的Bean定义的方法。
 * 　　5、2个冻结所有的Bean配置相关的方法。
 * 　　6、1个使所有的非延迟加载的单例类都实例化的方法。
 * 总结：
 * 工厂接口 ConfigurableListableBeanFactory 同时继承了3个接口，
 * ListableBeanFactory 、 AutowireCapableBeanFactory 和 ConfigurableBeanFactory ，
 * 扩展之后，加上自有的这8个方法，这个厂接口总共有83个方法，实在是巨大到不行了。
 * 这个工厂接口的自有方法总体上只是对父类接口功能的补充，
 * 包含了 BeanFactory 体系目前的所有方法，可以说是接口的集大成者.
 * </p>
 *
 * @author Juergen Hoeller
 * @see org.springframework.context.support.AbstractApplicationContext#getBeanFactory()
 * @since 03.11.2003
 */
public interface ConfigurableListableBeanFactory
		extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

	/**
	 * 忽略自动装配的依赖类型
	 *
	 * @param type
	 */
	void ignoreDependencyType(Class<?> type);

	/**
	 * 忽略自动装配的接口
	 *
	 * @param ifc
	 */
	void ignoreDependencyInterface(Class<?> ifc);

	/**
	 * 注册一个可分解的依赖
	 *
	 * @param dependencyType
	 * @param autowiredValue
	 */
	void registerResolvableDependency(Class<?> dependencyType, @Nullable Object autowiredValue);

	/**
	 * 判断指定的Bean是否有资格作为自动装配的候选者
	 *
	 * @param beanName
	 * @param descriptor
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor)
			throws NoSuchBeanDefinitionException;

	/**
	 * 返回注册的Bean定义
	 *
	 * @param beanName
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Return a unified view over all bean names managed by this factory.
	 * <p>Includes bean definition names as well as names of manually registered
	 * singleton instances, with bean definition names consistently coming first,
	 * analogous to how type/annotation specific retrieval of bean names works.
	 *
	 * @return the composite iterator for the bean names view
	 * @see #containsBeanDefinition
	 * @see #registerSingleton
	 * @see #getBeanNamesForType
	 * @see #getBeanNamesForAnnotation
	 * @since 4.1.2
	 */
	Iterator<String> getBeanNamesIterator();

	/**
	 * Clear the merged bean definition cache, removing entries for beans
	 * which are not considered eligible for full metadata caching yet.
	 * <p>Typically triggered after changes to the original bean definitions,
	 * e.g. after applying a {@link BeanFactoryPostProcessor}. Note that metadata
	 * for beans which have already been created at this point will be kept around.
	 *
	 * @see #getBeanDefinition
	 * @see #getMergedBeanDefinition
	 * @since 4.2
	 */
	void clearMetadataCache();

	/**
	 * 暂时冻结所有的Bean配置
	 */
	void freezeConfiguration();

	/**
	 * 判断本工厂配置是否被冻结
	 *
	 * @return
	 */
	boolean isConfigurationFrozen();

	/**
	 * 使所有的非延迟加载的单例类都实例化
	 *
	 * @throws BeansException
	 */
	void preInstantiateSingletons() throws BeansException;

}
