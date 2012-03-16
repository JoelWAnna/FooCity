package foocityBackend;

public class Report {
	
	private int waterConsumed;
	private int waterGenerated;
	private int powerConsumed;
	private int powerGenerated;
	private int jobs;
	private int residents;
	private int income;
	private int cashFlow;
	private int availableFunds;
	private int expenses;
	private int turn;
	private int happyResidents; 
	private int totalPollution; 
	private int businessTax;
	private int incomeTax;
	private int occupationTax; 
	private int propertyTax;
	private int salesTax;
	private int totalPropertyValue;
	private int unemployment;
	private int managerRating;
	
	public Report(int waterC, int waterG, int powerC, int powerG, int j, int r, int i, int exp, int af, int cf, int t,
			int hr, int tp, int bt, int it, int ot, int pt, int st, int tpv, int u, int mr){
		this.waterConsumed = waterC;
		this.waterGenerated = waterG;
		this.powerConsumed = powerC;
		this.powerGenerated = powerG;
		this.jobs = j;
		this.residents = r;
		this.income = i;
		this.expenses = exp;
		this.availableFunds = af;
		this.cashFlow = cf;
		this.turn = t;
		this.happyResidents = hr;
		this.totalPollution = tp;
		this.incomeTax = it;
		this.businessTax = bt;
		this.occupationTax = ot;
		this.propertyTax = pt;
		this.salesTax = st;
		this.totalPropertyValue = tpv;
		this.unemployment = u;
		this.managerRating = mr;
	}
	
	public String getReportStringOneLine(){
		return Integer.toString(waterConsumed) + ":" + Integer.toString(waterGenerated) + ":" + Integer.toString(powerGenerated)
			+ ":" + Integer.toString(powerConsumed) + ":" + Integer.toString(jobs) + ":" + Integer.toString(residents)
			+ ":" + Integer.toString(income) + ":" + Integer.toString(expenses) + ":" +  Integer.toString(availableFunds)
			+ ":" + Integer.toString(cashFlow) + ":" + Integer.toString(turn) + ":" + Integer.toString(happyResidents)
			+ ":" + Integer.toString(totalPollution) + ":" + Integer.toString(incomeTax) + ":" + Integer.toString(businessTax)
			+ ":" + Integer.toString(occupationTax)	+ ":" + Integer.toString(propertyTax) + ":" + Integer.toString(salesTax)
			+ ":" + Integer.toString(totalPropertyValue) + ":" + Integer.toString(unemployment) + ":" + Integer.toString(managerRating) ;
	}
	
	public String getReportString() {
		final String PLACEHOLDER = " \n";
		StringBuilder sb = new StringBuilder();

		sb.append("End of Turn Report for Turn: \n");
		sb.append(Integer.toString(turn) + "\n");
		
		sb.append(PLACEHOLDER);
		sb.append(PLACEHOLDER);
		
		sb.append("RESIDENTS\n");
		sb.append("TAX REVENUE\n");
		
		sb.append("Happy: " + Integer.toString(happyResidents) + " (" 
				+ Integer.toString((int) (100.0 * (((float) happyResidents)/ ((float)residents)))) + "%)\n");
		sb.append("Income tax: " + Integer.toString(incomeTax) + "\n");
		
		sb.append("Unhappy: " + Integer.toString(residents - happyResidents) + " (" 
				+ Integer.toString(100 - (int) (100.0 * (((float) happyResidents)/ ((float)residents)))) + "%)\n");
		sb.append("Occupation tax: " + Integer.toString(occupationTax) + "\n");
		
		sb.append("Total: " + Integer.toString(residents) + "\n");
		sb.append("Sales tax: " + Integer.toString(salesTax) + "\n");
		
		sb.append("\n");
		sb.append("Business tax: " + Integer.toString(businessTax) + "\n");
		
		sb.append("Jobs: " + Integer.toString(jobs) + "\n");
		sb.append("Property tax: " + Integer.toString(propertyTax) + "\n");
		
		sb.append("Unemployment: " + Integer.toString(unemployment) + "%\n");
		sb.append("------------------------\n");
		
		sb.append("\n");
		sb.append("Total tax revenue: " + income + "\n");
		
		sb.append("Total pollution: " + Integer.toString(totalPollution) + "\n");
		sb.append("Expenses: " + Integer.toString(expenses) + "\n");
		
		sb.append("Total Property Value: " + Integer.toString(totalPropertyValue) + "\n");
		sb.append("Cash Flow: " + Integer.toString(cashFlow) + "\n");
		
		sb.append("Manager Score: " + Integer.toString(managerRating) + "%\n");
		sb.append("Available Funds: " + Integer.toString(availableFunds) + "\n");

		return sb.toString();
	}
	
	public int getTurn(){
		return turn;
	}

	public int getWaterConsumed() {
		return waterConsumed;
	}

	public int getWaterGenerated() {
		return waterGenerated;
	}

	public int getPowerConsumed() {
		return powerConsumed;
	}

	public int getPowerGenerated() {
		return powerGenerated;
	}

	public int getJobs() {
		return jobs;
	}

	public int getResidents() {
		return residents;
	}

	public int getIncome() {
		return income;
	}

	public int getCashFlow() {
		return cashFlow;
	}

	public int getAvailableFunds() {
		return availableFunds;
	}

	public int getExpenses() {
		return expenses;
	}
	
}
