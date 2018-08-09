package command;

import javax.servlet.http.HttpServletRequest;
import enums.Action;

public class Commander{
	public static Command order(HttpServletRequest request) {
		Command cmd = null;
		switch(Action.valueOf(request.getParameter("action").toUpperCase())) {
		case MOVE :
			cmd = new MoveCommand(request);
			break;
		case ADD :
			cmd = new AddCommand(request);
			break;
		case MODIFY :
			cmd = new ModifyCommand(request);
			break;
		case REMOVE : 
			cmd = new RemoveCommand(request);
			break;
		case LOGIN : 
			cmd = new LoginCommand(request);
			break;
		case SEARCH :
			cmd = new SearchCommand(request);
			break;
		case RETRIEVE :
			cmd = new RetrieveCommand(request);
			break;
		default:
			break;
		}
		return cmd;
	}
}
