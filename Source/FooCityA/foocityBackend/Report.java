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
	
	public Report(int waterC, int waterG, int powerC, int powerG, int j, int r, int i, int exp, int af, int cf, int t){
		this.waterConsumed = waterC;
		this.waterGenerated = waterG;
		this.powerGenerated = powerG;
		this.powerConsumed = powerC;
		this.jobs = j;
		this.residents = r;
		this.income = i;
		this.expenses = exp;
		this.availableFunds = af;
		this.cashFlow = cf;
		this.turn = t;
	}
	
	public String getReportString() {
		final String PLACEHOLDER = " \n";
		StringBuilder sb = new StringBuilder();

		sb.append("End of Turn Report for Turn: \n");
		sb.append(Integer.toString(turn) + "\n");
		sb.append(PLACEHOLDER);

		sb.append(PLACEHOLDER);
		sb.append("Produced\n");
		sb.append("Consumed\n");
		sb.append("Water\n");
		sb.append(Integer.toString(waterGenerated) + "\n");
		sb.append(Integer.toString(waterConsumed) + "\n");
		sb.append("Electricity" + "\n");
		sb.append(Float.toString(powerGenerated) + "\n");
		sb.append(Float.toString(powerConsumed) + "\n");
		sb.append("Jobs" + "\n");
		sb.append(Float.toString(jobs) + "\n");
		sb.append(Float.toString(residents) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Tax income: " + "\n");
		sb.append(Integer.toString(income) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Upkeep expenses: " + "\n");
		sb.append(Integer.toString(expenses) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Cash Flow: " + "\n");
		sb.append("$" + Integer.toString(cashFlow) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Available Funds:" + "\n");
		sb.append("$" + Integer.toString(availableFunds) + "\n");

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
