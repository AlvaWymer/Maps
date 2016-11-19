/**
 * 
 */
package com.express.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * @author Kitty
 *
 */
public class LoginInterceptor implements Interceptor
{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation arg0) throws Exception
	{
		String actionName = arg0.getInvocationContext().getName();
		System.out.println("actionName="+actionName);
		if(actionName.equals("adminlogin") || actionName.equals("loginout"))
		{
			return arg0.invoke();
		}
		else
		{
			if(arg0.getInvocationContext().getSession().get("currentUser") != null)
			{
				return arg0.invoke();
			}
			else
			{
				return "globalError";
			}
		}
	}

}
