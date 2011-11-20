package com.google.code.yourpresenter.service;

import java.io.Serializable;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;

@SuppressWarnings("serial")
@Service("cacheService")
@Repository
// there are specific needs for ehcache annotations, see:
// http://code.google.com/p/ehcache-spring-annotations/wiki/FrequentlyAskedQuestions
// 3. Where does @Cacheable go in my source?
public class EhCacheServiceImpl implements ICacheService, Serializable {

	@TriggersRemove(cacheName={"scheduleCache", "stateCache"}, 
	        keyGenerator = @KeyGenerator (
	                name = "HashCodeCacheKeyGenerator",
	                properties = @Property( name="includeMethod", value="false" )
	            )
	        )
	@Override
	public void clearScheduleCaches(String scheduleName) {
	}

}
