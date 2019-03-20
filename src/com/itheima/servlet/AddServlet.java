package com.itheima.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.domain.Student;
import com.itheima.service.StudentService;
import com.itheima.service.impl.StudentServiceImpl;


/**
 * 用于处理学生的添加请求
 * @author xiaomi
 *
 */
public class AddServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
		
		
		try {

			//1. 获取客户端提交上来的数据
			String sname = request.getParameter("sname"); //sname:zhangsan
			String gender = request.getParameter("gender");
			String phone = request.getParameter("phone");
			String birthday = request.getParameter("birthday"); // 1989-10-18
			String info = request.getParameter("info");
			//String hobby = request.getParameter("hobby");//hobby : 游泳，写字， 足球。
			String [] h  = request.getParameterValues("hobby");
//			[篮球，足球，写字] --- 篮球，足球，写字
			
			String hobby = Arrays.toString(h);
			hobby = hobby.substring(1, hobby.length()-1);
			
			//2. 添加到数据库
			//string -- date
			Date date= new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
			
			Student student = new Student(sname, gender, phone, hobby, info, date);
			StudentService service = new StudentServiceImpl();
			service.insert(student);
			
			//3. 跳转到列表页
			//再查一次数据库，然后再装到作用域中 ，然后再跳转。
			//这里是直接跳转到页面上， 那么这个页面会重新翻译一次，上面的那个request的请求存放的数据是没有了。 
			//request.getRequestDispatcher("list.jsp").forward(request, response);
			
			//servlet除了能跳jsp之外。 还能跳servlet
			request.getRequestDispatcher("StudentListServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
