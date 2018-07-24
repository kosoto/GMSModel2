package command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import domain.MemberBean;
import enums.Domain;
import service.MemberServiceImpl;

public class SearchCommand extends Command{
	List<MemberBean> search;
	public SearchCommand(HttpServletRequest request) {
		setRequest(request);
		setDomain(request.getServletPath().substring(1, request.getServletPath().indexOf(".")));
		setAction(request.getParameter("action"));
		setPage(request.getParameter("page"));
		execute();
	}
	@Override
	public void execute() {
		switch (Domain.valueOf(domain.toUpperCase())) {
		case MEMBER:
			request.setAttribute("search", MemberServiceImpl.getInstance().findByTeamId(request.getParameter("team-id")));
			break;
		default:
			break;
		}
		super.execute();
	}
	public List<MemberBean> getSearch() {
		return search;
	}
	
}