package mycourse.onkshare.test;

import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class SrpingConfigTest {

	ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
	
	{
		
	}
	
	/*@Test
	public void test() {
		SqlSessionTemplate template = (SqlSessionTemplate) context.getBean("sqlSessionTemplate");
		ContentMapper mapper = template.getMapper(ContentMapper.class);
		Content content = mapper.selectByPrimaryKey(28L);
		System.out.println(content);
	}*/

	@Test
	public void testStart() {
		Object redisTemplate = context.getBean("redisTemplate");
		RedisTemplate template = (RedisTemplate)redisTemplate;
		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				boolean flag = connection.persist("mycourse.onkshare.disk.storage".getBytes());
				return null;
			}
		});
	}

}
