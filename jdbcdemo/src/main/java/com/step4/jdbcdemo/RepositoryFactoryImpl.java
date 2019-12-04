package com.step4.jdbcdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import com.step4.jdbcdemo.model.Item;
import com.step4.jdbcdemo.repository.RepositoryCallBackMapper;

@Component
public class RepositoryFactoryImpl<T extends Item> implements RepositoryFactory<T>, BeanDefinitionRegistryPostProcessor {

	private static Logger logger = LoggerFactory.getLogger(RepositoryFactoryImpl.class);
	
	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		registerRepositories();
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
	}
	
	public void  registerRepositories() {
		PersistanceDictionary persistanceDictionary = beanFactory.getBean(PersistanceDictionary.class);
		@SuppressWarnings("unchecked")
		RepositoryCallBackMapper<T> callBackMapper = beanFactory.getBean(RepositoryCallBackMapper.class);

		for (String model : persistanceDictionary.keySet()) {
			PersistenceEntity entity = persistanceDictionary.get(model);

			MetaRepository<T> _r = null;

			try {
				_r = new MetaRepository<T>(entity.getName(),  Class.forName(entity.getClassName() ), entity.getName(),
						persistanceDictionary, callBackMapper);
				_r.setBeanFactory(beanFactory);
				_r.afterPropertiesSet();
				
				beanFactory.autowireBean(_r);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (beanFactory.containsBean(entity.getName()+ "Repository"))
				((DefaultListableBeanFactory) beanFactory).destroySingleton(entity.getName()+ "Repository");
			
			beanFactory.registerSingleton(entity.getName() + "Repository", _r );

			logger.info("Registered repository for model {} !", entity.getName());

		}
	}

}