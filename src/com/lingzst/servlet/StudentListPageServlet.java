package com.lingzst.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.valves.StuckThreadDetectionValve;

import com.lingzst.domain.PageBean;
import com.lingzst.service.StudentService;
import com.lingzst.service.impl.StudentServiceImpl;


/**
 * 这是用于分页显示学生列表的servlet
 * @author xiaomi
 *
 */
public class StudentListPageServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			//1. 获取需要显示的页码数
			int currentPage =Integer.parseInt( request.getParameter("currentPage"));
			
			//2. 根据指定的页数，去获取该页的数据回来
			//List<Student> --- list.jsp
			
			StudentService service = new StudentServiceImpl();
			PageBean pageBean= service.findStudentByPage(currentPage);
			request.setAttribute("pageBean", pageBean);
			//3. 跳转界面。
			request.getRequestDispatcher("list_page.jsp").forward(request, response);
		} catch (SQLException e) {
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
