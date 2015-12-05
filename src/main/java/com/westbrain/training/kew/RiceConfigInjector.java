package com.westbrain.training.kew;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.kuali.common.util.spring.PropertySourceConversionResult;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.google.common.base.Preconditions;

public class RiceConfigInjector {

	public static void injectDefaults(ConfigurableEnvironment env) {
		try {
			Properties envProperties = getAllEnumerableProperties(env);
			envProperties.put("rice.struts.message.resources", "");
			JAXBConfigImpl config = new JAXBConfigImpl("classpath:META-INF/common-config-defaults.xml", envProperties);
			config.parseConfig();	
			ConfigContext.init(config);
		} catch (IOException e) {
			throw new ConfigurationException("Failed to parse rice configuration defaults", e);
		}
	}
	
	private static Properties getAllEnumerableProperties(ConfigurableEnvironment env) {

		// Extract the list of PropertySources from the environment
		List<PropertySource<?>> sources = getPropertySources(env);

		// Spring provides PropertySource objects ordered from highest priority to lowest priority
		// We reverse the order here so things follow the typical "last one in wins" strategy
		Collections.reverse(sources);

		// Convert the list of PropertySource's to a list of Properties objects
		PropertySourceConversionResult result = convertEnumerablePropertySources(sources);

		// Combine them into a single Properties object
		return combine(result.getPropertiesList());
	}
	
	private static final Properties combine(List<Properties> properties) {
		Properties combined = new Properties();
		for (Properties p : properties) {
			combined.putAll(toEmpty(p));
		}
		return combined;
	}
	
	private static final Properties toEmpty(Properties properties) {
		return properties == null ? new Properties() : properties;
	}
	
	/**
	 * Aggregate all <code>PropertySource<?><code> objects from the environment into a <code>List</code>
	 */
	private static List<PropertySource<?>> getPropertySources(ConfigurableEnvironment env) {
		Preconditions.checkNotNull(env, "'env' cannot be null");
		MutablePropertySources mps = env.getPropertySources();
		List<PropertySource<?>> sources = new ArrayList<PropertySource<?>>();
		Iterator<PropertySource<?>> itr = mps.iterator();
		while (itr.hasNext()) {
			PropertySource<?> source = itr.next();
			sources.add(source);
		}
		return sources;
	}
	
	private static PropertySourceConversionResult convertEnumerablePropertySources(List<PropertySource<?>> sources) {
		PropertySourceConversionResult result = new PropertySourceConversionResult();
		List<Properties> list = new ArrayList<Properties>();
		List<PropertySource<?>> converted = new ArrayList<PropertySource<?>>();
		List<PropertySource<?>> skipped = new ArrayList<PropertySource<?>>();
		// Extract property values from the sources and place them in a Properties object
		for (PropertySource<?> source : sources) {
			if (source instanceof EnumerablePropertySource) {
				EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>) source;
				Properties sourceProperties = convert(eps);
				list.add(sourceProperties);
				converted.add(source);
			}
		}
		result.setConverted(converted);
		result.setSkipped(skipped);
		result.setPropertiesList(list);
		return result;
	}
	
	/**
	 * Convert an EnumerablePropertySource into a Properties object.
	 */
	private static Properties convert(EnumerablePropertySource<?> source) {
		Properties properties = new Properties();
		String[] names = source.getPropertyNames();
		for (String name : names) {
			Object object = source.getProperty(name);
			if (object != null) {
				String value = object.toString();
				properties.setProperty(name, value);
			}
		}
		return properties;
	}
	
}
