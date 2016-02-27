package com.example.mobilesafe.utiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStringUtiles {
	/**
	 * ͨ����������ȡ�ַ���
	 * @param is
	 * @return �쳣�ͷ���null
	 */
	public static String getStringByInputStream(InputStream is) {
		// TODO Auto-generated method stub
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while((len = is.read(b))!=-1){
				bao.write(b);
			}
			is.close();
			return bao.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
