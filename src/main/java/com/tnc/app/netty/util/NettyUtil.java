package com.tnc.app.netty.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.tnc.app.netty.model.ProxyRequestVO;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class NettyUtil {
	
	/**
	 * ProxyRequestVO 생성
	 * @param httpContent
	 * @return
	 */
	public static ProxyRequestVO createProxyRequestVO(HttpContent httpContent) throws Exception{
		ProxyRequestVO proxyRequestVO = null;
		ObjectMapper objectMapper = new ObjectMapper();
		
		// vo에 등록되어있지 않은 항목 체크
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		// null 혹은 비어있는 항목은 변환하지 않는다.
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		
		ByteBuf buf = httpContent.content();
		byte[] bytes = ByteBufUtil.getBytes(buf);

		// HttpRequestBody가 비어있으면 Exception
		if(bytes.length == 0) {
			
		}
		// HttpRequestBody에 포함된 항목으로 VO 생성 VO에 선언 안된 항목이 넘어올 시 UnrecognizedPropertyException
		proxyRequestVO = objectMapper.readValue(bytes, ProxyRequestVO.class);
		
		return proxyRequestVO;
	}
	
	/**
	 * HttpMethod 체크
	 * POST면 true
	 * 아니면 false
	 * */
	public static boolean verifyHttpMethod(HttpMethod httpMethod) {
		if(HttpMethod.POST.equals(httpMethod)) {
			return true;
		}
		
		return false; 
	}
	
	public static FullHttpResponse createFullHttpResponse(HttpResponseStatus status, String msg) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; " + HttpHeaderValues.CHARSET + "=utf-8");
		return response;
	}
	
	public static FullHttpResponse createFullHttpResponse(HttpResponseStatus status, String msg, String detailMsg) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.copiedBuffer((msg + "\r\n" + detailMsg).getBytes(CharsetUtil.UTF_8)));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; " + HttpHeaderValues.CHARSET + "=utf-8");
		return response;
	}
}
