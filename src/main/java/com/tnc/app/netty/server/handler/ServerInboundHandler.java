package com.tnc.app.netty.server.handler;

import java.io.EOFException;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tnc.app.business.UriController;
import com.tnc.app.netty.constant.NettyConstant;
import com.tnc.app.netty.exception.NettyException;
import com.tnc.app.netty.model.ProxyRequestVO;
import com.tnc.app.netty.util.NettyUtil;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

public class ServerInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	Logger logger = LoggerFactory.getLogger(ServerInboundHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("ServerInboundHandler > channelActive");
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		logger.info("ServerInboundHandler > channelRead");
		
		HttpRequest req = msg;
		HttpContent reqContent = msg;
		ProxyRequestVO vo = null;

		try {
			// POST method만 허용
			if(!NettyUtil.verifyHttpMethod(req.method())) {
				throw new NettyException(NettyConstant.BUSINESS_ERROR_003);
			}
			
			// TODO: uri가 sockp로 시작하는지 체크
			// true? 
			//	1. ProxyRequestVO 생성
			//	2. URI, Method명 설정
			//	3. URI로 분기 로직을 태우고 ExternalHost, ExternalPort, ExternalPath 설정
			// 
			// false?
			//	1.받은 데이터 그대로 
			
			// ProxyRequestVO 생성
			vo = NettyUtil.createProxyRequestVO(reqContent);
			
			// URI, Method 설정
			vo.setRequestUri(req.uri());
			vo.setHttpMehtod(req.method().name());
			
			// TODO: 아래를 하기전에 logger 부터 보자
			// TODO: 이 부분에서 ClientToProxyRequestVO에 있는 URI로 분기 로직을 태우고 ExternalHost, ExternalPort, ExternalPath 설정
			// TODO: 이 부분을 Annotation을 활용할 수 있도록 고민해본다.
			UriController uriController = new UriController();
			uriController.requestExternal(vo, ctx);
			
			// TODO: 위에 것들을 하고 URI에 따른 실제 로직을 추가해야 한다. (AllatUtil.approval() ,또는 approvalCancel())
			
//			ClientBootstrapper clientBootstrapper = new ClientBootstrapper(ctx, vo);
//			clientBootstrapper.init();
			
		} catch (NettyException e) {
			e.printStackTrace();
			FullHttpResponse response = NettyUtil.createFullHttpResponse(HttpResponseStatus.BAD_REQUEST, e.getMessage());
			ctx.write(response.retain()).addListener(ChannelFutureListener.CLOSE);
		} catch (EOFException e) {
			e.printStackTrace();
			FullHttpResponse response = NettyUtil.createFullHttpResponse(HttpResponseStatus.BAD_REQUEST ,NettyConstant.BUSINESS_ERROR_001);
			ctx.write(response.retain()).addListener(ChannelFutureListener.CLOSE);
		} catch (UnrecognizedPropertyException e) {
			e.printStackTrace();
			FullHttpResponse response = NettyUtil.createFullHttpResponse(HttpResponseStatus.BAD_REQUEST, NettyConstant.BUSINESS_ERROR_002, "허용되지 않은 파라미터 : " + e.getUnrecognizedPropertyName());
			ctx.write(response.retain()).addListener(ChannelFutureListener.CLOSE);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
