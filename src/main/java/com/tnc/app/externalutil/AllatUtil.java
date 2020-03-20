/******************************  Comment  *****************************
File Name         : AllatUtil.java
File Description  : Allat Script API Utility Function(Class)
Version           :
[ Notice ]
 이 파일은 NewAllatPay를 사용하기 위한 Utilty Function을 구현한
Source Code입니다. 이 파일에 내요을 임의로 수정하실 경우 기술지원을
받으실 수 없음을 알려드립니다. 이 파일 내용에 문제가 있을 경우,
아래 연락처로 문의 주시기 바랍니다.

TEL       : 02-3783-9990
EMAIL     : allatpay@allat.co.kr
Homepage  : www.allatpay.com
******** Copyright Allat Corp. All Right Reserved  *******/
package com.tnc.app.externalutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
//import javax.servlet.http.HttpServletRequest;
//
//import com.tnc.business.morder.constant.MobileOrderConst;
//import com.tnc.business.morder.mobile.vo.OrderOnlinePayInfo;
//import com.tnc.business.payment.allat.constant.AllatConst;
//import com.tnc.business.payment.allat.vo.AllatInfo;
//import com.tnc.common.mstdata.util.SystemPropsUtil;
//import com.tnc.common.session.manager.SessionLoginUtil;

import net.sf.json.JSONObject;

public class AllatUtil {

///Fields
	private static final String util_lang = "JSP";
	private static final String util_ver = "1.0.7.1";

	private static final String approval_uri = "/servlet/AllatPayUtf8/pay/approval.jsp";
	private static final String sanction_uri = "/servlet/AllatPay/pay/sanction.jsp";
	private static final String cancel_uri = "/servlet/AllatPay/pay/cancel.jsp";
	private static final String cashreg_uri = "/servlet/AllatPay/pay/cash_registry.jsp";
	private static final String cashapp_uri = "/servlet/AllatPay/pay/cash_approval.jsp";
	private static final String cashcan_uri = "/servlet/AllatPay/pay/cash_cancel.jsp";
	private static final String escrowchk_uri = "/servlet/AllatPay/pay/escrow_check.jsp";
	private static final String escrowret_uri = "/servlet/AllatPay/pay/escrow_return.jsp";
	private static final String escrowconfirm_uri = "/servlet/AllatPay/pay/escrow_confirm.jsp";
	private static final String certreg_uri = "/servlet/AllatPay/pay/fix.jsp";
	private static final String certcancel_uri = "/servlet/AllatPay/pay/fix_cancel.jsp";

	private static final String c2c_approval_uri = "/servlet/AllatPay/pay/c2c_approval.jsp";
	private static final String c2c_cancel_uri = "/servlet/AllatPay/pay/c2c_cancel.jsp";
	private static final String c2c_sellerreg_uri = "/servlet/AllatPay/pay/seller_registry.jsp";
	private static final String c2c_productreg_uri = "/servlet/AllatPay/pay/product_registry.jsp";
	private static final String c2c_buyerchg_uri = "/servlet/AllatPay/pay/buyer_change.jsp";
	private static final String c2c_escrowchk_uri = "/servlet/AllatPay/pay/c2c_escrow_check.jsp";
	private static final String c2c_escrowconfirm_uri = "/servlet/AllatPay/pay/c2c_escrow_confirm.jsp";
	private static final String c2c_esrejectcheck_uri = "/servlet/AllatPay/pay/c2c_reject_check.jsp";
	private static final String c2c_expressreg_uri = "/servlet/AllatPay/pay/c2c_express_reg.jsp";

	private static final String allat_host = "tx.allatpay.com";

	private static final String utilVer = "&opt_lang=" + util_lang + "&opt_lang_ver=" + util_ver;

// TNC Add Const  -----------------------------------------------------------------------------------
	private static final String tnc_allatpay_sslflag = "SSL";
	private static final String tnc_allatpay_option_pin = "NOUSE";
	private static final String tnc_allatpay_option_mod = "APP";
//----------------------------------------------------------------------------------------------------

///Constructor

///Method
	public HashMap approvalReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = true;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, approval_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, approval_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap sanctionReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, sanction_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, sanction_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		} finally {
			return retHm;
		}
	}

	public HashMap cancelReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, cancel_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, cancel_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap cashregReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, cashreg_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, cashreg_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap cashappReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, cashapp_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, cashapp_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap cashcanReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, cashcan_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, cashcan_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap escrowchkReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, escrowchk_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, escrowchk_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap escrowRetReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, escrowret_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, escrowret_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap escrowConfirmReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, escrowconfirm_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, escrowconfirm_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

//////////////////////
	public HashMap CertRegReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, certreg_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, certreg_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap CertCancelReq(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, certcancel_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, certcancel_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap approvalReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_approval_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_approval_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap cancellReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_cancel_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_cancel_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap sellerRegReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_sellerreg_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_sellerreg_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap productRegReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_productreg_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_productreg_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap buyerChkReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_buyerchg_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_buyerchg_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap escrowChkReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_escrowchk_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_escrowchk_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap escrowConfirmReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_escrowconfirm_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_escrowconfirm_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap esRejectCheckReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_esrejectcheck_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_esrejectcheck_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

	public HashMap expressregReqC2C(String strReq, String sslFlag) {
		HashMap retHm = null;
		boolean isEnc = false;
		try {
			if (sslFlag.equals("SSL")) {
				retHm = SendRepo(strReq, allat_host, c2c_expressreg_uri, 443);
			} else {
				isEnc = checkEnc(strReq);
				if (isEnc) {
					retHm = SendRepo(strReq, allat_host, c2c_expressreg_uri, 80);
				} else {
					return retHm = getValue("reply_cd=0230\nreply_msg=암호화 오류\n");
				}
			}
		} catch (Exception e) {
			retHm = getValue("reply_cd=0221\nreply_msg=Exception : " + e.getMessage() + "\n");
		}
		return retHm;
	}

////////////////////////
	private HashMap SendRepo(String srpReq, String srpHost, String srpUri, int srpPort) {
		HashMap retHm = null;
		String retTxt = sendReq(srpReq, srpHost, srpUri, srpPort);
		retHm = getValue(retTxt);
		return retHm;
	}

////--------------------------Connect And Client Data Send----------------------------------------
	private String sendReq(String sendMsg, String host, String uri, int port) {
		String sendstr = "";
		String headerStr = "";
		String result = "";
		String getstr = "";

		// 443 일 때
		SSLSocketFactory sslFactory = null;
		SSLSocket sslSoc = null;

		// 80 일 때
		SocketFactory sFactory = null;
		Socket sSoc = null;

		// 공통
		PrintWriter sOut = null;
		BufferedReader sIn = null;

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String sApplyTime = "&allat_apply_ymdhms=" + formatter.format(cal.getTime());
		String sUtilVer = "&allat_opt_lang=" + util_lang + "&allat_opt_ver=" + util_ver;
		sendMsg = sendMsg + sUtilVer + sApplyTime;
		try {
			if (port == 80) { // 80 Port 일 때
				sFactory = SocketFactory.getDefault();
				sSoc = (Socket) sFactory.createSocket(InetAddress.getByName(host), port);
				sOut = new PrintWriter(sSoc.getOutputStream(), true);
				sIn = new BufferedReader(new InputStreamReader(sSoc.getInputStream(), "euc-kr"));
			} else { // 80 이외에는 443 Port 로 통신
				sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				sslSoc = (SSLSocket) sslFactory.createSocket(host, port);
				sOut = new PrintWriter(sslSoc.getOutputStream(), true);
				sIn = new BufferedReader(new InputStreamReader(sslSoc.getInputStream(), "euc-kr"));
			}

		} catch (UnknownHostException uhe) {
			result = "reply_cd=0221\nreply_msg=Exception: " + uhe.getMessage() + "\n";
			return result;
		} catch (IOException ioe) {
			result = "reply_cd=0221\nreply_msg=Exception:" + ioe.getMessage() + "\n";
			return result;
		} catch (Exception e) {
			result = "reply_cd=0221\nreply_msg=Exception:" + e.getMessage() + "\n";
			return result;
		}

		try {
			byte[] reqbyte = sendMsg.getBytes();
			int reqlen = reqbyte.length;

			sendstr = "POST " + uri + " HTTP/1.0\r\n";
			sendstr += "Host: " + host + ":" + port + "\r\n";
			sendstr += "Content-type: application/x-www-form-urlencoded\r\n";
			sendstr += "Content-length: " + reqlen + "\r\n";
			sendstr += "Accept: */*\r\n";
			sendstr += "\r\n";
			sendstr += sendMsg + "\r\n";
			sendstr += "\r\n";
			sOut.println(sendstr);

			while ((getstr = sIn.readLine()).length() != 0) {
				headerStr += getstr + "\n";
			}

			while ((getstr = sIn.readLine()) != null) {
				result += getstr + "\n";
			}
		} catch (Exception e) {
			result = "reply_cd=0212\n" + "reply_msg=Socket Connect Error:" + e.getMessage() + "\n";
			return result;
		} finally {
			try {
				if (sOut != null)
					sOut.close();
				if (sIn != null)
					sIn.close();
				if (sslSoc != null)
					sslSoc.close(); // 443
				if (sSoc != null)
					sSoc.close();
			} catch (Exception ef) {
			}
		}
		return result;
	}

////-----------------------Result Text Convert HashMap---------------------------------------
	private HashMap getValue(String sText) {
		HashMap retHm = new HashMap();
		String sArg1 = null;
		String sArg2 = null;

		StringTokenizer fstTk = new StringTokenizer(sText, "\n");
		while (fstTk != null && fstTk.hasMoreTokens()) {
			String tmpTk = fstTk.nextToken();
			StringTokenizer secTk = new StringTokenizer(tmpTk, "=");
			for (int i = 0; i < 2; i++) {
				if (i == 0) {
					if (secTk.hasMoreTokens())
						sArg1 = secTk.nextToken().trim();
					else
						sArg1 = "";
				} else {
					if (secTk.hasMoreTokens())
						sArg2 = secTk.nextToken().trim();
					else
						sArg2 = "";
				}
			}
			retHm.put(sArg1, sArg2);
		}
		if (retHm.get("reply_cd") == null) {
			retHm.put("reply_cd", "0299");
			retHm.put("reply_msg", sText);
		}
		return retHm;
	}

	private boolean checkEnc(String srcStr) {
		int ckIdx;

		ckIdx = srcStr.indexOf("allat_enc_data=");

		if (ckIdx == -1) {
			return false;
		} else {
			ckIdx += "allat_enc_data=".length() + 5;
		}
		if ((srcStr.substring(ckIdx, ckIdx + 1)).equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public String setValue(HashMap hm) {
		String formData = "";
		int i = 0;
		boolean bFirst = false;
		if (hm == null) {
			formData = null;
			return formData;
		}
		Iterator ir = hm.keySet().iterator();
		while (ir.hasNext()) {
			String sKey = (String) ir.next();
			String sValue = (String) hm.get(sKey);
			if (bFirst) {
				formData += sKey + "" + sValue + "";
			} else {
				formData += "00000010" + sKey + "" + sValue + "";
				bFirst = true;
			}
		}
		return formData;
	}

//-------------------------------------------------------------------------------------
	/**
	 * <pre>
	* 설명: 금결원O2O 승인요청
	 * </pre>
	 *
	 * @param strReq 요청데이터 정보
	 * @return 올앳요청결과
	 */
	public static String approval(String mid, String apikey, String pay_amt, String enc_data) {
		// 1. 요청데이터 정보 설정
		String strReq = "";
		strReq += "allat_shop_id=" + mid;
		strReq += "&allat_amt=" + pay_amt;
		strReq += "&allat_enc_data=" + enc_data;
		strReq += "&allat_cross_key=" + apikey;

		// 2.올앳 결제 서버와 통신
		/*
		 * -------------------------------------------------------------------------- 실제
		 * 결제 : allat_test_yn=N 일 경우, reply_cd=0000 이면 정상 테스트 결제 : allat_test_yn=Y 일 경우,
		 * reply_cd=0001 이면 정상
		 * --------------------------------------------------------------------------
		 */
		AllatUtil allatUtil = new AllatUtil();
		HashMap result = allatUtil.approvalReq(strReq, tnc_allatpay_sslflag);
		return JSONObject.fromObject(result).toString();
	}

	/**
	 * <pre>
	* 설명: 금결원O2O 승인취소요청
	 * </pre>
	 *
	 * @param strReq 요청데이터 정보
	 * @return 올앳요청결과
	 */
	public static String approvalCancel(String order_id, String mid, String apikey, String cancel_amt,
			String pay_method, String test_yn) {
		String enc_data = ""; // EncData
		String strReq = "";

		// 1. 요청데이터 정보 설정
		HashMap<String, String> reqHm = new HashMap<String, String>();
		reqHm.put("allat_shop_id", mid);
		reqHm.put("allat_order_no", order_id);
		reqHm.put("allat_amt", cancel_amt);
		reqHm.put("allat_pay_type", pay_method);
		reqHm.put("allat_test_yn", test_yn);
		reqHm.put("allat_opt_pin", tnc_allatpay_option_pin);
		reqHm.put("allat_opt_mod", tnc_allatpay_option_mod);

		// 2.올앳 결제 서버와 통신
		/*
		 * -------------------------------------------------------------------------- 실제
		 * 결제 : allat_test_yn=N 일 경우, reply_cd=0000 이면 정상 테스트 결제 : allat_test_yn=Y 일 경우,
		 * reply_cd=0001 이면 정상
		 * --------------------------------------------------------------------------
		 */
		AllatUtil allatUtil = new AllatUtil();
		enc_data = allatUtil.setValue(reqHm);
		strReq += "allat_shop_id=" + mid;
		strReq += "&allat_amt=" + cancel_amt;
		strReq += "&allat_enc_data=" + enc_data;
		strReq += "&allat_cross_key=" + apikey;

		HashMap result = allatUtil.cancelReq(strReq, tnc_allatpay_sslflag);
		return JSONObject.fromObject(result).toString();
	}
}
