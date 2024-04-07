package apitest;

import com.ib.client.Contract;

public class OrderSend {
	Contract contract;

	OrderSend(Contract contract) {
		this.contract = contract;
	}

	public void createOrderBacket(String data, double price, double sig, double posN, double livepos, double qty,
			double traget, double stop) {

//		p.placeBracketOder(contract);
		if (livepos == 0 || livepos !=0) {
			if (posN == 0) {
				System.out.println("No position, Try to open");
				if (sig == 1) {
					System.out.println("Buy bracket order to open");

					PlaceOrderATS p = new PlaceOrderATS();
//					System.out.println("price "+price+" traget"+(price+ traget)+" stop "+(price- stop));
					p.placeBracketOder(contract, "BUY", qty, "MKT", 0, (price + traget), (price - stop));
				} else if (sig == -1) {
					System.out.println("Sell bracket order to open");

					PlaceOrderATS p = new PlaceOrderATS();
//					System.out.println("price "+price+" traget"+(price- traget)+" stop "+(price+ stop));
					p.placeBracketOder(contract, "SELL", qty, "MKT", 0, (price - traget), (price + stop));
				}

			} else {

				System.out.println("Have Position Do nothing");
			}

		} else {
			System.out.println("Have Barkert position Wait");
		}
	}

	public void createOrderDynamic(String data, double price, double sig, double posN, double livepos, double average,
			double qty, double traget, double stop) {
		// == Trade == EUR GBP CASH 0.0 20000.0
		if (livepos == 0) {
			// Sig Buy
			if (posN == 0) {
				System.out.println("No position");
				if (sig == 1) {
					System.out.println("sig buy");
					// no position then buy to open
					System.out.println("Buy to open");
					PlaceOrderATS p = new PlaceOrderATS();
					p.placeOrder(contract, OrderATS.buyMarket(qty));

					LogFile w = new LogFile();
					w.appendPlaceOrder(data + ", Buy to Open, " + contract.symbol() + ", " + contract.currency() + ", "
							+ posN + ", " + qty);

				}
				// Sig Sell
				else if (sig == -1) {
					System.out.println("sig sell");
					// no position then sell to open
					System.out.println("sell  to open");
					PlaceOrderATS p = new PlaceOrderATS();
					p.placeOrder(contract, OrderATS.sellMarket(qty));

					LogFile w = new LogFile();
					w.appendPlaceOrder(data + ", Sell to Open, " + contract.symbol() + ", " + contract.currency() + ", "
							+ posN + ", " + qty);

				}
			}

			else if (posN > 0) {// Sell to close
				System.out.println("Have position: " + posN);
				if (price > (average + traget) || price < (average - stop)) { // sell to close
					System.out.println(price + " " + average + " " + traget + " " + stop);
					System.out.println("Sell to close" + "price:" + price + " , average - stop:" + (average - stop)
							+ " ,average + traget:" + (average + traget));

					System.out.println("Sell to close");
					PlaceOrderATS p = new PlaceOrderATS();
					p.placeOrder(contract, OrderATS.sellMarket(qty));

					LogFile w = new LogFile();
					w.appendPlaceOrder(data + ", Sell to Close, " + contract.symbol() + ", " + contract.currency()
							+ ", " + posN + ", " + qty);

				}
			} else if (posN < 0) {// Buy to close
				System.out.println("Have position: " + posN);
				if (price > (average + stop) || price < (average - traget)) { // sell to close
					System.out.println(price + " " + average + " " + traget + " " + stop);
					System.out.println("Buy to close" + "price:" + price + " , average + stop:" + (average + stop)
							+ " ,average - traget:" + (average - traget));
					PlaceOrderATS p = new PlaceOrderATS();
					p.placeOrder(contract, OrderATS.buyMarket(qty));

					LogFile w = new LogFile();
					w.appendPlaceOrder(data + ", Buy to Close, " + contract.symbol() + ", " + contract.currency() + ", "
							+ posN + ", " + qty);

				}
			}
		}
	}

	public void createOrder(String data, double sig, double posN, double liveposition, double qty) {
		// == Trade == EUR GBP CASH 0.0 20000.0
//		if (liveposition == 0) {

			System.out.println("sig: " + sig);
			// Sig Buy
			if (sig == 1.0) {
				System.out.println("posN" + posN);
				PlaceOrderATS p = new PlaceOrderATS();
				if (posN == 0.0) { // no position then buy to open

					System.out.println("Buy to open");
					p.placeOrder(contract, OrderATS.buyMarket(qty));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Buy to Open, " + contract.symbol() + ", " + contract.currency() + ", "
//							+ posN + ", " + qty);

				} else if (posN > 0) { // buy already
					System.out.println("Buy alreay ");

				} else if (posN < 0) { // Ex posN = -20, then Buy to close

					System.out.println("Buy to close");
					p.placeOrder(contract, OrderATS.buyMarket(qty));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Buy to Close, " + contract.symbol() + ", " + contract.currency() + ", "
//							+ posN + ", " + qty);

				}
			}
			// Sig Sell
			else if (sig == -1) {

				if (posN == 0) { // no position then sell to open
					System.out.println("sell  to open");
					PlaceOrderATS p = new PlaceOrderATS();

					p.placeOrder(contract, OrderATS.sellMarket(qty));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Sell to Open, " + contract.symbol() + ", " + contract.currency() + ", "
//							+ posN + ", " + qty);

				} else if (posN > 0) { // Ex posN = 20, then Sell to close
					System.out.println("Sell to close ");
					PlaceOrderATS p = new PlaceOrderATS();

					p.placeOrder(contract, OrderATS.sellMarket(qty));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Sell to Close, " + contract.symbol() + ", " + contract.currency()
//							+ ", " + posN + ", " + qty);

				} else if (posN < 0) { // sell already
					System.out.println("sell already");
				}
			}
			// Sig Close ALL
			else if (sig == 10) { //

				// Buy to Close, Ex posN = -10, then buy to close
				if (posN < 0) {
					System.out.println("Buy to close");
					PlaceOrderATS p = new PlaceOrderATS();
					p.placeOrder(contract, OrderATS.buyMarket(Math.abs(posN)));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Buy to Close, " + contract.symbol() + ", " + contract.currency() + ", "
//							+ posN + ", " + qty);

				} // Sell to close
				else if (posN > 0) {
					System.out.println("Sell to close ");
					PlaceOrderATS p = new PlaceOrderATS();
					p.placeOrder(contract, OrderATS.sellMarket(Math.abs(posN)));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Sell to Close, " + contract.symbol() + ", " + contract.currency()
//							+ ", " + posN + ", " + qty);
				}

			}
			// Sig Wait
			else {
				System.out.println("============Signal wait==============");
			}

//		} else {
//			System.out.println("============Have live position wait==============");
//		}

	}
	
	public void createOrderOption(String data, double sig, double posN, double liveposition, double qty) {

			System.out.println("sig: " + sig);
			// Sig Buy
			if (sig == 1.0) {
				System.out.println("posN" + posN);
				PlaceOrderATS p = new PlaceOrderATS();
				if (posN == 0.0) { // no position then buy to open

					System.out.println("Buy to open");
					p.placeOrder(contract, OrderATS.buyMarket(qty));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Buy to Open, " + contract.symbol() + ", " + contract.currency() + ", "
//							+ posN + ", " + qty);

				} else if (posN > 0) { // buy already
					System.out.println("Buy alreay ");

				} 
			}
			// Sig Sell
			else if (sig == -1.0) {
				System.out.println("SIG ----------1");
				 if (posN > 0) { // Ex posN = 20, then Sell to close
					System.out.println("Sell to close ");
					PlaceOrderATS p = new PlaceOrderATS();

					p.placeOrder(contract, OrderATS.sellMarket(posN));

//					LogFile w = new LogFile();
//					w.appendPlaceOrder(data + ", Sell to Close, " + contract.symbol() + ", " + contract.currency()
//							+ ", " + posN + ", " + qty);

				} 
			}
			

	}
}
