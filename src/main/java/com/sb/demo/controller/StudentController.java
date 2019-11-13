package com.sb.demo.controller;

import com.rabbitmq.client.Channel;
import com.sb.demo.entity.CourseEntity;
import com.sb.demo.entity.StudentEntity;
import com.sb.demo.pub.controller.BaseRestController;
import com.sb.demo.pub.rabbit.RabbitConfig;
import com.sb.demo.pub.utils.JsonBackData;
import com.sb.demo.service.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/student")
public class StudentController extends BaseRestController<IStudentService, StudentEntity> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${spring.cache.redis.key-prefix}")
	private String prefix;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@GetMapping("/testRedis")
	public JsonBackData testRedis(){
		JsonBackData back = new JsonBackData();
		StudentEntity student = new StudentEntity();
		student.setCode("r01");
		student.setName("测试reids01");
		List<CourseEntity> courses = new ArrayList<CourseEntity>();
		CourseEntity c1 = new CourseEntity();
		c1.setCode("c01");
		c1.setName("测试课程01");
		CourseEntity c2 = new CourseEntity();
		c1.setCode("c02");
		c1.setName("测试课程02");
		courses.add(c1);
		courses.add(c2);
		student.setCourses(courses);
		redisTemplate.opsForValue().set("stu-02", student);
		back.setBackMsg("操作redis成功");
		return back;
	}
	
	@GetMapping("/getRedis")
	public JsonBackData getRedis(){
		JsonBackData back = new JsonBackData();
		StudentEntity value = (StudentEntity) redisTemplate.opsForValue().get("stu-02");
		back.setBackData(value);
		back.setBackMsg("操作redis成功");
		return back;
	}

	@GetMapping("/sendMsg")
	public JsonBackData sendMsg() {
		JsonBackData back = new JsonBackData();
		StudentEntity entity = new StudentEntity();
		entity.setCode("msg01");
		entity.setName("测试消息01");
		rabbitTemplate.convertAndSend(RabbitConfig.STUDENT_EXCHANGE, RabbitConfig.STUDENT_ROUTING_KEY, entity);
		back.setBackData(entity);
		back.setBackMsg("发送消息成功");
		return back;
	}
	
	@GetMapping("/saveStudent")
	@Cacheable(value="student",key="#key")
	public StudentEntity saveStudent(String key) {
		StudentEntity student = new StudentEntity();
		student.setCode("r01");
		student.setName("测试reids01");
		List<CourseEntity> courses = new ArrayList<CourseEntity>();
		CourseEntity c1 = new CourseEntity();
		c1.setCode("c01");
		c1.setName("测试课程01");
		CourseEntity c2 = new CourseEntity();
		c1.setCode("c02");
		c1.setName("测试课程02");
		courses.add(c1);
		courses.add(c2);
		student.setCourses(courses);
		return student;
	}

	@RabbitListener(queues = { RabbitConfig.STUDENT_QUEUE })
	public void listenerManualAck(Object book, Message message, Channel channel) {
		logger.info("[listenerManualAck 监听的消息] - [{}]", book.toString());
		try {
			// TODO 通知 MQ 消息已被成功消费,可以ACK了
//			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//			channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
		} catch (IOException e) {
			// TODO 如果报错了,那么我们可以进行容错处理,比如转移当前消息进入其它队列
			e.printStackTrace();

		}
	}
	
	@RabbitListener(queues={RabbitConfig.DEAD_LETTER_QUEUE})
	public void listenerDeadQueue(Object book, Message message, Channel channel){
		logger.info("[listenerDeadQueue 监听的消息] - [{}]", book.toString());
		try {
			// TODO 通知 MQ 消息已被成功消费,可以ACK了
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//			channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
		} catch (IOException e) {
			// TODO 如果报错了,那么我们可以进行容错处理,比如转移当前消息进入其它队列
			e.printStackTrace();

		}
	}
}
