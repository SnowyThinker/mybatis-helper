package io.github.snowthinker.mh;

import org.apache.ibatis.session.Configuration;

public interface ConfigurationCustomizer {
	
	void customize(Configuration configuration);
	
}
