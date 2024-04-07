package apitest;

public class A_ControlImp extends A_Control{
	public A_ControlImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		mediator.sendMessage(msg, this);
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
		System.out.println(msg);
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		mediator.sendMessage(A_MSG.Start, this);
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
