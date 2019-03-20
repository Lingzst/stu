package com.lingzst.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.lingzst.dao.StudentDao;
import com.lingzst.domain.Student;
import com.lingzst.util.JDBCUtil02;
import com.lingzst.util.TextUtils;

/**
 * 这是StudentDao的实现。 针对前面定义的规范，做出具体的实现。
 * @author xiaomi
 */
public class StudentDaoImpl implements StudentDao {


	/**
	 * 查询所有学生
	 * @throws SQLException 
	 */
	@Override
	public List<Student> findAll() throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		return runner.query("select * from stu", new BeanListHandler<Student>(Student.class));
	}

	@Override
	public void insert(Student student) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		
		runner.update("insert into stu values(null , ?,?,?,?,?,?)" ,
				student.getSname(),
				student.getGender(),
				student.getPhone(),
				student.getBirthday(),
				student.getHobby(),
				student.getInfo()
				);
	}

	@Override
	public void delete(int sid) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		runner.update("delete from stu where sid=?" ,sid);
	}

	@Override
	public Student findStudentById(int sid) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		return runner.query("select * from stu where sid = ?", new BeanHandler<Student>(Student.class) ,sid);
	}

	@Override
	public void update(Student student) throws SQLException {
		
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		runner.update("update stu set sname=? , gender=? , phone=? , birthday=? , hobby=? , info=? where sid = ?", 
				student.getSname(),
				student.getGender(),
				student.getPhone(),
				student.getBirthday(),
				student.getHobby(),
				student.getInfo(),
				student.getSid());
	}

	@Override
	public List<Student> searchStudent(String sname, String sgender) throws SQLException {
		System.out.println("现在要执行模糊查询了，收到的name ="+sname + "==genser=="+sgender);
		
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		
		//String sql = "select * from stu where sname=? or sgender=?";
		
		/*
		 * 这里要分析一下：
		 * 	如果只有姓名 ，select * from stu where sname like ? ;
		 * 	如果只有性别 ， select * from stu where gender = ?
		 * 
		 * 如果两个都有 select * from stu where sname like ? and gender=?
		 * 
		 * 如果两个都没有就查询所有。
		 * 
		 */
		String sql = "select * from stu where 1=1 ";
		List<String> list = new ArrayList<String> ();
				
		//判断有没有姓名， 如果有，就组拼到sql语句里面
		if(!TextUtils.isEmpty(sname)){
			sql = sql + "  and sname like ?";
			list.add("%"+sname+"%");
		}
		
		//判断有没有性别，有的话，就组拼到sql语句里面。
		if(!TextUtils.isEmpty(sgender)){
			sql = sql + " and gender = ?";
			list.add(sgender);
		}
		
		
		return runner.query(sql , new BeanListHandler<Student>(Student.class) ,list.toArray() );
		
	}

	@Override
	public List<Student> findStudentByPage(int currentPage) throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		//第一个问号，代表一页返回多少条记录  ， 第二个问号， 跳过前面的多少条记录。
		//5 0 --- 第一页 (1-1)*5
		//5 5  --- 第二页 (2-1)*5
		//5 10  --- 第三页
		return runner.query("select * from stu limit ? offset ?", 
				new BeanListHandler<Student>(Student.class) , PAGE_SIZE , (currentPage-1)*PAGE_SIZE);
	}

	@Override
	public int findCount() throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtil02.getDataSource());
		//用于处理 平均值 、 总的个数。 
		Long  result = (Long) runner.query("SELECT COUNT(*) FROM stu" , new ScalarHandler() );
		return result.intValue();
	}

}
