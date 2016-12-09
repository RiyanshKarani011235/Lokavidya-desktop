package com.iitb.lokavidya.core.utils;

import java.io.File;

public class ValidateProject {
	String location,name;
	public ValidateProject(String location) {
		// TODO Auto-generated constructor stub
		this.location=location;
		name=new File(location).getName();
	}
	public void validate_name()
	{
		File dir;
		dir=new File(location,"images");
		System.out.println(dir.getAbsolutePath());
		for(File x:dir.listFiles())
		{
			String sub=x.getName().split("\\_")[0];
			System.out.println("Substring is "+sub);
			if(sub.equals(name))
				continue;
			else
			{
				String newPath=x.getAbsolutePath();
				newPath=newPath.replace(sub, name);
				File fnew=new File(newPath);
				x.renameTo(fnew);
			}
		}
		dir=new File(location,"output");
		for(File x:dir.listFiles())
		{
			int i=x.getName().indexOf(".");
			String sub=x.getName().substring(0, i);
			System.out.println("Substring is "+sub);
			if(sub.equals(name))
				continue;
			else
			{
				String newPath=x.getAbsolutePath();
				newPath=newPath.replace(sub, name);
				File fnew=new File(newPath);
				x.renameTo(fnew);
			}
		}
		dir=new File(location,"audio");
		for(File x:dir.listFiles())
		{
			String sub=x.getName().split("\\_")[0];
			System.out.println("Substring is "+sub);
			if(sub.equals(name))
				continue;
			else
			{
				String newPath=x.getAbsolutePath();
				newPath=newPath.replace(sub, name);
				File fnew=new File(newPath);
				x.renameTo(fnew);
			}
		}
		dir=new File(location,"video");
		for(File x:dir.listFiles())
		{
			String sub=x.getName().split("\\_")[0];
			System.out.println("Substring is "+sub);
			if(sub.equals(name))
				continue;
			else
			{
				String newPath=x.getAbsolutePath();
				newPath=newPath.replace(sub, name);
				File fnew=new File(newPath);
				x.renameTo(fnew);
			}
		}
		dir=new File(location,"presentation");
		for(File x:dir.listFiles())
		{
			int i=x.getName().indexOf(".");
			String sub=x.getName().substring(0, i);
			System.out.println("Substring is "+sub);
			if(sub.equals(name))
				continue;
			else
			{
				String newPath=x.getAbsolutePath();
				newPath=newPath.replace(sub, name);
				File fnew=new File(newPath);
				x.renameTo(fnew);
			}
		}
		dir=new File(location,"temp");
		for(File x:dir.listFiles())
		{
			String sub=x.getName().split("\\_")[0];
			System.out.println("Substring is "+sub);
			if(sub.equals(name))
				continue;
			else
			{
				String newPath=x.getAbsolutePath();
				newPath=newPath.replace(sub, name);
				File fnew=new File(newPath);
				x.renameTo(fnew);
			}
		}
	}

}
