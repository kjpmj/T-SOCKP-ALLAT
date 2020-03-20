package com.tnc.app.business;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tnc.app.externalutil.AllatUtil;
import com.tnc.app.netty.constant.NettyConstant;
import com.tnc.app.netty.exception.NettyException;
import com.tnc.app.netty.model.ProxyRequestVO;
import com.tnc.app.netty.util.NettyUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class UriController {
	Logger logger = LoggerFactory.getLogger(UriController.class);

	public void requestExternal(ProxyRequestVO vo, ChannelHandlerContext ctx) throws Exception {
		String reqUri = vo.getRequestUri();

		// Allat 승인
		if (DefineCode.URI_ALLAT_APPROVAL.equals(reqUri)) {
			String mid = "";
			String apikey = "";
			String payamt = "";
			String enc_data = "";

			for (Map<String, String> map : vo.getParameters()) {
				mid = "mid".equals(map.get("key")) ? map.get("value") : mid;
				apikey = "apikey".equals(map.get("key")) ? map.get("value") : apikey;
				payamt = "payamt".equals(map.get("key")) ? map.get("value") : payamt;
				enc_data = "enc_data".equals(map.get("key")) ? map.get("value") : enc_data;
			}

			String resMsg = AllatUtil.approval(mid, apikey, payamt, enc_data);
			logger.info("Allat 승인 결과: " + resMsg);

			FullHttpResponse response = NettyUtil.createFullHttpResponse(HttpResponseStatus.OK, resMsg);
			ctx.write(response.retain());

			// Allat 승인취소
		} else if (DefineCode.URI_ALLAT_APPROVAL_CANCEL.equals(reqUri)) {
			String order_id = "";
			String mid = "";
			String apikey = "";
			String cancel_amt = "";
			String pay_method = "";
			String test_yn = "";
			
			for (Map<String, String> map : vo.getParameters()) {
				order_id 	= "order_id".equals(map.get("key")) ? map.get("value") : order_id;
				mid 		= "mid".equals(map.get("key")) ? map.get("value") : mid;
				apikey 		= "apikey".equals(map.get("key")) ? map.get("value") : apikey;
				cancel_amt 	= "cancel_amt".equals(map.get("key")) ? map.get("value") : cancel_amt;
				pay_method 	= "pay_method".equals(map.get("key")) ? map.get("value") : pay_method;
				test_yn 	= "test_yn".equals(map.get("key")) ? map.get("value") : test_yn;
			}
			
			String resMsg = AllatUtil.approvalCancel(order_id, mid, apikey, cancel_amt, pay_method, test_yn);
			logger.info("Allat 승인취소 결과: " + resMsg);

			FullHttpResponse response = NettyUtil.createFullHttpResponse(HttpResponseStatus.OK, resMsg);
			ctx.write(response.retain());
			
		} else {
			throw new NettyException(NettyConstant.BUSINESS_ERROR_004);
		}
	}
}
