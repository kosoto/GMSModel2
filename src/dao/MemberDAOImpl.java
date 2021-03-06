package dao;

import java.util.*;
import domain.*;
import enums.*;
import factory.DatabaseFactory;
import factory.QueryFactory;
import pool.DBConstants;
import template.ColumnFinder;
import template.PstmtQuery;
import template.QueryTemplate;

import java.sql.*;

public class MemberDAOImpl implements MemberDAO{
	private static MemberDAO instance = new MemberDAOImpl();
	public static MemberDAO getInstance() {return instance;}
	private MemberDAOImpl() {}
	
	@Override
	public void insertMember(MemberBean member) {
		try {
			DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeUpdate(
							String.format(MemberQuery.INSERT_MEMBER.toString(),
									member.getMemberId(), member.getPassword(), member.getName(), member.getSsn()
									, member.getAge(), member.getGender(), member.getTeamId(), member.getRoll()));
		} catch (Exception e) {e.printStackTrace();}
	}
	
	@Override
	public String selectMemberCount() {
		String result = "";
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							QueryFactory.createQuery(
									MemberQuery.COUNT,
									Domain.MEMBER,
									"", "").getQuery());
			while(rs.next()) {
				result = rs.getString("NMEMBER");	
			}
		} catch (Exception e) {e.printStackTrace();}
		return result;
	}
	@Override
	public void updateMember(MemberBean member) {
		try {
			DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
			.getConnection().createStatement().executeUpdate(String.format(MemberQuery.UPDATE_MEMBER.toString(),
					member.getPassword(), member.getTeamId(), member.getRoll(), member.getMemberId()));
		} catch (Exception e) {e.printStackTrace();}
	}
	
	@Override
	public void deleteMember(MemberBean member) {
		try {
			DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
			.getConnection().createStatement().executeUpdate(String.format(MemberQuery.DELETE_MEMBER.toString(),
					member.getMemberId(), member.getPassword()));
		} catch (Exception e) {e.printStackTrace();}
	}
	
	@Override
	public MemberBean login(MemberBean member) {
		MemberBean mem = null;
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							String.format(MemberQuery.LOGIN.toString()
									, member.getMemberId() , member.getPassword() ));
			if(rs.next()) {
				do{
					mem = new MemberBean();
					mem.setMemberId(rs.getString("USERID"));
					mem.setTeamId(rs.getString("TEAMID"));
					mem.setName(rs.getString("NAME"));
					mem.setSsn(rs.getString("SSN"));
					mem.setRoll(rs.getString("ROLL"));
					mem.setPassword(rs.getString("PW"));
					mem.setGender(rs.getString("GENDER"));
					mem.setAge(rs.getString("AGE"));
				}while(rs.next());
			}			
		} catch (Exception e) {e.printStackTrace();}
		return mem;
	}
	
	@Override
	public MemberBean selectUser(MemberBean member) {
		MemberBean mem = null;
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							String.format(MemberQuery.SELECT_OVERLAP_USER.toString()
									, member.getName() , member.getSsn() ));
			if(rs.next()) {
				do{
					mem = new MemberBean();
					mem.setMemberId(rs.getString("USERID"));
				}while(rs.next());
			}
		} catch (Exception e) {e.printStackTrace();}
		return mem;
	}

	@Override
	public List<MemberBean> selectMemberAll() {
		List<MemberBean> list = new ArrayList<>();
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							QueryFactory.createQuery(
									MemberQuery.SELECT, 
									Domain.MEMBER, "", "").getQuery());
			MemberBean mem = null;
			while(rs.next()) {
				mem = new MemberBean();
				mem.setMemberId(rs.getString("MEMID"));				
				mem.setName(rs.getString("NAME"));				
				mem.setPassword(rs.getString("PW"));				
				mem.setRoll(rs.getString("ROLL"));				
				mem.setSsn(rs.getString("SSN"));				
				mem.setTeamId(rs.getString("TEAMID"));
				mem.setGender(rs.getString("GENDER"));
				mem.setAge(rs.getString("AGE"));
				list.add(mem);
			}
		} catch (Exception e) {e.printStackTrace();}
		return list;
	}
		
	@Override
	public MemberBean selectById(String id) {
		MemberBean mem = null;
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							QueryFactory.createQuery(
									MemberQuery.SELECT, 
									Domain.MEMBER, 
									Columns.MEMBER_ID.toString(), id).getQuery());
			if(rs.next()) {
				do{
					mem = new MemberBean();
					mem.setMemberId(rs.getString("MEMID"));
					mem.setTeamId(rs.getString("TEAMID"));
					mem.setName(rs.getString("NAME"));
					mem.setRoll(rs.getString("ROLL"));
					mem.setPassword(rs.getString("PW"));
					mem.setSsn(rs.getString("SSN"));
					mem.setGender(rs.getString("GENDER"));
					mem.setAge(rs.getString("AGE"));
				}while(rs.next());
			}		
		} catch (Exception e) {e.printStackTrace();}
		return mem;
	}
	
	@Override
	public List<MemberBean> selectSome(Columns column, String word) {
		QueryTemplate q = new PstmtQuery();
		List<MemberBean> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("column", column);
		map.put("value", "%"+word+"%");
		map.put("table", Domain.MEMBER);
		q.play(map);
		for(Object o : q.getList()) {
			list.add((MemberBean) o);
		}
		/*try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							QueryFactory.createQuery(
									MemberQuery.SELECT, 
									Domain.MEMBER, 
									column.toString(), word).getQuery());
			MemberBean mem = null;
			while(rs.next()) {
				mem = new MemberBean();
				mem.setMemberId(rs.getString("MEMID"));
				mem.setTeamId(rs.getString("TEAMID"));
				mem.setName(rs.getString("NAME"));
				mem.setRoll(rs.getString("ROLL"));
				mem.setPassword(rs.getString("PW"));
				mem.setSsn(rs.getString("SSN"));
				mem.setGender(rs.getString("GENDER"));
				mem.setAge(rs.getString("AGE"));
				list.add(mem);
			}
		} catch (Exception e) {e.printStackTrace();}*/
		return list;
	}
	@Override
	public List<MemberBean> selectMemberAll(Map<?, ?> param) {
		QueryTemplate q = new PstmtQuery();
		List<MemberBean> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("column", "B.NUM");
		map.put("table", " ( SELECT "
				+ " ROWNUM AS \"NUM\", "
				+ ColumnFinder.find(Domain.MEMBER).toUpperCase()
				+ " FROM (SELECT ROWNUM RO, M.* FROM MEMBER M ORDER BY ROWNUM DESC )A "
				+ " ) B " );
		map.put("beginRow", param.get("beginRow"));
		map.put("endRow", param.get("endRow"));
		q.play(map);
		for(Object o : q.getList()) {
			list.add((MemberBean) o);
		}
		return list;
	}
	@Override
	public List<MemberBean> selectMemberAll(String page) {
		QueryTemplate q = new PstmtQuery();
		List<MemberBean> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("column", "B.PAGE");
		map.put("value", page);
		map.put("table", " ( SELECT "
				+ " ROWNUM AS \"NUM\", "
				+ ColumnFinder.find(Domain.MEMBER)
				+ " , ROUND(((ROWNUM+2)/5),0) AS \"PAGE\" "
				+ " FROM (SELECT ROWNUM RO, M.* FROM MEMBER M ORDER BY ROWNUM DESC )A "
				+ " ) B " );
		q.play(map);
		for(Object o : q.getList()) {
			list.add((MemberBean) o);
		}
		return list;
	}
	
	/*
	@Override
	public List<MemberBean> selectByTeamId(String team) {
		List<MemberBean> list = new ArrayList<>();
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							QueryFactory.createQuery(
									MemberQuery.SELECT, 
									Domain.MEMBER, 
									Columns.TEAM_ID.toString(), team).getQuery());
			MemberBean mem = null;
			while(rs.next()) {
				mem = new MemberBean();
				mem.setMemberId(rs.getString("MEMID"));
				mem.setTeamId(rs.getString("TEAMID"));
				mem.setName(rs.getString("NAME"));
				mem.setRoll(rs.getString("ROLL"));
				mem.setPassword(rs.getString("PW"));
				mem.setSsn(rs.getString("SSN"));
				mem.setGender(rs.getString("GENDER"));
				mem.setAge(rs.getString("AGE"));
				list.add(mem);
			}
		} catch (Exception e) {e.printStackTrace();}
		return list;
	}
	
	@Override
	public List<MemberBean> selectByName(String name) {
		List<MemberBean> list = new ArrayList<>();
		try {
			ResultSet rs = DatabaseFactory.createDatabase(Vendor.ORACLE, DBConstants.USERNAME, DBConstants.PASSWORD)
					.getConnection().createStatement().executeQuery(
							QueryFactory.createQuery(
									MemberQuery.SELECT, 
									Domain.MEMBER, 
									Columns.NAME.toString(),
									"%"+name+"%").getQuery());
			MemberBean mem = null;
			while(rs.next()) {
				mem = new MemberBean();
				mem.setMemberId(rs.getString("MEMID"));
				mem.setTeamId(rs.getString("TEAMID"));
				mem.setName(rs.getString("NAME"));
				mem.setRoll(rs.getString("ROLL"));
				mem.setPassword(rs.getString("PW"));
				mem.setSsn(rs.getString("SSN"));
				mem.setGender(rs.getString("GENDER"));
				mem.setAge(rs.getString("AGE"));
				list.add(mem);
			}
		} catch (Exception e) {e.printStackTrace();}
		return list;
	}
	@Override
	public MemberBean selectById(MemberBean member) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
}
