package com.zx.sms.connect.manager.cmpp;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import com.zx.sms.handler.api.gate.SessionConnectedHandler;
import com.zx.sms.handler.api.smsbiz.MessageReceiveHandler;
/**
 *经测试，35个连接，每个连接每200/s条消息
 *lenovoX250能承担7000/s消息编码解析无压力。
 *10000/s的消息服务不稳定，开个网页，或者打开其它程序导致系统抖动，会有大量消息延迟 (超过500ms)
 *
 *低负载时消息编码解码可控制在10ms以内。
 *
 */


public class ClientTestCMPPEndPoint {
	private static final Logger logger = LoggerFactory.getLogger(ClientTestCMPPEndPoint.class);

	@Test
	public void testCMPPEndpoint() throws Exception {
	
		final EndpointManager manager = EndpointManager.INS;
	
	
		CMPPClientEndpointEntity client = new CMPPClientEndpointEntity();
		client.setId("client");
		client.setHost("127.0.0.1");
		client.setPort(20003);
		client.setChartset(Charset.forName("utf-8"));
		client.setGroupName("test");
		client.setUserName("GSDT01");
		client.setPassword("1qaz2wsx");


		client.setMaxChannels((short)12);
		client.setWindows((short)16);
		client.setVersion((short)0x20);
		client.setRetryWaitTimeSec((short)10);
		client.setUseSSL(false);
		client.setReSendFailMsg(false);

		List<BusinessHandlerInterface> clienthandlers = new ArrayList<BusinessHandlerInterface>();
		clienthandlers.add( new SessionConnectedHandler(new AtomicInteger(100)));
		client.setBusinessHandlerSet(clienthandlers);
		manager.addEndpointEntity(client);
		
		manager.openAll();
		//LockSupport.park();

        System.out.println("start.....");
        
		Thread.sleep(300000);
		EndpointManager.INS.close();
	}
}
