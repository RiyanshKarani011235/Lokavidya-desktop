package com.iitb.lokavidya.core.utils;

import java.io.File;

public class ValidateAndroidProject {

	String location,name;
	public ValidateAndroidProject(String location) {
		// TODO Auto-generated constructor stub
		this.location=location;
		name=new File(location).getName();
		System.out.println("Changing project "+name+" at "+location);
	}
	public void validate_name()
	{
		File dir;
		dir=new File(location,"images");
		for(File x:dir.listFiles())
		{
			System.out.println("Found worked");
			String sub=x.getName().split("\\.")[0];
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
			System.out.println("Found worked");
			String sub=x.getName().split("\\.")[0];
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
