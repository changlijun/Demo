package cn.changlijun.utils;

import android.content.Context;

public class ScreenUtils {
	/**
	 * 
	 * @param context
	 * @return �������ֵ
	 */
	public static int GetDisplayWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	/**
	 * 
	 * @param context
	 * @return �߶�����ֵ
	 */
	public static int GetDisplayHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
