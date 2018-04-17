/**
 * 
 */
package com.kony.tag.codereview.lua;

/**
 * @author student
 *
 */
public class LuaFunction
{
	public String getFunctionName() 
	{
		return functionName;
	}

	public void setFunctionName(String functionName) 
	{
		this.functionName = functionName;
	}

	public int getParamCount() 
	{
		return paramCount;
	}

	public void setParamCount(int paramCount) 
	{
		this.paramCount = paramCount;
	}

	String functionName=" ";
	int paramCount=0;
}